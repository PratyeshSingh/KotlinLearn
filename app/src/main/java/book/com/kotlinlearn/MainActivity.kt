package book.com.kotlinlearn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import book.com.kotlinlearn.adapter.MyAdapter
import book.com.kotlinlearn.model.ResponseDetails
import book.com.kotlinlearn.presenter.MainActivityPresenter
import book.com.kotlinlearn.util.isNetWorkAvailable
import book.com.kotlinlearn.view.MainActivityView

class MainActivity : AppCompatActivity(), MainActivityView, OnBottomReachedListener, SearchView.OnQueryTextListener {

    var query: String? = ""
    private val QUERY = "QUERY"

    var mRecyclerView: RecyclerView? = null

    var mMyAdapter: MyAdapter? = null
    var progressBar: ProgressBar? = null
    var simpleSearchView: SearchView? = null

    var currentPage: Int = 0
    var totalPages: Int = 0
    var reloadOnTextSearch: Boolean = false
    var scrolledPosition: Int = 0

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.second_activity -> {
                val intent: Intent = SecondActivity.getInstance(this, 1, "Second Activity")
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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
        showToast(this, "Internet Connection FAiled \n Try Again later")
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}
