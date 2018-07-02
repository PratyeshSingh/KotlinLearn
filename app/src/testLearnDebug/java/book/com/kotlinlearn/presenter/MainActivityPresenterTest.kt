package book.com.kotlinlearn.presenter

import android.net.Uri
import book.com.kotlinlearn.model.ImageData
import book.com.kotlinlearn.model.Photo
import book.com.kotlinlearn.model.ResponseDetails
import book.com.kotlinlearn.util.UrlUtils
import book.com.kotlinlearn.view.MainActivityView
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class,
        MainActivityPresenter::class)
class MainActivityPresenterTest {

    internal var URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&";
    internal var mMainActivityPresenter: MainActivityPresenter? = null
    internal var mockMainActivityView: MainActivityView? = null

    @Before
    fun setUp() {
        mockMainActivityView = PowerMockito.mock(MainActivityView::class.java)
        assertNotNull(mockMainActivityView)
        mMainActivityPresenter = MainActivityPresenter(mockMainActivityView!!)
        assertNotNull(mMainActivityPresenter)
    }

    @After
    fun tearDown() {
        mockMainActivityView = null
        mMainActivityPresenter = null
    }

    @SuppressWarnings("unchecked")
    @Test
    fun fetchAndLoadData() {
        PowerMockito.mockStatic(Uri::class.java)
        val spyMainActivityPresenter = PowerMockito.spy(mMainActivityPresenter)
        PowerMockito.doNothing().`when`(spyMainActivityPresenter)?.executeApiCall();

        val mockUri = PowerMockito.mock(Uri::class.java)
        val mockUriBuilder = PowerMockito.mock(Uri.Builder::class.java)

        `when`(Uri.parse(UrlUtils.URL)).thenReturn(mockUri)
        `when`(mockUri.buildUpon()).thenReturn(mockUriBuilder)
        `when`(mockUriBuilder.clearQuery()).thenReturn(mockUriBuilder)
        `when`(mockUriBuilder.build()).thenReturn(mockUri)

        spyMainActivityPresenter?.fetchAndLoadData("url", 1)
        verify(spyMainActivityPresenter?.mMainActivityView, times(1))?.showProgressBar()
        verify(spyMainActivityPresenter, times(1))?.executeApiCall()
    }

    @Test
    fun onDestroy() {
        mMainActivityPresenter!!.onDestroy()
        assertNull(mMainActivityPresenter!!.mMyAsyncTask)
        assertNull(mMainActivityPresenter!!.mMainActivityView)
    }

    @Test
    fun onTaskCompleted() {
        val dataModelList = ArrayList<ImageData>()
        val imageData = ImageData(URL)
        dataModelList.add(imageData)
        val mPhoto = Photo(dataModelList,0,0,100,10)
        val responseDetails = ResponseDetails(mPhoto,"200")

        mMainActivityPresenter?.onTaskCompleted(responseDetails)
        verify(mMainActivityPresenter?.mMainActivityView, times(1))?.hideProgressBar()
        verify(mMainActivityPresenter?.mMainActivityView, times(1))?.setUiOnTaskCompleted(responseDetails)
    }
}