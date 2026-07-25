package com.example.wire.core.feature.security.domain.repository

import com.example.wire.core.feature.security.domain.model.SecuritySettings


interface SecurityRepository {

    suspend fun verifyPin(

        pin:String,
        ): Boolean
    suspend fun enableBiometric(

    )
    suspend fun disableBiometric(

    )
    suspend fun getSecuritySettings(

    ): SecuritySettings
    suspend fun createPin(

        pin: String
    )
    suspend fun hasPin(

    ): Boolean
}