package com.luca.pantry.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.Adapter.ProdottoAdapter
import com.luca.pantry.EmptyActivity
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.launch

class ItemViewFragment : Fragment() {
    private lateinit var adapter: ProdottoAdapter
    private lateinit var recyclerView:  RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.all_items_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProdottoAdapter(
            prodotti = emptyList(),
            onRename = { prodotto ->
                val intent = Intent(requireContext(), EmptyActivity::class.java).apply {
                    putExtra("ORIGIN", "modifyproduct")
                    putExtra("NAMEPRODUCT", prodotto.productName)
                    putExtra("QUANTITYPRODUCT", prodotto.quantity)
                    putExtra("EXPIREDATEPRODUCT", prodotto.expiringDate)
                    putExtra("CONTAINERPRODUCT", prodotto.container)
                    putExtra("BARCODEPRODUCT", prodotto.barcode)
                    putExtra("IMAGEPRODUCT", prodotto.imageUrl)
                }
                startActivity(intent)
            },
            onChangeQuantity = { prodotto ->
                showQuantityDialog(prodotto)
            },
            onDelete = { prodotto ->
                deleteProduct(prodotto)
            }

        )

        recyclerView.adapter = adapter

        setupRecyclerView()
    }

    private fun showQuantityDialog(prodotto: Prodotto) {
        val input = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(prodotto.quantity.toString())
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Modifica quantità")
            .setView(input)
            .setPositiveButton("Salva") { _, _ ->
                val newQuantity = input.text.toString().toIntOrNull()
                if (newQuantity != null) {
                    lifecycleScope.launch {
                        val updated = prodotto.copy(quantity = newQuantity)
                        PantryApp.database.prodottoDao().update(updated)
                        setupRecyclerView()
                    }
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun deleteProduct(prodotto: Prodotto) {
        AlertDialog.Builder(requireContext())
            .setTitle("Elimina prodotto")
            .setMessage("Sei sicuro di voler eliminare ${prodotto.productName}?")
            .setPositiveButton("Elimina") {_, _ ->
                lifecycleScope.launch {
                    PantryApp.database.prodottoDao().deleteProduct(prodotto)
                    setupRecyclerView()
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun setupRecyclerView() {
        val dao = PantryApp.database.prodottoDao()
        val container = arguments?.getString("CONTAINER")

        if (container == null) {
            lifecycleScope.launch {
                val prodotti = dao.getAllItems()

                adapter.updateData(prodotti)
            }
        } else {
            lifecycleScope.launch {
                val prodotti = dao.getItemsByContainer(container)

                adapter.updateData(prodotti)
            }
        }

    }
}
