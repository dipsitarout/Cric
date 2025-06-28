package com.example.cric

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.sin
import kotlin.math.cos
import kotlin.random.Random
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import androidx.compose.material.icons.filled.MailOutline
import java.util.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Theme Colors
val PrimaryColor = Color(0xFF6366F1)
val SecondaryColor = Color(0xFF8B5CF6)

// Weather-based color schemes
data class WeatherTheme(
    val background: List<Color>,
    val cardBackground: Color,
    val textColor: Color,
    val accentColor: Color
)

fun getWeatherTheme(condition: String): WeatherTheme {
    return when {
        condition.contains("sunny", ignoreCase = true) ||
                condition.contains("clear", ignoreCase = true) -> WeatherTheme(
            background = listOf(Color(0xFFFFB74D), Color(0xFFFF8A65), Color(0xFFFFAB40)),
            cardBackground = Color(0x80FFF3E0),
            textColor = Color(0xFF4A4A4A),
            accentColor = Color(0xFFFF6F00)
        )
        condition.contains("rain", ignoreCase = true) ||
                condition.contains("drizzle", ignoreCase = true) -> WeatherTheme(
            background = listOf(Color(0xFF64B5F6), Color(0xFF42A5F5), Color(0xFF1E88E5)),
            cardBackground = Color(0x80E3F2FD),
            textColor = Color(0xFF1A1A1A),
            accentColor = Color(0xFF1976D2)
        )
        condition.contains("cloud", ignoreCase = true) ||
                condition.contains("overcast", ignoreCase = true) -> WeatherTheme(
            background = listOf(Color(0xFF90A4AE), Color(0xFF78909C), Color(0xFF607D8B)),
            cardBackground = Color(0x80ECEFF1),
            textColor = Color(0xFF2A2A2A),
            accentColor = Color(0xFF455A64)
        )
        condition.contains("snow", ignoreCase = true) -> WeatherTheme(
            background = listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC), Color(0xFF81D4FA)),
            cardBackground = Color(0x80F1F8E9),
            textColor = Color(0xFF1A1A1A),
            accentColor = Color(0xFF0277BD)
        )
        condition.contains("fog", ignoreCase = true) ||
                condition.contains("mist", ignoreCase = true) -> WeatherTheme(
            background = listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0), Color(0xFFBDBDBD)),
            cardBackground = Color(0x80FAFAFA),
            textColor = Color(0xFF424242),
            accentColor = Color(0xFF757575)
        )
        else -> WeatherTheme(
            background = listOf(PrimaryColor, SecondaryColor, Color(0xFF4F46E5)),
            cardBackground = Color(0x80F8FAFC),
            textColor = Color(0xFF1A1A1A),
            accentColor = PrimaryColor
        )
    }
}

// Enhanced LocationHelper with better geocoding

// Fixed LocationHelper with better error handling
class LocationHelper(private val context: Context) {
    suspend fun getCityNameFromCoordinates(lat: Double, lng: Double): String = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            // Check if geocoder is present
            if (Geocoder.isPresent()) {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                addresses?.firstOrNull()?.let { address ->
                    // Try different address components in order of preference
                    val cityName = address.locality
                        ?: address.subLocality
                        ?: address.subAdminArea
                        ?: address.adminArea
                        ?: address.countryName
                        ?: "Unknown Location"

                    Log.d("LocationHelper", "Found city: $cityName for coordinates: $lat, $lng")
                    return@withContext cityName
                }
            } else {
                Log.e("LocationHelper", "Geocoder not present on this device")
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Error getting city name: ${e.message}")
        }

        // Fallback to reverse geocoding API
        return@withContext getCityFromAPI(lat, lng)
    }

    suspend fun getCoordinatesFromCityName(cityName: String): LatLng? = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            if (Geocoder.isPresent()) {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(cityName, 1)
                addresses?.firstOrNull()?.let { address ->
                    Log.d("LocationHelper", "Found coordinates for $cityName: ${address.latitude}, ${address.longitude}")
                    return@withContext LatLng(address.latitude, address.longitude)
                }
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Error getting coordinates: ${e.message}")
        }

        // Fallback to geocoding API
        return@withContext getCoordinatesFromAPI(cityName)
    }

    // Fallback method using HTTP API
    private suspend fun getCityFromAPI(lat: Double, lng: Double): String {
        return try {
            // Using OpenStreetMap Nominatim API as fallback
            val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lng"
            Log.d("LocationHelper", "Calling API: $url")

            // You would need to implement HTTP client here
            // For now, return a placeholder
            "Location ($lat, $lng)"
        } catch (e: Exception) {
            Log.e("LocationHelper", "API fallback failed: ${e.message}")
            "Unknown Location"
        }
    }

    private suspend fun getCoordinatesFromAPI(cityName: String): LatLng? {
        return try {
            // Using OpenStreetMap Nominatim API as fallback
            val url = "https://nominatim.openstreetmap.org/search?format=json&q=${cityName}"
            Log.d("LocationHelper", "Calling API: $url")

            // You would need to implement HTTP client here
            // For now, return null
            null
        } catch (e: Exception) {
            Log.e("LocationHelper", "API fallback failed: ${e.message}")
            null
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cityName: String = "",
    onLocationSelected: (String) -> Unit,
    onMapReady: (GoogleMap?) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val locationHelper = remember { LocationHelper(context) }

    var mapView by remember { mutableStateOf<MapView?>(null) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var mapInitialized by remember { mutableStateOf(false) }
    var lastSearchedCity by remember { mutableStateOf("") }

    // Effect to search and mark city when cityName changes
    LaunchedEffect(cityName, googleMap, mapInitialized) {
        if (cityName.isNotBlank() &&
            cityName != lastSearchedCity &&
            googleMap != null &&
            mapInitialized) {

            Log.d("GoogleMapView", "Searching for city: $cityName")
            lastSearchedCity = cityName

            try {
                val coordinates = locationHelper.getCoordinatesFromCityName(cityName)
                coordinates?.let { latLng ->
                    googleMap?.let { map ->
                        Log.d("GoogleMapView", "Moving to coordinates: ${latLng.latitude}, ${latLng.longitude}")

                        // Clear previous markers
                        map.clear()

                        // Add marker for the searched city
                        map.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(cityName)
                        )

                        // Move camera to the location with animation
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(latLng, 12f),
                            2000, // 2 second animation
                            object : GoogleMap.CancelableCallback {
                                override fun onFinish() {
                                    Log.d("GoogleMapView", "Camera animation finished")
                                }
                                override fun onCancel() {
                                    Log.d("GoogleMapView", "Camera animation cancelled")
                                }
                            }
                        )
                    }
                } ?: run {
                    Log.w("GoogleMapView", "No coordinates found for city: $cityName")
                }
            } catch (e: Exception) {
                Log.e("GoogleMapView", "Error searching for city: ${e.message}")
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                onCreate(Bundle())
                onResume()

                getMapAsync { map ->
                    googleMap = map
                    mapInitialized = true
                    onMapReady(map)

                    try {
                        Log.d("GoogleMapView", "Map initialized successfully")

                        // Set initial camera position (India)
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(20.5937, 78.9629), 5f
                            )
                        )

                        // Enable map controls
                        map.uiSettings.apply {
                            isZoomControlsEnabled = true
                            isCompassEnabled = true
                            isMyLocationButtonEnabled = false
                            isMapToolbarEnabled = true
                            isZoomGesturesEnabled = true
                            isScrollGesturesEnabled = true
                        }

                        // Set map click listener
                        map.setOnMapClickListener { latLng ->
                            try {
                                Log.d("GoogleMapView", "Map clicked at: ${latLng.latitude}, ${latLng.longitude}")

                                // Clear previous markers
                                map.clear()

                                // Add marker at clicked position
                                val marker = map.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title("Selected Location")
                                )

                                // Move camera to clicked position
                                map.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(latLng, 12f)
                                )

                                // Get city name from coordinates in a coroutine
                                CoroutineScope(Dispatchers.Main).launch {
                                    try {
                                        Log.d("GoogleMapView", "Getting city name for coordinates...")

                                        val cityName = withContext(Dispatchers.IO) {
                                            locationHelper.getCityNameFromCoordinates(
                                                latLng.latitude,
                                                latLng.longitude
                                            )
                                        }

                                        Log.d("GoogleMapView", "City name found: $cityName")

                                        // Update marker title
                                        marker?.title = cityName

                                        // Notify parent component
                                        onLocationSelected(cityName)

                                    } catch (e: Exception) {
                                        Log.e("GoogleMapView", "Error getting city name: ${e.message}")
                                        onLocationSelected("Unknown Location")
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("GoogleMapView", "Error handling map click: ${e.message}")
                            }
                        }

                        // Set marker click listener
                        map.setOnMarkerClickListener { marker ->
                            Log.d("GoogleMapView", "Marker clicked: ${marker.title}")
                            marker.showInfoWindow()
                            true
                        }

                    } catch (e: Exception) {
                        Log.e("GoogleMapView", "Error setting up map: ${e.message}")
                    }
                }
                mapView = this
            }
        },
        update = { view ->
            // Handle any updates if needed
            Log.d("GoogleMapView", "Map view updated")
        }
    )

    // Handle lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            mapView?.let { mv ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        Log.d("GoogleMapView", "Lifecycle: ON_CREATE")
                        mv.onCreate(Bundle())
                    }
                    Lifecycle.Event.ON_START -> {
                        Log.d("GoogleMapView", "Lifecycle: ON_START")
                        mv.onStart()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        Log.d("GoogleMapView", "Lifecycle: ON_RESUME")
                        mv.onResume()
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        Log.d("GoogleMapView", "Lifecycle: ON_PAUSE")
                        mv.onPause()
                    }
                    Lifecycle.Event.ON_STOP -> {
                        Log.d("GoogleMapView", "Lifecycle: ON_STOP")
                        mv.onStop()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        Log.d("GoogleMapView", "Lifecycle: ON_DESTROY")
                        mv.onDestroy()
                    }
                    else -> {}
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView?.onDestroy()
        }
    }
}

// Enhanced SimpleMapView with better functionality
@Composable
fun SimpleMapView(
    modifier: Modifier = Modifier,
    cityName: String = "",
    onLocationSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<android.webkit.WebView?>(null) }
    var lastSearchedCity by remember { mutableStateOf("") }

    // Effect to search city when name changes
    LaunchedEffect(cityName, webView) {
        if (cityName.isNotBlank() &&
            cityName != lastSearchedCity &&
            webView != null) {

            Log.d("SimpleMapView", "Searching for city in WebView: $cityName")
            lastSearchedCity = cityName

            webView?.evaluateJavascript(
                "if(typeof searchCity === 'function') { searchCity('$cityName'); }",
                { result ->
                    Log.d("SimpleMapView", "Search result: $result")
                }
            )
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            android.webkit.WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.allowContentAccess = true

                webViewClient = object : android.webkit.WebViewClient() {
                    override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("SimpleMapView", "WebView page loaded")

                        // Search for city if provided
                        if (cityName.isNotBlank() && cityName != lastSearchedCity) {
                            Log.d("SimpleMapView", "Auto-searching for: $cityName")
                            view?.evaluateJavascript("searchCity('$cityName')", null)
                            lastSearchedCity = cityName
                        }
                    }

                    override fun onReceivedError(
                        view: android.webkit.WebView?,
                        errorCode: Int,
                        description: String?,
                        failingUrl: String?
                    ) {
                        super.onReceivedError(view, errorCode, description, failingUrl)
                        Log.e("SimpleMapView", "WebView error: $description")
                    }
                }

                // Load OpenStreetMap with enhanced functionality
                loadData("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
                        <style>
                            #map { height: 100vh; width: 100%; }
                            body { margin: 0; padding: 0; }
                            .loading { 
                                position: absolute; 
                                top: 50%; 
                                left: 50%; 
                                transform: translate(-50%, -50%); 
                                z-index: 1000; 
                                background: white; 
                                padding: 10px; 
                                border-radius: 5px; 
                            }
                        </style>
                    </head>
                    <body>
                        <div id="loading" class="loading">Loading map...</div>
                        <div id="map"></div>
                        <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
                        <script>
                            console.log('Initializing map...');
                            
                            var map = L.map('map').setView([20.5937, 78.9629], 5);
                            var marker;
                            
                            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                                attribution: '© OpenStreetMap contributors',
                                maxZoom: 18
                            }).addTo(map);
                            
                            // Hide loading indicator when map is ready
                            map.whenReady(function() {
                                console.log('Map is ready');
                                document.getElementById('loading').style.display = 'none';
                            });
                            
                            // Handle map clicks
                            map.on('click', function(e) {
                                console.log('Map clicked at:', e.latlng.lat, e.latlng.lng);
                                addMarker(e.latlng.lat, e.latlng.lng);
                            });
                            
                            function addMarker(lat, lng) {
                                console.log('Adding marker at:', lat, lng);
                                
                                if (marker) {
                                    map.removeLayer(marker);
                                }
                                
                                marker = L.marker([lat, lng]).addTo(map);
                                
                                // Show loading popup
                                marker.bindPopup('Getting location...').openPopup();
                                
                                // Reverse geocoding using Nominatim
                                var url = 'https://nominatim.openstreetmap.org/reverse?format=json&lat=' + lat + '&lon=' + lng + '&addressdetails=1';
                                console.log('Calling reverse geocoding API:', url);
                                
                                fetch(url, {
                                    headers: {
                                        'User-Agent': 'WeatherApp/1.0'
                                    }
                                })
                                .then(response => {
                                    console.log('API response status:', response.status);
                                    return response.json();
                                })
                                .then(data => {
                                    console.log('Geocoding result:', data);
                                    
                                    var city = 'Unknown Location';
                                    if (data && data.address) {
                                        city = data.address.city || 
                                               data.address.town || 
                                               data.address.village || 
                                               data.address.county ||
                                               data.address.state_district ||
                                               data.address.state || 
                                               data.address.country || 
                                               'Unknown Location';
                                    }
                                    
                                    console.log('Final city name:', city);
                                    marker.bindPopup('Location: ' + city).openPopup();
                                    
                                    // Send to Android
                                    if (typeof Android !== 'undefined' && Android.onLocationSelected) {
                                        Android.onLocationSelected(city);
                                    } else {
                                        console.error('Android interface not available');
                                    }
                                })
                                .catch(err => {
                                    console.error('Geocoding error:', err);
                                    marker.bindPopup('Error getting location').openPopup();
                                    
                                    if (typeof Android !== 'undefined' && Android.onLocationSelected) {
                                        Android.onLocationSelected('Unknown Location');
                                    }
                                });
                            }
                            
                            // Search for city and add marker
                            function searchCity(cityName) {
                                if (!cityName || cityName.trim() === '') {
                                    console.log('Empty city name provided');
                                    return;
                                }
                                
                                console.log('Searching for city:', cityName);
                                
                                var url = 'https://nominatim.openstreetmap.org/search?format=json&q=' + encodeURIComponent(cityName) + '&limit=1&addressdetails=1';
                                console.log('Calling search API:', url);
                                
                                fetch(url, {
                                    headers: {
                                        'User-Agent': 'WeatherApp/1.0'
                                    }
                                })
                                .then(response => {
                                    console.log('Search API response status:', response.status);
                                    return response.json();
                                })
                                .then(data => {
                                    console.log('Search result:', data);
                                    
                                    if (data && data.length > 0) {
                                        var result = data[0];
                                        var lat = parseFloat(result.lat);
                                        var lng = parseFloat(result.lon);
                                        
                                        console.log('Found coordinates:', lat, lng);
                                        
                                        // Add marker and center map
                                        addMarker(lat, lng);
                                        map.setView([lat, lng], 12);
                                    } else {
                                        console.log('City not found:', cityName);
                                        alert('City "' + cityName + '" not found');
                                    }
                                })
                                .catch(err => {
                                    console.error('Search error:', err);
                                    alert('Search failed for "' + cityName + '"');
                                });
                            }
                            
                            // Make searchCity available globally
                            window.searchCity = searchCity;
                            
                            console.log('Map initialization complete');
                        </script>
                    </body>
                    </html>
                """.trimIndent(), "text/html", "UTF-8")

                addJavascriptInterface(object {
                    @android.webkit.JavascriptInterface
                    fun onLocationSelected(city: String) {
                        Log.d("SimpleMapView", "Location selected from WebView: $city")
                        // Post to main thread
                        post {
                            onLocationSelected(city)
                        }
                    }
                }, "AndroidInterface")

                webView = this
            }
        },
        update = { view ->
            // Update map when city name changes
            if (cityName.isNotBlank() && cityName != lastSearchedCity) {
                Log.d("SimpleMapView", "Updating WebView with city: $cityName")
                view.evaluateJavascript("searchCity('$cityName')", null)
                lastSearchedCity = cityName
            }
        }
    )
}

@Composable
fun LoadingCard(theme: WeatherTheme) {
    var rotationState by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            rotationState += 10f
            kotlinx.coroutines.delay(50)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Weather Icon
            Canvas(
                modifier = Modifier.size(80.dp)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 3

                // Spinning sun rays
                repeat(8) { i ->
                    val angle = (rotationState + i * 45f) * (3.14159f / 180f)
                    val startX = center.x + cos(angle) * radius
                    val startY = center.y + sin(angle) * radius
                    val endX = center.x + cos(angle) * (radius + 15f)
                    val endY = center.y + sin(angle) * (radius + 15f)

                    drawLine(
                        color = theme.accentColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 4f
                    )
                }

                // Center circle
                drawCircle(
                    color = theme.accentColor,
                    radius = radius * 0.6f,
                    center = center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pulsing dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val scale by animateFloatAsState(
                        targetValue = if ((rotationState.toInt() / 10) % 3 == index) 1.2f else 0.8f,
                        animationSpec = tween(300),
                        label = "dot_scale"
                    )

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(theme.accentColor.copy(alpha = 0.7f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Fetching Weather Data...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = theme.textColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please wait while we get the latest weather information",
                fontSize = 14.sp,
                color = theme.textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Updated WeatherScreen with better map integration
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    val weather by viewModel.weatherState.collectAsState()
    val error by viewModel.errorState.collectAsState()
    var city by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }
    var useSimpleMap by remember { mutableStateOf(false) }
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }

    val weatherTheme = remember(weather) {
        weather?.let { getWeatherTheme(it.current.weather_descriptions.joinToString()) }
            ?: WeatherTheme(
                background = listOf(PrimaryColor, SecondaryColor, Color(0xFF4F46E5)),
                cardBackground = Color(0x80F8FAFC),
                textColor = Color(0xFF1A1A1A),
                accentColor = PrimaryColor
            )
    }

    // Function to search weather
    val searchWeather = {
        if(city.isNotBlank()) {
            isLoading = true
            viewModel.fetchWeather(city)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(weatherTheme.background)
            )
    ) {
        // Weather Effects Background
        weather?.let {
            WeatherEffects(
                condition = it.current.weather_descriptions.joinToString(),
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // App Title
            Text(
                text = "Weather Forecast",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Search Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Enter City Name", color = weatherTheme.textColor) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = weatherTheme.accentColor,
                            unfocusedBorderColor = weatherTheme.accentColor.copy(alpha = 0.5f),
                            focusedTextColor = weatherTheme.textColor,
                            unfocusedTextColor = weatherTheme.textColor
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = weatherTheme.accentColor
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = { searchWeather() }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = searchWeather,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = weatherTheme.accentColor
                        )
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Get Weather",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Map Selection Button
                    OutlinedButton(
                        onClick = {
                            showMap = !showMap
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, weatherTheme.accentColor),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = weatherTheme.accentColor
                        )
                    ) {
                        Icon(
                            Icons.Default.MailOutline,
                            contentDescription = null,
                            tint = weatherTheme.accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (showMap) "Hide Map" else "Select from Map",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = weatherTheme.accentColor
                        )
                    }

                    // Enhanced Map with better integration
                    AnimatedVisibility(
                        visible = showMap,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                if (useSimpleMap) {
                                    SimpleMapView(
                                        modifier = Modifier.fillMaxSize(),
                                        cityName = city,
                                        onLocationSelected = { selectedCity ->
                                            city = selectedCity
                                            if (selectedCity != "Unknown Location") {
                                                isLoading = true
                                                viewModel.fetchWeather(selectedCity)
                                            }
                                        }
                                    )
                                } else {
                                    GoogleMapView(
                                        modifier = Modifier.fillMaxSize(),
                                        cityName = city,
                                        onLocationSelected = { selectedCity ->
                                            city = selectedCity
                                            if (selectedCity != "Unknown Location") {
                                                isLoading = true
                                                viewModel.fetchWeather(selectedCity)
                                            }
                                        },
                                        onMapReady = { mapInstance ->
                                            googleMapInstance = mapInstance
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tap on map to select location or type city name above",
                                    fontSize = 12.sp,
                                    color = weatherTheme.textColor.copy(alpha = 0.6f),
                                    modifier = Modifier.weight(1f)
                                )

                                TextButton(
                                    onClick = { useSimpleMap = !useSimpleMap }
                                ) {
                                    Text(
                                        text = if (useSimpleMap) "Use Google Maps" else "Use Simple Map",
                                        fontSize = 10.sp,
                                        color = weatherTheme.accentColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Weather Content
            LaunchedEffect(weather, error) {
                if (weather != null || error != null) {
                    isLoading = false
                }
            }

            when {
                isLoading -> {
                    LoadingCard(theme = weatherTheme)
                }
                error != null -> {
                    ErrorCard(error = error!!, theme = weatherTheme)
                }
                weather != null -> {
                    WeatherDetailsCard(weather = weather!!, theme = weatherTheme)
                }
                else -> {
                    WelcomeCard(theme = weatherTheme)
                }
            }
        }
    }
}
@Composable
fun WeatherEffects(condition: String, modifier: Modifier = Modifier) {
    when {
//        condition.contains("rain", ignoreCase = true) -> {
//            RainEffect(modifier = modifier)
//        }
//        condition.contains("snow", ignoreCase = true) -> {
//            SnowEffect(modifier = modifier)
//        }
        condition.contains("fog", ignoreCase = true) ||
                condition.contains("mist", ignoreCase = true) -> {
            FogEffect(modifier = modifier)
        }
    }
}

//@Composable
//fun RainEffect(modifier: Modifier = Modifier) {
//    var animationState by remember { mutableStateOf(0f) }
//
//    val raindrops = remember {
//        List(50) {
//            RainDrop(
//                x = Random.nextFloat() * 400f,
//                y = Random.nextFloat() * 800f,
//                speed = Random.nextFloat() * 5f + 2f
//            )
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            animationState += 0.1f
//            kotlinx.coroutines.delay(50)
//        }
//    }
//
//    Canvas(modifier = modifier) {
//        raindrops.forEach { drop ->
//            val currentY = (drop.y + animationState * drop.speed * 10) % (size.height + 100)
//            val adjustedX = drop.x % size.width
//            drawLine(
//                color = Color.White.copy(alpha = 0.6f),
//                start = Offset(adjustedX, currentY),
//                end = Offset(adjustedX, currentY + 20),
//                strokeWidth = 2f
//            )
//        }
//    }
//}
//
//@Composable
//fun SnowEffect(modifier: Modifier = Modifier) {
//    var animationState by remember { mutableStateOf(0f) }
//
//    val snowflakes = remember {
//        List(30) {
//            SnowFlake(
//                x = Random.nextFloat() * 400f,
//                y = Random.nextFloat() * 800f,
//                speed = Random.nextFloat() * 2f + 0.5f,
//                size = Random.nextFloat() * 4f + 2f
//            )
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            animationState += 0.05f
//            kotlinx.coroutines.delay(100)
//        }
//    }
//
//    Canvas(modifier = modifier) {
//        snowflakes.forEach { flake ->
//            val currentY = (flake.y + animationState * flake.speed * 10) % (size.height + 100)
//            val currentX = (flake.x + sin(animationState * 2) * 20) % size.width
//            drawCircle(
//                color = Color.White.copy(alpha = 0.8f),
//                radius = flake.size,
//                center = Offset(currentX, currentY)
//            )
//        }
//    }
//}

@Composable
fun FogEffect(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.blur(radius = 1.dp)) {
        repeat(10) { i ->
            val alpha = (0.1f + i * 0.05f).coerceAtMost(0.3f)
            drawRect(
                color = Color.White.copy(alpha = alpha),
                size = size.copy(height = size.height * 0.1f),
                topLeft = Offset(0f, size.height * i * 0.1f)
            )
        }
    }
}

@Composable
fun ErrorCard(error: String, theme: WeatherTheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️",
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops! Something went wrong",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = theme.textColor,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                fontSize = 14.sp,
                color = theme.textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WelcomeCard(theme: WeatherTheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌤️",
                fontSize = 64.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to Weather App!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = theme.textColor,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter a city name or select from map to get started",
                fontSize = 14.sp,
                color = theme.textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WeatherDetailsCard(weather: WeatherResponse, theme: WeatherTheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Location Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = theme.accentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${weather.location.name}, ${weather.location.country}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.textColor,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = weather.location.localtime,
                fontSize = 14.sp,
                color = theme.textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Temperature Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${weather.current.temperature}°",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Light,
                    color = theme.accentColor
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = weather.current.weather_descriptions.joinToString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.textColor
                    )
                    Text(
                        text = "Feels like ${weather.current.feelslike}°C",
                        fontSize = 14.sp,
                        color = theme.textColor.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Weather Details Grid
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherDetailItem(
                        icon = "💧",
                        label = "Humidity",
                        value = "${weather.current.humidity}%",
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    )
                    WeatherDetailItem(
                        icon = "💨",
                        label = "Wind Speed",
                        value = "${weather.current.wind_speed} km/h",
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherDetailItem(
                        icon = "☀️",
                        label = "UV Index",
                        value = "${weather.current.uv_index}",
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    )
                    WeatherDetailItem(
                        icon = "👁️",
                        label = "Visibility",
                        value = "${weather.current.visibility} km",
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherDetailItem(
                        icon = "☁️",
                        label = "Cloud Cover",
                        value = "${weather.current.cloudcover}%",
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    )
                    WeatherDetailItem(
                        icon = if (weather.current.is_day == "yes") "🌞" else "🌙",
                        label = "Time",
                        value = if (weather.current.is_day == "yes") "Day" else "Night",
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
@Composable
fun WeatherDetailItem(
    icon: String,
    label: String,
    value: String,
    theme: WeatherTheme,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = theme.accentColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = theme.textColor,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = theme.textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}




