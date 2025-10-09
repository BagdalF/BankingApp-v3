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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.bankingapp.controllers.deleteUsuario
import com.example.bankingapp.controllers.getAllUsuarios
import com.example.bankingapp.controllers.getUsuarioById
import com.example.bankingapp.controllers.insertUsuario
import com.example.bankingapp.controllers.updateUsuario
import com.example.bankingapp.services.PreferencesHelper
import com.example.bankingapp.lists.populateWithGenericProfiles
import com.example.bankingapp.lists.populateWithGenericTransactions
import kotlinx.coroutines.launch

class BankingAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                populateWithGenericProfiles(
                    AppDatabase.getDatabase(applicationContext).usuariosDAO()
                )

                populateWithGenericTransactions(
                    AppDatabase.getDatabase(applicationContext).transacoesDAO()
                )
            }
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

    val db = AppDatabase.getDatabase(context)
    val usuariosDao = db.usuariosDAO()
    val transacoesDao = db.transacoesDAO()

    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableIntStateOf(0) }
    var lastSelectedItem by remember { mutableIntStateOf(0) }

    // Usuário logado em memória (sincronizado com prefs)
    var currentUser by remember { mutableStateOf(prefs.user) }

    val routes = listOf(
        Route("Home", "home/", Icons.Filled.Home),
        Route("Profile", "profile/", Icons.Filled.Person),
        Route("Bank Statement", "statement/", Icons.Filled.Settings),
        Route("Transaction", "transaction/", Icons.Filled.Info)
    )

    NavHost(navController = navController, startDestination = "login") {

        // ========================= LOGIN =========================
        composable("login") {
            Header(title = "Login")
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    LoginScreen (onLogin = { email, password, errorMessage ->
                        scope.launch {
                            val usuarios = getAllUsuarios(usuariosDao)
                            val user = usuarios.find { it.email == email && it.password == password }
                            if (user != null) {
                                prefs.user = user
                                prefs.isLogged = true
                                currentUser = user
                                navController.navigate("home/${user.id}") {
                                    popUpTo("login") { inclusive = true }
                                }
                                Toast.makeText(context, "Logged Successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                errorMessage()
                            }
                        }
                    }, onNavigateRegister = {
                        navController.navigate("register")
                    })
                }
            }
        }

        // ========================= REGISTER =========================
        composable("register") {
            Header(title = "Create Your Account")
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    RegisterScreen { firstName, lastName, email, phone, password, errorMessage ->
                        scope.launch {
                            try {
                                val novoUsuario = insertUsuario(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    phone = phone,
                                    password = password,
                                    usuariosDao = usuariosDao
                                )

                                if (novoUsuario == null) {
                                    throw Exception("Erro ao criar usuário")
                                }

                                prefs.user = novoUsuario
                                prefs.isLogged = true
                                currentUser = novoUsuario
                                navController.navigate("home/${novoUsuario.id}") {
                                    popUpTo("home/${novoUsuario.id}") { inclusive = true }
                                }
                                Toast.makeText(context, "Logged Successfully!", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                errorMessage()
                            }
                        }
                    }
                }
            }
        }

        // ========================= HOME =========================
        composable("home/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""
            LaunchedEffect(param) {
                if (param.isEmpty()) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    return@LaunchedEffect
                }

                val user = getUsuarioById(param.toInt(), usuariosDao)
                if (user == null) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                } else {
                    currentUser = user
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
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
                    currentUser?.let { HomeScreen(it) }
                }
            }
        }

        // ========================= PROFILE =========================
        composable("profile/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""

            LaunchedEffect(param) {
                val user = if (param.isNotEmpty()) getUsuarioById(param.toInt(), usuariosDao) else null
                if (user == null) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                } else {
                    currentUser = user
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
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

                    currentUser?.let { user ->
                        EditProfileScreen(profile = user,
                        onDelete = {
                            scope.launch {
                                deleteUsuario(user.id, usuariosDao)
                                prefs.user = null
                                currentUser = null
                                navController.navigate("login") { popUpTo("login") { inclusive = true } }
                            }
                        },
                        onSaveChanges = {firstName, lastName, phone, email ->
                            scope.launch {
                                updateUsuario(
                                    id = user.id,
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    phone = phone,
                                    password = user.password,
                                    usuariosDao = usuariosDao
                                )
                                val updated = getUsuarioById(user.id, usuariosDao)
                                if (updated != null) {
                                    prefs.user = updated
                                    currentUser = updated
                                }
                            }
                        })
                    }
                }
            }
        }

        // ========================= STATEMENT =========================
        composable("statement/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""

            LaunchedEffect(param) {
                val user = if (param.isNotEmpty()) getUsuarioById(param.toInt(), usuariosDao) else null
                if (user == null) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                } else {
                    currentUser = user
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
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

                    currentUser?.let { StatementScreen(user = it, usuariosDao = usuariosDao, transacoesDao = transacoesDao) }
                }
            }
        }

        // ========================= TRANSACTION =========================
        composable("transaction/{id}") { backstackEntry ->
            val param = backstackEntry.arguments?.getString("id") ?: ""

            LaunchedEffect(param) {
                val user = if (param.isNotEmpty()) getUsuarioById(param.toInt(), usuariosDao) else null
                if (user == null) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                } else {
                    currentUser = user
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        routes = routes,
                        selectedItem = selectedItem,
                        onItemSelected = { index ->
                            navController.navigate(routes[index].route + currentUser?.id.toString())
                            lastSelectedItem = selectedItem
                            selectedItem = index
                        }
                    )
                }
            ) { innerPadding ->
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

                    currentUser?.let { TransactionScreen(currentUser = it, usuariosDao = usuariosDao, transacoesDao = transacoesDao) }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    routes: List<Route>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
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
                onClick = { onItemSelected(index) },
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
}
