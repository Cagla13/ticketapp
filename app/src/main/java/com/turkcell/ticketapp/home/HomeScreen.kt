package com.turkcell.ticketapp.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkcell.data.remote.EventDto
import com.turkcell.data.remote.TicketDto
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Etkinlikler", "Biletlerim")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ticket App", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1B5E20))
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium) }
                    )
                }
            }

            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF4CAF50))
                    }
                }
                is HomeUiState.Success -> {
                    if (selectedTab == 0) {
                        EventList(state.events)
                    } else {
                        TicketList(state.tickets)
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.message, color = Color.Red)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.fetchHomeData() }) {
                                Text("Yeniden Dene")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventList(events: List<EventDto>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(events) { event ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(event.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(event.description ?: "Açıklama yok", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📅 ${event.date ?: "-"}", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun TicketList(tickets: List<TicketDto>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(tickets) { ticket ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(ticket.event?.title ?: "Bilinmeyen Etkinlik", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF33691E))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Tarih: ${ticket.event?.date ?: "-"}", fontSize = 14.sp)
                    Text("Bilet No: ${ticket.number ?: "-"}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}