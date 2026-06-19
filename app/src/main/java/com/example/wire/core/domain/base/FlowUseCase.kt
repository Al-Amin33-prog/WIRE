package com.example.wire.core.domain.base

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<in P, R> {
    abstract operator fun invoke(params: P): Flow<R>
}