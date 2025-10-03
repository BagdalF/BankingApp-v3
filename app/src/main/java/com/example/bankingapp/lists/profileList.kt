package com.example.bankingapp.lists

import com.example.bankingapp.controllers.ProfileData

// Mutable list to store user profiles
val profileList: MutableList<ProfileData> = mutableListOf()


// Function to add initial generic data (optional, you can also add directly)
fun populateWithGenericProfiles() {
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
    // Add more ProfileData objects as needed
}

// You can call this function from somewhere appropriate in your app,
// for example, when the application starts or when you need to initialize this data.
// Or, if this list is meant to be globally accessible with initial data,
// you can populate it directly in an init block or by calling the function here.

// Example of populating it right when this file is loaded (if appropriate for your app structure)
