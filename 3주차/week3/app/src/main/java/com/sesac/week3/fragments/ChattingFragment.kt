package com.sesac.week3.fragments

import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.week3.R
import com.sesac.week3.adapters.ChatAdapter
import com.sesac.week3.databinding.FragmentChattingBinding
import com.sesac.week3.models.Chat
import com.sesac.week3.viewmodels.ChatViewModel

class ChattingFragment : Fragment() {
    private var _binding: FragmentChattingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChattingBinding.inflate(inflater, container, false)

        initialize()

        return binding.root
    }

    private fun initialize() {
        initViews()
        callbackAttachToRecyclerView()
        initViewModel()
        createChats()
    }


    private fun callbackAttachToRecyclerView() {
        //RecyclerView 에 스와이프나 드래그 이벤트가 발생할 때 콜백함수를 호출해주는 클래스가 이미 존재한다. 이 클래스를 사용하면 된다
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val dragStartPosition = viewHolder.adapterPosition
                val dragEndPosition = target.adapterPosition
                adapter.swapData(dragStartPosition, dragEndPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(viewHolder.layoutPosition)
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                val icon: Bitmap
                //스와이프 이벤트가 발생했을 때
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = (itemView.bottom - itemView.top).toFloat()
                    val width = height / 4
                    val paint = Paint()

                    //왼쪽 방향으로 스와이프했을 때
                    if(dX < 0) {
                        paint.color = Color.parseColor("#ff0000")
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, paint)
                    }
                }
            }

        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.chatRecyclerView)
    }

    private fun initViews() {
        adapter = ChatAdapter()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerView.adapter = adapter

    }

    private fun initViewModel() {
        viewModel.getChats().observe(viewLifecycleOwner) {
            adapter.addItems(it)
        }
    }

    private fun createChats() {
        if(viewModel.getChats().value.isNullOrEmpty()) {
            val chats = mutableListOf<Chat>()
            chats.add(
                Chat(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNlBOIgxwJYlsbFxzgEap5UGe4OyCQx-zHYw&usqp=CAU",
                    "아이디1",
                    "첫번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://item.kakaocdn.net/do/bef59207f5155a4eddd632c9a833e80d7154249a3890514a43687a85e6b6cc82",
                    "아이디2",
                    "두번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://t1.daumcdn.net/cfile/blog/993A18425CAC29DA2B",
                    "아이디3",
                    "세번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://img.hankyung.com/photo/202008/03.23495040.1.jpg",
                    "아이디4",
                    "네번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://imgnn.seoul.co.kr/img/upload/2012/04/16/SSI_20120416112839_V.jpg",
                    "아이디5",
                    "다섯번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://cdn.cashfeed.co.kr/attachments/ddd989df3d.jpg",
                    "아이디6",
                    "여섯번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTUGM5p0-mBRgo0qwklMR2FZOJQ5Fi2ZRv9wA&usqp=CAU",
                    "아이디7",
                    "일곱번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-SQ5w70zHHYxJY4glrH1bX-0NctLy6oc3ew&usqp=CAU",
                    "아이디8",
                    "여덟번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://t1.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/2D9/image/GUi7Tl3TQkKZmkR3sRj4si5fMeI.jpg",
                    "아이디9",
                    "아홉번째 채팅방입니다"
                ),
            )
            chats.add(
                Chat(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMbYkXf_QaHOw05hQdIp7iT0MKgcD1xt94qw&usqp=CAU",
                    "아이디10",
                    "열번째 채팅방입니다"
                ),
            )

            viewModel.getChats().value = chats
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}