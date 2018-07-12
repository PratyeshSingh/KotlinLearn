package book.com.kotlinlearn.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import book.com.kotlinlearn.R
import butterknife.*

class BottomContainer(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        LinearLayout(context, attrs, defStyleAttr) {

    @BindView(R.id.textView2)
    lateinit var textView2: TextView
    @BindView(R.id.textView)
    lateinit var mTextView: TextView

    lateinit var unbinder: Unbinder

    @JvmField
    @BindString(R.string.first_name)
    var firstName: String? = ""

    @JvmField
    @BindString(R.string.second_name)
    var secondName: String? = ""

    constructor(context: Context) : this(context, null)

    //    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val view: View = inflate(context, R.layout.customview_bottom_container, this)
//        unbinder = ButterKnife.bind(this, view)
        unbinder = ButterKnife.bind(view)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder.unbind()
    }

    @OnClick(R.id.textView, R.id.textView2)
    fun onClick(view: View) {
        Log.d("BottomContainer :", "onClick")
        when (view.id) {
            R.id.textView -> mTextView.text = firstName
            R.id.textView2 -> textView2.text = secondName
            else -> {
                Log.d("BottomContainer :", "onClick >> else")
            }
        }
    }

}