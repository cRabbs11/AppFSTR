package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.entity.*
import com.ekochkov.appfstr.domain.Interactor
import com.ekochkov.appfstr.utils.Constants
import com.ekochkov.appfstr.utils.Constants.MOUNTAIN_UPDATE_SUCCESS_VALUE
import com.ekochkov.appfstr.utils.Constants.NOT_FIND_MOUNTAIN_IN_DB
import com.ekochkov.appfstr.utils.PerevalRetrofit
import com.ekochkov.appfstr.utils.SingleLiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class AddMountainFragmentViewModel: ViewModel() {

    var isMountainEditLiveData = MutableLiveData<Boolean>()
    var dateLiveData = MutableLiveData<Long>()
    var mountainNameLiveData = MutableLiveData<String>()
    var coordsLiveData = MutableLiveData<Coords>()
    var imageListLiveData = MutableLiveData<List<Image>>()
    var categoryLiveData = MutableLiveData<String>()
    var subCategoryLiveData = MutableLiveData<List<String>>()
    var userLiveData : LiveData<User?>
    val toastEventLiveData = SingleLiveEvent<String>()
    private var cashedId = Constants.MOUNTAIN_CASHED_ID_VALUE_DEFAULT

    @Inject
    lateinit var retrofit: PerevalRetrofit

    @Inject
    lateinit var interactor: Interactor

    private val compositeDisposable = CompositeDisposable()

    init {
        App.instance.dagger.inject(this)
        setMountainDate(Date().time)
        isMountainEditLiveData.postValue(false)
        userLiveData = interactor.getUserLiveData()
    }

    fun updateImage(oldImage: Image, newImage: Image) {
        val oldList = imageListLiveData.value?: arrayListOf()
        if (oldList.contains(oldImage)) {
            val newList = arrayListOf<Image>()
            newList.addAll(oldList)
            val index = newList.indexOf(oldImage)
            newList.add(index, newImage)
            newList.remove(oldImage)
            imageListLiveData.postValue(newList)
        }
    }

    fun addImage(image: Image) {
        val oldList = imageListLiveData.value?: arrayListOf()
        val newList = arrayListOf<Image>()
        newList.addAll(oldList)
        newList.add(image)
        imageListLiveData.postValue(newList)
    }

    /**
     * достает категорию из mountain
     */
    private fun getCategoryFromMountain(mountain: Mountain): String {
        var category = ""
        if (!mountain.addTime.isNullOrEmpty()) {
            val calendar = Calendar.getInstance()
            calendar.time = Date(mountain.addTime!!.toLong())
            val monthInt = calendar.get(Calendar.MONTH)
            when (monthInt) {
                in 0..1, 11 -> { category = mountain.level!!.winter }
                in 2..4 -> { category = mountain.level!!.spring }
                in 5..7 -> { category = mountain.level!!.summer }
                in 8..10 -> { category = mountain.level!!.autumn }
            }
        }
        return category
    }

    /**
     * достает подкатегории ("оценочно", "не уверен") из mountain
     */
    private fun getSubCategoryFromMountain(mountain: Mountain): List<String> {
        var categoryList = arrayListOf<String>()
        var category = ""
        if (!mountain.addTime.isNullOrEmpty()) {
            val calendar = Calendar.getInstance()
            calendar.time = Date(mountain.addTime!!.toLong())
            val monthInt = calendar.get(Calendar.MONTH)
            when (monthInt) {
                in 0..1, 11 -> { category = mountain.level!!.winter }
                in 2..4 -> { category = mountain.level!!.spring }
                in 5..7 -> { category = mountain.level!!.summer }
                in 8..10 -> { category = mountain.level!!.autumn }
            }
        }
        if (category.contains(Mountain.SUB_CATEGORY_ESTIMATED)) {
            categoryList.add(Mountain.SUB_CATEGORY_ESTIMATED)
        }
        if (category.contains(Mountain.SUB_CATEGORY_NOT_SURE)) {
            categoryList.add(Mountain.SUB_CATEGORY_NOT_SURE)
        }
        return categoryList
    }

    fun setEditMountain(mountainCahsedId: Int) {
        isMountainEditLiveData.postValue(true)
        Completable.fromAction{
            val mountain = interactor.getMountainFromBD(mountainCahsedId)
            if (mountain!=null) {
                mountainNameLiveData.postValue(mountain.title)
                coordsLiveData.postValue(mountain.coords)
                imageListLiveData.postValue(mountain.images)
                dateLiveData.postValue(mountain.addTime?.toLong())
                categoryLiveData.postValue(getCategoryFromMountain(mountain))
                subCategoryLiveData.postValue(getSubCategoryFromMountain(mountain))
                cashedId = mountain.cashedID
            } else {
                toastEventLiveData.postValue(NOT_FIND_MOUNTAIN_IN_DB)
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun setMountainName(name: String) {
        mountainNameLiveData.postValue(name)
    }

    fun removeImage(image: Image) {
        val oldList = imageListLiveData.value
        if (!oldList.isNullOrEmpty() && oldList.contains(image)) {
            val newList = arrayListOf<Image>()
            newList.addAll(oldList)
            newList.remove(image)
            imageListLiveData.postValue(newList)
        }
    }

    fun setCategory(category: String) {
        categoryLiveData.postValue(category)
    }

    fun addToSubCategory(subCategory: String) {
        val subCategoryList = subCategoryLiveData.value
        if (subCategoryList.isNullOrEmpty()) {
            subCategoryLiveData.postValue(listOf(subCategory))
        } else if (!subCategoryList.contains(subCategory)){
            val newList = arrayListOf<String>()
            newList.addAll(subCategoryList)
            newList.add(subCategory)
            subCategoryLiveData.postValue(newList)
        }
    }

    fun removeFromSubCategory(subCategory: String) {
        val subCategoryList = subCategoryLiveData.value
        if (!subCategoryList.isNullOrEmpty() && subCategoryList.contains(subCategory)) {
            val newList = arrayListOf<String>()
            newList.addAll(subCategoryList)
            newList.remove(subCategory)
            subCategoryLiveData.postValue(newList)
        }
    }

    fun setMountainDate(date: Long) {
        dateLiveData.postValue(date)
    }

    fun setMountainCoords(coords: Coords) {
        coordsLiveData.postValue(coords)
    }

    fun createMountain() {
        val complateble = Completable.fromAction {
            val mountain = fillMountain()
            if (isInputDataIsCorrect()) {
                if (isMountainEditLiveData.value!!) {
                    mountain.cashedID = cashedId

                    val result = interactor.updateMountainInDB(mountain)
                    println("!!! result: $result")
                    if (result==MOUNTAIN_UPDATE_SUCCESS_VALUE) {
                        toastEventLiveData.postValue(Constants.MOUNTAIN_UPDATE_SUCCESS)
                    } else {
                        toastEventLiveData.postValue(Constants.MOUNTAIN_UPDATE_FAILURE)
                    }
                } else {
                    val result = interactor.addMountainInBD(mountain)
                    if (result!=-1L) {
                        toastEventLiveData.postValue(Constants.MOUNTAIN_ADD_TO_DB_SUCCESS)
                    } else {
                        toastEventLiveData.postValue(Constants.MOUNTAIN_ADD_TO_DB_FAILURE)
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe()

        compositeDisposable.add(complateble)
    }

    private fun getLevel(date: Long, category: String): Level {
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        val month = calendar.get(Calendar.MONTH)

        var winter = ""
        var spring = ""
        var summer = ""
        var autumn = ""

        when (month) {
            in 0..1, 11 -> { winter = category }
            in 2..4 -> { spring = category }
            in 5..7 -> { summer = category }
            in 8..10 -> { autumn = category }
        }

        return Level(winter = winter, spring = spring, summer = summer, autumn = autumn)
    }

    private fun fillMountain(): Mountain {
        val date = dateLiveData.value.toString()?: Date().time.toString()
        var fullCategory = "${categoryLiveData.value}"
        println("clear category: ${fullCategory}")
        subCategoryLiveData.value?.forEach {
            if (it.isNotEmpty()) {
                fullCategory += " (${it}) "
            }
        }
        println("fill category: ${fullCategory}")
        val level = getLevel(dateLiveData.value?: Date().time, fullCategory)

        val mountain = Mountain(
            addTime = date,
            beautyTitle = "test_title",
            connect = "test_connect",
            coords = coordsLiveData.value,
            images = imageListLiveData.value?: arrayListOf<Image>(),
            level = level,
            otherTitles = "test_other_titles",
            title = mountainNameLiveData.value,
            type = Mountain.TYPE_PASS, //поле обязательно должно быть "pass"
            userId = userLiveData.value?.id,
            id = Mountain.ID_NOT_SET
        )
        return mountain
    }

    private fun isInputDataIsCorrect(): Boolean {
        if (mountainNameLiveData.value.isNullOrEmpty()) {
            toastEventLiveData.postValue(Constants.NOT_FIND_MOUNTAIN_NAME)
            return false
        }
        if (categoryLiveData.value.isNullOrEmpty()) {
            toastEventLiveData.postValue(Constants.NOT_FIND_MOUNTAIN_CATEGORY)
            return false
        }
        if (dateLiveData.value==null) {
            toastEventLiveData.postValue(Constants.NOT_FIND_MOUNTAIN_DATE)
            return false
        }
        if (coordsLiveData.value==null) {
            toastEventLiveData.postValue(Constants.NOT_FIND_MOUNTAIN_COORDS)
            return false
        }
        if (imageListLiveData.value.isNullOrEmpty()) {
            toastEventLiveData.postValue(Constants.NOT_FIND_MOUNTAIN_IMAGE)
            return false
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

