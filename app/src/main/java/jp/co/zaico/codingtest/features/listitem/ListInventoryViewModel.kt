package jp.co.zaico.codingtest.features.listitem

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import jp.co.zaico.codingtest.R
import jp.co.zaico.codingtest.core.data.model.Inventory
import javax.inject.Inject

@HiltViewModel
class ListInventoryViewModel @Inject constructor(
    private val context: Application,
    private val httpClient: HttpClient
): ViewModel() {

    // データ取得
    suspend fun getInventories() : List<Inventory>  {
        return httpClient.get(
            String.format("%s/api/v1/inventories", context.getString(R.string.api_endpoint))
        ) {
            header("Authorization", String.format("Bearer %s", context.getString(R.string.api_token)))
        }.body()
    }

}
