package jp.co.zaico.codingtest.features.listitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.R
import jp.co.zaico.codingtest.core.data.model.Inventory
import jp.co.zaico.codingtest.databinding.FragmentFirstBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListInventoryFragment : Fragment() {
    private val viewModel: ListInventoryViewModel by viewModels()

    private var _binding: FragmentFirstBinding? = null

    private lateinit var myAdapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(layoutInflater)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadAndDisplayInventories()
    }

    private fun setupRecyclerView() {
        myAdapter = MyAdapter(object : MyAdapter.OnItemClickListener {
            override fun itemClick(item: Inventory) {
                val bundle = bundleOf("inventoryId" to item.id.toString())
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            layoutManager.orientation
        )

        _binding!!.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = myAdapter
        }
    }

    private fun loadAndDisplayInventories() {
        lifecycleScope.launch {
            viewModel.getInventories().apply {
                myAdapter.submitList(this)
            }
        }
    }


}

val diff_util= object: DiffUtil.ItemCallback<Inventory>(){
    override fun areItemsTheSame(oldItem: Inventory, newItem: Inventory): Boolean
    {
        return oldItem.title== newItem.title
    }

    override fun areContentsTheSame(oldItem: Inventory, newItem: Inventory): Boolean
    {
        return oldItem== newItem
    }

}

class MyAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Inventory, MyAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnItemClickListener{
        fun itemClick(item: Inventory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.first_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item= getItem(position)
        (holder.itemView.findViewById<View>(R.id.textView_id) as TextView).text = item.id.toString()
        (holder.itemView.findViewById<View>(R.id.textView_title) as TextView).text = item.title

        holder.itemView.setOnClickListener{
            itemClickListener.itemClick(item)
        }
    }

}