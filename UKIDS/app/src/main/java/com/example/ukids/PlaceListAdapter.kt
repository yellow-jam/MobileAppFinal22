package com.example.ukids

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ukids.databinding.PlaceListItemBinding

class MyViewHolder(val binding: PlaceListItemBinding): RecyclerView.ViewHolder(binding.root)
class PlaceListAdapter(val context: Context, val datas: MutableList<ItemModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder (PlaceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val model = datas!![position]
        binding.placename.text = model.parkNm
        binding.placetype.text = model.parkType
        binding.placeaddr.text = model.roadAdd
    }
}