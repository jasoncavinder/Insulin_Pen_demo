/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.utilities

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter


object BindingUtils {
    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: TextView, list: List<String>?) {
        view.text = list?.joinToString { it -> it }
    }


    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun setImageUri(view: ImageView, imageUri: String?) {
        when (imageUri) {
            null -> view.setImageURI(null)
            else -> view.setImageURI(Uri.parse(imageUri))
        }
    }

    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun setImageUri(view: ImageView, imageUri: Uri) {
        view.setImageURI(imageUri)
    }

    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun setImageDrawable(view: ImageView, drawable: Drawable) {
        view.setImageDrawable(drawable)
    }

    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

}

/*
object DataBindingAdapters {

}
*/

/*public class BindingUtils {
    @BindingAdapter("android:text")
    public static void setFloat(TextView view, float value) {
        if (Float.isNaN(value)) view.setText("");
        else view.setText( ... you custom formatting );
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static float getFloat(TextView view) {
        String num = view.getText().toString();
        if(num.isEmpty()) return 0.0F;
        try {
            return Float.parseFloat(num);
        } catch (NumberFormatException e) {
            return 0.0F;
        }
    }
}*/
