package book.com.kotlinlearn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import book.com.kotlinlearn.R
import book.com.kotlinlearn.model.ImageData
import book.com.kotlinlearn.util.ImageUtil
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MyAdapter::class, LayoutInflater::class,
        Context::class, ViewGroup::class, ImageUtil::class)
class MyAdapterTest {

    internal var URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&";
    var mMyAdapter: MyAdapter? = null
    lateinit var dataModelList: ArrayList<ImageData>

    @Before
    fun setUp() {
        dataModelList = ArrayList();
        val imageModel = ImageData();
        imageModel.imageUrl = URL
        dataModelList.add(imageModel);
        mMyAdapter = MyAdapter(dataModelList);
        assertNotNull(mMyAdapter);
    }

    @Test
    fun updateList() {
        val dataList = java.util.ArrayList<ImageData>()
        val imageModel = ImageData();
        imageModel.imageUrl = URL
        dataList.add(imageModel)
        val spyMyAdapter = PowerMockito.spy(mMyAdapter)

        PowerMockito.doNothing().`when`(spyMyAdapter)?.refreshOnItemUI(Mockito.anyInt(), Mockito.anyInt())
        spyMyAdapter?.updateList(dataList)
        assertEquals(2, spyMyAdapter?.mDataModelList?.size)
        Mockito.verify(spyMyAdapter, Mockito.times(1))?.refreshOnItemUI(Mockito.anyInt(), Mockito.anyInt())
    }

    @Test
    fun getItemViewType() {
        assertEquals(R.layout.item, mMyAdapter?.getItemViewType(1))
    }

    @Test
    fun onCreateViewHolder() {
        PowerMockito.mockStatic(LayoutInflater::class.java)
        val context = PowerMockito.mock(Context::class.java)
        val parent = PowerMockito.mock(ViewGroup::class.java)
        PowerMockito.doReturn(context).`when`(parent).getContext()

        val mockLayoutInflater = PowerMockito.mock(LayoutInflater::class.java)
        PowerMockito.`when`(LayoutInflater.from(context)).thenReturn(mockLayoutInflater)

        val mockView = PowerMockito.mock(View::class.java)
        PowerMockito.doReturn(mockView).`when`(mockLayoutInflater).inflate(R.layout.item, parent, false)

        val image = PowerMockito.mock(ImageView::class.java)
        PowerMockito.doReturn(image).`when`(mockView).findViewById<ImageView>(R.id.image)

        mMyAdapter?.onCreateViewHolder(parent, R.layout.item)
        PowerMockito.verifyStatic(Mockito.times(1))
        LayoutInflater.from(context)

        Mockito.verify(mockLayoutInflater, Mockito.times(1)).inflate(R.layout.item, parent, false)
    }

    @Test
    fun getItemCount() {
        assertEquals(dataModelList.size, mMyAdapter?.getItemCount())
    }

    @Test
    fun onBindViewHolder() {
//        PowerMockito.mockStatic(ImageUtil::class.java)
//        val context = PowerMockito.mock(Context::class.java)
//        PowerMockito.`when`(ImageUtil::class.java.simpleName).thenReturn("ImageUtil")
//        ImageUtil.init(context)
//        val mockImageUtil = PowerMockito.mock(ImageUtil::class.java)
//
//        PowerMockito.whenNew<ImageUtil>("book.com.kotlinlearn.util.ImageUtil")
//                .withArguments(context).thenReturn(mockImageUtil)
//
////        PowerMockito.`when`(mockImageUtil::class.java.simpleName).thenReturn("ImageUtil")
////        Mockito.`when`(ImageUtil.getInstance()).thenReturn(mockImageUtil)
//        PowerMockito.doNothing().`when`(mockImageUtil).loadImage(Mockito.anyString(), Mockito.any(ImageView::class.java))
//
//        val mockBaseViewHolder = PowerMockito.mock(MyAdapter.BaseViewHolder::class.java)
//        mMyAdapter?.onBindViewHolder(mockBaseViewHolder, 0)
//
//        PowerMockito.verifyStatic(Mockito.times(1))
//        ImageUtil.getInstance()
//
//        Mockito.verify(mockImageUtil, Mockito.times(1)).loadImage(Mockito.anyString(), Mockito.any(ImageView::class.java))
    }
}