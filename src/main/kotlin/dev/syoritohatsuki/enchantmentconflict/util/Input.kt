package dev.syoritohatsuki.enchantmentconflict.util

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping

object Input {
    fun KeyMapping.keyPressed(): Boolean = InputConstants.isKeyDown(this.key.value)
}