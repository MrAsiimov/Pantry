package com.luca.pantry.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.R
import android.widget.TextView

class ContainerAdapter(private var containers: List<Container>): RecyclerView.Adapter<ContainerAdapter.ContainerViewHolder>() {
    class ContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image_container_icon)
        val name = itemView.findViewById<TextView>(R.id.text_container_nome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container, parent, false)
        return ContainerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, position: Int) {
        val container = containers[position]

        holder.name.text = container.nameContainer

        val drawableId = holder.itemView.context.resources.getIdentifier(
            container.imageuri,
            "drawable",
            holder.itemView.context.packageName
        )

        if (drawableId != 0) {
            holder.image.setImageResource(drawableId)
        } else {
            holder.image.setImageResource(R.drawable.ic_notfound)
        }


    }

    override fun getItemCount(): Int = containers.size

    fun updateData(newContainers: List<Container>) {
        containers = newContainers
        notifyDataSetChanged()
    }

}