package com.example.wire.core.common.util



import kotlin.math.absoluteValue

object AvatarUtils {// Premium Fintech Palette
private val avatarColors = listOf(
        0xFF2ECC71, // Emerald Green
        0xFF3498DB, // Peter River Blue
        0xFF9B59B6, // Amethyst Purple
        0xFFF1C40F, // Sun Flower Yellow
        0xFFE67E22, // Carrot Orange
        0xFF1ABC9C, // Turquoise
        0xFFE74C3C, // Alizarin Red
        0xFF2980B9  // Belize Hole Blue
    )

    fun getColorForName(name: String): Int {
        if (name.isBlank()) return 0xFF95A5A6.toInt() // Default Gray
        val index = name.hashCode().absoluteValue % avatarColors.size
        return avatarColors[index].toInt()
    }
}