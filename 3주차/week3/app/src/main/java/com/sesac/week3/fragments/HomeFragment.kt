package com.sesac.week3.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sesac.week3.Constant
import com.sesac.week3.activities.AddGoodsActivity
import com.sesac.week3.adapters.GoodsAdapter
import com.sesac.week3.databinding.FilterItemBinding
import com.sesac.week3.databinding.FragmentHomeBinding
import com.sesac.week3.models.Goods
import com.sesac.week3.viewmodels.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private lateinit var adapter: GoodsAdapter
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var updateLauncher: ActivityResultLauncher<Intent>

    private var updatePosition = Constant.NON_MODIFY_COMMAND

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        initialize()
        return binding.root
    }

    private fun initialize() {
        createLauncher()
        initViews()
        initViewModel()
    }

    private fun createLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                val newGoods = it.data?.getParcelableExtra<Goods>(Constant.ADD_GOODS_EXTRA_KEY)
                newGoods?.let { goods ->
                    goods.productID = generateProductID(goods)
                    viewModel.addGoods(mutableListOf(goods))
                }
            }
        }

        updateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                val updateGoods = it.data?.getParcelableExtra<Goods>(Constant.ADD_GOODS_EXTRA_KEY)
                updateGoods?.let { goods ->
                    if(updatePosition > -1) {
                        viewModel.updateGoods(goods, updatePosition)
                    }
                }
            }
        }
    }

    private fun initViews() {
        adapter = GoodsAdapter()
        adapter.onLongClickListener = { position: Int ->
            AlertDialog.Builder(requireContext())
                .setTitle("DELETE")
                .setMessage("해당 아이템을 삭제하시겠습니까?")
                .setPositiveButton("삭제하기") { _, _ ->
                    viewModel.removeGoods(position)
                }
                .setNegativeButton("닫기", null)
                .create().show()

        }
        adapter.onClickListener = {position: Int ->
            updatePosition = position
            val intent = Intent(requireContext(), AddGoodsActivity::class.java)
            intent.putExtra(Constant.ADD_GOODS_EXTRA_KEY, adapter.items[position])
            updateLauncher.launch(intent)
        }
        viewModel.adapter = adapter

        binding.goodsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.goodsRecyclerView.adapter = adapter

        binding.addGoodsFab.setOnClickListener {
            launcher.launch(Intent(requireContext(), AddGoodsActivity::class.java))
        }
        binding.noButton.setOnClickListener {
            binding.reviewContainer.visibility = View.GONE
        }
        binding.yesButton.setOnClickListener {
            binding.reviewContainer.visibility = View.GONE
        }


        binding.filterButton.setOnClickListener {
            val customView = FilterItemBinding.inflate(LayoutInflater.from(requireContext()))
            val dialog = AlertDialog.Builder(requireActivity())
                .setView(customView.root)
                .create()

            dialog.show()

            customView.filteringButton.setOnClickListener {

                if(customView.filterPriceEditText.text.isNotEmpty()) {
                    val filteringPrice = customView.filterPriceEditText.text.toString().toInt()
                    viewModel.installFilter(filteringPrice)
                    viewModel.isFiltered = true
                }

                dialog.dismiss()
            }
            customView.removeFilterButton.setOnClickListener {

                if(viewModel.isFiltered) {
                    val removeFilter: (MutableList<Goods>) -> Unit = { origin ->
                        adapter.removeFilter(origin)
                        viewModel.isFiltered = false
                    }
                    viewModel.removeFilter(removeFilter)
                }

                dialog.dismiss()
            }
        }
    }

    private fun initViewModel() {
        viewModel.getGoods().observe(viewLifecycleOwner) {
            if(updatePosition == Constant.NON_MODIFY_COMMAND) {
                adapter.update(it)
                binding.goodsRecyclerView.scrollToPosition(0)
            } else {
                adapter.modify(updatePosition)
                updatePosition = Constant.NON_MODIFY_COMMAND
            }
        }


        viewModel.getFilteredGoods()?.observe(viewLifecycleOwner) {
            adapter.installFilter(it)
        }
    }

    private fun generateProductID(goods: Goods): String {
        return goods.explain + System.currentTimeMillis().toShort()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}