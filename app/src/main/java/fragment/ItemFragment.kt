package fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.NumberPicker
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.luca.pantry.R
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

        setNumberPicker()
        setupDatePicker()
        setupContainerDropdown()
        setupSaveButton()
        setButtons()
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
        val picker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Seleziona scadenza")
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
            val date = textDateEdit.text.toString()
            val container = textContainerEdit.text.toString()

            Toast.makeText(context, "Quantity: ${npQuantity.value}\nNome Prodotto: ${textContainerEdit.text}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setButtons() {
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
            minValue = 1
            maxValue = 100
            value = 1

            setFormatter { displayedValues ->
                if (displayedValues == value) displayedValues.toString()
                else ""
            }
        }
    }
}