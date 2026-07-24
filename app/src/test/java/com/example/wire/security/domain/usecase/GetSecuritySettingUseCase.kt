package com.example.wire.security.domain.usecase
import com.example.wire.core.feature.security.domain.model.SecuritySettings
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import com.example.wire.core.feature.security.domain.usecase.GetSecuritySettingsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class GetSecuritySettingsUseCaseTest {

    private val repository = mockk<SecurityRepository>()

    private lateinit var useCase: GetSecuritySettingsUseCase

    @Before
    fun setup() {
        useCase = GetSecuritySettingsUseCase(repository)
    }

    @Test
    fun `returns security settings`() = runTest {

        val settings = SecuritySettings(
            hasPin = true,
            isBiometricEnabled = true
        )

        coEvery {
            repository.getSecuritySettings()
        } returns settings

        val result =   useCase(Unit)

        assertEquals(settings, result)
    }
}