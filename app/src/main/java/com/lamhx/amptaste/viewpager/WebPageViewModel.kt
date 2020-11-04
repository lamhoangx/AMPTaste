package com.lamhx.amptaste.viewpager

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lamhx.amptaste.entity.DummyData

class WebPageViewModel : ViewModel() {

    val url = MutableLiveData<String>()
    fun setIndex(index: Int) {
        setUrl(DummyData.dummyData[index])
    }

    private fun setUrl(url: String) {
        this.url.value = url
    }

}