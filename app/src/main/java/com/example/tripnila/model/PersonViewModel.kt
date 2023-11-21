package com.example.tripnila.model

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripnila.data.Staycation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PersonViewModel : ViewModel() {
    val personsValue = listOf(
        Person("John", 25),
        Person("Alice", 30),
        Person("Bob", 22)
        // Add more persons as needed
    )

    private val _persons = MutableStateFlow<List<Person>>(personsValue)
    val persons: StateFlow<List<Person>> get() = _persons


    private val _selectedPerson = MutableStateFlow<Person?>(null)
    val selectedPerson: StateFlow<Person?> get() = _selectedPerson

    fun selectPerson(person: Person) {
        _selectedPerson.value = person
    }

    fun clearSelectedPerson() {
        _selectedPerson.value = null
    }
}

data class Person(val name: String, val age: Int)