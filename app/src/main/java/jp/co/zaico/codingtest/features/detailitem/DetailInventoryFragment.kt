package jp.co.zaico.codingtest.features.detailitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.core.data.model.Inventory
import jp.co.zaico.codingtest.databinding.FragmentSecondBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailInventoryFragment : Fragment() {
    private val viewModel: DetailInventoryViewModel by viewModels()

    private var _binding: FragmentSecondBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return _binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inventoryId = requireArguments().getString("inventoryId")!!.toInt()

        lifecycleScope.launch {
            val inventory = viewModel.getInventory(inventoryId)
            initView(inventory)
        }

    }

    private fun initView(inventory: Inventory) {
        _binding!!.textViewId.text = inventory.id.toString()
        _binding!!.textViewTitle.text = inventory.title
        _binding!!.textViewQuantity.text = inventory.quantity
    }

}