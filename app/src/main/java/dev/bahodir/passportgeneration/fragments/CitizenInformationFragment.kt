package dev.bahodir.passportgeneration.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.bahodir.passportgeneration.R
import dev.bahodir.passportgeneration.databinding.FragmentCitizenInformationBinding
import dev.bahodir.passportgeneration.user.User
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM = "user"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CitizenInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CitizenInformationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: User? = null
    //private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_PARAM) as User?
            //param2 = it.getString(ARG_PARAM2)
        }
    }
    private var _binding: FragmentCitizenInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCitizenInformationBinding.inflate(inflater, container, false)

        binding.name.text = "${user?.name} ${user?.surname}"
        binding.image.setImageURI(Uri.fromFile(File(user?.photo_path)))
        binding.title.text = "${user?.name} ${user?.surname} ${user?.middle_name}"
        binding.region.text = "${user?.region} :Region"
        binding.city.text = "${user?.district} : District"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CitizenInformationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: User) =
            CitizenInformationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM, user)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}