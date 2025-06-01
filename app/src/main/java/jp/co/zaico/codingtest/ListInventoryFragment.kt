package jp.co.zaico.codingtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.zaico.codingtest.databinding.FragmentFirstBinding

class ListInventoryFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private lateinit var viewModel: ListInventoryViewModel
    private lateinit var myAdapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(layoutInflater)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ListInventoryViewModel(requireContext())

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
        viewModel.getInventories().apply {
            myAdapter.submitList(this)
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
        val _view= LayoutInflater.from(parent.context)
            .inflate(R.layout.first_item, parent, false)
        return ViewHolder(_view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val _item= getItem(position)
        (holder.itemView.findViewById<View>(R.id.textView_id) as TextView).text = _item.id.toString()
        (holder.itemView.findViewById<View>(R.id.textView_title) as TextView).text = _item.title

        holder.itemView.setOnClickListener{
            itemClickListener.itemClick(_item)
        }
    }

}