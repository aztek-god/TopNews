package dv.serg.topnews.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "news_resource")
data class SubSource(@PrimaryKey(autoGenerate = false) val id: Int,
                     val name: String,
                     val description: String,
                     val code: String,
                     @ColumnInfo(name = "image_uri")
                     val imageUri: String) {
    @Ignore
    var isSelected: Boolean = false
}


fun getResources(): List<SubSource> = listOf(
        SubSource(1, "Lenta", "Новости, статьи, фотографии, видео. Семь дней в неделю, 24 часа в сутки.", "lenta", "lenta"),
        SubSource(2, "Lenta", "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.", "buzzfeed", "lenta"),
        SubSource(3, "Lenta", "The home of BBC Sport online. Includes live sports coverage, breaking news, results, video, audio and analysis on Football, F1, Cricket, Rugby Union, Rugby League, Golf, Tennis and all the main world sports, plus major events such as the Olympic Games.\n" +
                "\n", "axios", "lenta"),
        SubSource(4, "Lenta", "description", "bloomberg", "lenta"),
        SubSource(5, "Lenta", "description", "cnn", "lenta"),
        SubSource(6, "Lenta", "description", "cnbc", "lenta"),
        SubSource(7, "Lenta", "description", "engadget", "lenta"),
        SubSource(8, "Lenta", "description", "espn", "lenta"),
        SubSource(9, "Lenta", "description", "focus", "lenta"),
        SubSource(10, "Lenta", "description", "fortune", "lenta"),
        SubSource(11, "Lenta", "description", "globo", "lenta"),
        SubSource(12, "Lenta", "description", "gruenderszene", "lenta"),
        SubSource(13, "Lenta", "description", "handelsblatt", "lenta"),
        SubSource(14, "Lenta", "description", "independent", "lenta"),
        SubSource(15, "Lenta", "description", "lequipe", "lenta")
)