package dev.syoritohatsuki.enchantmentconflict.core.component

import dev.syoritohatsuki.enchantmentconflict.EnchantmentConflict
import dev.syoritohatsuki.enchantmentconflict.item.enchantment.ItemEnchantmentsConflict
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import java.util.function.UnaryOperator


object EnchantmentConflictDataComponents {
    val CONFLICTED_ENCHANTMENTS: DataComponentType<ItemEnchantmentsConflict> = register("conflicted_enchantments") { builder ->
        builder.persistent(ItemEnchantmentsConflict.CODEC).cacheEncoding()
    }

    private fun <T : Any> register(id: String, builder: UnaryOperator<DataComponentType.Builder<T>>): DataComponentType<T> = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(EnchantmentConflict.MOD_ID, id),
        builder.apply(DataComponentType.builder()).build()
    )
}