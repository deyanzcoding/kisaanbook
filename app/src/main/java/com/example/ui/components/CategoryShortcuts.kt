package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShortcutItem(
    val title: String,
    val titleUrdu: String,
    val icon: ImageVector,
    val color: Color,
    val actionType: String
)

@Composable
fun CategoryShortcuts(
    onShortcutClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ShortcutItem("Crop Advice", "زرعی مشورہ", Icons.Default.Eco, Color(0xFF2E7D32), "ADVICE"),
        ShortcutItem("Ask a Farmer", "سوال پوچھیں", Icons.Default.QuestionAnswer, Color(0xFF1565C0), "ASK"),
        ShortcutItem("Mandi Rates", "منڈی کے ریٹ", Icons.Default.TrendingUp, Color(0xFFE65100), "MANDI"),
        ShortcutItem("Seeds & Fertilizer", "بیج و کھاد", Icons.Default.LocalFlorist, Color(0xFF00897B), "SUPPLIES"),
        ShortcutItem("Weather Advisory", "موسمی صورتحال", Icons.Default.WbSunny, Color(0xFFF9A825), "WEATHER"),
        ShortcutItem("Machinery & Rental", "ٹریکٹر و مشینری", Icons.Default.Agriculture, Color(0xFF6A1B9A), "MACHINERY")
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(items) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onShortcutClick(item.actionType) }
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(item.color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.color,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun CropFilterChips(
    categories: List<String>,
    selectedCategory: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSelect(category) }
            ) {
                Text(
                    text = category,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                )
            }
        }
    }
}
