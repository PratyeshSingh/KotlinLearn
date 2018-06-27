package book.com.kotlinlearn

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import book.com.kotlinlearn.adapter.MyAdapter
import book.com.kotlinlearn.model.ResponseDetails
import book.com.kotlinlearn.presenter.MainActivityPresenter
import book.com.kotlinlearn.util.isNetWorkAvailable
import book.com.kotlinlearn.view.MainActivityView

class MainActivity : AppCompatActivity(), MainActivityView, OnBottomReachedListener, SearchView.OnQueryTextListener {

    protected var query: String? = ""
    internal val QUERY = "QUERY"

    protected var mRecyclerView: RecyclerView? = null

    protected var mMyAdapter: MyAdapter? = null
    protected var progressBar: ProgressBar? = null
    protected var simpleSearchView: SearchView? = null

    protected var currentPage: Int = 0
    protected var totalPages: Int = 0
    protected var reloadOnTextSearch: Boolean = false
    protected var scrolledPosition: Int = 0

    var mMainActivityPresenter: MainActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        mMainActivityPresenter = MainActivityPresenter(this)

        query = savedInstanceState?.getString(QUERY)

        simpleSearchView = findViewById(R.id.search)
        simpleSearchView?.setOnQueryTextListener(this)

        mRecyclerView = findViewById(R.id.list_item)
        progressBar = findViewById(R.id.progressBar)

        val mLayoutManager = GridLayoutManager(this, 3);
        mRecyclerView?.layoutManager = mLayoutManager
    }

    override fun onQueryTextSubmit(typedQuery: String): Boolean {
        query = typedQuery
        currentPage = 0
        reloadOnTextSearch = true
        if (isNetWorkAvailable(this)) {
            mMainActivityPresenter?.fetchAndLoadData(query!!, currentPage)
        } else {
            networkFailed()
        }
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(QUERY, query)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        simpleSearchView = null
        mMainActivityPresenter?.onDestroy()
        mMainActivityPresenter = null
        mRecyclerView = null
        mMyAdapter = null
        hideProgressBar()
        progressBar = null
        currentPage = 0
        super.onDestroy()
    }

    override fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar?.visibility = View.GONE
    }

    override fun setUiOnTaskCompleted(responseDetails: ResponseDetails) {
        if (mMyAdapter == null || reloadOnTextSearch) {
            mMyAdapter = MyAdapter(responseDetails.photos!!.photo!!)
            mMyAdapter!!.onBottomReachedListener = this
        } else {
            mMyAdapter?.updateList(responseDetails.photos!!.photo!!)
            mRecyclerView?.scrollToPosition(scrolledPosition)
        }

        reloadOnTextSearch = false
        currentPage = responseDetails.photos!!.page
        totalPages = responseDetails.photos!!.pages
        mRecyclerView?.setAdapter(mMyAdapter)
    }

    override fun onBottomReached(position: Int) {
        if (currentPage == totalPages) {
            return
        }
        scrolledPosition = position
        if (isNetWorkAvailable(this)) {
            mMainActivityPresenter?.fetchAndLoadData(query!!, currentPage)
        } else {
            networkFailed()
        }
    }

    override fun networkFailed() {
        hideProgressBar()
        Toast.makeText(this, "Internet Connection FAiled \n Try Again later", Toast.LENGTH_LONG).show()
    }
}
