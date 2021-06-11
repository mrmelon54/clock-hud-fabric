package net.onpointcoding.clockhud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.onpointcoding.clockhud.client.ClockHudClient;
import net.onpointcoding.clockhud.config.ConfigStructure;
import net.onpointcoding.clockhud.enums.ClockPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    private ItemStack clockItemStack;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient client, CallbackInfo ci) {
        clockItemStack = new ItemStack(Items.CLOCK);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ConfigStructure config = ClockHudClient.getInstance().getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        if (!config.clockEnabled || client.options.debugEnabled) return;

        String clockText = client.world != null ? String.valueOf(client.world.getTimeOfDay()) : "";
        int textLength = client.textRenderer.getWidth(clockText);
        int textHeight = client.textRenderer.fontHeight;

        int clockSize = config.iconEnabled ? 16 : 0;
        int clockGap = config.iconEnabled ? 2 : 0;
        int offsetForIcon = clockSize + clockGap;
        int iconOffset = config.iconPosition == ClockPosition.RIGHT ? textLength + clockGap : 0;
        int clockY = switch (config.position.getVerticalPosition()) {
            case NEGATIVE -> 0;
            case NEUTRAL -> (textHeight - clockSize) / 2;
            case POSITIVE -> textHeight - clockSize;
        };

        int myX = switch (config.position.getHorizontalPosition()) {
            case NEGATIVE -> config.xOffset;
            case NEUTRAL -> this.scaledWidth / 2 - (textLength + offsetForIcon) / 2 + config.xOffset;
            case POSITIVE -> this.scaledWidth - (textLength + offsetForIcon) + config.xOffset;
        };
        int myY = switch (config.position.getVerticalPosition()) {
            case NEGATIVE -> config.yOffset;
            case NEUTRAL -> this.scaledHeight / 2 - textHeight / 2 + config.yOffset;
            case POSITIVE -> this.scaledHeight - textHeight + config.yOffset;
        };

        if (clockItemStack != null && config.iconEnabled)
            MinecraftClient.getInstance().getItemRenderer().renderInGui(clockItemStack, myX + iconOffset, myY + clockY);
        client.textRenderer.draw(matrices, new LiteralText(clockText), myX + (config.iconPosition == ClockPosition.LEFT ? offsetForIcon : 0), myY, config.colour);
    }
}
