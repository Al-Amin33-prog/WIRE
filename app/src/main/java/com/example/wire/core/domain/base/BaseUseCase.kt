package com.example.wire.core.domain.base

abstract class BaseUseCase<in P, R> {
    abstract suspend operator fun invoke(params: P): R
}