package com.luca.pantry.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.R

class ImageSelectorAdapter (private val images: List<Int>, private val onImageSelected: (Int) -> Unit): RecyclerView.Adapter<ImageSelectorAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image_option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_option, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val drawableId = images[position]
        holder.image.setImageResource(drawableId)
        holder.image.setOnClickListener {
            onImageSelected(drawableId)
        }
    }

    override fun getItemCount(): Int = images.size
}