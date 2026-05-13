package com.suraj.weathersnap.Presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.suraj.weathersnap.data.local.SavedReport
import com.suraj.weathersnap.ui.theme.AccentGreen
import com.suraj.weathersnap.ui.theme.CardBackground
import com.suraj.weathersnap.ui.theme.ChipBackground
import com.suraj.weathersnap.ui.theme.CompressedTeal
import com.suraj.weathersnap.ui.theme.DarkBackground
import com.suraj.weathersnap.ui.theme.DarkButtonBg
import com.suraj.weathersnap.ui.theme.DividerColor
import com.suraj.weathersnap.ui.theme.HeaderGreenDark
import com.suraj.weathersnap.ui.theme.HeaderGreenLight
import com.suraj.weathersnap.ui.theme.HumidityBlue
import com.suraj.weathersnap.ui.theme.InputBorder
import com.suraj.weathersnap.ui.theme.OriginalOrange
import com.suraj.weathersnap.ui.theme.PressureOrange
import com.suraj.weathersnap.ui.theme.TempOrange
import com.suraj.weathersnap.ui.theme.TextMuted
import com.suraj.weathersnap.ui.theme.TextPrimary
import com.suraj.weathersnap.ui.theme.TextSecondary
import com.suraj.weathersnap.ui.theme.WindTeal
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateReportScreen(
    viewModel: WeatherViewModel = koinViewModel(),
    onBack: () -> Unit,
    onSaved: () -> Unit,
    padding: PaddingValues,
    onOpenCamera: () -> Boolean
) {
    val photoProperties = viewModel.PhotoProperties.collectAsState().value
    val fieldNotes by viewModel.fieldNotes.collectAsState()
    val weatherCardUiState by viewModel.weatherCardUiState.collectAsState()
    val weatherDataState = weatherCardUiState

    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(HeaderGreenLight, HeaderGreenDark)),
                        RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(padding)
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Create Report",
                        color = Color(0xFF1A2205),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("Capture, compress, annotate", color = Color(0xFF3A4F10), fontSize = 11.sp)
                }
                TextButton(
                    onClick = onBack,
                    modifier = Modifier
                        .width(80.dp)
                        .background(DarkButtonBg, RoundedCornerShape(30.dp))
                ) {
                    Text(
                        "Back",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(DarkBackground)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(16.dp))


            when (weatherDataState) {
                is WeatherCardUiState.Ideal -> {}
                is WeatherCardUiState.Loading -> {}
                is WeatherCardUiState.Error -> {}
                is WeatherCardUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(CardBackground, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    "${weatherDataState.weatherData.city}, ${weatherDataState.weatherData.country}",
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    weatherDataState.weatherData.condition,
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                "${weatherDataState.weatherData.temperature}°C",
                                color = TempOrange,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MetricChip(
                                "Humidity",
                                "${weatherDataState.weatherData.humidity}%",
                                HumidityBlue,
                                Modifier.weight(1f)
                            )
                            MetricChip(
                                "Wind",
                                "${weatherDataState.weatherData.windSpeed} m/s",
                                WindTeal,
                                Modifier.weight(1f)
                            )
                            MetricChip(
                                "Pressure",
                                "${weatherDataState.weatherData.pressure}",
                                PressureOrange,
                                Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(CardBackground, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        if (photoProperties != null) {
                            AsyncImage(
                                model = photoProperties.imagePath,
                                contentDescription = "Captured",
                                modifier = Modifier.fillMaxWidth().heightIn(max = 250.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min =  200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF3A4A18),
                                                Color(0xFF2A3A14)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Photo preview", color = TextMuted, fontSize = 14.sp)
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        photoProperties?.let {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricChip("Original",
                                    "${photoProperties.originalKb} Kb",OriginalOrange ,
                                    Modifier.weight(1f))
                                MetricChip("Compressed",  "${photoProperties.compressedKb} Kb" ,CompressedTeal,
                                    Modifier.weight(1f))
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Capture Photo Button (outlined)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background( color = HeaderGreenLight, RoundedCornerShape(24.dp))
//                            .border(1.dp, HeaderGreenLight, RoundedCornerShape(24.dp))
                                .clickable(onClick = {
                                    onOpenCamera()
                                })
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val btnName = photoProperties?.let { "Retake Photo" } ?: "Capture Photo"
                            Text(
                                btnName,
                                color = Color(0xFF1A2205),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }



                    Spacer(Modifier.height(16.dp))

                    // Field Notes
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(CardBackground, RoundedCornerShape(12.dp))
                            .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            "Field Notes",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(10.dp))
                        OutlinedTextField(
                            value = fieldNotes,
                            onValueChange = { viewModel.onFieldNotesChange(it) },
                            placeholder = { Text("Notes", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = ChipBackground,
                                unfocusedContainerColor = ChipBackground,
                                focusedBorderColor = InputBorder,
                                unfocusedBorderColor = DividerColor,
                                cursorColor = AccentGreen
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Save Report
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(HeaderGreenLight)
                            .clickable {
                                viewModel.saveReport(
                                    SavedReport(
                                        city = weatherDataState.weatherData.city,
                                        country = weatherDataState.weatherData.country,
                                        condition = weatherDataState.weatherData.condition,
                                        temperature = weatherDataState.weatherData.temperature,
                                        originalSizeKb = photoProperties?.originalKb ?: 0,
                                        compressedSizeKb = photoProperties?.compressedKb ?: 0,
                                        imagePath = photoProperties?.imagePath ?: "",
                                        reportNotes = fieldNotes,
                                    )
                                ); onSaved()
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Save Report",
                            color = Color(0xFF1A2205),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                }
            }

        }
    }


}
