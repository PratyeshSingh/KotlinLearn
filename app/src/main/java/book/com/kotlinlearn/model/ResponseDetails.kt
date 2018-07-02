package book.com.kotlinlearn.model

import com.google.gson.annotations.SerializedName

data class ResponseDetails(
        @SerializedName("photos") val photos: Photo?,
        @SerializedName("stat") val stat: String?
)