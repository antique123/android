package com.sesac.week3.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesac.week3.Repository
import com.sesac.week3.models.Post

class LifeViewModel : ViewModel() {
    private val post = MutableLiveData<MutableList<Post>>()


    fun getPost(): MutableLiveData<MutableList<Post>> {
        if(post.value == null) {
            post.value = Repository.getPost()
        }

        return post
    }

    fun add(newPost: MutableList<Post>) {
        post.value?.let { newPost.addAll(it) }
        post.value = newPost
    }

    fun remove(position: Int) {
        val item = post.value
        item?.removeAt(position)

        post.value = item
    }

    fun update(position: Int, updatedPost: Post) {
        val item = post.value
        item?.set(position, updatedPost)

        post.value = item
    }
}