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
        SubSource(1, "Lenta", "Новости, статьи, фотографии, видео. Семь дней в неделю, 24 часа в сутки.", "lenta", "lenta_icon"),
        SubSource(2, "CNN", "View the latest news and breaking news today for U.S., world, weather, entertainment, politics and health at CNN.", "cnn", "cnn_icon"),
        SubSource(3, "Independent", "National morning quality (tabloid) includes free online access to news and supplements. Insight by Robert Fisk and various other columnists."
                , "independent", "independent_icon"),
        SubSource(4, "RBC", "Главные новости политики, экономики и бизнеса, комментарии аналитиков, финансовые данные с российских и мировых биржевых систем на сайте rbc.ru.", "rbc", "rbc_icon"),
        SubSource(5, "Reuters", "Reuters.com brings you the latest news from around the world, covering breaking news in business, politics, entertainment, technology, video and pictures.", "reuters", "reuters_icon"),
        SubSource(6, "RT", "Актуальная картина дня на RT: круглосуточное ежедневное обновление новостей политики, бизнеса, финансов, спорта, науки, культуры. Онлайн-репортажи с места событий. Комментарии экспертов, актуальные интервью, фото и видео репортажи.", "rt", "rt_icon"),
        SubSource(7, "The Telegraph", "Latest news, business, sport, comment, lifestyle and culture from the Daily Telegraph and Sunday Telegraph newspapers and video from Telegraph TV.", "the-telegraph", "the_telegraph_icon"),
        SubSource(8, "Time", "Breaking news and analysis from TIME.com. Politics, world news, photos, video, tech reviews, health, science and entertainment news.", "time", "time_icon"),
        SubSource(9, "BBC News", "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories. BBC News provides trusted World and UK news as well as local and regional perspectives. Also entertainment, business, science, technology and health news.", "bbc-news", "bbc_news_icon"),
        SubSource(10, "Marca", "La mejor información deportiva en castellano actualizada minuto a minuto en noticias, vídeos, fotos, retransmisiones y resultados en directo.", "marca", "marca_icon"),
        SubSource(11, "Liberation", "Toute l'actualité en direct - photos et vidéos avec Libération.", "liberation", "liberation_icon"),
        SubSource(12, "NBC News", "Breaking news, videos, and the latest top stories in world news, business, politics, health and pop culture.", "nbc-news", "nbc_news_icon"),
        SubSource(13, "National Geographic", "Reporting our world daily: original nature and science news from National Geographic.", "national-geographic", "national_geographic_icon"),
        SubSource(14, "Polygon", "Polygon is a gaming website in partnership with Vox Media. Our culture focused site covers games, their creators, the fans, trending stories and entertainment news.", "polygon", "polygon_icon"),
        SubSource(15, "Recode", "Get the latest independent tech news, reviews and analysis from Recode with the most informed and respected journalists in technology and media.", "recode", "recode_icon"),
        SubSource(16, "Reddit", "Reddit is an entertainment, social news networking service, and news website. Reddit's registered community members can submit content, such as text posts or direct links.", "reddit-r-all", "reddit_r_all_icon"),
        SubSource(17, "RTE", "Get all of the latest breaking local and international news stories as they happen, with up to the minute updates and analysis, from Ireland's National Broadcaster.", "rte", "rte_icon"),
        SubSource(18, "Wired", "Wired is a monthly American magazine, published in print and online editions, that focuses on how emerging technologies affect culture, the economy, and politics.", "wired", "wired_icon"),
        SubSource(19, "Lequipe", "Get the latest independent tech news, reviews and analysis from Recode with the most informed and respected journalists in technology and media.", "lequipe", "lequipe_icon"),
        SubSource(20, "Bild", "Die Seite 1 für aktuelle Nachrichten und Themen, Bilder und Videos aus den Bereichen News, Wirtschaft, Politik, Show, Sport, und Promis.", "bild", "bild_icon")
)