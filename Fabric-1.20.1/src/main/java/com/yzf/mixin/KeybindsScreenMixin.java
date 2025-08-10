package com.yzf.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yzf.screen.LoadPresetScreen;
import com.yzf.screen.SavePresetScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeybindsScreen.class)
public abstract class KeybindsScreenMixin {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ControlsScreen;addDrawableChild(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 1))
    private void addCustomButton(CallbackInfo ci, @Local GridWidget widget) {
        widget.add(SavePresetScreen.getButton());
        widget.add(LoadPresetScreen.getButton());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton2(CallbackInfo ci, @Local DirectionalLayoutWidget widget) {
        widget.forEachElement((button) -> {
            var b = (ButtonWidget) button;
            b.setWidth(100);
        });
    }
}