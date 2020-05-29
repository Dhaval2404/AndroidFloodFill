package com.github.dhaval2404.floodfill.sample.data.shared_pref

import android.content.Context
import android.content.SharedPreferences
import java.util.Collections

/**
 * SharedPreferences Manager class
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 24 May 2020
 */
class SharedPrefManager(context: Context, prefName: String) {

    private val mSharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     *         otherwise false.
     */
    fun contains(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }

    fun getString(key: String): String? {
        return get(key, null)
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     *
     **/
    fun get(key: String, defValue: String?): String? {
        return mSharedPreferences.getString(key, defValue)
    }

    fun getStringSet(key: String): Set<String>? {
        return get(key, Collections.emptySet())
    }

    /**
     * Retrieve a set of String values from the preferences.
     *
     * <p>Note that you <em>must not</em> modify the set instance returned
     * by this call.  The consistency of the stored data is not guaranteed
     * if you do, nor is your ability to modify the instance at all.
     *
     * @param key The name of the preference to retrieve.
     * @param defValues Values to return if this preference does not exist.
     *
     * @return Returns the preference values if they exist, or defValues.
     * Throws ClassCastException if there is a preference with this name
     * that is not a Set.
     *
     */
    fun get(key: String, defValues: MutableSet<String>): MutableSet<String> {
        val set = mSharedPreferences.getStringSet(key, defValues)
        if (set != null) return set
        return Collections.emptySet()
    }

    fun getInt(key: String): Int {
        return get(key, 0)
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * an int.
     *
     */
    fun get(key: String, defValue: Int): Int {
        return mSharedPreferences.getInt(key, defValue)
    }

    fun getLong(key: String): Long {
        return get(key, 0L)
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a long.
     *
     */
    fun get(key: String, defValue: Long): Long {
        return mSharedPreferences.getLong(key, defValue)
    }

    fun getFloat(key: String): Float {
        return get(key, 0.0f)
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a float.
     *
     */
    fun get(key: String, defValue: Float): Float {
        return mSharedPreferences.getFloat(key, defValue)
    }

    fun getBoolean(key: String): Boolean {
        return get(key, false)
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a boolean.
     *
     */
    fun get(key: String, defValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defValue)
    }

    /**
     * Set a Any value in the preferences editor, to be written back once
     * commit is called.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun put(key: String, value: Any?) {
        val editor = mSharedPreferences.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Float -> editor.putFloat(key, value)
            is Double -> editor.putFloat(key, value.toFloat())
            is Long -> editor.putLong(key, value)
            is String -> editor.putString(key, value)
            is Enum<*> -> editor.putString(key, value.toString())
            null -> editor.remove(key)
            else -> {
                if (value is Set<*>) {
                    editor.putStringSet(key, (value as Collection<*>).map { it.toString() }.toSet())
                } else {
                    throw RuntimeException("Attempting to save non-supported preference")
                }
            }
        }
        editor.apply()
    }

    /**
     * Mark in the editor to remove <em>all</em> values from the
     * preferences.  Once commit is called, the only remaining preferences
     * will be any that you have defined in this editor.
     *
     * <p>Note that when committing back to the preferences, the clear
     * is done first, regardless of whether you called clear before
     * or after put methods on this editor.
     *
     * @return Returns true if the SharedPreferences clear successfully
     */
    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    /**
     * Mark in the editor that a preference value should be removed, which
     * will be done in the actual preferences once commit is called.
     *
     * <p>Note that when committing back to the preferences, all removals
     * are done first, regardless of whether you called remove before
     * or after put methods on this editor.
     *
     * @param key The name of the preference to remove.
     *
     * @return Returns true if the SharedPreferences remove key successfully
     */
    fun remove(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }
}
