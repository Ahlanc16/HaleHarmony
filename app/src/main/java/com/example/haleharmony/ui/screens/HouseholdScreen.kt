/*
package com.example.haleharmony.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.haleharmony.ui.viewmodel.CreateJoinState
import com.example.haleharmony.ui.viewmodel.HouseholdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdScreen(
    viewModel: HouseholdViewModel = viewModel(factory = HouseholdViewModel.Factory),
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Household") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            CreateOrJoinHousehold(viewModel = viewModel)
        }
    }
}

@Composable
fun CreateOrJoinHousehold(
    viewModel: HouseholdViewModel,
) {
    var householdName by remember { mutableStateOf("") }
    var householdId by remember { mutableStateOf("") }
    val createJoinState by viewModel.createJoinState.collectAsState()

    Column {
        OutlinedTextField(
            value = householdName,
            onValueChange = { householdName = it },
            label = { Text("Household Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.createHousehold(householdName) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Create Household")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = householdId,
            onValueChange = { householdId = it },
            label = { Text("Household ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.joinHousehold(householdId) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Join Household")
        }

        if (createJoinState is CreateJoinState.Error) {
            Text(
                text = (createJoinState as CreateJoinState.Error).message,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
*/