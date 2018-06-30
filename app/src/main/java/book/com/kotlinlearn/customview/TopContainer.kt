package book.com.kotlinlearn.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import book.com.kotlinlearn.R
import butterknife.ButterKnife
import butterknife.Unbinder

class TopContainer : LinearLayout {

    var unbinder: Unbinder

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val mView = View.inflate(context, R.layout.customview_top_container, this)
        unbinder = ButterKnife.bind(this, mView)
    }

    fun onDestroy() {
        unbinder.unbind()
    }
}