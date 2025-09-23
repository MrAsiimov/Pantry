package com.luca.pantry.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.luca.pantry.Adapter.ContainerAdapter
import com.luca.pantry.EmptyActivity
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.launch


class ContainerViewFragment : Fragment() {

    private val adapter = ContainerAdapter(emptyList())
    private lateinit var addContainerButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_view_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_container)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        addContainerButton = view.findViewById(R.id.add_button_container)

        setupRecyclerView()
        setupAddContainerButton()
        buttonAnimationConfig()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupAddContainerButton() {
        val addcontainer = "addcontainer"
        addContainerButton.setOnClickListener {
            val intent = Intent(requireContext(), EmptyActivity::class.java).apply {
                putExtra("ORIGIN", addcontainer)
            }
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch {
            val containers = PantryApp.database.containerDao().getAllContainers()
            adapter.updateData(containers)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig() {
        addContainerButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val down = AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_press)
                    v.startAnimation(down)
                }
                MotionEvent.ACTION_UP -> {
                    val up = AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
                MotionEvent.ACTION_CANCEL -> {
                    val up = AnimationUtils.loadAnimation(v.context, R.anim.button_scale_on_release)
                    v.startAnimation(up)
                }
            }
            false
        }
    }
}