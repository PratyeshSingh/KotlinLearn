package book.com.kotlinlearn

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.ProgressBar
import book.com.kotlinlearn.adapter.MyAdapter
import book.com.kotlinlearn.presenter.MainActivityPresenter
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MainActivityPresenter::class,
        MainActivity::class, MyAdapter::class)
class MainActivityTest {

    lateinit var spyMainActivity: MainActivity

    @Before
    fun setUp() {
        spyMainActivity = PowerMockito.spy(MainActivity())
        assertNotNull(spyMainActivity)
        spyMainActivity.mMainActivityPresenter = PowerMockito.mock(MainActivityPresenter::class.java)
        spyMainActivity.mRecyclerView = PowerMockito.mock(RecyclerView::class.java)
        spyMainActivity.mMyAdapter = PowerMockito.mock(MyAdapter::class.java);
        spyMainActivity.progressBar = PowerMockito.mock(ProgressBar::class.java);
        spyMainActivity.simpleSearchView = PowerMockito.mock(SearchView::class.java);
    }

    @After
    fun tearDown() {
        spyMainActivity.mMainActivityPresenter = null
        spyMainActivity.mRecyclerView = null
        spyMainActivity.mMyAdapter = null
        spyMainActivity.progressBar = null
        spyMainActivity.simpleSearchView = null
    }

    @Test
    fun onQueryTextSubmit() {
        val mockConnectivityManager = PowerMockito.mock(ConnectivityManager::class.java)
        PowerMockito.doReturn(mockConnectivityManager).`when`(spyMainActivity).getSystemService(Context.CONNECTIVITY_SERVICE)

        PowerMockito.doNothing().`when`(spyMainActivity).networkFailed()
        spyMainActivity.onQueryTextSubmit("shirt")
        verify(spyMainActivity, times(1)).networkFailed()
        assertEquals("shirt", spyMainActivity.query)
        assertEquals(0, spyMainActivity.currentPage)
        assertTrue(spyMainActivity.reloadOnTextSearch)

        val activeNetworkInfo = PowerMockito.mock(NetworkInfo::class.java)
        PowerMockito.doReturn(activeNetworkInfo).`when`(mockConnectivityManager).activeNetworkInfo
        PowerMockito.doReturn(true).`when`(activeNetworkInfo).isAvailable
        PowerMockito.doReturn(true).`when`(activeNetworkInfo).isConnected
        spyMainActivity.onQueryTextSubmit("shirt")
        verify(spyMainActivity.mMainActivityPresenter, times(1))?.fetchAndLoadData(Matchers.anyString(), Matchers.anyInt())
        assertEquals("shirt", spyMainActivity.query)
        assertEquals(0, spyMainActivity.currentPage)
        assertTrue(spyMainActivity.reloadOnTextSearch)
    }

    @Test
    fun showProgressBar() {
        spyMainActivity.showProgressBar()
        assertEquals(View.VISIBLE, spyMainActivity.progressBar?.visibility)
        verify(spyMainActivity.progressBar, times(1))?.visibility
    }

    @Test
    fun hideProgressBar() {
//        spyMainActivity.hideProgressBar()
////        assertEquals(View.GONE, spyMainActivity.progressBar?.visibility)
//        verify(spyMainActivity.progressBar, times(1))?.visibility
    }

    @Test
    fun setUiOnTaskCompleted() {

    }

    @Test
    fun onBottomReached() {

    }

    @Test
    fun networkFailed() {
        val msg = "Internet Connection FAiled \n Try Again later";
        PowerMockito.doNothing().`when`(spyMainActivity).showToast(spyMainActivity, msg)
        PowerMockito.doNothing().`when`(spyMainActivity).hideProgressBar()
        spyMainActivity.networkFailed()
        verify(spyMainActivity, times(1)).hideProgressBar()
        verify(spyMainActivity, times(1)).showToast(spyMainActivity, msg)
    }

}