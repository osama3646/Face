package com.example.facerecognition

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Preview
@Composable
fun Ve(){
    AddPerson(onDismiss = {})
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPerson(onDismiss: () -> Unit){
    var personName by remember {
        mutableStateOf("")
    }
    var personAge by remember {
        mutableStateOf("")
    }
    var personRelationship by remember {
        mutableStateOf("")
    }
    var personCity by remember {
        mutableStateOf("")
    }
    var personPhone by remember {
        mutableStateOf("")
    }

    var nameBol by remember {
        mutableStateOf(false)
    }
    var ageBol by remember {
        mutableStateOf(false)
    }
    var relationshipBol by remember {
        mutableStateOf(false)
    }
    var cityBol by remember {
        mutableStateOf(false)
    }
    var phoneBol by remember {
        mutableStateOf(false)
    }
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.add_new_person)) },
                    navigationIcon = {
                        IconButton(
                            onClick = { onDismiss() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = colorResource(id = R.color.primary)
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            nameBol = personName.isEmpty()
                            ageBol = personAge.isEmpty() || personAge.length > 2 /* TODO */
                            relationshipBol = personRelationship.isEmpty()
                            cityBol = personCity.isEmpty()
                            phoneBol = personPhone.isEmpty() || personPhone.length < 12
                            if (!nameBol && !ageBol && !relationshipBol && !cityBol && !phoneBol){
                                val db = Firebase.firestore
                                val person = hashMapOf(
                                    "name" to personName,
                                    "age" to personAge,
                                    "relationship" to personRelationship,
                                    "city" to personCity,
                                    "phone" to personPhone
                                )
                                db.collection("Person").add(person).addOnSuccessListener { documentReference ->
                                    Log.w(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }
                                onDismiss()
                        }
                        }) {
                            Text(text = stringResource(id = R.string.save), color = colorResource(id = R.color.primary))
                        }
                    }
                )
            }
        ){innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = personName,
                    onValueChange = {
                        personName = it
                        nameBol = personName.isEmpty()
                    },
                    label = {
                        Text(text = stringResource(id = R.string.name))
                    },
                    isError = nameBol
                )
                OutlinedTextField(
                    value = personAge,
                    onValueChange = {
                        if (it.length <= 2) personAge = it
                        ageBol = personAge.isEmpty()
                    },
                    label = {
                        Text(text = stringResource(id = R.string.age))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = ageBol
                )
                OutlinedTextField(
                    value = personRelationship,
                    onValueChange = {
                        personRelationship = it
                        relationshipBol = personRelationship.isEmpty()
                    },
                    label = {
                        Text(text = stringResource(id = R.string.relationship))
                    },
                    isError = relationshipBol
                )
                OutlinedTextField(
                    value = personCity,
                    onValueChange = {
                        personCity = it
                        cityBol = personCity.isEmpty()
                    },
                    label = {
                        Text(text = stringResource(id = R.string.city))
                    },
                    isError = cityBol
                )
                OutlinedTextField(
                    value = personPhone,
                    onValueChange = {
                        if (it.length <= 12) personPhone = it
                        phoneBol = personPhone.isEmpty() || personPhone.length > 12
                    },
                    label = {
                        Text(text = stringResource(id = R.string.phone))
                    },
                    placeholder = {
                                  Text(text = stringResource(id = R.string.phone_layout))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneBol
                )
            }
        }
    }
}