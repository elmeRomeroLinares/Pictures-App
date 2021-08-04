package com.example.pictures_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pictures_app.ui.theme.PicturesAppTheme
import androidx.navigation.compose.rememberNavController
import com.example.pictures_app.fragments.posts.PostsListFragmentViewModel
import com.example.pictures_app.ui.post_detail.PostDetailBody
import com.example.pictures_app.ui.posts.PostsListBody
import com.example.pictures_app.ui.posts.postList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PicturesAndPostsApp()
        }
    }
}

@Composable
fun PicturesAndPostsApp() {
    PicturesAppTheme {
        val navController = rememberNavController()
        val state = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = state,
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "ComposeActivity")
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.MoreVert, contentDescription = null)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {state.drawerState.open()}
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            },
            drawerContent = {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    onClick = { scope.launch { state.drawerState.close() } },
                    content = { Text("Close Drawer") }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreen.PostsList.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreen.PostsList.name) {
                    PostsListBody(postList) {
                        navController.navigate(AppScreen.PostDetail.name)
                    }
                }
                composable(AppScreen.PostDetail.name) {
                    PostDetailBody()
                }
            }
        }
    }
}