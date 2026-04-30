package dev.syoritohatsuki.enchantmentconflict.mixin.world.item;

import dev.syoritohatsuki.enchantmentconflict.core.component.EnchantmentConflictDataComponents;
import dev.syoritohatsuki.enchantmentconflict.item.enchantment.ItemEnchantmentsConflict;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemsMixin {
    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=enchanted_book"
                    )),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0
            ),
            index = 1
    )
    private static Item.Properties modifyEnchantmentBookSettings(Item.Properties settings) {
        return settings.component(EnchantmentConflictDataComponents.INSTANCE.getCONFLICTED_ENCHANTMENTS(), ItemEnchantmentsConflict.Companion.getEMPTY());
    }
}
