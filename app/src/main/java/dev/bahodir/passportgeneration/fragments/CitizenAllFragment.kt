package dev.bahodir.passportgeneration.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dev.bahodir.passportgeneration.R
import dev.bahodir.passportgeneration.databinding.FragmentCitizenAllBinding
import dev.bahodir.passportgeneration.db.DB
import dev.bahodir.passportgeneration.rv.RVAdapter
import dev.bahodir.passportgeneration.user.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CitizenAllFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CitizenAllFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private var _binding: FragmentCitizenAllBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DB
    private lateinit var rvAdapter: RVAdapter
    private lateinit var matchedPeople: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCitizenAllBinding.inflate(inflater, container, false)
        db = DB.getInstance(requireContext())
        val dbList = db.userDB().getUser()

        rvAdapter = RVAdapter(dbList, object : RVAdapter.OnTouchMore {
            override fun onMore(user: User, position: Int, view: View) {
                var bundle = Bundle()
                bundle.putSerializable("user", user)
                findNavController().navigate(R.id.action_citizenAllFragment_to_citizenInformationFragment,bundle)
            }

        })
        binding.recycler.adapter = rvAdapter

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        matchedPeople = mutableListOf()
        binding.search.isSubmitButtonEnabled = true
        performSearch()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun performSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }
    private fun search(text: String?) {
        /*var matchedPeople = mutableListOf<User>()*/

        text?.let {
            db.userDB().getUser().forEach { user ->
                if (user.name?.contains(text, true)!! || user.surname?.contains(text, true)!!) {
                    matchedPeople.add(user)
                }
            }
            updateRecyclerView()
            if (matchedPeople.isEmpty()) {
                Toast.makeText(requireContext(), "No match found!", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerView()
        }
    }
    private fun updateRecyclerView() {
        binding.recycler.apply {
            rvAdapter.list = matchedPeople
            rvAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CitizenAllFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CitizenAllFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}