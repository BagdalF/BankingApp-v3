package com.example.bankingapp.lists

import com.example.bankingapp.data.Usuarios
import com.example.bankingapp.db.UsuariosDAO

data class ProfileData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String
)

val profileList: MutableList<ProfileData> = mutableListOf()

suspend fun populateWithGenericProfiles(usuariosDao: UsuariosDAO) {
profileList.add(
        ProfileData(
            id = 1,
            firstName = "a",
            lastName = "b",
            email = "abc",
            phone = "9",
            password = "123"
        )
    )
    profileList.add(
        ProfileData(
            id = 2,
            firstName = "Bob",
            lastName = "The Builder",
            email = "bob.builder@example.com",
            phone = "99999999",
            password = "123"
        )
    )
    profileList.add(
        ProfileData(
            id = 3,
            firstName = "Charlie",
            lastName = "Chaplin",
            email = "charlie.chaplin@example.com",
            phone = "99999999",
            password = "123"
        )
    )
    profileList.add(
        ProfileData(
            id = 4,
            firstName = "Alice",
            lastName = "Wonderland",
            email = "alice.wonderland@example.com",
            phone = "99999999",
            password = "123"
        )
    )

    for (profile in profileList) {
        usuariosDao.insert(
            Usuarios(
                id = profile.id,
                firstName = profile.firstName,
                lastName = profile.lastName,
                email = profile.email,
                phone = profile.phone,
                password = profile.password
            )
        )
    }
}