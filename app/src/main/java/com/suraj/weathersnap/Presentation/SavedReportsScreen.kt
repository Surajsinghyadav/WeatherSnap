package com.suraj.weathersnap.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.R
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.suraj.weathersnap.data.local.SavedReport
import com.suraj.weathersnap.data.mapper.toExternalModel
import com.suraj.weathersnap.toDateString
import com.suraj.weathersnap.ui.theme.CardBackground
import com.suraj.weathersnap.ui.theme.CardSurface
import com.suraj.weathersnap.ui.theme.ChipBackground
import com.suraj.weathersnap.ui.theme.CompressedTeal
import com.suraj.weathersnap.ui.theme.DarkBackground
import com.suraj.weathersnap.ui.theme.DarkButtonBg
import com.suraj.weathersnap.ui.theme.DividerColor
import com.suraj.weathersnap.ui.theme.HeaderGreenDark
import com.suraj.weathersnap.ui.theme.HeaderGreenLight
import com.suraj.weathersnap.ui.theme.OriginalOrange
import com.suraj.weathersnap.ui.theme.TempOrange
import com.suraj.weathersnap.ui.theme.TextMuted
import com.suraj.weathersnap.ui.theme.TextPrimary
import com.suraj.weathersnap.ui.theme.TextSecondary
import org.koin.androidx.compose.koinViewModel


@Composable
fun SavedReportsScreen(
    viewModel: WeatherViewModel = koinViewModel(),
    onBack: () -> Unit,
    padding: PaddingValues
) {
    val reports by viewModel.savedReports.collectAsState()
    val count   by viewModel.totalReportCount.collectAsState()

    Scaffold(topBar = {
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
                    "Saved Reports",
                    color = Color(0xFF1A2205),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$count reports stored locally",
                    color = Color(0xFF3A4F10), fontSize = 11.sp
                )
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
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reports) { report -> ReportCard(report.toExternalModel()) }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }

    }


}

@Composable
fun ReportCard(report: SavedReport) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {

        if (report.imagePath.isNotEmpty()) {
            AsyncImage(
                model = report.imagePath,
                contentDescription = "Report photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0D1008)),
                contentAlignment = Alignment.Center
            ) {
                Text("No photo", color = TextMuted, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "${report.city}, ${report.country}",
                    color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
                Text(report.condition, color = TextSecondary, fontSize = 13.sp)
                Text(report.date.toDateString(), color = TextMuted, fontSize = 11.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4A5A1A))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    "${report.temperature}°C",
                    color = TempOrange,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            MetricChip(
                label = "Original",
                value = "${report.originalSizeKb} Kb",
                valueColor = OriginalOrange,
                modifier = Modifier.weight(1f)

            )
            MetricChip(
                label = "Compressed",
                value = "${report.compressedSizeKb} Kb",
                valueColor = CompressedTeal,
                modifier = Modifier.weight(1f)

            )

        }

        Spacer(Modifier.height(10.dp))

        if (report.reportNotes.isNotBlank()){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CardSurface)
                    .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(report.reportNotes, color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SizeBadge(
    label : String,
    size : Int,
    color: Color,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .background(ChipBackground, RoundedCornerShape(8.dp))
            .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(label, color = TextMuted, fontSize = 10.sp)
        Text(
            "$size KB",
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}