package com.ekochkov.appfstr.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import com.ekochkov.appfstr.R
import com.ekochkov.appfstr.data.entity.*
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_ADD
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_CATEGORY_A1
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_CATEGORY_A2
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_CATEGORY_A3
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_CATEGORY_B1
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_CATEGORY_B2
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_CATEGORY_B3
import com.ekochkov.appfstr.data.entity.Mountain.Companion.DIFFICULT_CATEGORY_NONE
import com.ekochkov.appfstr.data.entity.Mountain.Companion.SUB_CATEGORY_ESTIMATED
import com.ekochkov.appfstr.data.entity.Mountain.Companion.SUB_CATEGORY_NOT_SURE
import com.ekochkov.appfstr.databinding.FragmentAddMountainBinding
import com.ekochkov.appfstr.diff.ImageDiff
import com.ekochkov.appfstr.utils.Base64Converter
import com.ekochkov.appfstr.utils.Constants
import com.ekochkov.appfstr.utils.DateConverter
import com.ekochkov.appfstr.view.adapters.ImageAdapter
import com.ekochkov.appfstr.view.viewHolders.ImageHolder
import com.ekochkov.appfstr.viewModel.AddMountainFragmentViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddMountainFragment: Fragment() {

    private val TAG_DATE_PICKER_FRAGMENT = "date_picker_fragment"
    private val TAG_COORDS_DIALOG_FRAGMENT = "coords_dialog_fragment"
    private val TAG_CAMERA_DIALOG_FRAGMENT = "coords_camera_fragment"
    private val TEXT_SELECT_DATE = "Выберите дату"
    private val MAX_IMAGES_COUNT = 3

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var binding: FragmentAddMountainBinding
    private val viewModel : AddMountainFragmentViewModel by viewModels()
    val compositeDisposable = CompositeDisposable()

    val permissionReadStorageLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            openGallery()
        }
    }

    val permissionCameraLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            openCamera()
        }
    }

    val imageListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val image = buildImage(it.data?.data)
        if (image!=null) {
            viewModel.addImage(image)
        }
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

        val mountainCashedId = arguments?.getInt(Constants.MOUNTAIN_CASHED_ID)
        if (mountainCashedId!=null) {
            viewModel.setEditMountain(mountainCashedId)
        }


        imageAdapter = ImageAdapter(object: ImageHolder.onClickListener {
            override fun onDeleteImageClick(image: Image) {
                viewModel.removeImage(image)
            }

            override fun onImageChanged(oldImage: Image, newImage: Image) {
                viewModel.updateImage(oldImage, newImage)
            }
        })
        binding.imageRecyclerView.adapter = imageAdapter

        viewModel.toastEventLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        //название перевала
        viewModel.mountainNameLiveData.observe(viewLifecycleOwner) {
            if (binding.mountainNameEditText.editText?.text.toString() != it) {
                binding.mountainNameEditText.editText?.setText(it)
            }
        }
        binding.mountainNameEditText.editText?.doAfterTextChanged {
            if (!it.isNullOrEmpty()) {
                viewModel.setMountainName(it.toString())
            }
        }

        //категория
        binding.categoryChipGroup.setOnCheckedChangeListener { _, checkedId ->
            var category = ""
            when (checkedId) {
                binding.chipCategoryNone.id -> {
                    if (binding.chipCategoryNone.isChecked) { category = DIFFICULT_CATEGORY_NONE} }
                binding.chipCategoryA1.id -> {
                    if (binding.chipCategoryA1.isChecked) { category = DIFFICULT_CATEGORY_A1 } }
                binding.chipCategoryA2.id -> {
                    if (binding.chipCategoryA2.isChecked) { category = DIFFICULT_CATEGORY_A2 } }
                binding.chipCategoryA3.id -> {
                    if (binding.chipCategoryA3.isChecked) { category = DIFFICULT_CATEGORY_A3 } }
                binding.chipCategoryB1.id -> {
                    if (binding.chipCategoryB1.isChecked) { category = DIFFICULT_CATEGORY_B1 } }
                binding.chipCategoryB2.id -> {
                    if (binding.chipCategoryB2.isChecked) { category = DIFFICULT_CATEGORY_B2 } }
                binding.chipCategoryB3.id -> {
                    if (binding.chipCategoryB3.isChecked) { category = DIFFICULT_CATEGORY_B3 } }
            }
            if (category.isNotEmpty()) {
                if (binding.chipAddDifficult.isChecked) {
                    category += DIFFICULT_ADD
                }
                viewModel.setCategory(category)
            }
        }
        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            setCategoryAndDifficult(it)
        }

        //подкатегория
        viewModel.subCategoryLiveData.observe(viewLifecycleOwner) {
            binding.chipNotSure.isChecked = false
            binding.chipEstimated.isChecked = false

            it.forEach { subCategory ->
                when (subCategory) {
                    SUB_CATEGORY_NOT_SURE -> {
                        binding.chipNotSure.isChecked = true
                    }
                    SUB_CATEGORY_ESTIMATED -> {
                        binding.chipEstimated.isChecked = true
                    }
                }
            }
        }
        binding.chipAddDifficult.setOnClickListener {
            var category = ""
            if (binding.chipCategoryNone.isChecked) {
                category = DIFFICULT_CATEGORY_NONE
            } else if (binding.chipCategoryA1.isChecked) {
                category = DIFFICULT_CATEGORY_A1
            } else if (binding.chipCategoryA2.isChecked) {
                category = DIFFICULT_CATEGORY_A2
            } else if (binding.chipCategoryA3.isChecked) {
                category = DIFFICULT_CATEGORY_A3
            } else if (binding.chipCategoryB1.isChecked) {
                category = DIFFICULT_CATEGORY_B1 }
            else if (binding.chipCategoryB2.isChecked) {
                category = DIFFICULT_CATEGORY_B2
            } else if (binding.chipCategoryB3.isChecked) {
                category = DIFFICULT_CATEGORY_B3 }

            if (category.isNotEmpty() && binding.chipAddDifficult.isChecked) {
                category += DIFFICULT_ADD
            }
            viewModel.setCategory(category)
            setSubDifficultToCategoryText(binding.chipAddDifficult.isChecked)
        }

        binding.chipEstimated.setOnClickListener {
            if (binding.chipEstimated.isChecked) {
                viewModel.addToSubCategory(SUB_CATEGORY_ESTIMATED)
            } else {
                viewModel.removeFromSubCategory(SUB_CATEGORY_ESTIMATED)
            }
        }
        binding.chipNotSure.setOnClickListener {
            if (binding.chipNotSure.isChecked) {
                viewModel.addToSubCategory(SUB_CATEGORY_NOT_SURE)
            } else {
                viewModel.removeFromSubCategory(SUB_CATEGORY_NOT_SURE)
            }
        }

        //координаты и высота
        viewModel.coordsLiveData.observe(viewLifecycleOwner) {
            val coordsText = "${it.latitude}\n${it.longitude}"
            val heightText = "${it.height} ${getString(R.string.metres)}"

            binding.heightText.text = heightText
            binding.openCoordsBtn.text = coordsText
        }

        binding.openCoordsBtn.setOnClickListener {
            openCoordsDialog()
        }
        childFragmentManager.setFragmentResultListener(CoordsDialogFragment.REQUEST_COORDS_KEY, viewLifecycleOwner) { requestKey, bundle ->
            when (requestKey) {
                CoordsDialogFragment.REQUEST_COORDS_KEY -> {
                    val mountainLat = bundle.getString(CoordsDialogFragment.BUNDLE_LAT_KEY, "")
                    val mountainLon = bundle.getString(CoordsDialogFragment.BUNDLE_LON_KEY, "")
                    val mountainHeight = bundle.getString(CoordsDialogFragment.BUNDLE_HEIGHT_KEY, "")
                    viewModel.setMountainCoords(Coords(mountainHeight, mountainLat, mountainLon))
                }
            }
        }

        //дата
        viewModel.dateLiveData.observe(viewLifecycleOwner) {
            binding.openDateBtn.text = DateConverter.fromLongToText(it, DateConverter.FORMAT_DD_MM_YYYY)
        }

        binding.openDateBtn.setOnClickListener {
            openDatePicker()
        }

        //изображения
        viewModel.imageListLiveData.observe(viewLifecycleOwner) {
            updateImageRecyclerView(it)

            if (it.isEmpty()) {
                binding.imageRecyclerView.visibility = View.GONE
            } else {
                binding.imageRecyclerView.visibility = View.VISIBLE
            }
            if (it.size>=MAX_IMAGES_COUNT) {
                binding.addFromCameraBtn.isEnabled = false
                binding.addFromGalleryBtn.isEnabled = false
            } else {
                binding.addFromCameraBtn.isEnabled = true
                binding.addFromGalleryBtn.isEnabled = true
            }
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
                openCamera()
            } else {
                requestPermission(Manifest.permission.CAMERA)
            }
        }

        binding.addMountainBtn.setOnClickListener {
            viewModel.createMountain()
        }

        //камера
        childFragmentManager.setFragmentResultListener(CameraFragment.REQUEST_CAMERA_KEY, viewLifecycleOwner) { requestKey, bundle ->
            when (requestKey) {
                CameraFragment.REQUEST_CAMERA_KEY -> {
                    val image = buildImage(Uri.parse(bundle.getString(CameraFragment.BUNDLE_IMAGE_URI_STRING_KEY)))
                    if (image!=null) {
                        viewModel.addImage(image)
                    }
                }
            }
        }

        viewModel.isMountainEditLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.addMountainBtn.text = getString(R.string.update)
            } else {
                binding.addMountainBtn.text = getString(R.string.add)
            }
        }
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

    private fun openCamera() {
        val coordsDialog = CameraFragment()
        coordsDialog.show(childFragmentManager, TAG_CAMERA_DIALOG_FRAGMENT)
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

    private fun buildImage(uri: Uri?): Image? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && uri!=null) {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            val stringImage = Base64Converter.fromBitmapToBase64(bitmap)
            return Image(Image.DEFAULT_TITLE, stringImage)
        }
        //В задании есть требование по версии ОС не ниже android 29
        return null
    }


    private fun setCategoryAndDifficult(fullCategory: String) {
        println("fullCategory: ${fullCategory}")
        var category = fullCategory
        if (category.isNotEmpty()) {
            val index = category.indexOf(DIFFICULT_ADD)
            binding.chipAddDifficult.isChecked = index!=-1

            if (category.contains(DIFFICULT_CATEGORY_NONE)) {
                binding.chipCategoryNone.isChecked = true
            } else if (category.contains(DIFFICULT_CATEGORY_A1)) {
                binding.chipCategoryA1.isChecked = true
            } else if (category.contains(DIFFICULT_CATEGORY_A2)) {
                binding.chipCategoryA2.isChecked = true
            } else if (category.contains(DIFFICULT_CATEGORY_A3)) {
                binding.chipCategoryA3.isChecked = true
            } else if (category.contains(DIFFICULT_CATEGORY_B1)) {
                binding.chipCategoryB1.isChecked = true
            } else if (category.contains(DIFFICULT_CATEGORY_B2)) {
                binding.chipCategoryB2.isChecked = true
            } else if (category.contains(DIFFICULT_CATEGORY_B3)) {
                binding.chipCategoryB3.isChecked = true
            }
        }
    }

    private fun updateImageRecyclerView(newList: List<Image>) {
        val completible = Completable.fromAction{
            val diff = ImageDiff(imageAdapter.images, newList)
            val diffResult = DiffUtil.calculateDiff(diff)
            imageAdapter.images.clear()
            imageAdapter.images.addAll(newList)
            diffResult.dispatchUpdatesTo(imageAdapter)
        }.subscribe()
        compositeDisposable.add(completible)
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

    private fun openDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(TEXT_SELECT_DATE)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(childFragmentManager, TAG_DATE_PICKER_FRAGMENT)
        datePicker.addOnPositiveButtonClickListener {
            viewModel.setMountainDate(it)
        }
    }

    private fun openCoordsDialog() {
        val coordsDialog = CoordsDialogFragment()
        coordsDialog.show(childFragmentManager, TAG_COORDS_DIALOG_FRAGMENT)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }
}