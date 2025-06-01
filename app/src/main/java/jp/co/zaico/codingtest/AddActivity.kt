package jp.co.zaico.codingtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.databinding.ActivityAddBinding
import jp.co.zaico.codingtest.viewmodel.AddViewModel

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {
    private val viewModel: AddViewModel by viewModels()

    private lateinit var binding: ActivityAddBinding

    companion object {
        fun createIntent(context: Context) = Intent(context, AddActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 物品作成ボタンクリック時の挙動
        binding.fab.setOnClickListener { _ ->
            val itemName = binding.editTextItemName.text.toString()
            if (itemName.isNotEmpty()) {
                val code = viewModel.registerInventory(itemName)
                if (code == 200) {
                    Toast.makeText(this, "物品名登録が成功しました", Toast.LENGTH_SHORT).show()
                    finish() // 一覧画面に戻る
                } else {
                    Toast.makeText(this, "物品名登録が失敗しました", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "物品名を入力してください", Toast.LENGTH_SHORT).show()
            }

        }
    }

}