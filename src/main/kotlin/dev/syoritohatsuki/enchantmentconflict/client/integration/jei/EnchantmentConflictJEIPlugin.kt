package dev.syoritohatsuki.enchantmentconflict.client.integration.jei

import dev.syoritohatsuki.enchantmentconflict.EnchantmentConflict
import dev.syoritohatsuki.enchantmentconflict.item.enchantment.ItemEnchantmentsConflict.Companion.mapConflicts
import mezz.jei.api.IModPlugin
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.EnchantmentInstance
import kotlin.jvm.optionals.getOrNull

object EnchantmentConflictJEIPlugin : IModPlugin {

    var pluginIdentifier: Identifier = Identifier.fromNamespaceAndPath(EnchantmentConflict.MOD_ID, "jei_plugin")

    override fun getPluginUid(): Identifier = pluginIdentifier

    override fun registerRecipes(registration: IRecipeRegistration) {
        val level = Minecraft.getInstance().level ?: return
        val registry = level.registryAccess().lookup(Registries.ENCHANTMENT).getOrNull() ?: return

        registry.listElements().forEach { holder ->
            val conflicts = registry.listElements().mapConflicts(holder)

            if (conflicts.isEmpty()) return@forEach

            registration.addItemStackInfo((1..holder.value().maxLevel).map { level ->
                EnchantmentHelper.createBook(EnchantmentInstance(holder, level))
            }, enchantmentConflictDescriptionComponent(holder, conflicts))
        }
    }

    private fun enchantmentConflictDescriptionComponent(
        holder: Holder.Reference<Enchantment>, conflicts: List<Holder<Enchantment>>
    ): Component = Component.empty().apply {
        append(holder.value().description.copy().withStyle(ChatFormatting.BOLD))
        append("\n\n")
        append(
            Component.translatable("jei.description.enchantment-conflict.conflict_with")
                .withStyle(ChatFormatting.DARK_GRAY)
        )
        append("\n")

        conflicts.forEach {
            append(
                Component.literal("• ").append(it.value().description.copy()).withStyle(ChatFormatting.RED).append("\n")
            )
        }
    }
}