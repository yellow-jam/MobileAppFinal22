package com.example.ukids

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ukids.databinding.PlaceListItemBinding

class XmlViewHolder(val binding: PlaceListItemBinding): RecyclerView.ViewHolder(binding.root)
class XmlAdapter (val context: Context, val datas: MutableList<myRow>?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return XmlViewHolder(PlaceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as XmlViewHolder).binding
        val model = datas!![position]
        binding.placename.text = model.FACLT_NM
        binding.placetype.text = ""
        binding.placeaddr.text = model.REFINE_LOTNO_ADDR ?: model.REFINE_ROADNM_ADDR
    }
}