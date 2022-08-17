package com.sesac.week3.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesac.week3.models.Chat

class ChatViewModel : ViewModel() {
    private var chats = MutableLiveData<MutableList<Chat>>()

    fun addChat(newChat: Chat) {

    }

    fun retainChat(chats: MutableList<Chat>) {
        this.chats.value?.retainAll(chats)
    }

    fun removeChat(position: Int) {
        chats.value?.removeAt(position)
    }

    fun getChats() = chats
}