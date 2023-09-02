package com.veljko.detective.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.veljko.detective.R

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
    private var param1: String? = null
    private var param2: String? = null

    private var spinnerT: Spinner? = null
    private var spinnerD: Spinner? = null
    private var typeSel: String? = null
    private var diffSel: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        val adapterD = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, types)
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

        val finishButton = view.findViewById<Button>(R.id.FinishButton) as Button

        finishButton.setOnClickListener {
            getParentFragmentManager().popBackStack();
        }




        return view
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddObjectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddObjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}