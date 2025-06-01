package jp.co.zaico.codingtest.core.data.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    val title: String
)
