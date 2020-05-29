package com.github.dhaval2404.floodfill.sample.data.repository

import com.github.dhaval2404.floodfill.sample.data.shared_pref.SharedPrefManager

/**
 * Manage App Preferences
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 23 May 2020
 */
interface AppPrefRepository {

    fun setRecentPaletteColor(color: String)

    fun getRecentPaletteColor(): String?

    fun setRecentColor(color: Int)

    fun getRecentColor(): Int

}

class AppPrefRepositoryImpl(private val prefManager: SharedPrefManager) : AppPrefRepository {

    companion object {
        private const val RECENT_PALETTE_COLOR = "recent_palette_color"
        private const val RECENT_COLOR = "recent_color"
    }

    override fun setRecentPaletteColor(color: String) {
        prefManager.put(RECENT_PALETTE_COLOR, color)
    }

    override fun getRecentPaletteColor() = prefManager.getString(RECENT_PALETTE_COLOR)

    override fun setRecentColor(color: Int) {
        prefManager.put(RECENT_COLOR, color)
    }

    override fun getRecentColor() = prefManager.get(RECENT_COLOR, 0)

}