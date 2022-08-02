package com.sesac.constraintlayout.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sesac.constraintlayout.Model
import com.sesac.constraintlayout.R
import com.sesac.constraintlayout.RecyclerViewAdapter
import com.sesac.constraintlayout.TopRecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var topRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var topAdapter: TopRecyclerViewAdapter
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(view)
        initTopRecyclerView()
        initRecyclerView()

        return view
    }

    private fun initViews(view: View) {
        topRecyclerView = view.findViewById(R.id.top_recycler_view)
        recyclerView = view.findViewById(R.id.main_recycler_view)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            GlobalScope.launch (Dispatchers.Main) {
                delay(2000)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initTopRecyclerView() {
        topAdapter = TopRecyclerViewAdapter()
        topAdapter.onClickListener = { position ->
            Toast.makeText(context, "$position 번째 버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        topRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        topRecyclerView.adapter = topAdapter
    }

    private fun initRecyclerView() {
        adapter = RecyclerViewAdapter(requireContext())
        adapter.onItemClickListener = { position ->
            Toast.makeText(context, "$position 번째 게시글 클릭", Toast.LENGTH_SHORT).show()
        }
        adapter.onLikeItemClickListener = { position ->
            Toast.makeText(context, "$position 번째 Like 아이템 클릭", Toast.LENGTH_SHORT).show()
        }
        adapter.onWriteItemClickListener = { position ->
            Toast.makeText(context, "$position 번째 댓글쓰기 아이템 클릭", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                layoutManager?.apply {
                    val totalItemCount = itemCount
                    val lastVisible = findLastCompletelyVisibleItemPosition()

                    if(lastVisible >= totalItemCount-1) {
                        Toast.makeText(requireContext(), "$lastVisible is last", Toast.LENGTH_SHORT).show()
                        setAdditionalData()
                    }
                }
            }
        })
    }

    private fun setTopData() {
        topAdapter.buttonTextList.add("동네질문")
        topAdapter.buttonTextList.add("동네맛집")
        topAdapter.buttonTextList.add("동네소식")
        topAdapter.buttonTextList.add("분실/실종센터")
        topAdapter.buttonTextList.add("동네사건사고")
        topAdapter.buttonTextList.add("해주세요")
        topAdapter.buttonTextList.add("취미생활")
        topAdapter.buttonTextList.add("고양이")
        topAdapter.buttonTextList.add("강아지")
        topAdapter.buttonTextList.add("건강")
        topAdapter.buttonTextList.add("살림")
        topAdapter.buttonTextList.add("인테리어")
        topAdapter.buttonTextList.add("교육/학원")
        topAdapter.buttonTextList.add("동네사진전")
        topAdapter.buttonTextList.add("출산/육아")
        topAdapter.buttonTextList.add("기타")
        topRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setData() {
        adapter.itemTextModelList.add(Model("동네질문", "혹시 동네에 대량으로 프린트 할 수 있는 곳이 있을까요?! 200장 정도 뽑아야돼요ㅠㅠ", "3분 전", false))
        adapter.itemTextModelList.add(Model("일상", "당근을 왜 이제야 깔았는지.. 그동안 상태좋은데 버린것만 생각하면 너무 아깝 한 300개이상 나눔이랑 판매했을듯", "8분 전", false))
        adapter.itemTextModelList.add(Model("일상", "영어회화 공부하고싶은데 혹시 영어잘하시는데 일본어 공부하고싶으신분 있나요? 서로 회화봐주는 식으로 스터디하고싶은데 저는 일본에서 대학생활 3년정도 해서 프리토킹 어느정도 가능합니다! 영어 회화 가능하신 분이랑 재능교환하고싶습니다!", "28분 전", false))
        adapter.itemTextModelList.add(Model("동네소식", "대방역 근처에 수선잘하는 수선집 추천부탁드려요~~", "41분 전", false))
        adapter.itemTextModelList.add(Model("동네질문", "혹시 전기톱같은걸로 간단한 철(가운데 얇은 부분) 잘라주는 곳 있을까요? 그리고 비용은 어느정도 될까요 잘아시는분알려주세요!! (쇠톱으로 수작업 하기에는 위험할거 같기도 하고 해서 구해봅니다)", "3시간 전", false))
        adapter.itemTextModelList.add(Model("동네질문", "안녕하세요~ 이력서 용 증명사진을 찍으려고하는데 보정 잘해주는 곳 어디 없을까요? 마곡 발산 염창 등촌 가양 증미 쪽이면 더 좋을거 같아요~", "5시간 전", false))
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setAdditionalData() {
        adapter.itemTextModelList.add(Model("동네질문", "2 혹시 동네에 대량으로 프린트 할 수 있는 곳이 있을까요?! 200장 정도 뽑아야돼요ㅠㅠ", "3분 전", false))
        adapter.itemTextModelList.add(Model("일상", "2 당근을 왜 이제야 깔았는지.. 그동안 상태좋은데 버린것만 생각하면 너무 아깝 한 300개이상 나눔이랑 판매했을듯", "8분 전", false))
        adapter.itemTextModelList.add(Model("일상", "2 영어회화 공부하고싶은데 혹시 영어잘하시는데 일본어 공부하고싶으신분 있나요? 서로 회화봐주는 식으로 스터디하고싶은데 저는 일본에서 대학생활 3년정도 해서 프리토킹 어느정도 가능합니다! 영어 회화 가능하신 분이랑 재능교환하고싶습니다!", "28분 전", false))
        adapter.itemTextModelList.add(Model("동네소식", "2 대방역 근처에 수선잘하는 수선집 추천부탁드려요~~", "41분 전", false))
        adapter.itemTextModelList.add(Model("동네질문", "2 혹시 전기톱같은걸로 간단한 철(가운데 얇은 부분) 잘라주는 곳 있을까요? 그리고 비용은 어느정도 될까요 잘아시는분알려주세요!! (쇠톱으로 수작업 하기에는 위험할거 같기도 하고 해서 구해봅니다)", "3시간 전", false))
        adapter.itemTextModelList.add(Model("동네질문", "2 안녕하세요~ 이력서 용 증명사진을 찍으려고하는데 보정 잘해주는 곳 어디 없을까요? 마곡 발산 염창 등촌 가양 증미 쪽이면 더 좋을거 같아요~", "5시간 전", false))
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()

        topAdapter.clearItem()
        adapter.clearItem()
    }

    override fun onStart() {
        super.onStart()

        setTopData()
        setData()
    }
}