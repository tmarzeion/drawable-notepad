package com.example.tomek.notepad.epoxy

import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.example.tomek.notepad.R
import kotlinx.android.synthetic.main.no_content_layout.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class NoContentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.no_content_layout, this)
    }

    @TextProp(defaultRes = R.string.no_notes)
    fun setDescription(text: CharSequence) {
        descriptionTextView.setText(text)
    }

    @ModelProp
    fun setColor(@ColorRes color: Int) {
        var color = color
        if (color == 0) color = R.color.colorPrimary
        descriptionTextView.setTextColor(ContextCompat.getColor(context, color))
        imageView.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
    }

    @ModelProp
    fun setDrawable(@DrawableRes drawable: Int) {
        var drawable = drawable
        if (drawable == 0) drawable = R.drawable.no_content
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawable))
    }

}
