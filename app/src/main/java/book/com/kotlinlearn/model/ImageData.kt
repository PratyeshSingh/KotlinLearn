package book.com.kotlinlearn.model

import com.google.gson.annotations.SerializedName

class ImageData {
    @SerializedName(value = "url_q")
    var imageUrl: String = ""
}