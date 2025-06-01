package jp.co.zaico.codingtest.features.detailitem

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import jp.co.zaico.codingtest.R
import jp.co.zaico.codingtest.core.data.model.Inventory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

@HiltViewModel
class DetailInventoryViewModel @Inject constructor(
    private val context: Application,
    private val httpClient: HttpClient
) : ViewModel() {

    // データ取得
    @SuppressLint("DefaultLocale")
    suspend fun getInventory(inventoryId: Int): Inventory  {
            val response: HttpResponse = httpClient.get(
                String.format(
                    "%s/api/v1/inventories/%d",
                    context.getString(R.string.api_endpoint),
                    inventoryId
                )
            ) {
                header("Authorization", String.format("Bearer %s", context.getString(R.string.api_token)))
            }

            val jsonText = response.bodyAsText()
            val json = Json.parseToJsonElement(jsonText).jsonObject

            return Inventory(
                id = json["id"].toString().replace(""""""", "").toInt(),
                title = json["title"].toString().replace(""""""", ""),
                quantity = json["quantity"].toString().replace(""""""", "")
            )

    }

}