package book.com.kotlinlearn.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import book.com.kotlinlearn.OnBottomReachedListener
import book.com.kotlinlearn.R
import book.com.kotlinlearn.model.ImageData
import book.com.kotlinlearn.util.ImageUtil

class MyAdapter constructor(dataModelList: ArrayList<ImageData>) : RecyclerView.Adapter<MyAdapter.BaseViewHolder>() {

    val mDataModelList: ArrayList<ImageData> = dataModelList
    var onBottomReachedListener: OnBottomReachedListener? = null

    fun updateList(dataModelList: List<ImageData>) {
        val startPosition = getItemCount();
        val count = dataModelList.size
        mDataModelList.addAll(dataModelList);
        refreshOnItemUI(startPosition, count)
    }

    fun refreshOnItemUI(startPosition: Int, count: Int) {
        notifyItemRangeInserted(startPosition, count);
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataModelList.size
    }

    fun getItem(position: Int): ImageData {
        return mDataModelList[position]
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val dataModel = getItem(position)
        setImage(dataModel, holder)
        if (position == itemCount - 1) {
            onBottomReachedListener?.onBottomReached(position)
        }
    }

    internal fun setImage(dataModel: ImageData, holder: BaseViewHolder) {
        ImageUtil.getInstance().loadImage(dataModel.imageUrl, holder.image)
    }

    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView

        init {
            image = itemView.findViewById(R.id.image)
        }
    }
}