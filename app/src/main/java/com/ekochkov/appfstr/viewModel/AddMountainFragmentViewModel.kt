package com.ekochkov.appfstr.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekochkov.appfstr.App
import com.ekochkov.appfstr.data.dao.UserDao
import com.ekochkov.appfstr.data.entity.*
import com.ekochkov.appfstr.domain.Interactor
import com.ekochkov.appfstr.utils.PerevalRetrofit
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class AddMountainFragmentViewModel: ViewModel() {

    private var mountainName = ""
    private var mountainCategory = ""
    private var mountainSubCategory = arrayListOf<String>()
    private var mountainDate = 0L
    private var mountainLat = ""
    private var mountainLon = ""
    private var mountainHeight = ""

    val imageListLiveData = MutableLiveData<List<Image>>()

    @Inject
    lateinit var retrofit: PerevalRetrofit

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var interactor: Interactor



    private val compositeDisposable = CompositeDisposable()

    init {
        App.instance.dagger.inject(this)
    }

    fun setMountainName(name: String) {
        mountainName = name
    }

    fun setMountainCategory(category: String) {
        mountainCategory = category
    }

    fun setMountainSubCategory(subCategory: List<String>) {
        mountainSubCategory.clear()
        mountainSubCategory.addAll(subCategory)
    }

    fun setMountainDate(date: Long) {
        mountainDate = date
    }

    fun setMountainCoords(lat: String, lon: String, height: String) {
        mountainLat = lat
        mountainLon = lon
        mountainHeight = height
    }

    fun addImage(image: Image) {
        val oldList = imageListLiveData.value?: arrayListOf()
        val newList = arrayListOf<Image>()
        newList.addAll(oldList)
        newList.add(image)
        imageListLiveData.postValue(newList)
    }

    fun putMountainOnServer() {
        println("!!! put mountain on server")
        val coordsDTO = CoordsDTO(
                height = "test_height",
                latitude = "test_latitude",
                longitude = "test_longtitude")
        val imageDTO = ImageDTO(
                title = "test_title",
                url = "test_url")
        val leveDTO = LevelDTO(
                autumn = "test_autumn",
                spring = "test_spring",
                winter = "test_winter",
                summer = "test_summer")

        //val user = userDao.getUser().value
        val userDTO = UserDTO(
                email = "test_email",
                fam = "test_fam",
                id = 999,
                name = "name",
                phone = "phone")

        val mountainDTO = MountainDTO(
                add_time = "test_time",
                beautyTitle = "test_title",
                connect = "test_connect",
                coords = coordsDTO,
                id = 1,
                images = listOf(imageDTO),
                level = leveDTO,
                other_titles = "test_other_titles",
                title = "test_title",
                type = "pass", //поле обязательно должно быть "pass"
                user = userDTO
        )

        //POST запрос работает
        //retrofit.putMountain(mountainDTO).enqueue(object: retrofit2.Callback<String> {
        //    override fun onResponse(call: Call<String>, response: Response<String>) {
        //        println("!!! onResponse")
        //        println("!!! ${response.body()}")
        //    }
//
        //    override fun onFailure(call: Call<String>, t: Throwable) {
        //        println("!!! onFailure")
        //        println("!!! ${t.printStackTrace()}")
        //    }
//
        //})
        println("!!! put mountain on server done")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

