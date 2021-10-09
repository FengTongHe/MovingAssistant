package com.example.movingAssistant

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.moving_item.view.*

class ItemAdapter(private val itemList: List<MovingItemsControl>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.moving_item,
        parent, false)

        return  ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.imageView.setImageURI(Uri.parse(currentItem.photoUri))
        holder.textView1.text = currentItem.barcodeNum
        holder.textView2.text = "Customer ID: " + currentItem.customerId
    }

    override fun getItemCount() = itemList.size

    class ItemViewHolder(i_view: View) : RecyclerView.ViewHolder(i_view){
        val imageView: ImageView = i_view.image_view
        val textView1: TextView = i_view.text_view_1
        val textView2: TextView = i_view.text_view_2
    }

}