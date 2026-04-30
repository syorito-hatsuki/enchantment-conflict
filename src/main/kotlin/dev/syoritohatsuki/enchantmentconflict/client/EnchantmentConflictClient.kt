package dev.syoritohatsuki.enchantmentconflict.client

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.logging.LogUtils
import dev.syoritohatsuki.enchantmentconflict.EnchantmentConflict
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger


object EnchantmentConflictClient : ClientModInitializer {
    val logger: Logger = LogUtils.getLogger()

    private val CATEGORY: KeyMapping.Category = KeyMapping.Category.register(
        Identifier.fromNamespaceAndPath(EnchantmentConflict.MOD_ID, EnchantmentConflict.MOD_ID)
    )

    var showEnchantmentsConflict: KeyMapping = KeyMappingHelper.registerKeyMapping(
        KeyMapping(
            "key.${EnchantmentConflict.MOD_ID}.show_conflicted_enchantments",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            CATEGORY
        )
    )

    override fun onInitializeClient() {
        logger.info("${javaClass.simpleName} initialized with mod-id ${EnchantmentConflict.MOD_ID}")
    }
}