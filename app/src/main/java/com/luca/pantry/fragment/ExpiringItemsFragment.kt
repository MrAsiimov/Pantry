package com.luca.pantry.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.Adapter.ProdottoAdapter
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.launch

class ExpiringItemsFragment : Fragment() {
    private var adapter = ProdottoAdapter(emptyList())
    private lateinit var recyclerView:  RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expiring_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.expiring_items_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val dao = PantryApp.database.prodottoDao()

        lifecycleScope.launch {
            val prodotti = dao.getExpiringItems()


            adapter.updateData(prodotti)
        }
    }
}