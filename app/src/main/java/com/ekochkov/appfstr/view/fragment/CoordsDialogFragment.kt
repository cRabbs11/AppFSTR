package com.ekochkov.appfstr.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.ekochkov.appfstr.R

import com.ekochkov.appfstr.databinding.DialogFragmentCoordsBinding
import com.ekochkov.appfstr.utils.CoordsConverter

class CoordsDialogFragment: DialogFragment() {

    companion object {
        const val REQUEST_COORDS_KEY = "coords"
        const val BUNDLE_LAT_KEY = "lat"
        const val BUNDLE_LON_KEY = "lon"
        const val BUNDLE_HEIGHT_KEY = "height"

    }

    private lateinit var binding: DialogFragmentCoordsBinding
    private val N_VALUE = "N"
    private val S_VALUE = "S"
    private val E_VALUE = "E"
    private val W_VALUE = "W"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentCoordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.latitudeText.setOnClickListener {
            println("!!! click")
            if ((it as TextView).text.equals(N_VALUE)) {
                it.setText(S_VALUE)
            } else {
                it.setText(N_VALUE)
            }
        }

        binding.longtitudeText.setOnClickListener {
            if ((it as TextView).text.equals(E_VALUE)) {
                it.setText(W_VALUE)
            } else {
                it.setText(E_VALUE)
            }
        }

        binding.addBtn.setOnClickListener {
            if (isInputDataCorrect()) {
                val height = binding.heightEditText.text.toString()

                val lat = binding.latitudeDegrEditText.text.toString()
                val lon = binding.longtitudeDegrEditText.text.toString()

                parentFragmentManager.setFragmentResult(
                    REQUEST_COORDS_KEY,
                    bundleOf(
                        BUNDLE_LAT_KEY to lat,
                        BUNDLE_LON_KEY to lon,
                        BUNDLE_HEIGHT_KEY to height))
                dismiss()
            } else {
                Toast.makeText(requireContext(), getString(R.string.not_all_data_inserted), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInputDataCorrect(): Boolean {
        val height = binding.heightEditText.text.toString()
        val lat = binding.latitudeDegrEditText.text.toString()
        val lon = binding.longtitudeDegrEditText.text.toString()
        return (height.isNotEmpty() &&
                height.toInt()>0 &&
                lat.isNotEmpty() &&
                lat.toDouble()<90 && lat.toDouble()>-90 &&
                lon.isNotEmpty() &&
                lon.toDouble()<90 && lon.toDouble()>-90)
    }
}