package com.dscfuta.topnotch

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */
class Utils {

    companion object {

        fun setColorFromText(firstLetter: String, context: Context): Drawable?{
            return when(firstLetter){

                "A" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_a)
                "B" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_b)
                "C" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_c)
                "D" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_d)
                "E" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_e)
                "F" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_f)
                "G" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_g)
                "H" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_h)
                "I" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_i)
                "J" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_j)
                "K" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_k)
                "L" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_l)
                "M" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_m)
                "N" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_n)
                "O" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_o)
                "P" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_p)
                "Q" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_q)
                "R" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_r)
                "S" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_s)
                "T" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_t)
                "U" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_u)
                "V" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_v)
                "W" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_w)
                "X" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_x)
                "Y" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_y)
                "Z" ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_z)



                else ->  ContextCompat.getDrawable(context, R.drawable.text_label_background_a)

            }
        }

    }


}