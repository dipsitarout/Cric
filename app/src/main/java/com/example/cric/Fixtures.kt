package com.example.cric

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cric8.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Fixtures(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                navController = navController,
                onNavigate = { destination ->
                    if (destination != currentRoute) {
                        navController.navigate(destination) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cric8 Logo
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFFFFB800), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "C8",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CRIC8",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Search...",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Voice Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                // Fixtures Section
                item {
                    var selectedTab by remember { mutableStateOf("Fixtures") }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF6366F1)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Beautiful Fixtures/Results Toggle
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(25.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.15f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                ) {
                                    // Fixtures Tab
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (selectedTab == "Fixtures")
                                                Color(0xFFCDFF47) else Color.Transparent
                                        ),
                                        onClick = { selectedTab = "Fixtures" }
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Fixtures",
                                                fontSize = 14.sp,
                                                fontWeight = if (selectedTab == "Fixtures")
                                                    FontWeight.Bold else FontWeight.Medium,
                                                color = if (selectedTab == "Fixtures")
                                                    Color.Black else Color.White
                                            )
                                        }
                                    }

                                    // Results Tab
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (selectedTab == "Results")
                                                Color(0xFFCDFF47) else Color.Transparent
                                        ),
                                        onClick = { selectedTab = "Results" }
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Results",
                                                fontSize = 14.sp,
                                                fontWeight = if (selectedTab == "Results")
                                                    FontWeight.Bold else FontWeight.Medium,
                                                color = if (selectedTab == "Results")
                                                    Color.Black else Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (selectedTab == "Fixtures") {
                                // Match Type Tags for Fixtures
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(listOf("ODI", "T20", "TEST", "T10", "HUNDRED")) { matchType ->
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (matchType == "ODI") Color.White.copy(alpha = 0.2f) else Color.Transparent
                                            )
                                        ) {
                                            Text(
                                                text = matchType,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Fixture Cards
                                val fixtures = listOf(
                                    Triple(
                                        Triple("Tomorrow • 6:00 AM", "3rd ODI", "NEP 🇳🇵 vs 🇮🇪 IRE"),
                                        "Match yet to begin",
                                        "ICC International Stadium"
                                    ),
                                    Triple(
                                        Triple("Tomorrow • 8:00 PM", "3rd ODI", "PAK 🇵🇰 vs 🏴󠁧󠁢󠁥󠁮󠁧󠁿 ENG"),
                                        "Match yet to begin",
                                        "Karachi International Stadium"
                                    ),
                                    Triple(
                                        Triple("Tomorrow • 5:00 PM", "3rd ODI", "SRI 🇱🇰 vs 🏴󠁧󠁢󠁥󠁮󠁧󠁿 ENG"),
                                        "Match yet to begin",
                                        "Colombo International Stadium"
                                    )
                                )

                                fixtures.forEach { fixture ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            // Match Time and Type
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = fixture.first.first,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.Gray
                                                )

                                                Text(
                                                    text = fixture.first.second,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF6366F1)
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Teams
                                            Text(
                                                text = fixture.first.third,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Match Status
                                            Text(
                                                text = fixture.second,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF4CAF50),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            // Stadium
                                            Text(
                                                text = fixture.third,
                                                fontSize = 11.sp,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Results Section with flags added
                                val results = listOf(
                                    ResultMatch(
                                        matchTitle = "INDIA VS WEST INDIES",
                                        matchSubtitle = "MEN'S T20 TRI- Series East London",
                                        team1Name = "WI",
                                        team1Flag = "🇻🇨",
                                        team1Score = "94/6",
                                        team2Name = "IND",
                                        team2Flag = "🇮🇳",
                                        team2Score = "95/2",
                                        matchDetails = "(15.0 Ov • T20)",
                                        resultText = "India won by 8 wickets"
                                    ),
                                    ResultMatch(
                                        matchTitle = "INDIA VS UAE",
                                        matchSubtitle = "MEN'S ODI Series East London",
                                        team1Name = "UAE",
                                        team1Flag = "🇦🇪",
                                        team1Score = "140/9",
                                        team2Name = "IND",
                                        team2Flag = "🇮🇳",
                                        team2Score = "141/2",
                                        matchDetails = "(35.0 Ov • ODI)",
                                        resultText = "India won by 8 wickets"
                                    ),
                                    ResultMatch(
                                        matchTitle = "USA VS WEST INDIES",
                                        matchSubtitle = "MEN'S T20 TRI- Series East London",
                                        team1Name = "WI",
                                        team1Flag = "🇻🇨",
                                        team1Score = "94/6",
                                        team2Name = "USA",
                                        team2Flag = "🇺🇸",
                                        team2Score = "95/2",
                                        matchDetails = "(15.0 Ov • T20)",
                                        resultText = "USA won by 8 wickets"
                                    )
                                )

                                results.forEach { result ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            // Match Header
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "🏏",
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = result.matchTitle,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }

                                            Text(
                                                text = result.matchSubtitle,
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // Teams and Scores
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Team 1
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    // Team 1 Flag
                                                    Text(
                                                        text = result.team1Flag,
                                                        fontSize = 24.sp
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = result.team1Name,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.Black
                                                    )
                                                }

                                                // Score
                                                Text(
                                                    text = result.team1Score,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(12.dp))

                                            // Team 2
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    // Team 2 Flag
                                                    Text(
                                                        text = result.team2Flag,
                                                        fontSize = 24.sp
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = result.team2Name,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.Black
                                                    )
                                                }

                                                Text(
                                                    text = result.team2Score,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }

                                            Text(
                                                text = result.matchDetails,
                                                fontSize = 11.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(start = 32.dp)
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Text(
                                                text = result.resultText,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF4CAF50)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

// Data class for Result matches
data class ResultMatch(
    val matchTitle: String,
    val matchSubtitle: String,
    val team1Name: String,
    val team1Flag: String,
    val team1Score: String,
    val team2Name: String,
    val team2Flag: String,
    val team2Score: String,
    val matchDetails: String,
    val resultText: String
)

@Composable
fun MatchCard(
    isLive: Boolean,
    matchTitle: String,
    matchDetails: String,
    team1: String,
    team2: String,
    team1Flag: String,
    team2Flag: String,
    team1Score: String,
    team1Details: String,
    team2Score: String,
    team2Details: String,
    result: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Live Badge (only show if live)
            if (isLive) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Red, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Live",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Live ●",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Match Title
            Text(
                text = matchTitle,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = matchDetails,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Team Scores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team 1
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = team1Flag, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = team1,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = team1Score,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (team1Details.isNotEmpty()) {
                        Text(
                            text = team1Details,
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,  // <-- Add this
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = currentRoute == "HomeScreen",
            onClick = { onNavigate("HomeScreen") }, // match your nav graph route
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6366F1),
                selectedTextColor = Color(0xFF6366F1),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Fixtures"
                )
            },
            label = { Text("Fixtures") },
            selected = currentRoute == "fixtures",
            onClick = { onNavigate("fixtures") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6366F1),
                selectedTextColor = Color(0xFF6366F1),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6366F1),
                selectedTextColor = Color(0xFF6366F1),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}
