package com.luca.pantry.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.luca.pantry.EntityDB.Container
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.launch

class ContainerFragment : Fragment() {
    private lateinit var imageViewContainer: ImageView
    private lateinit var textContainerNameEdit: TextInputEditText
    private lateinit var cancel_button: MaterialButton
    private lateinit var save_button: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewContainer = view.findViewById(R.id.image_view_container)
        textContainerNameEdit = view.findViewById(R.id.text_container_name_edit)
        cancel_button = view.findViewById(R.id.btn_cancel_container)
        save_button = view.findViewById(R.id.btn_save_container)

        setupCancelButton()
        setupSaveButton()
    }

    private fun setupSaveButton() {
        save_button.setOnClickListener {
            var container_name = textContainerNameEdit.text.toString()

            if (container_name.isEmpty()){
                Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
            } else {
                val container = Container(
                    container_name
                )

                lifecycleScope.launch {
                    PantryApp.database.containerDao().addContainer(container)
                    Toast.makeText(requireContext(), "Contitore aggiunto", Toast.LENGTH_SHORT).show()
                }
                activity?.finish()
            }
        }
    }

    private fun setupCancelButton() {
        cancel_button.setOnClickListener {
            activity?.finish()
        }
    }

}
