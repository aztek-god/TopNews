package dv.serg.topnews.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "article")
data class Article(
        @Ignore
        @SerializedName("source")
        @Expose
        var source: Source? = null,
        @SerializedName("author")
        @Expose
        var author: String? = null,
        @SerializedName("title")
        @Expose
        var title: String? = null,
        @SerializedName("description")
        @Expose
        var description: String? = null,
        @SerializedName("url")
        @Expose
        var url: String? = null,
        @ColumnInfo(name = "url_to_image")
        @SerializedName("urlToImage")
        @Expose
        var urlToImage: String? = null,
        @ColumnInfo(name = "published_at")
        @SerializedName("publishedAt")
        @Expose
        var publishedAt: String? = null) {
    @ColumnInfo(name = "source_name")
    var sourceName: String? = source?.name

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "type")
    var type: Type? = null


    enum class Type : Serializable {
        HISTORY, BOOKMARK;
    }

    class TypeConverter {
        companion object {
            @JvmStatic
            @android.arch.persistence.room.TypeConverter
            fun fromTypeToInt(type: Type): Int {
                return type.ordinal
            }

            @JvmStatic
            @android.arch.persistence.room.TypeConverter
            fun fromIntToType(index: Int): Type {
                return Type.values()[index]
            }
        }
    }
}
