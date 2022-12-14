package com.sesac.week3.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.week3.Constant
import com.sesac.week3.models.Post
import com.sesac.week3.activities.AddPostActivity
import com.sesac.week3.adapters.CategoryAdapter
import com.sesac.week3.adapters.PostAdapter
import com.sesac.week3.databinding.FragmentLifeBinding
import com.sesac.week3.viewmodels.LifeViewModel
import java.util.ArrayList


class LifeFragment : Fragment() {
    private var _binding: FragmentLifeBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var updateLauncher: ActivityResultLauncher<Intent>
    private val viewModel by lazy { ViewModelProvider(this).get(LifeViewModel::class.java) }
    private val postAdapter by lazy { PostAdapter(resources.displayMetrics, requireContext()) }
    private val categoryAdapter by lazy { CategoryAdapter(createCategories(), requireContext())}
    private var updatePosition: Int = Constant.NON_MODIFY_COMMAND

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentLifeBinding.inflate(layoutInflater, container, false)

        initialize()
        return binding.root
    }

    private fun initialize() {
        createLauncher()
        initViews()
        initViewModel()
    }

    private fun initViews() {

        binding.addPostFab.setOnClickListener {
            launcher.launch(Intent(requireActivity(), AddPostActivity::class.java))
        }

        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.categoryRecyclerView.adapter = categoryAdapter

        binding.postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.postRecyclerView.adapter = postAdapter

        postAdapter.onLongClickListener = { position ->
            AlertDialog.Builder(requireActivity())
                .setTitle("DELETE")
                .setMessage("?????? ???????????? ?????????????????????????")
                .setPositiveButton("????????????") { _, _ ->
                    viewModel.remove(position)
                }
                .setNegativeButton("??????", null)
                .create().show()
        }

        postAdapter.onClickListener = { position ->
            updatePosition = position
            val intent = Intent(requireActivity(), AddPostActivity::class.java)
            intent.putExtra(Constant.ADD_POST_EXTRA_KEY, postAdapter.items[position])
            updateLauncher.launch(intent)
        }

    }

    private fun initViewModel() {
        viewModel.getPost().observe(viewLifecycleOwner) {
            postAdapter.update(it)
        }
        viewModel.getPost()
    }

    private fun createLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val post = result.data?.getParcelableExtra<Post>(Constant.ADD_POST_EXTRA_KEY)
                post?.let { viewModel.add(mutableListOf(it)) }
                binding.postRecyclerView.scrollToPosition(0)
            }
        }

        updateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val post = result.data?.getParcelableExtra<Post>(Constant.ADD_POST_EXTRA_KEY)
                post?.let { viewModel.update(updatePosition, post) }
                updatePosition = Constant.NON_MODIFY_COMMAND
            }
        }
    }

    private fun createCategories(): MutableList<String> {
        return mutableListOf(
            "????????????", "????????????", "????????????", "??????/????????????", "??????????????????",
            "????????????", "????????????", "?????????", "?????????", "??????", "??????",
            "????????????", "??????/??????", "???????????????", "??????/??????", "??????"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}