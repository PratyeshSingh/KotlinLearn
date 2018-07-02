package book.com.kotlinlearn.model

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("photo") val photo: ArrayList<ImageData>?,
        @SerializedName("page") val page: Int,
        @SerializedName("pages") val pages: Int,
        @SerializedName("perpage") val perpage: Int,
        @SerializedName("total") val total: Int
)