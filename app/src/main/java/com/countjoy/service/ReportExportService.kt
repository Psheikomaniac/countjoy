package com.countjoy.service

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.countjoy.domain.model.AnalyticsReport
import com.countjoy.domain.model.ExportOptions
import java.io.File
import java.io.FileWriter
import java.time.format.DateTimeFormatter

class ReportExportService(private val context: Context) {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
    
    fun exportAsPdf(report: AnalyticsReport, options: ExportOptions): File {
        // PDF export would require a PDF library like iText or Apache PDFBox
        // For now, returning a placeholder
        val fileName = "analytics_report_${report.generatedAt.atZone(java.time.ZoneId.systemDefault()).format(dateFormatter)}.pdf"
        return createExportFile(fileName, "application/pdf") { file ->
            // PDF generation logic would go here
            file.writeText("PDF Report - Placeholder")
        }
    }
    
    fun exportAsCsv(report: AnalyticsReport, options: ExportOptions): File {
        val fileName = "analytics_report_${report.generatedAt.atZone(java.time.ZoneId.systemDefault()).format(dateFormatter)}.csv"
        return createExportFile(fileName, "text/csv") { file ->
            val csv = buildString {
                // Header
                appendLine("Analytics Report - ${report.timeRange}")
                appendLine("Generated: ${report.generatedAt}")
                appendLine()
                
                // Statistics
                appendLine("Event Statistics")
                appendLine("Metric,Value")
                appendLine("Total Events,${report.statistics.totalEvents}")
                appendLine("Active Events,${report.statistics.activeEvents}")
                appendLine("Completed Events,${report.statistics.completedEvents}")
                appendLine("Success Rate,${report.statistics.successRate}%")
                appendLine("Average Duration,${report.statistics.averageDuration}")
                appendLine()
                
                // Category Distribution
                appendLine("Category Distribution")
                appendLine("Category,Count,Percentage")
                report.categoryDistribution.forEach { category ->
                    appendLine("${category.category},${category.count},${category.percentage}%")
                }
                appendLine()
                
                // Milestone Statistics
                appendLine("Milestone Statistics")
                appendLine("Total Milestones,${report.milestoneStats.totalMilestones}")
                appendLine("Achieved Milestones,${report.milestoneStats.achievedMilestones}")
                appendLine("Achievement Rate,${report.milestoneStats.achievementRate}%")
                appendLine()
                
                // Productivity Insights
                appendLine("Productivity Insights")
                appendLine("Most Productive Day,${report.productivityInsights.mostProductiveDay}")
                appendLine("Most Productive Hour,${report.productivityInsights.mostProductiveHour}")
                appendLine("Average Events Per Week,${report.productivityInsights.averageEventsPerWeek}")
                appendLine("Current Streak,${report.productivityInsights.streakDays} days")
                
                if (options.includeRawData) {
                    appendLine()
                    appendLine("Raw Event Data")
                    appendLine("Title,Category,Priority,Created,Target,Status")
                    report.statistics.upcomingEvents.forEach { event ->
                        appendLine("${event.title},${event.category},${event.priority},${event.createdAt},${event.targetDateTime},Active")
                    }
                    report.statistics.recentlyCompleted.forEach { event ->
                        appendLine("${event.title},${event.category},${event.priority},${event.createdAt},${event.targetDateTime},Completed")
                    }
                }
            }
            file.writeText(csv)
        }
    }
    
    fun exportAsJson(report: AnalyticsReport, options: ExportOptions): File {
        val fileName = "analytics_report_${report.generatedAt.atZone(java.time.ZoneId.systemDefault()).format(dateFormatter)}.json"
        return createExportFile(fileName, "application/json") { file ->
            val jsonData = buildJsonReport(report, options)
            file.writeText(jsonData)
        }
    }
    
    fun exportAsHtml(report: AnalyticsReport, options: ExportOptions): File {
        val fileName = "analytics_report_${report.generatedAt.atZone(java.time.ZoneId.systemDefault()).format(dateFormatter)}.html"
        return createExportFile(fileName, "text/html") { file ->
            val html = buildHtmlReport(report, options)
            file.writeText(html)
        }
    }
    
    private fun createExportFile(
        fileName: String,
        mimeType: String,
        writer: (File) -> Unit
    ): File {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore for Android 10+
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
            
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    val tempFile = File.createTempFile("export", null, context.cacheDir)
                    writer(tempFile)
                    tempFile.inputStream().use { input ->
                        input.copyTo(outputStream)
                    }
                    tempFile.delete()
                }
                
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }
            
            File(fileName) // Return reference
        } else {
            // Use external storage for older versions
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            writer(file)
            file
        }
    }
    
    private fun buildJsonReport(report: AnalyticsReport, options: ExportOptions): String {
        val jsonMap = mutableMapOf<String, Any>(
            "generatedAt" to report.generatedAt.toString(),
            "timeRange" to report.timeRange.name,
            "statistics" to mapOf(
                "totalEvents" to report.statistics.totalEvents,
                "activeEvents" to report.statistics.activeEvents,
                "completedEvents" to report.statistics.completedEvents,
                "successRate" to report.statistics.successRate,
                "averageDuration" to report.statistics.averageDuration.toString()
            ),
            "categoryDistribution" to report.categoryDistribution.map { category ->
                mapOf(
                    "category" to category.category,
                    "count" to category.count,
                    "percentage" to category.percentage
                )
            },
            "milestoneStats" to mapOf(
                "totalMilestones" to report.milestoneStats.totalMilestones,
                "achievedMilestones" to report.milestoneStats.achievedMilestones,
                "achievementRate" to report.milestoneStats.achievementRate
            ),
            "productivityInsights" to mapOf(
                "mostProductiveDay" to report.productivityInsights.mostProductiveDay,
                "mostProductiveHour" to report.productivityInsights.mostProductiveHour,
                "averageEventsPerWeek" to report.productivityInsights.averageEventsPerWeek,
                "streakDays" to report.productivityInsights.streakDays,
                "suggestions" to report.productivityInsights.suggestions
            )
        )
        
        if (options.includeCharts) {
            jsonMap["chartData"] = mapOf(
                "creationTimeline" to report.chartData.creationTimeline.size,
                "categoryPieChart" to report.chartData.categoryPieChart.size,
                "durationHistogram" to report.chartData.durationHistogram.size
            )
        }
        
        // Simple JSON conversion
        return org.json.JSONObject(jsonMap).toString(2)
    }
    
    private fun buildHtmlReport(report: AnalyticsReport, options: ExportOptions): String {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Analytics Report</title>
            <meta charset="UTF-8">
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                h1 { color: #333; }
                h2 { color: #666; margin-top: 30px; }
                table { border-collapse: collapse; width: 100%; margin: 20px 0; }
                th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                th { background-color: #f2f2f2; }
                .stat-card { background: #f9f9f9; padding: 15px; margin: 10px 0; border-radius: 5px; }
                .percentage { color: #4CAF50; font-weight: bold; }
            </style>
        </head>
        <body>
            <h1>Analytics Report - ${report.timeRange}</h1>
            <p>Generated: ${report.generatedAt}</p>
            
            <h2>Event Statistics</h2>
            <div class="stat-card">
                <p><strong>Total Events:</strong> ${report.statistics.totalEvents}</p>
                <p><strong>Active Events:</strong> ${report.statistics.activeEvents}</p>
                <p><strong>Completed Events:</strong> ${report.statistics.completedEvents}</p>
                <p><strong>Success Rate:</strong> <span class="percentage">${report.statistics.successRate}%</span></p>
                <p><strong>Average Duration:</strong> ${report.statistics.averageDuration}</p>
            </div>
            
            <h2>Category Distribution</h2>
            <table>
                <tr>
                    <th>Category</th>
                    <th>Count</th>
                    <th>Percentage</th>
                </tr>
                ${report.categoryDistribution.joinToString("") { category ->
                    """<tr>
                        <td>${category.category}</td>
                        <td>${category.count}</td>
                        <td class="percentage">${category.percentage}%</td>
                    </tr>"""
                }}
            </table>
            
            <h2>Milestone Statistics</h2>
            <div class="stat-card">
                <p><strong>Total Milestones:</strong> ${report.milestoneStats.totalMilestones}</p>
                <p><strong>Achieved:</strong> ${report.milestoneStats.achievedMilestones}</p>
                <p><strong>Achievement Rate:</strong> <span class="percentage">${report.milestoneStats.achievementRate}%</span></p>
            </div>
            
            <h2>Productivity Insights</h2>
            <div class="stat-card">
                <p><strong>Most Productive Day:</strong> ${report.productivityInsights.mostProductiveDay}</p>
                <p><strong>Peak Hour:</strong> ${report.productivityInsights.mostProductiveHour}:00</p>
                <p><strong>Weekly Average:</strong> ${report.productivityInsights.averageEventsPerWeek} events</p>
                <p><strong>Current Streak:</strong> ${report.productivityInsights.streakDays} days</p>
            </div>
            
            ${if (report.productivityInsights.suggestions.isNotEmpty()) {
                """<h2>Suggestions</h2>
                <ul>
                ${report.productivityInsights.suggestions.joinToString("") { "<li>$it</li>" }}
                </ul>"""
            } else ""}
        </body>
        </html>
        """.trimIndent()
    }
}