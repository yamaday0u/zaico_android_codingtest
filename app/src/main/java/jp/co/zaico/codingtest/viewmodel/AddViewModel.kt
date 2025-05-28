package jp.co.zaico.codingtest.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import jp.co.zaico.codingtest.R
import jp.co.zaico.codingtest.model.request.RequestBody
import jp.co.zaico.codingtest.model.response.Response
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class AddViewModel(
    val context: Context
): ViewModel() {

    // データ登録
    fun registerInventory(itemName: String) : Int = runBlocking {

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        val requestBody = RequestBody(itemName)

        return@runBlocking GlobalScope.async {
            val response: HttpResponse = client!!.post(
                String.format("%s/api/v1/inventories", context.getString(R.string.api_endpoint))
            ) {
                header("Authorization", String.format("Bearer %s", context.getString(R.string.api_token)))
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            val body: Response = response.body()
            return@async body.code

        }.await()

    }
}