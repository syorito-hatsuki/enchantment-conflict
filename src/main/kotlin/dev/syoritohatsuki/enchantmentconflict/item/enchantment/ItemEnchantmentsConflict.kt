package dev.syoritohatsuki.enchantmentconflict.item.enchantment

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import dev.syoritohatsuki.enchantmentconflict.client.EnchantmentConflictClient
import dev.syoritohatsuki.enchantmentconflict.util.Input.keyPressed
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import java.util.function.Consumer
import java.util.stream.Stream

class ItemEnchantmentsConflict : TooltipProvider {

    companion object {
        val EMPTY: ItemEnchantmentsConflict = ItemEnchantmentsConflict()

        val CODEC: Codec<ItemEnchantmentsConflict> = object : Codec<ItemEnchantmentsConflict> {
            override fun <T> encode(input: ItemEnchantmentsConflict, ops: DynamicOps<T>, prefix: T): DataResult<T> =
                DataResult.success(prefix)

            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<ItemEnchantmentsConflict, T>> =
                DataResult.success(Pair.of(EMPTY, input))
        }

        fun Stream<Holder.Reference<Enchantment>>.mapConflicts(holder: Holder<Enchantment>) = filter {
            it != holder && (holder.value().exclusiveSet.contains(it) || it.value().exclusiveSet.contains(holder))
        }.toList() ?: emptyList()

        private fun Consumer<Component>.applyEnchantmentConflictDescriptionComponent(
            index: Int, current: Holder<Enchantment>, conflicts: List<Holder<Enchantment>>
        ): Component = Component.empty().apply {
            if (!EnchantmentConflictClient.showEnchantmentsConflict.keyPressed()) {
                if (index == 0) {
                    accept(Component.empty())
                    accept(
                        Component.translatable(
                            "key.description.enchantment-conflict.show_conflicted_enchantments",
                            EnchantmentConflictClient.showEnchantmentsConflict.translatedKeyMessage.copy()
                                .withStyle(ChatFormatting.GOLD)
                        ).withStyle(ChatFormatting.DARK_GRAY)
                    )
                }
                return@apply
            }

            accept(Component.empty())
            accept(
                Component.translatable(
                    "key.description.enchantment-conflict.conflict_with", current.value().description.copy()
                ).withStyle(ChatFormatting.DARK_GRAY)
            )

            conflicts.forEach { conflict ->
                accept(
                    Component.literal("• ").append(conflict.value().description.copy()).withStyle(ChatFormatting.RED)
                )
            }
        }
    }

    override fun addToTooltip(
        context: Item.TooltipContext, consumer: Consumer<Component>, flag: TooltipFlag, components: DataComponentGetter
    ) {
        val storedEnchantments = components.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)

        val enchantmentEntries = storedEnchantments.entrySet()
        if (enchantmentEntries.isEmpty()) return

        val registry = context.registries()?.lookupOrThrow(Registries.ENCHANTMENT) ?: return

        enchantmentEntries.forEachIndexed { index, entry ->
            val current = entry.key

            val conflicts = registry.listElements().mapConflicts(current)

            if (conflicts.isEmpty()) return@forEachIndexed

            consumer.applyEnchantmentConflictDescriptionComponent(index, current, conflicts)
        }
    }
}