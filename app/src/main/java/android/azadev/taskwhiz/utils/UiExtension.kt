package android.azadev.taskwhiz.utils

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/14/2024
 */

object UiExtension {

    fun Spinner.selected(action: (position: Int, TextView) -> Unit) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                action(position, view as TextView)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun View.visibleIf(boolean: Boolean) {
        if (boolean) this.visibility = View.VISIBLE
        else this.visibility = View.INVISIBLE
    }
}