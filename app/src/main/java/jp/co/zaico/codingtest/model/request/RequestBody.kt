package jp.co.zaico.codingtest.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    val title: String
)
