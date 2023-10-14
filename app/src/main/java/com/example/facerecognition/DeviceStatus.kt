package com.example.facerecognition

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
fun s(){
    deviceStatus(batteryLev = 0.75f, connectStu = false)
}
@Composable
fun deviceStatus(batteryLev: Float, connectStu: Boolean){
    Box (
        modifier = Modifier.size(300.dp),
        contentAlignment = Alignment.Center
    ){
        val painter = painterResource(id = R.drawable.img_2)
        if (connectStu){
            Canvas(
                modifier = Modifier
                    .size(300.dp)
                    .background(colorResource(id = R.color.bg_color))
                    .padding(20.dp)
            ){
                val strokeWidth = 18.dp.toPx()
                drawArc(
                    color = Color(com.example.facerecognition.ui.theme.primary.value),
                    -90f,
                    360f * batteryLev,
                    false,
                    style = Stroke(strokeWidth, cap = StrokeCap.Round),
                    size = Size(size.width, size.height)
                )
                drawCircle(
                    color = Color.White,
                    radius = 212f
                )
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta = (360f * batteryLev + 270f) * (PI / 180f).toFloat()
                val r = size.width / 2f
                val a = cos(beta) * r
                val b = sin(beta) * r

                drawCircle(
                    color = Color(com.example.facerecognition.ui.theme.primary.value),
                    radius = 35f,
                    center = Offset(center.x + a, center.y + b )
                )
                translate((center.x + a)-23, (center.y + b) - 23){
                    with(painter){
                        draw(
                            size = Size(25.dp.toPx(), 25.dp.toPx())
                        )
                    }
                }
            }
            Canvas(
                modifier = Modifier
                    .shadow(10.dp, shape = CircleShape)
                    .size(220.dp)
            ){
                drawCircle(
                    color = Color(com.example.facerecognition.ui.theme.primary.value),
                    radius = 103.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
            Text(
                text = stringResource(id = R.string.connected),
                fontSize = 32.sp,
                color = colorResource(id = R.color.white)
            )
        }else{
            Canvas(
                modifier = Modifier
                    .size(300.dp)
                    .background(colorResource(id = R.color.bg_color))
                    .padding(20.dp)
            ){
                drawCircle(
                    color = Color(com.example.facerecognition.ui.theme.primary.value),
                    radius = 242f
                )
                drawCircle(
                    color = Color.White,
                    radius = 212f
                )
            }
            Canvas(
                modifier = Modifier
                    .shadow(10.dp, shape = CircleShape)
                    .size(220.dp)
            ){
                drawCircle(
                    color = Color(com.example.facerecognition.ui.theme.bg_color.value),
                    radius = 103.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
            Text(
                text = stringResource(id = R.string.disconnected),
                fontSize = 25.sp,
                color = colorResource(id = R.color.white),
                textAlign = TextAlign.Center
            )
        }
    }
}