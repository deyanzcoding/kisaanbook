package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.SampleData
import com.example.model.Answer
import com.example.model.Question
import com.example.model.UserType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CommunityScreen(
    questions: List<Question>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onAskQuestionClick: () -> Unit,
    onAddAnswer: (questionId: String, text: String) -> Unit,
    onMarkAccepted: (questionId: String, answerId: String) -> Unit,
    onUpvoteAnswer: (questionId: String, answerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterChip by remember { mutableStateOf("All") }

    // Required Filter Chips: "All", "Farmer problems", "Zari mahir suggestions", "Seeds", "Agrochemicals", "Others"
    val filterChips = remember {
        listOf(
            "All",
            "Farmer problems",
            "Zari mahir suggestions",
            "Seeds",
            "Agrochemicals",
            "Others"
        )
    }

    // Scroll state for collapsing behavior
    val listState = rememberLazyListState()
    val isScrolledDown by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 60
        }
    }

    // Filter questions
    val filteredQuestions = remember(questions, selectedFilterChip, searchQuery) {
        questions.filter { q ->
            val matchesChip = when (selectedFilterChip) {
                "All" -> true
                "Farmer problems" -> q.cropCategory.equals("Farmer problems", true) || q.author.userType == UserType.FARMER
                "Zari mahir suggestions" -> q.cropCategory.equals("Zari mahir suggestions", true) || q.answers.any { it.isExpert }
                "Seeds" -> q.cropCategory.equals("Seeds", true) || q.title.contains("بیج", true) || q.title.contains("seed", true)
                "Agrochemicals" -> q.cropCategory.equals("Agrochemicals", true) || q.title.contains("سپرے", true) || q.description.contains("سپرے", true)
                "Others" -> true
                else -> q.cropCategory.equals(selectedFilterChip, true)
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                q.title.contains(searchQuery, ignoreCase = true) ||
                q.description.contains(searchQuery, ignoreCase = true) ||
                q.author.name.contains(searchQuery, ignoreCase = true)
            }
            matchesChip && matchesSearch
        }
    }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Collapsible Top Section: Title & Search Bar (collapses smoothly on scroll)
            item {
                AnimatedVisibility(
                    visible = !isScrolledDown,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Title row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Farmer Community Q&A",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "کسانوں کے سوالات اور ماہرین کے مشورے",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search crop problems, treatments, fertilizers...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("community_search_input"),
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // Sticky Pinned Filter Chips Row:
            // "All", "Farmer problems", "Zari mahir suggestions", "Seeds", "Agrochemicals", "Others"
            stickyHeader {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    shadowElevation = if (isScrolledDown) 4.dp else 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filterChips) { chip ->
                                val isSelected = chip == selectedFilterChip
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedFilterChip = chip },
                                    label = {
                                        Text(
                                            text = chip,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                    }
                }
            }

            // Frustration-Free Input Card (Compact Media, Prominent Mic Button)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = SampleData.currentUser.avatarInitials,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Ask question or problem",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Audio, photos, or text description",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Prominent Voice (Mic) & Media action bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Prominent Voice (Mic) Button - farmers prefer audio recording!
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Voice recording started. Speak your farm problem...", Toast.LENGTH_SHORT).show()
                                    onAskQuestionClick()
                                },
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(46.dp)
                                    .testTag("prominent_voice_mic_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Mic,
                                    contentDescription = "Voice Recording",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Voice (Mic)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            // Picture button
                            OutlinedButton(
                                onClick = onAskQuestionClick,
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PhotoCamera,
                                    contentDescription = "Attach Picture",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Picture", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            // Text Post button
                            OutlinedButton(
                                onClick = onAskQuestionClick,
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Write text",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Text", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Questions List
            if (filteredQuestions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.HelpOutline, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("No questions found in this category.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(filteredQuestions, key = { it.id }) { question ->
                    QuestionCardItem(
                        question = question,
                        onAddAnswer = { text -> onAddAnswer(question.id, text) },
                        onMarkAccepted = { answerId -> onMarkAccepted(question.id, answerId) },
                        onUpvoteAnswer = { answerId -> onUpvoteAnswer(question.id, answerId) }
                    )
                }
            }
        }
    }
}

/**
 * Question Card with Compact 200.dp fixed-height picture container,
 * user badges, and responsive comment/reply padding using Modifier.weight(1f)
 */
@Composable
fun QuestionCardItem(
    question: Question,
    onAddAnswer: (String) -> Unit,
    onMarkAccepted: (String) -> Unit,
    onUpvoteAnswer: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var answerInputText by remember { mutableStateOf("") }
    var showAnswerField by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Author DP + Name + User Badge (Green Leaf / Gold tick / Store) + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            when (question.author.userType) {
                                UserType.EXPERT -> Color(0xFFFFF8E1)
                                UserType.DEALER -> Color(0xFFE3F2FD)
                                UserType.FARMER -> MaterialTheme.colorScheme.primaryContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = question.author.avatarInitials,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = when (question.author.userType) {
                            UserType.EXPERT -> Color(0xFFB8860B)
                            UserType.DEALER -> Color(0xFF1976D2)
                            UserType.FARMER -> MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Line 1: Author name + Visual Badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = question.author.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        when (question.author.userType) {
                            UserType.EXPERT -> {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Zari-Mahir",
                                    tint = Color(0xFFD4AF37),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            UserType.DEALER -> {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = "Dokandar",
                                    tint = Color(0xFF1976D2),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            UserType.FARMER -> {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = "Kisaan",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    // Line 2: District · Timestamp
                    Text(
                        text = "${question.district} · ${question.timestamp}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Solved / Unsolved Badge
                if (question.isSolved) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(13.dp))
                            Spacer(Modifier.width(3.dp))
                            Text("Solved", color = Color(0xFF2E7D32), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Question Title & Description
            Text(
                text = question.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = question.description,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Compact fixed-height picture container (height = 200.dp, 12.dp rounded corners)
            // Ensures feed remains clean and not oversized
            if (question.mediaUri != null || question.cropCategory == "Agrochemicals") {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_wheat_field),
                        contentDescription = "Compact Question Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tags row & Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "#${question.cropCategory}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Answer button
                    TextButton(
                        onClick = { showAnswerField = !showAnswerField },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Reply, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Answer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Toggle View Answers
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${question.answers.size} answers",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Inline Answer Input Box
            if (showAnswerField) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = answerInputText,
                        onValueChange = { answerInputText = it },
                        placeholder = { Text("Write your advice or answer...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        maxLines = 3
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (answerInputText.isNotBlank()) {
                                onAddAnswer(answerInputText)
                                answerInputText = ""
                                showAnswerField = false
                                isExpanded = true
                            }
                        },
                        enabled = answerInputText.isNotBlank(),
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Answers / Comments List: responsive weight(1f) layout, no collapsing
            if (isExpanded && question.answers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    question.answers.forEach { ans ->
                        AnswerItemRow(
                            answer = ans,
                            isAccepted = ans.isAccepted,
                            onAcceptClick = { onMarkAccepted(ans.id) },
                            onUpvoteClick = { onUpvoteAnswer(ans.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Answer Item Row with standard 16.dp padding and 8.dp spacing between buttons.
 * Uses Modifier.weight(1f) for comment text area so action buttons are properly placed.
 */
@Composable
fun AnswerItemRow(
    answer: Answer,
    isAccepted: Boolean,
    onAcceptClick: () -> Unit,
    onUpvoteClick: () -> Unit
) {
    Surface(
        color = if (isAccepted) Color(0xFFF1F8E9) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Author DP
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (answer.isExpert) Color(0xFFFFF8E1) else MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = answer.authorName.take(2).uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (answer.isExpert) Color(0xFFB8860B) else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // CRUCIAL: Modifier.weight(1f) for text area so action buttons never collapse
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name + badge + time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = answer.authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    when (answer.authorType) {
                        UserType.EXPERT -> Icon(Icons.Default.Verified, null, tint = Color(0xFFD4AF37), modifier = Modifier.size(13.dp))
                        UserType.DEALER -> Icon(Icons.Default.Storefront, null, tint = Color(0xFF1976D2), modifier = Modifier.size(13.dp))
                        UserType.FARMER -> Icon(Icons.Default.Eco, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(13.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "· ${answer.district} · ${answer.timestamp}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Text
                Text(
                    text = answer.text,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Actions: Upvote and Accept with standard 8.dp spacing
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Upvote button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onUpvoteClick() }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "Upvote",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${answer.upvotes}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Mark as accepted
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onAcceptClick() }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = if (isAccepted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = "Accepted answer",
                            tint = if (isAccepted) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAccepted) "Accepted" else "Accept",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isAccepted) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
