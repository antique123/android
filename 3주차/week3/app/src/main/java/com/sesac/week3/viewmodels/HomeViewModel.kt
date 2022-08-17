package com.sesac.week3.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesac.week3.Repository
import com.sesac.week3.adapters.GoodsAdapter
import com.sesac.week3.models.Goods

class HomeViewModel : ViewModel() {
    private val goods = MutableLiveData<MutableList<Goods>>()
    private var filteredGoods: MutableLiveData<MutableList<Goods>>? = MutableLiveData<MutableList<Goods>>()

    private var filteringPrice = -1
    var isFiltered = false

    lateinit var  adapter: GoodsAdapter
    fun addGoods(goods: MutableList<Goods>) {
        if(isFiltered) {
            this.goods.value?.let { it.addAll(0, goods) }
            val list = mutableListOf<Goods>()

            for(item in goods) {
                if(item.price.toInt() <= filteringPrice) {
                    //필터링 조건에 맞는 아이템이 들어온 경우 리스트에 따로 모은다
                    list.add(item)
                }
            }
            filteredGoods?.value?.let { list.addAll(it) }
            filteredGoods?.value = list

        } else {
            this.goods.value?.let { goods.addAll(it) }
            this.goods.value = goods
        }
    }


    fun removeGoods(position: Int) {
        val goods = this.goods.value
        goods?.removeAt(position)
        this.goods.value = goods
    }

    fun updateGoods(updatedGoods: Goods, position: Int) {
        if(isFiltered) {
            var isExist = -1
            val filteredItem = filteredGoods?.value
            filteredItem?.size?.let {
                for(i in 0 until it) {
                    if(filteredItem[i].productID == updatedGoods.productID) {
                        isExist = i
                    }
                }
                if(isExist > -1) {
                    // 현재 필터에 해당 아이템이 존재하는 경우
                    if(updatedGoods.price.toInt() <= filteringPrice) {
                        filteredItem.set(isExist, updatedGoods)
                    } else {
                        filteredItem.removeAt(isExist)
                    }
                    filteredGoods?.value = filteredItem
                } else {
                    //현재 필터 리스트에 해당 아이템이 존재하지 않는 경우
                    if(updatedGoods.price.toInt() <= filteringPrice) {
                        filteredItem.add(0, updatedGoods)
                        filteredGoods?.value = filteredItem
                    }
                }

            }
        }

        val goods = this.goods.value
        goods?.set(position, updatedGoods)
        this.goods.value = goods
        /*else {
            val goods = this.goods.value
            goods?.set(position, updatedGoods)
            this.goods.value = goods
        }

         */
    }

    fun installFilter(price: Int) {
        filteringPrice = price

        filteredGoods?.value = goods.value?.filter {
            it.price.toInt() <= filteringPrice
        } as MutableList<Goods>
    }

    fun removeFilter(removeFilter: (MutableList<Goods>) -> Unit) {
        filteredGoods?.value?.clear()
        filteringPrice = -1
        goods.value?.let { removeFilter(it) }
    }


    fun getGoods(): MutableLiveData<MutableList<Goods>> {
        if(goods.value.isNullOrEmpty()) {
            goods.value = Repository.getGoods()
        }
        return goods
    }

    fun getFilteredGoods() = filteredGoods
}