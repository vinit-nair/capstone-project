package com.example.gopaywallet.data.model

    data class ProfileUpdateRequest(
        val fullName: String,
        val email: String,
        val phoneNumber: String
    )