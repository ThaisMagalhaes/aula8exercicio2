package com.example.aula8exercicio2

data class ApiResponse(
    val results: List<UserResponse>
)

data class UserResponse(
    val name: UserName?
)

data class UserName(
    val title: String?,
    val first: String?,
    val last: String?
) {
    val fullName: String
        get() = listOfNotNull(title, first, last).joinToString(" ")
}