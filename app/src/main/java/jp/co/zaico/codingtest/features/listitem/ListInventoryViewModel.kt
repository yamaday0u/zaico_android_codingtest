package jp.co.zaico.codingtest.features.listitem

import android.content.Context
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import jp.co.zaico.codingtest.core.data.model.Inventory
import jp.co.zaico.codingtest.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class ListInventoryViewModel(
    val context: Context
): ViewModel() {

    // データ取得
    fun getInventories() : List<Inventory> = runBlocking {

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        return@runBlocking GlobalScope.async {
            val response: HttpResponse = client!!.get(
                String.format("%s/api/v1/inventories", context.getString(R.string.api_endpoint))
            ) {
                header("Authorization", String.format("Bearer %s", context.getString(R.string.api_token)))
            }

            val items = mutableListOf<Inventory>()

            val jsonText = response.bodyAsText()
            val jsonArray: JsonArray = Json.parseToJsonElement(jsonText).jsonArray
            for (json in jsonArray) {
                items.add(
                    Inventory(
                        id = json.jsonObject["id"].toString().replace(""""""", "").toInt(),
                        title = json.jsonObject["title"].toString().replace(""""""", ""),
                        quantity = json.jsonObject["quantity"].toString().replace(""""""", "")
                    )
                )
            }

            return@async items.toList()

        }.await()

    }

}
