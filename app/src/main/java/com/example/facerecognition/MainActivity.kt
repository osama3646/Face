@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.facerecognition

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facerecognition.repository.PersonRepository
import com.example.facerecognition.ui.theme.FaceRecognitionTheme
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaceRecognitionTheme {
                navBar()
            }
//            mainLayout()
        }
    }
}
@Composable
fun navBar(){
    val item = listOf(
        NavigationItem(
            title = "All",
            selectedIcon = Icons.Default.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = "Urgent",
            selectedIcon = Icons.Default.Info,
            unselectedIcon = Icons.Outlined.Info,
            badgeCount = 45
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Default.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )
    Surface(
        modifier = Modifier.fillMaxSize()
    ){
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    item.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = colorResource(id = R.color.scnondry)
                            ),
                            selected = index == selectedItemIndex,
                            onClick = {
                                selectedItemIndex = index
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex){
                                        item.selectedIcon }else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            badge = {
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            }
                        )
                    }
                }
            },
            drawerState = drawerState,
            scrimColor = Color.DarkGray
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "") },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = colorResource(id = R.color.bg_color),
                            titleContentColor = colorResource(id = R.color.primary),
                            navigationIconContentColor = colorResource(id = R.color.primary)
                            ),
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }
                    )
                }
            ){
                mainLayout()
            }
        }
    }
}
@Composable
fun mainLayout(){
    Column (
        modifier = Modifier.background(colorResource(id = R.color.bg_color)),

    ){
        topLayout()
        personList()
    }
}
@Composable
fun topLayout(){
    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    Column (
        modifier = Modifier.padding(20.dp)
    ){
        menu()
        Box (
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            deviceStatus(batteryLev = 0.65f, connectStu = true)
        }
        SmallFloatingActionButton(
            onClick = { isDialogOpen = true },
            shape = CircleShape,
            modifier = Modifier
                .width(28.dp)
                .height(28.dp),
            containerColor = colorResource(id = R.color.primary),
            contentColor = colorResource(id = R.color.bg_color)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, )
        }
    }
    if (isDialogOpen){
        AddPerson (onDismiss = {
            isDialogOpen = false
        })
    }
}
@Composable
fun menu(){
    SmallFloatingActionButton(
        onClick = { /*TODO*/ },
        shape = CircleShape,
        modifier = Modifier
            .width(38.dp)
            .height(38.dp),
        containerColor = colorResource(id = R.color.bg_color),
        contentColor = colorResource(id = R.color.primary)
    ) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
    }
}
@Composable
fun personList(){
    val personRepository = PersonRepository()
    val getAllData = personRepository.getAllData()
    Spacer(modifier = Modifier.height(20.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 20.dp)
    ){
        items(items = getAllData){ person ->
            PersonCard(person = person)
        }
    }
}

@Preview
@Composable
fun viewse(){
    FaceRecognitionTheme {
        navBar()
    }
}