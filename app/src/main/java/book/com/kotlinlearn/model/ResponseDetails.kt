package book.com.kotlinlearn.model

import com.google.gson.annotations.SerializedName

class ResponseDetails {
    @SerializedName("photos")
    var photos: Photo? = null
    @SerializedName("stat")
    var stat: String? = ""
}