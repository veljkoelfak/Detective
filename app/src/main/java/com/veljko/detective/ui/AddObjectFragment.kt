package com.veljko.detective.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.GeoPoint
import com.veljko.detective.R
import com.veljko.detective.UserDataViewModel
import java.io.File
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddObjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddObjectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var lat: Double? = null
    private var lng: Double? = null
    private var loc : GeoPoint? = null
    private var uri = Uri.EMPTY

    private var spinnerT: Spinner? = null
    private var spinnerD: Spinner? = null
    private var typeSel: String? = null
    private var diffSel: String? = null

    private var username: String? = null

    private val viewModel: UserDataViewModel by activityViewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lat = arguments?.getDouble("lat")
        lng = arguments?.getDouble("lng")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_object, container, false)

        spinnerT = view.findViewById(R.id.spinnerType)
        spinnerD = view.findViewById(R.id.spinnerDiff)

        val types = resources.getStringArray(R.array.spinner_type)
        val diffs = resources.getStringArray(R.array.spinner_difficulty)

        val adapterD = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, diffs)
        if (spinnerT != null) {
            val adapterT = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, types)

            spinnerT!!.adapter = adapterT

            spinnerT!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    typeSel = "Other"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    typeSel = types[position]
                }
            }



        }

        if (spinnerD != null) {
            val adapterD = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, diffs)

            spinnerD!!.adapter = adapterD

            spinnerD!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    diffSel = "1"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    diffSel = diffs[position]
                }
            }



        }

        viewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            username = user.displayName
        })

        val image = view.findViewById<ImageView>(R.id.addPhoto)

        image.setOnClickListener(View.OnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose an option")
            builder.setItems(options) { dialog, which ->
                when (options[which]) {
                    "Take Photo" -> {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val file: File = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            "Object.jpg"
                        )

                        uri = FileProvider.getUriForFile(
                            requireContext(),
                            requireContext().packageName + ".provider",
                            file
                        )
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        getPictureCamera.launch(takePicture)
                    }
                    "Choose from Gallery" -> {
                        val pickPhotoIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        getPicture.launch(pickPhotoIntent)
                    }
                    "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        })


        val finishButton = view.findViewById<Button>(R.id.FinishButton) as Button

        finishButton.setOnClickListener {

            val desc = view.findViewById<EditText>(R.id.objectDesc).text.toString()
            loc = GeoPoint(lat!!, lng!!)
            val date = com.google.firebase.Timestamp(Calendar.getInstance().time)

            viewModel.addObject(typeSel!!,desc, loc!!,username!!,diffSel!!.toInt(), date, uri.toString())

            viewModel.addobjectData.observe(viewLifecycleOwner, Observer { success ->
                if (success == true) {
                    getParentFragmentManager().popBackStack();

                }
            })


        }


        return view
    }


    private val getPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    uri = result.data?.data
                    val imageView = requireView().findViewById<ImageView>(R.id.addPhoto)
                    imageView.setImageURI(uri)
                }
            }
        }

    // Register for activity result for camera
    private val getPictureCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Object.jpg"
                )

                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().packageName + ".provider",
                    file
                )

                val imageView = requireView().findViewById<ImageView>(R.id.addPhoto)
                imageView.setImageURI(uri)
            }
        }






}