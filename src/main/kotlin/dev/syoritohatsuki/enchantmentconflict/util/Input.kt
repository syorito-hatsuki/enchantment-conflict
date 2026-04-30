package dev.syoritohatsuki.enchantmentconflict.util

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

object Input {
    fun KeyMapping.keyPressed(): Boolean = InputConstants.isKeyDown(Minecraft.getInstance().window, this.key.value)
}