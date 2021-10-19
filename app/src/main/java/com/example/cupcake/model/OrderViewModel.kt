package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY = 3.00

class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>()
    val quantity = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor = _flavor

    private val _date = MutableLiveData<String>()
    val date = _date

    private val _price = MutableLiveData<Double>()
    val price : LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }

    val optionsDate = getPickupOptions()

    init {
        reset()
    }

    fun setQuantity(value: Int) {
        _quantity.value = value
        updatePrice()
    }

    fun setFlavor(value: String) {
        _flavor.value = value
    }

    fun setDate(value: String) {
        _date.value = value
        updatePrice()
    }

    fun hasNoFlavor(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // Create a list of dates starting with the current date and the following 3 dates
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if (date.value.equals(optionsDate[0])) {
            calculatedPrice += PRICE_FOR_SAME_DAY
        }
        _price.value = calculatedPrice
    }

    fun reset() {
        _quantity.value = 0
        _date.value = optionsDate[0]
        _flavor.value = ""
        _price.value = 0.0

    }
}