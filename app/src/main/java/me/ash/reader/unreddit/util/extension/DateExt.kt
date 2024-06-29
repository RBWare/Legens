package me.ash.reader.unreddit.util.extension

val Long.isPast: Boolean
    get() = System.currentTimeMillis() >= this
