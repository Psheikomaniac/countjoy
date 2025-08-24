package com.countjoy.domain.model

import java.time.Duration
import java.time.Instant
import java.time.LocalDate

data class EventStatistics(
    val totalEvents: Int,
    val activeEvents: Int,
    val completedEvents: Int,
    val averageDuration: Duration,
    val successRate: Float,
    val eventsByCategory: Map<String, Int>,
    val eventsByPriority: Map<Int, Int>,
    val creationTrend: List<DateCount>,
    val completionTrend: List<DateCount>,
    val upcomingEvents: List<CountdownEvent>,
    val recentlyCompleted: List<CountdownEvent>
)

data class DateCount(
    val date: LocalDate,
    val count: Int
)

data class CategoryDistribution(
    val category: String,
    val count: Int,
    val percentage: Float,
    val color: Int
)

data class TimeRangeStats(
    val range: TimeRange,
    val totalEvents: Int,
    val completedEvents: Int,
    val averageDuration: Duration,
    val mostActiveDay: LocalDate?,
    val mostActiveHour: Int?
)

enum class TimeRange {
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    LAST_30_DAYS,
    THIS_YEAR,
    ALL_TIME
}

data class MilestoneStats(
    val totalMilestones: Int,
    val achievedMilestones: Int,
    val achievementRate: Float,
    val averageTimeToAchieve: Duration,
    val mostAchievedType: String?,
    val recentAchievements: List<Milestone>
)

data class ProductivityInsights(
    val mostProductiveDay: String,
    val mostProductiveHour: Int,
    val averageEventsPerWeek: Float,
    val averageCompletionTime: Duration,
    val streakDays: Int,
    val suggestions: List<String>
)

data class AnalyticsReport(
    val generatedAt: Instant,
    val timeRange: TimeRange,
    val statistics: EventStatistics,
    val categoryDistribution: List<CategoryDistribution>,
    val milestoneStats: MilestoneStats,
    val productivityInsights: ProductivityInsights,
    val chartData: ChartData
)

data class ChartData(
    val creationTimeline: List<DataPoint>,
    val categoryPieChart: List<PieSlice>,
    val durationHistogram: List<HistogramBin>,
    val completionRate: List<DataPoint>,
    val activityHeatmap: List<HeatmapCell>
)

data class DataPoint(
    val x: Float,
    val y: Float,
    val label: String
)

data class PieSlice(
    val value: Float,
    val label: String,
    val color: Int
)

data class HistogramBin(
    val range: String,
    val count: Int
)

data class HeatmapCell(
    val day: Int,
    val hour: Int,
    val intensity: Float
)

data class ExportOptions(
    val format: ExportFormat,
    val includeCharts: Boolean,
    val includeRawData: Boolean,
    val timeRange: TimeRange,
    val categories: List<String>?
)

enum class ExportFormat {
    PDF,
    CSV,
    JSON,
    HTML
}