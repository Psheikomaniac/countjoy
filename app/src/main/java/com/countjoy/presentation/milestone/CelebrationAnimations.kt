package com.countjoy.presentation.milestone

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import com.countjoy.domain.model.CelebrationEffect
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

@Composable
fun CelebrationAnimation(
    effect: CelebrationEffect,
    isPlaying: Boolean,
    onAnimationComplete: () -> Unit = {}
) {
    when (effect) {
        CelebrationEffect.CONFETTI -> ConfettiAnimation(isPlaying, onAnimationComplete)
        CelebrationEffect.FIREWORKS -> FireworksAnimation(isPlaying, onAnimationComplete)
        CelebrationEffect.STARS -> StarsAnimation(isPlaying, onAnimationComplete)
        CelebrationEffect.BALLOONS -> BalloonsAnimation(isPlaying, onAnimationComplete)
        CelebrationEffect.SPARKLES -> SparklesAnimation(isPlaying, onAnimationComplete)
        CelebrationEffect.NONE -> { /* No animation */ }
    }
}

@Composable
private fun ConfettiAnimation(
    isPlaying: Boolean,
    onAnimationComplete: () -> Unit
) {
    if (!isPlaying) return
    
    val density = LocalDensity.current
    var confettiPieces by remember { mutableStateOf(generateConfettiPieces()) }
    
    val animationProgress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 3000, easing = LinearEasing),
        finishedListener = { onAnimationComplete() }
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        confettiPieces.forEach { piece ->
            drawConfettiPiece(piece, animationProgress)
        }
    }
    
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(50)
            confettiPieces = confettiPieces.map { it.update() }
        }
    }
}

@Composable
private fun FireworksAnimation(
    isPlaying: Boolean,
    onAnimationComplete: () -> Unit
) {
    if (!isPlaying) return
    
    var fireworks by remember { mutableStateOf(generateFireworks()) }
    
    val animationProgress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        finishedListener = { onAnimationComplete() }
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        fireworks.forEach { firework ->
            drawFirework(firework, animationProgress)
        }
    }
}

@Composable
private fun StarsAnimation(
    isPlaying: Boolean,
    onAnimationComplete: () -> Unit
) {
    if (!isPlaying) return
    
    val stars = remember { generateStars() }
    
    val animationProgress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 2500, easing = FastOutSlowInEasing),
        finishedListener = { onAnimationComplete() }
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isPlaying) 360f else 0f,
        animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        stars.forEach { star ->
            drawStar(star, animationProgress, rotation)
        }
    }
}

@Composable
private fun BalloonsAnimation(
    isPlaying: Boolean,
    onAnimationComplete: () -> Unit
) {
    if (!isPlaying) return
    
    var balloons by remember { mutableStateOf(generateBalloons()) }
    
    val animationProgress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 4000, easing = LinearEasing),
        finishedListener = { onAnimationComplete() }
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        balloons.forEach { balloon ->
            drawBalloon(balloon, animationProgress)
        }
    }
    
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(50)
            balloons = balloons.map { it.update() }
        }
    }
}

@Composable
private fun SparklesAnimation(
    isPlaying: Boolean,
    onAnimationComplete: () -> Unit
) {
    if (!isPlaying) return
    
    val sparkles = remember { generateSparkles() }
    
    val animationProgress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ),
        finishedListener = { onAnimationComplete() }
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        sparkles.forEach { sparkle ->
            drawSparkle(sparkle, animationProgress)
        }
    }
}

// Data classes for animation elements
private data class ConfettiPiece(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val rotation: Float,
    val rotationSpeed: Float,
    val size: Float
) {
    fun update(): ConfettiPiece = copy(
        y = y + vy,
        x = x + vx + sin(y * 0.01f) * 2f,
        rotation = rotation + rotationSpeed,
        vy = vy + 0.5f // gravity
    )
}

private data class Firework(
    val centerX: Float,
    val centerY: Float,
    val color: Color,
    val particles: List<FireworkParticle>
)

private data class FireworkParticle(
    val angle: Float,
    val speed: Float,
    val color: Color
)

private data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val color: Color
)

private data class Balloon(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    val stringLength: Float
) {
    fun update(): Balloon = copy(
        y = y - vy,
        x = x + sin(y * 0.02f) * 0.5f
    )
}

private data class Sparkle(
    val x: Float,
    val y: Float,
    val size: Float,
    val color: Color,
    val delay: Float
)

// Generator functions
private fun generateConfettiPieces(): List<ConfettiPiece> {
    val colors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFF45B7D1),
        Color(0xFFFFA07A),
        Color(0xFF98D8C8),
        Color(0xFFFFD700)
    )
    
    return List(50) {
        ConfettiPiece(
            x = Random.nextFloat() * 1000f,
            y = Random.nextFloat() * -500f,
            vx = Random.nextFloat() * 4f - 2f,
            vy = Random.nextFloat() * 5f + 2f,
            color = colors.random(),
            rotation = Random.nextFloat() * 360f,
            rotationSpeed = Random.nextFloat() * 10f - 5f,
            size = Random.nextFloat() * 20f + 10f
        )
    }
}

private fun generateFireworks(): List<Firework> {
    val colors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFF45B7D1),
        Color(0xFFFFA07A),
        Color(0xFFFFD700)
    )
    
    return List(5) {
        Firework(
            centerX = Random.nextFloat() * 800f + 100f,
            centerY = Random.nextFloat() * 400f + 200f,
            color = colors.random(),
            particles = List(20) { i ->
                FireworkParticle(
                    angle = (i * 18f),
                    speed = Random.nextFloat() * 100f + 50f,
                    color = colors.random()
                )
            }
        )
    }
}

private fun generateStars(): List<Star> {
    return List(20) {
        Star(
            x = Random.nextFloat() * 1000f,
            y = Random.nextFloat() * 800f,
            size = Random.nextFloat() * 30f + 20f,
            color = Color(0xFFFFD700)
        )
    }
}

private fun generateBalloons(): List<Balloon> {
    val colors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFF45B7D1),
        Color(0xFFFFA07A),
        Color(0xFF98D8C8)
    )
    
    return List(15) {
        Balloon(
            x = Random.nextFloat() * 1000f,
            y = 1000f + Random.nextFloat() * 500f,
            vx = Random.nextFloat() * 2f - 1f,
            vy = Random.nextFloat() * 2f + 1f,
            color = colors.random(),
            size = Random.nextFloat() * 40f + 30f,
            stringLength = Random.nextFloat() * 50f + 100f
        )
    }
}

private fun generateSparkles(): List<Sparkle> {
    return List(30) {
        Sparkle(
            x = Random.nextFloat() * 1000f,
            y = Random.nextFloat() * 800f,
            size = Random.nextFloat() * 15f + 5f,
            color = Color(0xFFFFD700),
            delay = Random.nextFloat()
        )
    }
}

// Drawing functions
private fun DrawScope.drawConfettiPiece(piece: ConfettiPiece, progress: Float) {
    if (piece.y > size.height) return
    
    rotate(degrees = piece.rotation, pivot = Offset(piece.x, piece.y)) {
        drawRect(
            color = piece.color.copy(alpha = 1f - progress * 0.3f),
            topLeft = Offset(piece.x - piece.size / 2, piece.y),
            size = androidx.compose.ui.geometry.Size(piece.size, piece.size * 0.6f)
        )
    }
}

private fun DrawScope.drawFirework(firework: Firework, progress: Float) {
    val explosionRadius = progress * 150f
    val alpha = 1f - progress
    
    firework.particles.forEach { particle ->
        val x = firework.centerX + (cos(particle.angle * PI / 180f) * explosionRadius).toFloat()
        val y = firework.centerY + (sin(particle.angle * PI / 180f) * explosionRadius).toFloat()
        
        drawCircle(
            color = particle.color.copy(alpha = alpha),
            radius = 3f * (1f - progress * 0.5f),
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawStar(star: Star, progress: Float, rotation: Float) {
    val alpha = sin(progress * PI).toFloat()
    val scale = 0.5f + progress * 0.5f
    
    rotate(degrees = rotation, pivot = Offset(star.x, star.y)) {
        scale(scale = scale, pivot = Offset(star.x, star.y)) {
            drawStar(
                center = Offset(star.x, star.y),
                radius = star.size,
                color = star.color.copy(alpha = alpha)
            )
        }
    }
}

private fun DrawScope.drawStar(
    center: Offset,
    radius: Float,
    color: Color,
    points: Int = 5
) {
    val path = Path()
    val angle = 2 * PI / points
    val halfAngle = angle / 2
    val innerRadius = radius * 0.5f
    
    for (i in 0 until points) {
        val outerX = center.x + (cos(i * angle - PI / 2) * radius).toFloat()
        val outerY = center.y + (sin(i * angle - PI / 2) * radius).toFloat()
        val innerX = center.x + (cos(i * angle + halfAngle - PI / 2) * innerRadius).toFloat()
        val innerY = center.y + (sin(i * angle + halfAngle - PI / 2) * innerRadius).toFloat()
        
        if (i == 0) {
            path.moveTo(outerX, outerY)
        } else {
            path.lineTo(outerX, outerY)
        }
        path.lineTo(innerX, innerY)
    }
    path.close()
    
    drawPath(path, color)
}

private fun DrawScope.drawBalloon(balloon: Balloon, progress: Float) {
    if (balloon.y < -balloon.size) return
    
    val alpha = 1f - progress * 0.2f
    
    // Draw string
    drawLine(
        color = Color.Gray.copy(alpha = alpha * 0.5f),
        start = Offset(balloon.x, balloon.y + balloon.size / 2),
        end = Offset(balloon.x, balloon.y + balloon.size / 2 + balloon.stringLength),
        strokeWidth = 1f
    )
    
    // Draw balloon
    drawOval(
        color = balloon.color.copy(alpha = alpha),
        topLeft = Offset(balloon.x - balloon.size / 2, balloon.y - balloon.size / 2),
        size = androidx.compose.ui.geometry.Size(balloon.size, balloon.size * 1.2f)
    )
}

private fun DrawScope.drawSparkle(sparkle: Sparkle, progress: Float) {
    val adjustedProgress = (progress + sparkle.delay) % 1f
    val alpha = sin(adjustedProgress * PI).toFloat()
    val scale = 0.5f + adjustedProgress * 0.5f
    
    scale(scale = scale, pivot = Offset(sparkle.x, sparkle.y)) {
        // Draw a four-pointed sparkle
        drawLine(
            color = sparkle.color.copy(alpha = alpha),
            start = Offset(sparkle.x - sparkle.size, sparkle.y),
            end = Offset(sparkle.x + sparkle.size, sparkle.y),
            strokeWidth = 2f
        )
        drawLine(
            color = sparkle.color.copy(alpha = alpha),
            start = Offset(sparkle.x, sparkle.y - sparkle.size),
            end = Offset(sparkle.x, sparkle.y + sparkle.size),
            strokeWidth = 2f
        )
    }
}