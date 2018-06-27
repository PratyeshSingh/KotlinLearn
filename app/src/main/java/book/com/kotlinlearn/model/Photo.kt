package book.com.kotlinlearn.model

import com.google.gson.annotations.SerializedName

class Photo {
    @SerializedName("photo")
    var photo: ArrayList<ImageData>? = null
    @SerializedName("page")
    var page: Int = 0
    @SerializedName("pages")
    var pages: Int = 0
    @SerializedName("perpage")
    var perpage: Int = 0
    @SerializedName("total")
    var total: Int = 0
}