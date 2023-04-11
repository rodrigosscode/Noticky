package br.com.sscode.ui.extension

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import br.com.sscode.core.FIRST_POSITION

@ColorInt
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int = obtainStyledAttributes(intArrayOf(themeAttrId)).let { typedArray ->
    typedArray.getColor(FIRST_POSITION, Color.WHITE).let { returnColor: Int ->
        typedArray.recycle()
        returnColor
    }
}
