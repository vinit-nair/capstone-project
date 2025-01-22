package com.example.gopaywallet.data.model

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("content")
    val transactions: List<Transaction>,
    val pageable: PageableInfo,
    val last: Boolean,
    val totalElements: Int,
    val totalPages: Int,
    val size: Int,
    val number: Int,
    val sort: SortInfo,
    val first: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

data class PageableInfo(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: SortInfo,
    val offset: Int,
    val unpaged: Boolean,
    val paged: Boolean
)

data class SortInfo(
    val empty: Boolean,
    val unsorted: Boolean,
    val sorted: Boolean
) 