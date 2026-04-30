package dev.syoritohatsuki.enchantmentconflict

import com.mojang.logging.LogUtils
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger

object EnchantmentConflict : ModInitializer {

    const val MOD_ID = "enchantment-conflict"
    val logger: Logger = LogUtils.getLogger()

    override fun onInitialize() {
        logger.info("${javaClass.simpleName} initialized with mod-id $MOD_ID")
    }
}