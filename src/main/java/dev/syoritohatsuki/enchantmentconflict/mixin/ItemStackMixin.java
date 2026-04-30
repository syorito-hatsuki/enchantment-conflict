package dev.syoritohatsuki.enchantmentconflict.mixin;

import dev.syoritohatsuki.enchantmentconflict.core.component.EnchantmentConflictDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag);

    @Inject(
            method = "addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
                    ordinal = 16
            )
    )
    public void addEnchantmentConflictDetailsToTooltip(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        addToTooltip(EnchantmentConflictDataComponents.INSTANCE.getCONFLICTED_ENCHANTMENTS(), context, display, builder, tooltipFlag);
    }
}
