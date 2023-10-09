package com.example.facerecognition

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facerecognition.model.Person

@Composable
fun PersonCard(person: Person) {
    Row (
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.white))
            .fillMaxWidth()
            .height(70.dp)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        leftBox(name = person.name, relationship = person.relationship)
        rightBox(lastSeen = person.lastSeen)
    }
}
@Composable
fun leftBox(name: String, relationship: String){
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "",
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = name,
                fontSize = 15.sp
            )
            Text(
                text = relationship,
                fontSize = 13.sp
            )
        }
    }
}
@Composable
fun rightBox(lastSeen: String){
    Row (
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxHeight()
    ){

        Text(
            text = lastSeen,
            fontSize = 13.sp

        )
        Spacer(modifier = Modifier.width(15.dp))
        Image(
            painter = painterResource(id = R.drawable.face_recognition),
            contentDescription = "",
            modifier = Modifier.width(20.dp),
        )
    }
}