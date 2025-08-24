package com.countjoy.service

import com.countjoy.domain.model.*
import com.countjoy.domain.repository.EventRepository
import com.countjoy.domain.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.*
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class AnalyticsService @Inject constructor(
    private val eventRepository: EventRepository,
    private val milestoneRepository: MilestoneRepository
) {
    
    suspend fun getEventStatistics(timeRange: TimeRange = TimeRange.ALL_TIME): EventStatistics {
        val allEvents = eventRepository.getAllEvents().first()
        val filteredEvents = filterEventsByTimeRange(allEvents, timeRange)
        
        val totalEvents = filteredEvents.size
        val activeEvents = filteredEvents.count { it.isActive }
        val completedEvents = filteredEvents.count { !it.isActive }
        
        val durations = filteredEvents.mapNotNull { event ->
            if (!event.isActive) {
                Duration.between(event.createdAt, event.targetDateTime)
            } else null
        }
        
        val averageDuration = if (durations.isNotEmpty()) {
            Duration.ofMillis(durations.map { it.toMillis() }.average().toLong())
        } else {
            Duration.ZERO
        }
        
        val successRate = if (totalEvents > 0) {
            (completedEvents.toFloat() / totalEvents.toFloat()) * 100
        } else {
            0f
        }
        
        val eventsByCategory = filteredEvents.groupingBy { it.category }.eachCount()
        val eventsByPriority = filteredEvents.groupingBy { it.priority }.eachCount()
        
        val creationTrend = calculateTrend(filteredEvents) { it.createdAt }
        val completionTrend = calculateTrend(
            filteredEvents.filter { !it.isActive }
        ) { it.targetDateTime }
        
        val upcomingEvents = filteredEvents
            .filter { it.isActive && it.targetDateTime.isAfter(Instant.now()) }
            .sortedBy { it.targetDateTime }
            .take(5)
        
        val recentlyCompleted = filteredEvents
            .filter { !it.isActive }
            .sortedByDescending { it.targetDateTime }
            .take(5)
        
        return EventStatistics(
            totalEvents = totalEvents,
            activeEvents = activeEvents,
            completedEvents = completedEvents,
            averageDuration = averageDuration,
            successRate = successRate,
            eventsByCategory = eventsByCategory,
            eventsByPriority = eventsByPriority,
            creationTrend = creationTrend,
            completionTrend = completionTrend,
            upcomingEvents = upcomingEvents,
            recentlyCompleted = recentlyCompleted
        )
    }
    
    suspend fun getCategoryDistribution(timeRange: TimeRange = TimeRange.ALL_TIME): List<CategoryDistribution> {
        val allEvents = eventRepository.getAllEvents().first()
        val filteredEvents = filterEventsByTimeRange(allEvents, timeRange)
        
        val totalEvents = filteredEvents.size
        val categoryGroups = filteredEvents.groupBy { it.category }
        
        return categoryGroups.map { (category, events) ->
            CategoryDistribution(
                category = category,
                count = events.size,
                percentage = if (totalEvents > 0) {
                    (events.size.toFloat() / totalEvents.toFloat()) * 100
                } else {
                    0f
                },
                color = getCategoryColor(category)
            )
        }.sortedByDescending { it.count }
    }
    
    suspend fun getMilestoneStatistics(): MilestoneStats {
        val achievements = milestoneRepository.getAchievementHistory().first()
        
        val totalMilestones = achievements.size
        val achievedMilestones = achievements.count { it.isAchieved }
        
        val achievementRate = if (totalMilestones > 0) {
            (achievedMilestones.toFloat() / totalMilestones.toFloat()) * 100
        } else {
            0f
        }
        
        val achievementTimes = achievements.mapNotNull { milestone ->
            milestone.achievedAt?.let { achievedAt ->
                Duration.between(Instant.now(), achievedAt)
            }
        }
        
        val averageTimeToAchieve = if (achievementTimes.isNotEmpty()) {
            Duration.ofMillis(achievementTimes.map { it.toMillis() }.average().toLong())
        } else {
            Duration.ZERO
        }
        
        val typeGroups = achievements.groupBy { it.type }
        val mostAchievedType = typeGroups.maxByOrNull { it.value.size }?.key?.name
        
        val recentAchievements = achievements
            .filter { it.isAchieved }
            .sortedByDescending { it.achievedAt }
            .take(10)
        
        return MilestoneStats(
            totalMilestones = totalMilestones,
            achievedMilestones = achievedMilestones,
            achievementRate = achievementRate,
            averageTimeToAchieve = averageTimeToAchieve,
            mostAchievedType = mostAchievedType,
            recentAchievements = recentAchievements
        )
    }
    
    suspend fun getProductivityInsights(timeRange: TimeRange = TimeRange.LAST_30_DAYS): ProductivityInsights {
        val allEvents = eventRepository.getAllEvents().first()
        val filteredEvents = filterEventsByTimeRange(allEvents, timeRange)
        
        // Calculate most productive day of week
        val eventsByDayOfWeek = filteredEvents.groupBy {
            it.createdAt.atZone(ZoneId.systemDefault()).dayOfWeek
        }
        val mostProductiveDay = eventsByDayOfWeek.maxByOrNull { it.value.size }?.key?.name ?: "N/A"
        
        // Calculate most productive hour
        val eventsByHour = filteredEvents.groupBy {
            it.createdAt.atZone(ZoneId.systemDefault()).hour
        }
        val mostProductiveHour = eventsByHour.maxByOrNull { it.value.size }?.key ?: 0
        
        // Calculate average events per week
        val weeks = ChronoUnit.WEEKS.between(
            filteredEvents.minOfOrNull { it.createdAt } ?: Instant.now(),
            Instant.now()
        ).coerceAtLeast(1)
        val averageEventsPerWeek = filteredEvents.size.toFloat() / weeks
        
        // Calculate average completion time
        val completionTimes = filteredEvents.mapNotNull { event ->
            if (!event.isActive) {
                Duration.between(event.createdAt, event.targetDateTime)
            } else null
        }
        
        val averageCompletionTime = if (completionTimes.isNotEmpty()) {
            Duration.ofMillis(completionTimes.map { it.toMillis() }.average().toLong())
        } else {
            Duration.ZERO
        }
        
        // Calculate streak days
        val streakDays = calculateStreakDays(filteredEvents)
        
        // Generate suggestions
        val suggestions = generateSuggestions(filteredEvents, mostProductiveDay, mostProductiveHour)
        
        return ProductivityInsights(
            mostProductiveDay = mostProductiveDay,
            mostProductiveHour = mostProductiveHour,
            averageEventsPerWeek = averageEventsPerWeek,
            averageCompletionTime = averageCompletionTime,
            streakDays = streakDays,
            suggestions = suggestions
        )
    }
    
    suspend fun generateAnalyticsReport(timeRange: TimeRange): AnalyticsReport {
        val statistics = getEventStatistics(timeRange)
        val categoryDistribution = getCategoryDistribution(timeRange)
        val milestoneStats = getMilestoneStatistics()
        val productivityInsights = getProductivityInsights(timeRange)
        val chartData = generateChartData(statistics, categoryDistribution)
        
        return AnalyticsReport(
            generatedAt = Instant.now(),
            timeRange = timeRange,
            statistics = statistics,
            categoryDistribution = categoryDistribution,
            milestoneStats = milestoneStats,
            productivityInsights = productivityInsights,
            chartData = chartData
        )
    }
    
    private fun filterEventsByTimeRange(
        events: List<CountdownEvent>,
        timeRange: TimeRange
    ): List<CountdownEvent> {
        val now = Instant.now()
        val startDate = when (timeRange) {
            TimeRange.TODAY -> now.minus(1, ChronoUnit.DAYS)
            TimeRange.THIS_WEEK -> now.minus(7, ChronoUnit.DAYS)
            TimeRange.THIS_MONTH -> now.minus(30, ChronoUnit.DAYS)
            TimeRange.LAST_30_DAYS -> now.minus(30, ChronoUnit.DAYS)
            TimeRange.THIS_YEAR -> now.minus(365, ChronoUnit.DAYS)
            TimeRange.ALL_TIME -> Instant.EPOCH
        }
        
        return events.filter { it.createdAt.isAfter(startDate) }
    }
    
    private fun calculateTrend(
        events: List<CountdownEvent>,
        dateExtractor: (CountdownEvent) -> Instant
    ): List<DateCount> {
        val eventsByDate = events.groupBy {
            LocalDate.ofInstant(dateExtractor(it), ZoneId.systemDefault())
        }
        
        return eventsByDate.map { (date, eventsOnDate) ->
            DateCount(date, eventsOnDate.size)
        }.sortedBy { it.date }
    }
    
    private fun getCategoryColor(category: String): Int {
        // Generate a color based on category name hash
        return category.hashCode() or 0xFF000000.toInt()
    }
    
    private fun calculateStreakDays(events: List<CountdownEvent>): Int {
        val today = LocalDate.now()
        val eventDates = events.map {
            LocalDate.ofInstant(it.createdAt, ZoneId.systemDefault())
        }.toSet()
        
        var streak = 0
        var currentDate = today
        
        while (eventDates.contains(currentDate)) {
            streak++
            currentDate = currentDate.minusDays(1)
        }
        
        return streak
    }
    
    private fun generateSuggestions(
        events: List<CountdownEvent>,
        mostProductiveDay: String,
        mostProductiveHour: Int
    ): List<String> {
        val suggestions = mutableListOf<String>()
        
        if (events.isEmpty()) {
            suggestions.add("Start creating countdown events to track your goals!")
        } else {
            suggestions.add("You're most productive on ${mostProductiveDay}s - schedule important events then!")
            suggestions.add("Your peak productivity hour is ${mostProductiveHour}:00 - use this time wisely!")
            
            val completionRate = events.count { !it.isActive }.toFloat() / events.size
            if (completionRate < 0.5f) {
                suggestions.add("Try breaking down large goals into smaller milestones")
            }
            
            if (events.size < 5) {
                suggestions.add("Add more events to better track your progress")
            }
        }
        
        return suggestions
    }
    
    private fun generateChartData(
        statistics: EventStatistics,
        categoryDistribution: List<CategoryDistribution>
    ): ChartData {
        // Creation timeline
        val creationTimeline = statistics.creationTrend.mapIndexed { index, dateCount ->
            DataPoint(
                x = index.toFloat(),
                y = dateCount.count.toFloat(),
                label = dateCount.date.toString()
            )
        }
        
        // Category pie chart
        val categoryPieChart = categoryDistribution.map { dist ->
            PieSlice(
                value = dist.percentage,
                label = dist.category,
                color = dist.color
            )
        }
        
        // Duration histogram
        val durationHistogram = listOf(
            HistogramBin("< 1 day", statistics.eventsByPriority[0] ?: 0),
            HistogramBin("1-7 days", statistics.eventsByPriority[1] ?: 0),
            HistogramBin("1-4 weeks", statistics.eventsByPriority[2] ?: 0),
            HistogramBin("1-3 months", statistics.eventsByPriority[3] ?: 0),
            HistogramBin("> 3 months", statistics.eventsByPriority[4] ?: 0)
        )
        
        // Completion rate over time
        val completionRate = statistics.completionTrend.mapIndexed { index, dateCount ->
            DataPoint(
                x = index.toFloat(),
                y = dateCount.count.toFloat(),
                label = dateCount.date.toString()
            )
        }
        
        // Activity heatmap (simplified)
        val activityHeatmap = (0..6).flatMap { day ->
            (0..23).map { hour ->
                HeatmapCell(
                    day = day,
                    hour = hour,
                    intensity = kotlin.random.Random.nextFloat() // Would calculate from real data
                )
            }
        }
        
        return ChartData(
            creationTimeline = creationTimeline,
            categoryPieChart = categoryPieChart,
            durationHistogram = durationHistogram,
            completionRate = completionRate,
            activityHeatmap = activityHeatmap
        )
    }
}