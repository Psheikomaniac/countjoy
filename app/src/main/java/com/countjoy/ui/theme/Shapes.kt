package com.countjoy.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Custom shape definitions for CountJoy app
 */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

/**
 * Additional custom shapes for specific components
 */
object CountJoyShapes {
    val CardShape = RoundedCornerShape(16.dp)
    val ButtonShape = RoundedCornerShape(12.dp)
    val TextFieldShape = RoundedCornerShape(12.dp)
    val DialogShape = RoundedCornerShape(24.dp)
    val ChipShape = RoundedCornerShape(8.dp)
    val FABShape = RoundedCornerShape(16.dp)
    val TopBarShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
}