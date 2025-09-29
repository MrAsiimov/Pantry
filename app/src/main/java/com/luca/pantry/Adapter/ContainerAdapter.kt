package com.luca.pantry.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.R
import android.widget.TextView
import androidx.core.os.bundleOf
import com.luca.pantry.EmptyActivity
import com.luca.pantry.fragment.ItemViewFragment

class ContainerAdapter(
    private var containers: List<Container>,
    private val onRename: (Container) -> Unit,
    private val onChangeImage: (Container) -> Unit,
    private val onDelete: (Container) -> Unit,
    private val onContainerClick: (Container) -> Unit
    ): RecyclerView.Adapter<ContainerAdapter.ContainerViewHolder>() {
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

        holder.itemView.setOnLongClickListener { view ->
            showPopupMenu(view, container)
            true
        }

        holder.itemView.setOnClickListener {
            onContainerClick(container)
        }

    }

    override fun getItemCount(): Int = containers.size

    fun updateData(newContainers: List<Container>) {
        containers = newContainers
        notifyDataSetChanged()
    }

    private fun showPopupMenu(anchor: View, container: Container) {
        val popupMenu = PopupMenu(anchor.context, anchor)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_container, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_rename -> {
                    onRename(container)
                    true
                }
                R.id.action_changeImage -> {
                    onChangeImage(container)
                    true
                }
                R.id.action_delete -> {
                    onDelete(container)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

}