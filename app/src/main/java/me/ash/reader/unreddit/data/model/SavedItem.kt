package me.ash.reader.unreddit.data.model

import me.ash.reader.unreddit.data.model.db.PostEntity

sealed class SavedItem(val timestamp: Long) {
    data class Post(val post: PostEntity) : SavedItem(post.time)

    data class Comment(
        val comment: me.ash.reader.unreddit.data.model.Comment.CommentEntity
    ) : SavedItem(comment.time)
}
