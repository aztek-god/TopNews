package dv.serg.topnews.app

object Constants {
    object App {
        const val API_KEY = "a1f8deb5e535412cbd4f401b32725855"
    }

    object SourceType {
        private val BUSINESS: Pair<Int, String> = Pair(0, "business")
        private val ENTERTAINMENT: Pair<Int, String> = Pair(1, "entertainment")
        private val GENERAL: Pair<Int, String> = Pair(2, "general")
        private val HEALTH: Pair<Int, String> = Pair(3, "health")
        private val SCIENCE: Pair<Int, String> = Pair(4, "science")
        private val SPORTS: Pair<Int, String> = Pair(5, "sports")
        private val TECHNOLOGY: Pair<Int, String> = Pair(6, "technology")

        val FRAGMENTS: Map<Int, String> = mapOf(
                BUSINESS,
                ENTERTAINMENT,
                GENERAL,
                HEALTH,
                SCIENCE,
                SPORTS,
                TECHNOLOGY
        )
    }

    object Resources {
        const val CHOOSER_TITLE: String = "chooser_title"
    }

    object Time {
        const val DEFAULT_DATETIME_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ss"
    }

    enum class RequestState {
        IDLE,
        LOADING,
        COMPLETE,
        ERROR
    }
}