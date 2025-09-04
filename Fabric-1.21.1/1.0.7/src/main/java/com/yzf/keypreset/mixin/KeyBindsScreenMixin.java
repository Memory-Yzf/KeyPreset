package com.yzf.keypreset.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yzf.keypreset.client.gui.screen.PresetScreen;
import com.yzf.keypreset.util.Constant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeybindsScreen.class)
public abstract class KeyBindsScreenMixin {
	@Shadow
	private ButtonWidget resetAllButton;

	@Inject(method = "initFooter", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"))
	private void addKeyPresetButton(CallbackInfo ci, @Local DirectionalLayoutWidget layout) {
		resetAllButton.setWidth(100);
		layout.add(ButtonWidget.builder(
				Constant.PRESET_TITLE,
				button -> MinecraftClient.getInstance().setScreen(new PresetScreen(MinecraftClient.getInstance().currentScreen))).width(100).build()
		);
	}

	@Redirect(method = "initFooter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;"))
	private ButtonWidget redirectDoneButtonBuild(ButtonWidget.Builder button) {
		return button.width(100).build();
	}
}