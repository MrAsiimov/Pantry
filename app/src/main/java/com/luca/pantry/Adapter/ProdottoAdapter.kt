package com.luca.pantry.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luca.pantry.R
import com.luca.pantry.EntityDB.Prodotto

class ProdottoAdapter(private var prodotti: List<Prodotto>): RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder>() {
    class ProdottoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.text_nome)
        val quantity = itemView.findViewById<TextView>(R.id.text_quantita)
        val expiring_date = itemView.findViewById<TextView>(R.id.text_scadenza)
        val container = itemView.findViewById<TextView>(R.id.text_contenitore)
        val imageView = itemView.findViewById<ImageView>(R.id.image_prodotto)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdottoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prodotto, parent, false)
        return ProdottoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdottoViewHolder, position: Int) {
        val prodotto = prodotti[position]
        holder.name.text = prodotto.productName
        holder.quantity.text = prodotto.quantity.toString()
        holder.expiring_date.text = prodotto.expiringDate
        holder.container.text = prodotto.container

        if (!prodotto.imageUrl.isNullOrEmpty()){
            Glide.with(holder.itemView.context)
                .load(prodotto.imageUrl)
                .into(holder.imageView)
        } else {
            val drawableId = R.drawable.ic_notfound
            val uri = "android.resource://${holder.itemView.context?.packageName}/$drawableId".toUri()
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = prodotti.size

    fun updateData(newProdotti: List<Prodotto>) {
        prodotti = newProdotti
        notifyDataSetChanged()
    }
}