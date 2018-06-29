package book.com.kotlinlearn.presenter

import book.com.kotlinlearn.controller.MyAsyncTask
import book.com.kotlinlearn.model.ResponseDetails
import book.com.kotlinlearn.util.UrlUtils
import book.com.kotlinlearn.view.MainActivityView

class MainActivityPresenter constructor(mainActivityView: MainActivityView) : MyAsyncTask.MyAsyncTaskListener {
    internal var mMainActivityView: MainActivityView? = null
    internal var mMyAsyncTask: MyAsyncTask? = null

    init {
        mMainActivityView = mainActivityView
    }

    fun fetchAndLoadData(query: String, currentPage: Int) {
        mMainActivityView?.showProgressBar()
        val completeUrl: String = UrlUtils.getUrl(query, (1 + currentPage).toString());
        mMyAsyncTask = MyAsyncTask(completeUrl, this);
        executeApiCall()
    }

    fun executeApiCall() {
        mMyAsyncTask?.execute();
    }

    fun onDestroy() {
        mMyAsyncTask?.cancelActive()
        mMyAsyncTask = null
        mMainActivityView = null
    }

    override fun onTaskCompleted(responseDetails: ResponseDetails) {
        mMainActivityView?.hideProgressBar()
        responseDetails.photos?.let {
            mMainActivityView?.setUiOnTaskCompleted(responseDetails)
        }
    }
}