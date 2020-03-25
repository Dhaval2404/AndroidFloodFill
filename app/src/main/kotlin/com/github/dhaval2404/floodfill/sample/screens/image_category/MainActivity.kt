package com.github.dhaval2404.floodfill.sample.screens.image_category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.floodfill.sample.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), MainNavigator {

    private val mViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel.setNavigator(this)
        imageCategoryRV.adapter = mViewModel.getAdapter()
    }

}