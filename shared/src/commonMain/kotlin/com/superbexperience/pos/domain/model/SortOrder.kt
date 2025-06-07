package com.superbexperience.pos.domain.model

enum class SortOrder {
    ASCENDING,
    DESCENDING,
    UNORDERED,
    ;

    fun toggle(): SortOrder {
        return when (this) {
            ASCENDING -> DESCENDING
            DESCENDING -> ASCENDING
            UNORDERED -> UNORDERED
        }
    }
}
