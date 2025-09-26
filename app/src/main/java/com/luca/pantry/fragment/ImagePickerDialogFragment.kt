package com.luca.pantry.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luca.pantry.Adapter.ImageSelectorAdapter
import com.luca.pantry.R

class ImagePickerDialogFragment(private val images: List<Int>, private val onImageSelected: (Int) -> Unit): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_image_picker, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_image_picker)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = ImageSelectorAdapter(images) { selectedDrawableId ->
            onImageSelected(selectedDrawableId)
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}