package dev.bahodir.passportgeneration.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dev.bahodir.passportgeneration.R
import dev.bahodir.passportgeneration.databinding.FragmentCitizenAddBinding
import dev.bahodir.passportgeneration.db.DB
import dev.bahodir.passportgeneration.user.User
import java.io.File
import java.io.FileOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CitizenAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CitizenAddFragment : Fragment() {
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
    private var image: String? = null
    private var _binding: FragmentCitizenAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCitizenAddBinding.inflate(inflater, container, false)

        db = DB.getInstance(requireContext())
        var dbDao = db.userDB()


        var count1 = 0
        binding.addImage.setOnClickListener {
            Dexter.withActivity(requireActivity())
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_LONG)
                            .show()
                        getImage.launch("image/*")
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Permission")
                            .setMessage("The task will not be performed if permission is not enabled")
                            .setNegativeButton(
                                "Cancel",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                    token?.cancelPermissionRequest()
                                })
                            .setPositiveButton(
                                "OK",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                    token?.continuePermissionRequest()
                                })
                            .show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        if (count1 > 0) {
                            var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            var uri: Uri =
                                Uri.fromParts("package", requireContext().packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        count1++
                    }
                }
                ).check()
        }
        var list = dbDao.getUser()

        binding.save.setOnClickListener {
            val name = binding.citizensName.text.toString()
            val surname = binding.citizensSurname.text.toString()
            val middleName = binding.citizensMiddleName.text.toString()
            val region = binding.region.selectedItem.toString()
            val city = binding.city.text.toString()
            val address = binding.homeAddress.text.toString()
            val receipt = binding.timeOfReceiptOfPassport.text.toString()
            val term = binding.passportTerm.text.toString()
            val gender = binding.gender.selectedItem.toString()

            if (name.isEmpty() && surname.isEmpty() && middleName.isEmpty() && city.isEmpty() && address.isEmpty() && receipt.isEmpty() && term.isEmpty()) {
                Toast.makeText(requireContext(), "Fill in all of the fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (image == null) {
                    Toast.makeText(
                        requireContext(),
                        "No image selected for user",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog
                        .setTitle("Are you sure the data is correct?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                dbDao.addUser(
                                    User(
                                        name = name,
                                        surname = surname,
                                        middle_name = middleName,
                                        region = region,
                                        district = city,
                                        home_address = address,
                                        receipt_passport = receipt,
                                        passport_term = term,
                                        serial_number = getSerialNumber(),
                                        gender = gender,
                                        photo_path = image
                                    )
                                )
                                p0?.dismiss()
                                findNavController().popBackStack()
                            }

                        })
                        .setNegativeButton("No", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                            }

                        }).show()
                }
            }
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getSerialNumber(): String {
        val a1 = (65..91).random().toChar()
        val a2 = (65..91).random().toChar()
        val number = (1000000..9999999).random()
        val serialNumber = "$a1$a2 $number"

        val passportList = db.userDB().getUser()
        val filterList = passportList.filter {
            it.id.toString() == serialNumber
        }
        return if (filterList.isEmpty()) {
            serialNumber
        } else {
            getSerialNumber()
        }
    }

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null)
            return@registerForActivityResult

        binding.addImage.setImageURI(uri)
        val openInputStream = requireContext().contentResolver?.openInputStream(uri)
        val m = System.currentTimeMillis()
        val file = File(requireContext().filesDir, "$m.jpg")
        val fileOutputStream = FileOutputStream(file)
        openInputStream?.copyTo(fileOutputStream)
        openInputStream?.close()
        fileOutputStream.close()
        image = file.absolutePath
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CitizenAddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CitizenAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}