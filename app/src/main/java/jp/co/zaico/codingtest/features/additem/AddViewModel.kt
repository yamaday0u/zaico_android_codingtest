package jp.co.zaico.codingtest.features.additem

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import jp.co.zaico.codingtest.R
import jp.co.zaico.codingtest.core.data.request.RequestBody
import jp.co.zaico.codingtest.core.data.response.Response
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val context: Application,
    private val httpClient: HttpClient
): ViewModel() {

    // データ登録
    suspend fun registerInventory(itemName: String) : Int {
        val requestBody = RequestBody(itemName)

        val response: HttpResponse = httpClient.post(
            String.format("%s/api/v1/inventories", context.getString(R.string.api_endpoint))
        ) {
            header("Authorization", String.format("Bearer %s", context.getString(R.string.api_token)))
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val body: Response = response.body()
        return body.code
    }
}