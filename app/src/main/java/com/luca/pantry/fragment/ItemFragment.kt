package com.luca.pantry.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.luca.pantry.EntityDB.Prodotto
import com.luca.pantry.PantryApp
import com.luca.pantry.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ItemFragment : Fragment() {
    private lateinit var textDate: TextInputLayout
    private lateinit var textDateEdit: TextInputEditText
    private lateinit var textContainer: TextInputLayout
    private lateinit var textContainerEdit: AutoCompleteTextView
    private lateinit var btnSave: MaterialButton
    private lateinit var npQuantity: NumberPicker
    private lateinit var textItemName: TextInputLayout
    private lateinit var textBarcodeEdit: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textDate = view.findViewById(R.id.text_date)
        textDateEdit = view.findViewById(R.id.text_date_edit)
        textContainer = view.findViewById(R.id.text_container)
        textContainerEdit = view.findViewById(R.id.text_container_edit)
        btnSave = view.findViewById(R.id.btn_save)
        npQuantity = view.findViewById(R.id.np_quantity)
        textItemName = view.findViewById(R.id.text_item_name)
        textBarcodeEdit = view.findViewById(R.id.text_barcode_edit)

        val barcodeText = view.findViewById<TextInputLayout>(R.id.text_barcode)

        val showBarcode = arguments?.getBoolean("showBarcode") ?: false
        if (showBarcode) {
            barcodeText.visibility = View.VISIBLE
        } else {
            barcodeText.visibility = View.GONE
        }

        setNumberPicker()
        setupDatePicker()
        setupContainerDropdown()
        setupSaveButton()
        setupCancelButton()
        buttonAnimationConfig()
        setButtonsNP()
    }

    private fun setupDatePicker() {
        textDate.setEndIconOnClickListener {
            showDatePicker()
        }
        textDateEdit.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker(){
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(today))
            .build()

        val picker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Seleziona scadenza")
            .setCalendarConstraints(constraints)
            .build()

        picker.show(parentFragmentManager, "DATE_PICKER")
        picker.addOnPositiveButtonClickListener { dateInMs ->
            val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)
                .format(Date(dateInMs))
            textDateEdit.setText(formatted)
        }
    }
    private fun setupContainerDropdown() {
        val options = listOf("Frigorifero", "Dispensa", "Freezer")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            options
        )
        textContainerEdit.setAdapter(adapter)
    }

    private fun setupSaveButton() {
        btnSave.setOnClickListener {
            val quantity = npQuantity.value.toString().toIntOrNull() ?: 0
            val itemName = textItemName.editText?.text.toString()
            val date = textDateEdit.text.toString()
            val container = textContainerEdit.text.toString()
            val barcode = textBarcodeEdit.text.toString()

            if (quantity == 0 || itemName.isEmpty() || date.isEmpty() || container.isEmpty() || barcode.isEmpty()) {
                Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
            } else {
                val prodotto = Prodotto(
                    itemName,
                    date,
                    container,
                    quantity,
                    barcode)

                lifecycleScope.launch {
                    PantryApp.database.prodottoDao().additem(prodotto)
                    Toast.makeText(requireContext(), "Prodotto aggiunto", Toast.LENGTH_SHORT).show()
                }
                activity?.finish()
            }
        }
    }

    private fun setupCancelButton() {
        val cancel_btn = view?.findViewById<MaterialButton>(R.id.btn_cancel)
        cancel_btn?.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setButtonsNP() {
        val decrement_btn = view?.findViewById<MaterialButton>(R.id.btn_decrement)
        val increment_btn = view?.findViewById<MaterialButton>(R.id.btn_increment)

        decrement_btn?.setOnClickListener {
            npQuantity.let { picker ->
                val current = picker.value
                if (current > picker.minValue) {
                    picker.value = current - 1
                }
            }
        }

        increment_btn?.setOnClickListener {
            npQuantity.let { picker ->
                val current = picker.value
                if (current < picker.maxValue) {
                    picker.value = current + 1
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setNumberPicker() {
        npQuantity.setOnTouchListener { _, event ->
            event.actionMasked == MotionEvent.ACTION_MOVE
        }

        npQuantity.apply {
            minValue = 0
            maxValue = 100
            value = 0

            //Set numberPicker to hide upper and lower value
            setFormatter { displayedValues ->
                if (displayedValues == value) displayedValues.toString()
                else ""
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonAnimationConfig() {
        btnSave = view?.findViewById(R.id.btn_save)!!
        var cancel_btn = view?.findViewById<MaterialButton>(R.id.btn_cancel)
        val decrement_btn = view?.findViewById<MaterialButton>(R.id.btn_decrement)
        val increment_btn = view?.findViewById<MaterialButton>(R.id.btn_increment)

        btnSave.setOnTouchListener { v, event ->
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

        cancel_btn?.setOnTouchListener { v, event ->
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

        decrement_btn?.setOnTouchListener { v, event ->
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

        increment_btn?.setOnTouchListener { v, event ->
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