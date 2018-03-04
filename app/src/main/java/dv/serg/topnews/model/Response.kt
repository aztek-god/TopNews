package dv.serg.topnews.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Response(

        @SerializedName("status")
        @Expose
        var status: String? = null,
        @SerializedName("totalResults")
        @Expose
        var totalResults: Int,
        @SerializedName("articles")
        @Expose
        var articles: List<Article>? = null

)
