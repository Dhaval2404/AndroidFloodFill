package com.github.dhaval2404.floodfill.sample.screens.base

import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

/**
 * Base ViewModel
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 22 May 2020
 */
open class BaseViewModel<N> : ViewModel(), KoinComponent {

    protected var mNavigator: N? = null

    fun setNavigator(navigator: N) {
        this.mNavigator = navigator
    }

}