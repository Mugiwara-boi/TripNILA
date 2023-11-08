package com.example.tripnila.repository

import com.example.tripnila.data.Tourist
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
   // private val usersCollection = db.collection("users")
    private val touristCollection = db.collection("tourist")
    private val hostCollection = db.collection("host")


    suspend fun getAllUsers(): List<Tourist> = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = touristCollection.get().await()
            val userList = mutableListOf<Tourist>()
            for (document in result) {
                val username = document.getString("username") ?: ""
                val password = document.getString("password") ?: ""
                val user = Tourist(username, password)
                userList.add(user)
            }
            userList
        } catch (e: Exception) {
            emptyList() // Return an empty list or handle the error according to your application's requirements
        }
    }

    suspend fun getUserByCredentials(
        username: String,
        password: String
    ): Tourist? = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = touristCollection
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .await()

            if (result.documents.isNotEmpty()) {
                val document = result.documents[0]
                val retrievedUsername = document.getString("username") ?: ""
                val retrievedPassword = document.getString("password") ?: ""
                Tourist(retrievedUsername, retrievedPassword)
            } else {
                null // No user found for provided credentials
            }
        } catch (e: Exception) {
            null // Handle the error according to your application's requirements
        }
    }
    suspend fun addUser(user: Tourist): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val usernameExists = checkIfUsernameExists(user.username)
            if (usernameExists) {
                // Username already exists, return false or handle as needed
                false
            } else {
                val userData = hashMapOf(
                    "fullName" to mapOf(
                        "firstName" to user.firstName,
                        "middleName" to user.middleName,
                        "lastName" to user.lastName
                    ),
                    "username" to user.username,
                    "password" to user.password
                )
                val touristDocRef = touristCollection.add(userData).await()
                val userId = touristDocRef.id // Get the ID of the newly added document in tourist collection
                val hostId = "HOST-$userId"

                // Create data for the host collection and add the document
                val hostData = hashMapOf(
                    "userId" to userId
                    // Add more fields as needed
                )
                hostCollection.document(hostId).set(hostData).await()

                true // Return true if addition was successful
            }
        } catch (e: Exception) {
            false // Return false or handle the error according to your application's requirements
        }
    }

    private suspend fun checkIfUsernameExists(
        username: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = touristCollection.whereEqualTo("username", username).get().await()
            result.documents.isNotEmpty()
        } catch (e: Exception) {
            // Handle the error according to your application's requirements
            false
        }
    }
}

