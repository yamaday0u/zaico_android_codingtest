package jp.co.zaico.codingtest.viewmodel

import android.app.Application
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import jp.co.zaico.codingtest.R
import jp.co.zaico.codingtest.features.additem.AddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelTest {

    // CoroutineのDispatcherをテスト用に置き換えるためのルール
    private val testDispatcher = UnconfinedTestDispatcher() // または StandardTestDispatcher()

    // MockKでモック化するオブジェクト
    private lateinit var mockContext: Application
    private lateinit var mockHttpClient: HttpClient
    private lateinit var viewModel: AddViewModel

    // APIエンドポイントとトークンのダミー値
    private val dummyApiEndpoint = "https://example.com"
    private val dummyApiToken = "test_token"


    @Before
    fun setUp() {
        // メインディスパッチャをテスト用に設定
        Dispatchers.setMain(testDispatcher)

        // Contextモックの準備
        mockContext = mockk()

        every { mockContext.getString(R.string.api_endpoint) } returns dummyApiEndpoint
        every { mockContext.getString(R.string.api_token) } returns dummyApiToken
    }

    // TODO: 複数APIに対応できるようにする。今はregistry_Inventory()からの呼び出しにのみ対応している
    private fun setupViewModelWithMockEngine() {
        val mockEngine = MockEngine { _ ->
            respond(
                content = """
                    {
                        "code": 200,
                        "status": "success",
                        "message": "Data was successfully created.",
                        "data_id": 12345 
                    }
                """.trimIndent(), // Responseクラスに合わせたJSON文字列
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        mockHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        viewModel = AddViewModel(mockContext, mockHttpClient)
    }

    @Test
    fun registry_Inventory() {
        val itemTitle = "Test Item"

        setupViewModelWithMockEngine()
        val result = viewModel.registerInventory(itemTitle)

        assertEquals(200, result)
    }

    @After
    fun tearDown() {
        // メインディスパッチャをリセット
        Dispatchers.resetMain()
        unmockkAll() // MockKのモックをクリア (任意)
    }

}