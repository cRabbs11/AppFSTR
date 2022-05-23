package com.ekochkov.appfstr.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ekochkov.appfstr.R
import com.ekochkov.appfstr.data.entity.*
import com.ekochkov.appfstr.databinding.FragmentAddMountainBinding
import com.ekochkov.appfstr.utils.Base64Converter
import com.ekochkov.appfstr.utils.CoordsConverter
import com.ekochkov.appfstr.utils.DateConverter
import com.ekochkov.appfstr.view.activity.MainActivity
import com.ekochkov.appfstr.view.adapters.ImageAdapter
import com.ekochkov.appfstr.view.viewHolders.ImageHolder
import com.ekochkov.appfstr.viewModel.AddMountainFragmentViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class AddMountainFragment: Fragment(), ImageHolder.onClickListener {

    private val TAG_DATE_PICKER_FRAGMENT = "date_picker_fragment"
    private val TAG_COORDS_DIALOG_FRAGMENT = "coords_dialog_fragment"
    private val TEXT_SELECT_DATE = "Выберите дату"
    private val TEXT_LATITUDE_EXAMPLE = "N 55 36.4999"
    private val TEXT_LONGTITUDE_EXAMPLE = "E 17 18.2332"

    private val DIFFICULT_CATEGORY_NONE = "Н/К"
    private val DIFFICULT_CATEGORY_A1 = "А1"
    private val DIFFICULT_CATEGORY_A2 = "А2"
    private val DIFFICULT_CATEGORY_A3 = "А3"
    private val DIFFICULT_CATEGORY_B1 = "Б1"
    private val DIFFICULT_CATEGORY_B2 = "Б2"
    private val DIFFICULT_CATEGORY_B3 = "Б3"
    private val SUB_CATEGORY_NOT_SURE = "не уверен"
    private val SUB_CATEGORY_ESTIMATED = "оценочно"
    private val DIFFICULT_ADD = "*"

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var binding: FragmentAddMountainBinding
    private val viewModel : AddMountainFragmentViewModel by viewModels()
    private var category = ""
    private var selectedDate = Date().time
    private var mountainLat = ""
    private var mountainLon = ""
    private var mountainHeight = ""
    private var imageList = arrayListOf<Image>()


    val permissionReadStorageLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            openGallery()
        }
    }

    val permissionCameraLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            (activity as MainActivity).openCameraFragment()
        }
    }

    val imageListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val imageUri = it.data?.data
        if (imageUri!=null) {
            if (Build.VERSION.SDK_INT >= 28) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, imageUri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                val stringImage = Base64Converter.fromBitmapToBase64(bitmap)
                imageList.add(Image("test_title", stringImage))
                updateImageRecyclerView()
            }
        } else {
            println("imageUri is null")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMountainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.setFragmentResultListener(CoordsDialogFragment.REQUEST_COORDS_KEY, viewLifecycleOwner) { requestKey, bundle ->
            when (requestKey) {
                CoordsDialogFragment.REQUEST_COORDS_KEY -> {
                    mountainLat = bundle.getString(CoordsDialogFragment.BUNDLE_LAT_KEY, "")
                    mountainLon = bundle.getString(CoordsDialogFragment.BUNDLE_LON_KEY, "")
                    mountainHeight = bundle.getString(CoordsDialogFragment.BUNDLE_HEIGHT_KEY, "")
                    var latDir = bundle.getString(CoordsDialogFragment.BUNDLE_LAT_DIR_KEY, "N")
                    var lonDir = bundle.getString(CoordsDialogFragment.BUNDLE_LON_DIR_KEY, "E")
                    binding.heightNameEditText.editText?.setText(mountainHeight)
                    var lat = CoordsConverter.fromDecimalToDegrees(mountainLat.toDouble())
                    var lon = CoordsConverter.fromDecimalToDegrees(mountainLon.toDouble())
                    val latText = "${lat[0]} ${lat[1]}.${lat[2]}"
                    val lonText = "${lon[0]} ${lon[1]}.${lon[2]}"

                    binding.openCoordsBtn.text = "$latDir $latText\n$lonDir $lonText"
                }
            }
        }

        binding.addMountainBtn.setOnClickListener {
            if (isInputDataIsCorrect()) {
                setInputData()
                //viewModel.putMountainOnServer()
            }
        }

        binding.openDateBtn.setOnClickListener {
            openDatePicker()
        }

        binding.openCoordsBtn.setOnClickListener {
            openCoordsDialog()
        }

        binding.addFromGalleryBtn.setOnClickListener {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGallery()
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        binding.addFromCameraBtn.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA)) {
                (activity as MainActivity).openCameraFragment()
            } else {
                requestPermission(Manifest.permission.CAMERA)
            }
        }

        binding.categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.chipCategoryNone.id -> {
                    category = DIFFICULT_CATEGORY_NONE}
                binding.chipCategoryA1.id -> {
                    category = DIFFICULT_CATEGORY_A1}
                binding.chipCategoryA2.id -> {
                    category = DIFFICULT_CATEGORY_A2}
                binding.chipCategoryA3.id -> {
                    category = DIFFICULT_CATEGORY_A3}
                binding.chipCategoryB1.id -> {
                    category = DIFFICULT_CATEGORY_B1}
                binding.chipCategoryB2.id -> {
                    category = DIFFICULT_CATEGORY_B2}
                binding.chipCategoryB3.id -> {
                    category = DIFFICULT_CATEGORY_B3}
            }
        }

        binding.chipAddDifficult.setOnClickListener {
            setSubDifficultToCategoryText(binding.chipAddDifficult.isChecked)
        }

        imageAdapter = ImageAdapter(this)
        binding.imageRecyclerView.adapter = imageAdapter

        initView()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        imageListener.launch(gallery)
    }

    private fun requestPermission(permission: String) {
        when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                permissionReadStorageLauncher.launch(permission)
            }
            Manifest.permission.CAMERA -> {
                permissionCameraLauncher.launch(permission)
            }
        }
    }

    private fun updateImageRecyclerView() {
        imageAdapter.images.clear()
        imageAdapter.images.addAll(imageList)
        imageAdapter.notifyDataSetChanged()
        updateUI()
    }

    private fun updateUI() {
        if (imageList.size>=3) {
            binding.addFromCameraBtn.isEnabled = false
            binding.addFromGalleryBtn.isEnabled = false
        } else {
            binding.addFromCameraBtn.isEnabled = true
            binding.addFromGalleryBtn.isEnabled = true
        }
    }


    private fun setSubDifficultToCategoryText(value: Boolean) {
        var textA1 = getString(R.string.category_a1)
        var textA2 = getString(R.string.category_a2)
        var textA3 = getString(R.string.category_a3)
        var textB1 = getString(R.string.category_b1)
        var textB2 = getString(R.string.category_b2)
        var textB3 = getString(R.string.category_b3)
        if (value) {
            val subCategory = getString(R.string.upper_star)
            textA1 += subCategory
            textA2 += subCategory
            textA3 += subCategory
            textB1 += subCategory
            textB2 += subCategory
            textB3 += subCategory
        }
        binding.chipCategoryA1.text = textA1
        binding.chipCategoryA2.text = textA2
        binding.chipCategoryA3.text = textA3
        binding.chipCategoryB1.text = textB1
        binding.chipCategoryB2.text = textB2
        binding.chipCategoryB3.text = textB3
    }

    private fun isInputDataIsCorrect(): Boolean {
        if (binding.mountainNameEditText.editText?.text.toString().isEmpty()) {
            showToast(getString(R.string.mountain_name_is_empty))
            return false
        }
        if (category.isEmpty()) {
            showToast(getString(R.string.mountain_category_is_empty))
            return false
        }
        if (selectedDate==0L) {
            showToast(getString(R.string.mountain_date_is_empty))
            return false
        }
        if (mountainLat.isEmpty() || mountainLon.isEmpty()) {
            showToast(getString(R.string.mountain_coords_is_empty))
            return false
        }
        if (mountainHeight.isEmpty()) {
            showToast(getString(R.string.mountain_height_is_empty))
            return false
        }
        return true
    }

    private fun setInputData() {
        viewModel.setMountainName(binding.mountainNameEditText.editText?.text.toString())

        var fullCategory = category
        if (binding.chipAddDifficult.isChecked) {
            fullCategory += DIFFICULT_ADD
        }
        viewModel.setMountainCategory(fullCategory)

        var subCategoryList = arrayListOf<String>()
        if (binding.chipNotSure.isChecked) {
            subCategoryList.add(SUB_CATEGORY_NOT_SURE)
        }
        if (binding.chipEstimated.isChecked) {
            subCategoryList.add(SUB_CATEGORY_ESTIMATED)
        }
        viewModel.setMountainSubCategory(subCategoryList)

        viewModel.setMountainDate(selectedDate)

        viewModel.setMountainCoords(mountainLat, mountainLon, mountainHeight)
    }

    private fun openDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(TEXT_SELECT_DATE)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(childFragmentManager, TAG_DATE_PICKER_FRAGMENT)

        datePicker.addOnPositiveButtonClickListener {
            selectedDate = it
            binding.openDateBtn.text = DateConverter.fromLongToText(selectedDate, DateConverter.FORMAT_DD_MM_YYYY)
        }
    }

    private fun openCoordsDialog() {
        val coordsDialog = CoordsDialogFragment()
        coordsDialog.show(childFragmentManager, TAG_COORDS_DIALOG_FRAGMENT)
    }

    private fun initView() {
        binding.openDateBtn.text = DateConverter.fromLongToText(selectedDate, DateConverter.FORMAT_DD_MM_YYYY)
        binding.openCoordsBtn.text = TEXT_LATITUDE_EXAMPLE + "\n" + TEXT_LONGTITUDE_EXAMPLE
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteImageClick(image: Image) {
        imageList.remove(image)
        updateImageRecyclerView()
    }
}