package book.com.kotlinlearn.controller

import android.os.AsyncTask
import android.util.Log
import book.com.kotlinlearn.model.ResponseDetails
import book.com.kotlinlearn.util.GsonUtil
import com.squareup.okhttp.Call
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.io.IOException

class MyAsyncTask constructor(url: String, listener: MyAsyncTaskListener) :
        AsyncTask<Void, Void, ResponseDetails>() {

    private val TAG = MyAsyncTask::class.java.simpleName

    private val mMyAsyncTaskListener: MyAsyncTaskListener = listener
    private val url: String = url

    private var isCanceled: Boolean = false
    lateinit var call: Call

    override fun doInBackground(vararg params: Void?): ResponseDetails? {
        var responseDetails: ResponseDetails? = null

        val client = OkHttpClient()
        Log.d("$TAG : url", url + "")
        val httpRequest = Request.Builder().url(url).build()

        call = client.newCall(httpRequest)
        try {
            val httpResponse = call.execute()

            val status = httpResponse.code()
            val responseJson = httpResponse.body().string()
            Log.d("$TAG : status", status.toString() + "")
            Log.d("$TAG : responseJson", responseJson)

            responseDetails = GsonUtil.fromJson(responseJson, ResponseDetails::class.java, url)
        } catch (e: IOException) {
            Log.e("$TAG : ERR", e.message)
        }

        return responseDetails;
    }

    fun cancelActive() {
        call.cancel()
        isCanceled = true
        cancel(true)
    }

    override fun onPostExecute(responseDetails: ResponseDetails?) {
        if (!isCanceled) {
            responseDetails?.let {
                mMyAsyncTaskListener.onTaskCompleted(responseDetails)
            }
        }
        super.onPostExecute(responseDetails)
    }

    interface MyAsyncTaskListener {
        fun onTaskCompleted(responseDetails: ResponseDetails)
    }
}