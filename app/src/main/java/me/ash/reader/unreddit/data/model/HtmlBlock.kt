package me.ash.reader.unreddit.data.model

data class HtmlBlock(val block: Block, val type: BlockType) {
    enum class BlockType {
        TEXT, CODE, TABLE
    }
}
