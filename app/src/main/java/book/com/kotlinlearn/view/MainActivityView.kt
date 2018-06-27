package book.com.kotlinlearn.view

import book.com.kotlinlearn.model.ResponseDetails

interface MainActivityView {

    fun setUiOnTaskCompleted(responseDetails: ResponseDetails)

    fun showProgressBar()

    fun hideProgressBar()

    fun networkFailed()
}