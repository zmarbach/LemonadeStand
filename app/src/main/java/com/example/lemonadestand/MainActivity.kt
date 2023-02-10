package com.example.lemonadestand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonadestand.ui.theme.LemonadeStandTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeStandTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LemonadeStand()
                }
            }
        }
    }
}

@Composable
fun LemonadeStand() {
    val step1 = LemonadeStep("Tap the lemon tree to select a lemon", painterResource(id = R.drawable.lemon_tree), "Lemon tree")
    val step2 = LemonadeStep("Keep tapping the lemon to squeeze it", painterResource(id = R.drawable.lemon_squeeze), "Lemon")
    val step3 = LemonadeStep("Tap the lemonade to drink it", painterResource(id = R.drawable.lemon_drink), "Full lemonade")
    val step4 = LemonadeStep("Tap the empty glass to restart", painterResource(id = R.drawable.lemon_restart), "Empty lemonade")

    var currentStep by remember { mutableStateOf(step1) }
    var numberOfClicks by remember { mutableStateOf(0) }
    //Randomly set the number of squeezes required to move from step 2 to step 3
    var numberOfRequiredSqueezes by remember { mutableStateOf((2..4).random()) }

    Column(modifier = Modifier.wrapContentSize(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(currentStep.text, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = currentStep.image,
            contentDescription = currentStep.imageContentDescription,
            modifier = Modifier
                .border(BorderStroke(2.dp, Color(105, 205, 216)), shape = RoundedCornerShape(4.dp))
                .clickable {
                    numberOfClicks++

                    currentStep = when(numberOfClicks){
                        //Do not move on to step 3 until the user has "squeezed" the lemon the required number of times
                        in 1..numberOfRequiredSqueezes -> step2
                        //Show other steps accordingly on each subsequent click after meeting number of required squeezes
                        numberOfRequiredSqueezes + 1 -> step3
                        numberOfRequiredSqueezes + 2 -> step4
                        //Restart scenario
                        else -> step1
                    }

                    //Reset numbers for restart scenario
                    if(currentStep == step1) {
                        numberOfClicks = 0
                        numberOfRequiredSqueezes = (2..4).random()
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LemonadeStandPreview() {
    LemonadeStandTheme {
        LemonadeStand()
    }
}

class LemonadeStep(var text: String, var image: Painter, var imageContentDescription: String){}