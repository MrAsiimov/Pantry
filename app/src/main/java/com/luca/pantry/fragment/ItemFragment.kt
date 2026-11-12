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
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
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
import androidx.core.net.toUri
import com.luca.pantry.Database.CurrentHousehold
import com.luca.pantry.EmptyActivity
import com.luca.pantry.Network.OpenFoodFactsClient
import com.luca.pantry.Network.MissingImageWorker
import android.app.AlertDialog
import com.luca.pantry.EntityDB.Container

class ItemFragment : Fragment() {
    private lateinit var textDate: TextInputLayout
    private lateinit var textDateEdit: TextInputEditText
    private lateinit var textContainer: TextInputLayout
    private lateinit var textContainerEdit: AutoCompleteTextView
    private lateinit var btnSave: MaterialButton
    private lateinit var npQuantity: NumberPicker
    private lateinit var textItemNameEdit: TextInputEditText
    private lateinit var barcodeText: TextInputLayout
    private lateinit var textBarcodeEdit: TextInputEditText
    private lateinit var imageView: ImageView
    private lateinit var selectedDateIso: String
    private var lookedUpImageUrl: String? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textDate = view.findViewById(R.id.text_date)
        textDateEdit = view.findViewById(R.id.text_date_edit)
        textContainer = view.findViewById(R.id.text_container)
        textContainerEdit = view.findViewById(R.id.text_container_edit)
        btnSave = view.findViewById(R.id.btn_save)
        npQuantity = view.findViewById(R.id.np_quantity)
        textItemNameEdit = view.findViewById(R.id.text_item_name_edit)
        barcodeText = view.findViewById(R.id.text_barcode)
        textBarcodeEdit = view.findViewById(R.id.text_barcode_edit)
        imageView = view.findViewById(R.id.image_view)


        buttonAnimationConfig()
        setNumberPicker()
        setButtonsNP()
        setupDatePicker()
        setupContainerDropdown()
        setupSaveButton()
        setupCancelButton()
        setFields()
        setupBarcodeLookup()
        setupNewContainerOption()
        setupContainerDropdown()

        if (arguments?.getBoolean("MODIFYPRODUCT") == true) {
            setFieldModify()
            (activity as EmptyActivity).setTextHeader("Modifica Prodotto")
        }

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

            val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)
            val savedFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)

            val selectedDate = Date(dateInMs)

            val displayDate = displayFormat.format(selectedDate)
            val savedDate = savedFormat.format(selectedDate)

            textDateEdit.setText(displayDate)
            selectedDateIso = savedDate
        }
    }
    private fun setupContainerDropdown() {
        lifecycleScope.launch {
            val names = PantryApp.database.containerDao()
                .getContainers(CurrentHousehold.id())
                .map { it.name }

            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                names + NEW_CONTAINER
            )
            textContainerEdit.setAdapter(adapter)
        }
    }

    private fun setupNewContainerOption() {
        textContainerEdit.setOnItemClickListener { parent, _, position, _ ->
            if (parent.getItemAtPosition(position) == NEW_CONTAINER) {
                textContainerEdit.setText("", false)
                showNewContainerDialog()
            }
        }
    }

    private fun showNewContainerDialog() {
        val input = EditText(requireContext()).apply { hint = "Nome contenitore" }

        AlertDialog.Builder(requireContext())
            .setTitle("Nuovo contenitore")
            .setView(input)
            .setPositiveButton("Crea") {_, _ ->
                val name = input.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(requireContext(), "Il nome non può essere vuoto", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val householdId = CurrentHousehold.id()
                    val dao = PantryApp.database.containerDao()

                    if (dao.countByName(householdId, name, "") > 0) {
                        context?.let { Toast.makeText(it, "Esiste già un contenitore con quel nome", Toast.LENGTH_SHORT).show() }
                        return@launch
                    }

                    val now = System.currentTimeMillis()
                    dao.addContainer(
                        Container(
                            householdId = householdId,
                            name = name,
                            iconKey = "ic_container",
                            createdAt = now,
                            updatedAt = now
                        )
                    )
                    setupContainerDropdown()
                    textContainerEdit.setText(name, false)
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun setupSaveButton() {
        val modify = arguments?.getBoolean("MODIFYPRODUCT") ?: false

        btnSave.setOnClickListener {
            val quantity = npQuantity.value
            val itemName = textItemNameEdit.text.toString().trim()
            val date = textDateEdit.text.toString()
            val containerName = textContainerEdit.text.toString().trim()
            val barcode = textBarcodeEdit.text.toString().trim().ifEmpty { null }

            var imageUrl = (if (!modify) arguments?.getString("IMAGEURL")
            else arguments?.getString("IMAGEPRODUCT"))
                ?.takeIf { it.isNotEmpty() }
            if (!modify && imageUrl == null) imageUrl = lookedUpImageUrl

            if (quantity == 0 || itemName.isEmpty() || date.isEmpty() || containerName.isEmpty()) {
                Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isoDate = try {
                val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)
                val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)
                isoFormat.format(displayFormat.parse(date)!!)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Data non valida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSave.isEnabled = false
            lifecycleScope.launch {
                try {
                    val container = PantryApp.database.containerDao()
                        .getContainers(CurrentHousehold.id())
                        .firstOrNull { it.name.equals(containerName, ignoreCase = true)}

                    if (container == null) {
                        context?.let {
                            Toast.makeText(it, "Seleziona un contenitore valido", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }

                    if (!modify && imageUrl == null && barcode != null) {
                        imageUrl = OpenFoodFactsClient.lookup(barcode)?.imageUrl
                    }

                    val now = System.currentTimeMillis()

                    if (!modify) {
                        PantryApp.database.prodottoDao().addItem(
                            Prodotto(
                                containerId = container.id,
                                name = itemName,
                                barcode = barcode,
                                expiryDate = isoDate,
                                quantity = quantity,
                                imageUrl = imageUrl,
                                createdAt = now,
                                updatedAt = now
                            )
                        )
                        context?.let { Toast.makeText(it, "Prodotto aggiunto", Toast.LENGTH_SHORT).show() }
                    } else {
                        val productId = arguments?.getString("PRODUCTID") ?: return@launch
                        val createdAt = arguments?.getLong("CREATEDATPRODUCT", now) ?: now

                        PantryApp.database.prodottoDao().update(
                            Prodotto(
                                id = productId,
                                containerId = container.id,
                                name = itemName,
                                barcode = barcode,
                                expiryDate = isoDate,
                                quantity = quantity,
                                imageUrl = imageUrl,
                                createdAt = createdAt,
                                updatedAt = now,
                                syncState = 1
                            )
                        )
                        context?.let { Toast.makeText(it, "Prodotto aggiornato", Toast.LENGTH_SHORT).show()}
                    }

                    if (imageUrl == null && barcode != null) {
                        context?.applicationContext?.let { MissingImageWorker.enqueue(it) }
                    }

                    closeScreen()
                } finally {
                    btnSave.isEnabled = true
                }
            }
        }
    }

    private fun closeScreen() {
        if (arguments?.getString("FROM") == "modify") {
            activity?.supportFragmentManager?.popBackStack()
        } else {
            activity?.finish()
        }
    }

    private fun setupCancelButton() {
        val cancel_btn = view?.findViewById<MaterialButton>(R.id.btn_cancel)
        cancel_btn?.setOnClickListener {
            closeScreen()
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

    private fun setFields() {
        val showBarcode = arguments?.getBoolean("SHOWBARCODE") ?: false
        val barcode = arguments?.getString("BARCODE")
        val name = arguments?.getString("NAME")
        val imageurl = arguments?.getString("IMAGEURL")

        if (name != null && imageurl != null) {
            textItemNameEdit.setText(name)

            if (imageurl.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(imageurl)
                    .into(imageView)
            }
        } else {
            val drawableId = R.drawable.ic_notfound
            val uri = "android.resource://${context?.packageName}/$drawableId".toUri()
            Glide.with(requireContext())
                .load(uri)
                .into(imageView)
        }


        if (showBarcode) {
            barcodeText.visibility = View.VISIBLE
        } else {
            barcodeText.visibility = View.GONE
        }

        if (barcode != null) {
            textBarcodeEdit.setText(barcode)
        }
    }

    private fun setFieldModify() {
        val name = arguments?.getString("NAMEPRODUCT")
        val quantity = arguments?.getInt("QUANTITYPRODUCT")
        val expiredate = arguments?.getString("EXPIREDATEPRODUCT")
        val containerId = arguments?.getString("CONTAINERID")
        val barcode = arguments?.getString("BARCODEPRODUCT")
        val imageurl = arguments?.getString("IMAGEPRODUCT")

        val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)
        val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)

        val parsedDate = isoFormat.parse(expiredate!!)
        val displayDate = parsedDate.let { displayFormat.format(it!!) }

        textItemNameEdit.setText(name)
        npQuantity.value = quantity ?: 100
        textDateEdit.setText(displayDate)
        textBarcodeEdit.setText(barcode)

        lifecycleScope.launch {
            val containerName = PantryApp.database.containerDao()
                .getContainers(CurrentHousehold.id())
                .firstOrNull { it.id == containerId }
                ?.name
            textContainerEdit.setText(containerName, false)
        }

        if (imageurl.isNullOrEmpty()) {
            Glide.with(requireContext()).load(R.drawable.ic_notfound).into(imageView)
        } else {
            Glide.with(requireContext()).load(imageurl).into(imageView)
        }
    }

    private fun setupBarcodeLookup() {
        val modify = arguments?.getBoolean("MODIFYPRODUCT") ?: false
        if (modify) return

        textBarcodeEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) return@setOnFocusChangeListener
            val code = textBarcodeEdit.text.toString().trim()
            //se arriva dalla scansione, nome e immagine già sono presenti
            if (code.length < 8 || arguments?.getString("IMAGEURL") != null) return@setOnFocusChangeListener

            viewLifecycleOwner.lifecycleScope.launch {
                lookedUpImageUrl = null
                val found = OpenFoodFactsClient.lookup(code) ?: return@launch
                found.imageUrl?.let {
                    lookedUpImageUrl = it
                    Glide.with(requireContext()).load(it).into(imageView)
                }
                if (textItemNameEdit.text.isNullOrBlank() && found.name != null) {
                    textItemNameEdit.setText(found.name)
                }
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

    companion object {
        private const val NEW_CONTAINER = "+ Nuovo contenitore"
    }
}
