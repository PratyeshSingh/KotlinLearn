package book.com.kotlinlearn.model

import com.google.gson.annotations.SerializedName

data class ImageData(
        @SerializedName(value = "url_q") val imageUrl: String)