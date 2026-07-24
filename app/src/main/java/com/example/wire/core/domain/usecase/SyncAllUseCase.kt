package com.example.wire.core.domain.usecase

import com.example.wire.core.data.repository.SyncRepository
import com.example.wire.core.domain.base.BaseUseCase
import javax.inject.Inject

class SyncAllUseCase  @Inject constructor(
    private val repository: SyncRepository
): BaseUseCase<Unit, Unit>(){
    override suspend fun invoke(params: Unit) {
        repository.syncAll()
    }
}