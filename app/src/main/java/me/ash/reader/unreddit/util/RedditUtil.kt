package me.ash.reader.unreddit.util

object RedditUtil {

    fun joinSubredditList(subreddits: List<String>): String {
        return subreddits.joinToString("+")
    }
}
