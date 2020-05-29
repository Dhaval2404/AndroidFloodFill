package com.github.dhaval2404.floodfill.sample.screens.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.github.dhaval2404.floodfill.sample.R

/**
 * Base Activity
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 23 May 2020
 */
abstract class BaseActivity<VM : ViewModel>(@LayoutRes contentLayoutId: Int) :
    AppCompatActivity(contentLayoutId) {

    protected lateinit var mViewModel: VM

    abstract fun getViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun startActivity(intent: Intent) {
        val animBundle = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.activity_slide_in_1,
            R.anim.activity_slide_in_2
        ).toBundle()
        startActivity(intent, animBundle)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(
            R.anim.activity_slide_from_left,
            R.anim.activity_slide_to_right
        )
    }

}