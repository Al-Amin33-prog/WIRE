package com.example.wire.security.domain.usecase
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import com.example.wire.core.feature.security.domain.usecase.VerifyPinUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class VerifyPinUseCaseTest {

    private val repository = mockk<SecurityRepository>()

    private lateinit var useCase: VerifyPinUseCase

    @Before
    fun setup() {
        useCase = VerifyPinUseCase(repository)
    }

    @Test
    fun `returns true when pin matches`() = runTest {

        coEvery {
            repository.verifyPin("1234")
        } returns true

        val result = useCase("1234")

        assertTrue(result)
    }
}