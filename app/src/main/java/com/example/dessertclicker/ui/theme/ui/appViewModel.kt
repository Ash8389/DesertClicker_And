package com.example.dessertclicker.ui.theme.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class appViewModel : ViewModel() {

     private val _uiState = MutableStateFlow(appUiState())
     val uiState: StateFlow<appUiState> = _uiState.asStateFlow()

    private val desserts: List<Dessert> = Datasource.dessertList
    val currentDesert: Dessert = desserts[_uiState.value.currentDesertIndex]

    fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }

    fun onClickDessert() {
        _uiState.update { currentState ->
            val newRevenue = currentState.revenue + currentDesert.price
            val newDessertSold = currentState.dessertsSold + 1

            val dessertToShow = determineDessertToShow(desserts, newDessertSold)
            val newIndex = desserts.indexOf(dessertToShow)

            currentState.copy(
                revenue = newRevenue,
                dessertsSold = newDessertSold,
                currentDesertIndex = newIndex
            )
        }
    }
}
