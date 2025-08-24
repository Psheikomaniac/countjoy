package com.countjoy.presentation.milestone

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import com.countjoy.domain.model.Milestone
import com.countjoy.domain.model.MilestoneType
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter

object MilestoneShareHelper {
    
    fun shareMilestoneAchievement(
        context: Context,
        milestone: Milestone,
        eventTitle: String
    ) {
        val shareText = buildShareText(milestone, eventTitle)
        val shareImage = createMilestoneImage(context, milestone, eventTitle)
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = if (shareImage != null) "image/*" else "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            shareImage?.let { 
                putExtra(Intent.EXTRA_STREAM, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        
        context.startActivity(Intent.createChooser(shareIntent, "Share Achievement"))
    }
    
    fun shareMilestoneHistory(
        context: Context,
        milestones: List<Milestone>,
        format: ShareFormat = ShareFormat.TEXT
    ) {
        when (format) {
            ShareFormat.TEXT -> shareAsText(context, milestones)
            ShareFormat.CSV -> shareAsCsv(context, milestones)
            ShareFormat.IMAGE -> shareAsImage(context, milestones)
        }
    }
    
    private fun buildShareText(milestone: Milestone, eventTitle: String): String {
        val emoji = when (milestone.type) {
            MilestoneType.PERCENTAGE_BASED -> "📊"
            MilestoneType.TIME_BASED -> "⏰"
            MilestoneType.CUSTOM -> "⭐"
        }
        
        return buildString {
            appendLine("$emoji Achievement Unlocked!")
            appendLine()
            appendLine("🎯 ${milestone.title}")
            appendLine("📝 ${milestone.message}")
            appendLine()
            appendLine("Event: $eventTitle")
            milestone.achievedAt?.let {
                appendLine("Achieved: ${it.toString()}")
            }
            appendLine()
            appendLine("#CountJoy #Milestone #Achievement")
        }
    }
    
    private fun createMilestoneImage(
        context: Context,
        milestone: Milestone,
        eventTitle: String
    ): Uri? {
        return try {
            val width = 1080
            val height = 1080
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            
            // Background gradient
            val gradientPaint = Paint().apply {
                shader = android.graphics.LinearGradient(
                    0f, 0f, width.toFloat(), height.toFloat(),
                    intArrayOf(0xFF6A11CB.toInt(), 0xFF2575FC.toInt()),
                    null,
                    android.graphics.Shader.TileMode.CLAMP
                )
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradientPaint)
            
            // White card background
            val cardPaint = Paint().apply {
                color = Color.WHITE
                setShadowLayer(20f, 0f, 10f, Color.argb(50, 0, 0, 0))
            }
            val cardMargin = 100f
            canvas.drawRoundRect(
                cardMargin,
                cardMargin,
                width - cardMargin,
                height - cardMargin,
                50f,
                50f,
                cardPaint
            )
            
            // Title
            val titlePaint = Paint().apply {
                color = Color.BLACK
                textSize = 80f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(
                "Achievement Unlocked!",
                width / 2f,
                300f,
                titlePaint
            )
            
            // Trophy emoji (simplified)
            val emojiPaint = Paint().apply {
                textSize = 200f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(
                "🏆",
                width / 2f,
                500f,
                emojiPaint
            )
            
            // Milestone title
            val milestoneTitlePaint = Paint().apply {
                color = Color.BLACK
                textSize = 60f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(
                milestone.title,
                width / 2f,
                700f,
                milestoneTitlePaint
            )
            
            // Milestone message
            val messagePaint = Paint().apply {
                color = Color.GRAY
                textSize = 40f
                textAlign = Paint.Align.CENTER
            }
            
            // Wrap text if too long
            val words = milestone.message.split(" ")
            var currentLine = ""
            var yPosition = 800f
            
            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                val textWidth = messagePaint.measureText(testLine)
                
                if (textWidth > width - 2 * cardMargin - 100) {
                    if (currentLine.isNotEmpty()) {
                        canvas.drawText(currentLine, width / 2f, yPosition, messagePaint)
                        yPosition += 50f
                    }
                    currentLine = word
                } else {
                    currentLine = testLine
                }
            }
            if (currentLine.isNotEmpty()) {
                canvas.drawText(currentLine, width / 2f, yPosition, messagePaint)
            }
            
            // Event title
            val eventPaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 35f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(
                eventTitle,
                width / 2f,
                height - 200f,
                eventPaint
            )
            
            // CountJoy branding
            val brandPaint = Paint().apply {
                color = Color.argb(150, 0, 0, 0)
                textSize = 25f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(
                "Created with CountJoy",
                width / 2f,
                height - 150f,
                brandPaint
            )
            
            // Save bitmap to file
            val file = File(context.cacheDir, "milestone_${System.currentTimeMillis()}.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun shareAsText(context: Context, milestones: List<Milestone>) {
        val text = buildString {
            appendLine("🏆 My CountJoy Achievements")
            appendLine("=" .repeat(30))
            appendLine()
            
            milestones.groupBy { it.type }.forEach { (type, typeMilestones) ->
                appendLine("${type.name.replace("_", " ")}:")
                typeMilestones.forEach { milestone ->
                    appendLine("  ✓ ${milestone.title}")
                    appendLine("    ${milestone.message}")
                    milestone.achievedAt?.let {
                        val localDateTime = it.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
                        appendLine("    Achieved: ${localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}")
                    }
                    appendLine()
                }
            }
            
            appendLine()
            appendLine("Total Achievements: ${milestones.size}")
        }
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, "My CountJoy Achievements")
        }
        
        context.startActivity(Intent.createChooser(shareIntent, "Share Achievements"))
    }
    
    private fun shareAsCsv(context: Context, milestones: List<Milestone>) {
        val csvContent = buildString {
            appendLine("Type,Title,Message,Value,Achieved,Date")
            milestones.forEach { milestone ->
                append("\"${milestone.type}\",")
                append("\"${milestone.title}\",")
                append("\"${milestone.message}\",")
                append("${milestone.value},")
                append("${milestone.isAchieved},")
                appendLine("\"${milestone.achievedAt ?: ""}\"")
            }
        }
        
        val file = File(context.cacheDir, "achievements_${System.currentTimeMillis()}.csv")
        file.writeText(csvContent)
        
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "CountJoy Achievements Export")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        context.startActivity(Intent.createChooser(shareIntent, "Export Achievements"))
    }
    
    private fun shareAsImage(context: Context, milestones: List<Milestone>) {
        // Create a summary image with all achievements
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Background
        canvas.drawColor(Color.WHITE)
        
        // Header
        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 60f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(
            "My Achievements",
            width / 2f,
            100f,
            headerPaint
        )
        
        // Achievement count
        val countPaint = Paint().apply {
            color = Color.GRAY
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(
            "${milestones.size} Milestones Achieved",
            width / 2f,
            180f,
            countPaint
        )
        
        // List achievements
        var yPosition = 300f
        val achievementPaint = Paint().apply {
            color = Color.BLACK
            textSize = 35f
        }
        
        milestones.take(20).forEach { milestone ->
            canvas.drawText(
                "✓ ${milestone.title}",
                100f,
                yPosition,
                achievementPaint
            )
            yPosition += 60f
        }
        
        if (milestones.size > 20) {
            canvas.drawText(
                "... and ${milestones.size - 20} more",
                100f,
                yPosition,
                achievementPaint
            )
        }
        
        // Save and share
        val file = File(context.cacheDir, "achievements_summary_${System.currentTimeMillis()}.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        context.startActivity(Intent.createChooser(shareIntent, "Share Achievement Summary"))
    }
    
    enum class ShareFormat {
        TEXT,
        CSV,
        IMAGE
    }
}