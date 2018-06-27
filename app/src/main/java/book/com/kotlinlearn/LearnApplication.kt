package book.com.kotlinlearn

import android.app.Application
import book.com.kotlinlearn.util.ImageUtil

class LearnApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ImageUtil.init(this)
    }
}