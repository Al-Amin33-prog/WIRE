package com.example.wire.feature.auth.domain.usecase



data class AuthUseCases(
    val login: LoginUseCase,
    val createAccount: CreateAccountUseCase,
    val logout: LogoutUseCase,
    val observeAuthState: ObserveAuthStateUseCase,
    val forgotPassword: ForgotPasswordUseCase,
    val googleSignIn: GoogleSignInUseCase,
    val getCurrentUser: GetCurrentUserUseCase,

)