package com.example.haleharmony.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.haleharmony.data.Bill
import com.example.haleharmony.data.Chore
import com.example.haleharmony.data.Event
import com.example.haleharmony.data.Visitor
import com.example.haleharmony.ui.navigation.BottomNavigationBar
import com.example.haleharmony.ui.navigation.Screen
import com.example.haleharmony.ui.viewmodel.BillsViewModel
import com.example.haleharmony.ui.viewmodel.ChoresViewModel
import com.example.haleharmony.ui.viewmodel.EventsViewModel
import com.example.haleharmony.ui.viewmodel.VisitorsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Chores.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Chores.route) { ChoresScreen() }
            composable(Screen.Bills.route) { BillsScreen() }
            composable(Screen.Events.route) { EventsScreen() }
            composable(Screen.Visitors.route) { VisitorsScreen() }
        }
    }
}

@Composable
fun ChoresScreen(choresViewModel: ChoresViewModel = viewModel()) {
    val uiState by choresViewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { choresViewModel.showAddChoreDialog() }) {
            Text("Add Chore")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(uiState.chores) { chore ->
                ChoreItem(
                    chore = chore, 
                    onChoreCompletedChanged = { choresViewModel.toggleChoreCompletion(chore.id) },
                    onDeleteChore = { choresViewModel.deleteChore(chore.id) },
                    onReassignChore = { choresViewModel.showReassignChoreDialog(chore) }
                )
            }
        }
    }

    if (uiState.showAddChoreDialog) {
        AddChoreDialog(
            onAddChore = { name, assignedTo ->
                choresViewModel.addChore(name, assignedTo)
                choresViewModel.dismissAddChoreDialog()
            },
            onDismiss = { choresViewModel.dismissAddChoreDialog() }
        )
    }

    if (uiState.showReassignChoreDialog) {
        ReassignChoreDialog(
            chore = uiState.choreToReassign!!,
            onReassignChore = { chore, newAssignee ->
                choresViewModel.reassignChore(chore.id, newAssignee)
                choresViewModel.dismissReassignChoreDialog()
            },
            onDismiss = { choresViewModel.dismissReassignChoreDialog() }
        )
    }
}

@Composable
fun ChoreItem(chore: Chore, onChoreCompletedChanged: () -> Unit, onDeleteChore: () -> Unit, onReassignChore: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = chore.isCompleted,
            onCheckedChange = { onChoreCompletedChanged() }
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = chore.name)
            Text(text = "Assigned to: ${chore.assignedTo}", style = androidx.compose.ui.text.TextStyle(color = Color.Gray))
        }
        Text(
            text = if (chore.isCompleted) "Complete" else "Not Complete",
            color = if (chore.isCompleted) Color.Green else Color.Red
        )
        IconButton(onClick = onReassignChore) {
            Icon(Icons.Default.Person, contentDescription = "Reassign Chore")
        }
        IconButton(onClick = onDeleteChore) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Chore")
        }
    }
}

@Composable
fun AddChoreDialog(onAddChore: (String, String) -> Unit, onDismiss: () -> Unit) {
    var choreName by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add a new chore")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = choreName,
                    onValueChange = { choreName = it },
                    label = { Text("Chore name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = assignedTo,
                    onValueChange = { assignedTo = it },
                    label = { Text("Assigned to") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { 
                        onAddChore(choreName, assignedTo)
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun ReassignChoreDialog(chore: Chore, onReassignChore: (Chore, String) -> Unit, onDismiss: () -> Unit) {
    var newAssignee by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Reassign \"${chore.name}\"")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = newAssignee,
                    onValueChange = { newAssignee = it },
                    label = { Text("New assignee") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { 
                        onReassignChore(chore, newAssignee)
                    }) {
                        Text("Reassign")
                    }
                }
            }
        }
    }
}


@Composable
fun BillsScreen(billsViewModel: BillsViewModel = viewModel()) {
    val uiState by billsViewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { billsViewModel.showAddBillDialog() }) {
            Text("Add Bill")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(uiState.bills) { bill ->
                BillItem(
                    bill = bill, 
                    onBillPaidChanged = { billsViewModel.toggleBillPaid(bill.id) },
                    onDeleteBill = { billsViewModel.deleteBill(bill.id) }
                )
            }
        }
    }

    if (uiState.showAddBillDialog) {
        AddBillDialog(
            onAddBill = { name, amount ->
                billsViewModel.addBill(name, amount, Date()) // Using current date for simplicity
                billsViewModel.dismissAddBillDialog()
            },
            onDismiss = { billsViewModel.dismissAddBillDialog() }
        )
    }
}

@Composable
fun BillItem(bill: Bill, onBillPaidChanged: () -> Unit, onDeleteBill: () -> Unit) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = bill.isPaid, onCheckedChange = { onBillPaidChanged() })
        Column(modifier = Modifier.weight(1f)) {
            Text(text = bill.name)
            Text(text = "$${String.format("%.2f", bill.amount)}")
        }
        Text(text = "Due: ${dateFormat.format(bill.dueDate)}")
        IconButton(onClick = onDeleteBill) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Bill")
        }
    }
}

@Composable
fun AddBillDialog(onAddBill: (String, Double) -> Unit, onDismiss: () -> Unit) {
    var billName by remember { mutableStateOf("") }
    var billAmount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add a new bill")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = billName,
                    onValueChange = { billName = it },
                    label = { Text("Bill name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = billAmount,
                    onValueChange = { billAmount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { 
                        val amount = billAmount.toDoubleOrNull() ?: 0.0
                        onAddBill(billName, amount)
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun EventsScreen(eventsViewModel: EventsViewModel = viewModel()) {
    val uiState by eventsViewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { eventsViewModel.showAddEventDialog() }) {
            Text("Add Event")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(uiState.events) { event ->
                EventItem(
                    event = event,
                    onDeleteEvent = { eventsViewModel.deleteEvent(event.id) }
                )
            }
        }
    }

    if (uiState.showAddEventDialog) {
        AddEventDialog(
            onAddEvent = { name, description ->
                eventsViewModel.addEvent(name, Date(), description)
                eventsViewModel.dismissAddEventDialog()
            },
            onDismiss = { eventsViewModel.dismissAddEventDialog() }
        )
    }
}

@Composable
fun EventItem(event: Event, onDeleteEvent: () -> Unit) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = event.name)
            Text(text = event.description, style = androidx.compose.ui.text.TextStyle(color = Color.Gray))
        }
        Text(text = dateFormat.format(event.date))
        IconButton(onClick = onDeleteEvent) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Event")
        }
    }
}

@Composable
fun AddEventDialog(onAddEvent: (String, String) -> Unit, onDismiss: () -> Unit) {
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add a new event")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    label = { Text("Event name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { 
                        onAddEvent(eventName, eventDescription)
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun VisitorsScreen(visitorsViewModel: VisitorsViewModel = viewModel()) {
    val uiState by visitorsViewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { visitorsViewModel.showAddVisitorDialog() }) {
            Text("Add Visitor")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(uiState.visitors) { visitor ->
                VisitorItem(
                    visitor = visitor,
                    onDeleteVisitor = { visitorsViewModel.deleteVisitor(visitor.id) }
                )
            }
        }
    }

    if (uiState.showAddVisitorDialog) {
        AddVisitorDialog(
            onAddVisitor = { name ->
                visitorsViewModel.addVisitor(name, Date(), Date()) // Using current date for simplicity
                visitorsViewModel.dismissAddVisitorDialog()
            },
            onDismiss = { visitorsViewModel.dismissAddVisitorDialog() }
        )
    }
}

@Composable
fun VisitorItem(visitor: Visitor, onDeleteVisitor: () -> Unit) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = visitor.name)
            Text(text = "Arrives: ${dateFormat.format(visitor.arrivalDate)}")
            Text(text = "Departs: ${dateFormat.format(visitor.departureDate)}")
        }
        IconButton(onClick = onDeleteVisitor) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Visitor")
        }
    }
}

@Composable
fun AddVisitorDialog(onAddVisitor: (String) -> Unit, onDismiss: () -> Unit) {
    var visitorName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add a new visitor")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = visitorName,
                    onValueChange = { visitorName = it },
                    label = { Text("Visitor name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { 
                        onAddVisitor(visitorName)
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
