package com.example.bankingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bankingapp.components.Header
import com.example.bankingapp.controllers.PreferencesHelper
import com.example.bankingapp.controllers.ProfileData
import com.example.bankingapp.lists.populateWithGenericProfiles
import com.example.bankingapp.lists.populateWithGenericTransactions
import com.example.bankingapp.lists.profileList

class BankingAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            populateWithGenericProfiles()
            populateWithGenericTransactions()
            AppNavigation()
        }
    }
}

data class Route(val name: String, val route: String, val icon: ImageVector)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { PreferencesHelper(context) }
    val currentUser = prefs.user

    var selectedItem by remember { mutableIntStateOf(0) }
    var lastSelectedItem by remember { mutableIntStateOf(0) }
    val routes = listOf(
        Route("Home", "home/", Icons.Filled.Home),
        Route("Profile", "profile/", Icons.Filled.Person),
        Route("Bank Statement", "statement/", Icons.Filled.Settings),
        Route("Transaction", "transaction/", Icons.Filled.Info)
    )

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            Header(title = "Login")
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    LoginScreen { email, password, errorMessage ->
                        val user = profileList.find { it.email == email && it.password == password }
                        if (user != null) {
                            prefs.user = user
                            prefs.isLogged = true
                            navController.navigate("home/${user.id}") {
                                popUpTo("login") { inclusive = true }
                            }

                            Toast.makeText(
                                context,
                                "Logged Successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            errorMessage()
                        }
                    }
                }
            }
        }

        // Tela inicial após login
        composable("home/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""
            if (param.isEmpty() || param.toInt() != currentUser?.id) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }

            Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.height(100.dp)
                ) {
                    routes.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.name) },
                            selected = selectedItem == index,
                            onClick = {
                                navController.navigate(item.route + currentUser?.id.toString())
                                lastSelectedItem = selectedItem
                                selectedItem = index
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.DarkGray,
                                unselectedIconColor = Color.DarkGray,
                                unselectedTextColor = Color.DarkGray,
                                indicatorColor = Color(0xFF1976D2)
                            )
                        )
                    }
                }
            }) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = "Home",
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    HomeScreen(currentUser)
                }
            }
        }

        composable("profile/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""
            if (param.isEmpty() || param.toInt() != currentUser?.id) {
                navController.navigate("login") { popUpTo("login") { inclusive = true } }
            }

            Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.height(100.dp)
                ) {
                    routes.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.name) },
                            selected = selectedItem == index,
                            onClick = {
                                navController.navigate(item.route + currentUser?.id.toString())
                                lastSelectedItem = selectedItem
                                selectedItem = index
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.DarkGray,
                                unselectedIconColor = Color.DarkGray,
                                unselectedTextColor = Color.DarkGray,
                                indicatorColor = Color(0xFF1976D2)
                            )
                        )
                    }
                }
            }) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = routes[selectedItem].name,
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    EditProfileScreen(currentUser!!) { firstName, lastName, phone, email ->
                        val updatedProfile = ProfileData(
                            id = currentUser.id,
                            firstName, lastName, phone, email,
                            password = currentUser.password
                        )
                        val index = profileList.indexOfFirst { it.id == currentUser.id }
                        if (index != -1) {
                            profileList[index] = updatedProfile
                        }
                        prefs.user = updatedProfile
                    }
                }
            }
        }

        composable("statement/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""
            if (param.isEmpty() || param.toInt() != currentUser?.id) {
                navController.navigate("login") { popUpTo("login") { inclusive = true } }
            }

            Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.height(100.dp)
                ) {
                    routes.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.name) },
                            selected = selectedItem == index,
                            onClick = {
                                navController.navigate(item.route + currentUser?.id.toString())
                                lastSelectedItem = selectedItem
                                selectedItem = index
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.DarkGray,
                                unselectedIconColor = Color.DarkGray,
                                unselectedTextColor = Color.DarkGray,
                                indicatorColor = Color(0xFF1976D2)
                            )
                        )
                    }
                }
            }) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = routes[selectedItem].name,
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    StatementScreen(currentUser)
                }
            }
        }

        composable("transaction/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""
            if (param.isEmpty() || param.toInt() != currentUser?.id) {
                navController.navigate("login") { popUpTo("login") { inclusive = true } }
            }

            Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.height(100.dp)
                ) {
                    routes.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.name) },
                            selected = selectedItem == index,
                            onClick = {
                                navController.navigate(item.route + currentUser?.id.toString())
                                lastSelectedItem = selectedItem
                                selectedItem = index
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.DarkGray,
                                unselectedIconColor = Color.DarkGray,
                                unselectedTextColor = Color.DarkGray,
                                indicatorColor = Color(0xFF1976D2)
                            )
                        )
                    }
                }
            }) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Header(
                        title = routes[selectedItem].name,
                        onIconClick = {
                            navController.popBackStack()
                            val aux = selectedItem
                            selectedItem = lastSelectedItem
                            lastSelectedItem = aux
                        }
                    )

                    TransactionScreen(currentUser)
                }
            }
        }
    }
}
