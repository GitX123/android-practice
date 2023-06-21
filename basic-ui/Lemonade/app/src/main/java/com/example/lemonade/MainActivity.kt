package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                LemonApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LemonApp(modifier: Modifier = Modifier) {
    var drawableResourceId: Int
    var contentDescriptionResourceId: Int
    var textDescriptionId: Int
    var onImageClick: () -> Unit
    var stage by remember { mutableStateOf(1) }
    var squeezeCount by remember { mutableStateOf(0) }

    when(stage) {
        1 -> {
            drawableResourceId = R.drawable.lemon_tree
            contentDescriptionResourceId = R.string.stage1_image_description
            textDescriptionId = R.string.stage1_description
            onImageClick = {
                stage = 2
                squeezeCount = (2..4).random()
            }
        }
        2 -> {
            drawableResourceId = R.drawable.lemon_squeeze
            contentDescriptionResourceId = R.string.stage2_image_description
            textDescriptionId = R.string.stage2_description
            onImageClick = {
                squeezeCount--
                if(squeezeCount == 0) {
                    stage = 3
                }
            }
        }
        3 -> {
            drawableResourceId = R.drawable.lemon_drink
            contentDescriptionResourceId = R.string.stage3_image_description
            textDescriptionId = R.string.stage3_description
            onImageClick = {
                stage = 4
            }
        }
        4 -> {
            drawableResourceId = R.drawable.lemon_restart
            contentDescriptionResourceId = R.string.stage4_image_description
            textDescriptionId = R.string.stage4_description
            onImageClick = {
                stage = 1
            }
        }
        else -> {
            drawableResourceId = R.drawable.lemon_tree
            contentDescriptionResourceId = R.string.stage1_image_description
            textDescriptionId = R.string.stage1_description
            onImageClick = {
                stage = 1
            }
        }
    }

    // TODO: Scaffold
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lemonade",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            LemonTextAndImage(
                modifier,
                drawableResourceId,
                contentDescriptionResourceId,
                textDescriptionId,
                onImageClick
            )
        }
    }
}

@Composable
fun LemonTextAndImage(
    modifier: Modifier,
    drawableResourceId: Int,
    contentDescriptionResourceId: Int,
    textDescriptionId: Int,
    onImageClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // image
        Button(onClick = onImageClick) {
            Image(
                painter = painterResource(id = drawableResourceId),
                contentDescription = stringResource(id = contentDescriptionResourceId),
                modifier = modifier
                    .width(dimensionResource(id = R.dimen.button_image_width))
                    .height(dimensionResource(id = R.dimen.button_image_height))
                    .padding(dimensionResource(id = R.dimen.button_interior_padding))
            )
        }
        
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.padding_vertical)))

        // description
        Text(
            text = stringResource(id = textDescriptionId),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LemonadeTheme {
        LemonApp()
    }
}