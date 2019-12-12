package com.github.dhaval2404.floodfill.sample.model

import java.io.Serializable

data class DrawMove(
    val color: Int,
    val points: FloatArray
) : Serializable {

    /* Android Studio auto generated method
     * For: array property in class need to override equals() / hashcode()
     **/
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DrawMove

        if (color != other.color) return false
        if (!points.contentEquals(other.points)) return false

        return true
    }

    /* Android Studio auto generated method
     * For: array property in class need to override equals() / hashcode()
     **/
    override fun hashCode(): Int {
        var result = color
        result = 31 * result + points.contentHashCode()
        return result
    }
}
