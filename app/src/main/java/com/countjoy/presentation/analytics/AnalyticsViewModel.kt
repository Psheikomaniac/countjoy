package com.countjoy.presentation.analytics

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.domain.model.*
import com.countjoy.service.AnalyticsService
import com.countjoy.service.ReportExportService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsService: AnalyticsService,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _statistics = MutableStateFlow<EventStatistics?>(null)
    val statistics: StateFlow<EventStatistics?> = _statistics.asStateFlow()
    
    private val _categoryDistribution = MutableStateFlow<List<CategoryDistribution>?>(null)
    val categoryDistribution: StateFlow<List<CategoryDistribution>?> = _categoryDistribution.asStateFlow()
    
    private val _milestoneStats = MutableStateFlow<MilestoneStats?>(null)
    val milestoneStats: StateFlow<MilestoneStats?> = _milestoneStats.asStateFlow()
    
    private val _insights = MutableStateFlow<ProductivityInsights?>(null)
    val insights: StateFlow<ProductivityInsights?> = _insights.asStateFlow()
    
    private val _selectedTimeRange = MutableStateFlow(TimeRange.LAST_30_DAYS)
    val selectedTimeRange: StateFlow<TimeRange> = _selectedTimeRange.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _exportStatus = MutableStateFlow<ExportStatus>(ExportStatus.Idle)
    val exportStatus: StateFlow<ExportStatus> = _exportStatus.asStateFlow()
    
    init {
        loadAnalytics()
    }
    
    fun setTimeRange(timeRange: TimeRange) {
        _selectedTimeRange.value = timeRange
        loadAnalytics()
    }
    
    fun refreshData() {
        loadAnalytics()
    }
    
    private fun loadAnalytics() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val timeRange = _selectedTimeRange.value
                
                _statistics.value = analyticsService.getEventStatistics(timeRange)
                _categoryDistribution.value = analyticsService.getCategoryDistribution(timeRange)
                _milestoneStats.value = analyticsService.getMilestoneStatistics()
                _insights.value = analyticsService.getProductivityInsights(timeRange)
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun exportReport(options: ExportOptions) {
        viewModelScope.launch {
            _exportStatus.value = ExportStatus.Exporting
            
            try {
                val report = analyticsService.generateAnalyticsReport(options.timeRange)
                val exportService = ReportExportService(context)
                
                when (options.format) {
                    ExportFormat.PDF -> exportService.exportAsPdf(report, options)
                    ExportFormat.CSV -> exportService.exportAsCsv(report, options)
                    ExportFormat.JSON -> exportService.exportAsJson(report, options)
                    ExportFormat.HTML -> exportService.exportAsHtml(report, options)
                }
                
                _exportStatus.value = ExportStatus.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _exportStatus.value = ExportStatus.Error(e.message ?: "Export failed")
            }
        }
    }
    
    sealed class ExportStatus {
        object Idle : ExportStatus()
        object Exporting : ExportStatus()
        object Success : ExportStatus()
        data class Error(val message: String) : ExportStatus()
    }
}