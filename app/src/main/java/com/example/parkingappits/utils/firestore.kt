package com.example.parkingappits.utils

import com.google.firebase.firestore.DocumentSnapshot

inline fun <reified T> DocumentSnapshot.toTypedObject(): T? {
    return this.toObject(T::class.java)
}
