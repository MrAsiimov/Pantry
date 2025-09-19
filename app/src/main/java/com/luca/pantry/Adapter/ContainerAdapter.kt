package com.luca.pantry.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.R
import android.widget.TextView

class ContainerAdapter(private var containers: List<Container>): RecyclerView.Adapter<ContainerAdapter.ContainerViewHolder>() {
    class ContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.text_container_nome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container, parent, false)
        return ContainerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, position: Int) {
        holder.name.text = containers[position].nameContainer
    }

    override fun getItemCount(): Int = containers.size

    fun updateData(newContainers: List<Container>) {
        containers = newContainers
        notifyDataSetChanged()
    }

}