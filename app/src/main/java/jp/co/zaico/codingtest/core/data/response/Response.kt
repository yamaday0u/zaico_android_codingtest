package jp.co.zaico.codingtest.core.data.response

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val code: Int,
    val status: String,
    val message: String,
    val data_id: Int?
)
