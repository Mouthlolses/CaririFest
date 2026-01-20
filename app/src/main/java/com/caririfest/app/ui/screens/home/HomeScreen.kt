package com.caririfest.app.ui.screens.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.caririfest.app.navigation.BottomNavigationBar
import com.caririfest.app.navigation.NavItems
import com.caririfest.app.navigation.NavigationGraph
import com.caririfest.app.ui.components.EventCard

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        NavItems.NEWS.route,
        NavItems.HOME.route,
        NavItems.SEARCH.route,
        NavItems.OFFER.route
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        NavigationGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )

        if (currentRoute in bottomBarRoutes) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding()
            ) {
                BottomNavigationBar(navController)
            }
        }
    }
}


@Composable
fun HomeScreenLayout(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val cityLocation by viewModel.cityLocation.collectAsStateWithLifecycle()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier,
            ) {
                items(cityLocation) { city ->
                    com.caririfest.app.ui.components.Card(
                        modifier = Modifier,
                        img = city.img,
                        title = city.name,
                        onCardClick = {
                            navController.navigate("detailsScreen/${city.id}")
                        },
                        cardEnable = false
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenLayoutPreview() {
    HomeScreenLayout(
        navController = rememberNavController()
    )
}

