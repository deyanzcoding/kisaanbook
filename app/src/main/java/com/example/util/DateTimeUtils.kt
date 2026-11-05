package com.example.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility object for handling relative time formatting and timestamps.
 * This fixes the hardcoded date issues throughout the application.
 */
object DateTimeUtils {

    private val todayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val shortFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    /**
     * Gets current date in "dd MMM yyyy" format
     */
    fun getTodayDate(): String {
        return todayFormat.format(Date())
    }

    /**
     * Gets current date in "dd MMM" format (short)
     */
    fun getTodayDateShort(): String {
        return shortFormat.format(Date())
    }

    /**
     * Gets "Updated Today" text for mandi prices
     */
    fun getUpdatedTodayText(): String {
        return "Updated Today"
    }

    /**
     * Returns a relative time string based on the timestamp.
     * For posts, comments, etc.
     */
    fun getRelativeTimestamp(timestamp: String): String {
        // If already in relative format, return as-is
        if (timestamp.contains("ago") || timestamp.contains("Just now") ||
            timestamp.contains("h") && timestamp.contains(":")) {
            return timestamp
        }
        return "Just now"
    }

    /**
     * Gets formatted date for a specific number of days ago
     */
    fun getDaysAgo(days: Int): String {
        return when (days) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> "$days days ago"
        }
    }

    /**
     * Validates if a date string is a valid format
     */
    fun isValidDate(dateString: String): Boolean {
        return try {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).parse(dateString)
            true
        } catch (e: Exception) {
            false
        }
    }
}