package de.jumpingpxl.labymod.customhitboxes.util.dynamicelements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Consumer;

public class DynamicBooleanElement extends DynamicSettingsElement {

	private static final DrawUtils DRAW_UTILS = LabyMod.getInstance().getDrawUtils();
	private static final String ENABLED = "ON";
	private static final String DISABLED = "OFF";

	private final Button toggleButton;
	private final Consumer<Boolean> toggleListener;
	private boolean currentValue;

	private DynamicBooleanElement(String displayName, Material icon, boolean currentValue,
	                              Consumer<Boolean> toggleListener) {
		super(displayName, icon);

		this.currentValue = currentValue;
		this.toggleListener = toggleListener;

		toggleButton = new Button(0, 0, getObjectWidth(), getEntryHeight() - 2,
				ITextComponent.getTextComponentOrEmpty(""), onPress -> {
			this.currentValue = !this.currentValue;
			this.toggleListener.accept(this.currentValue);
		});
	}

	public static DynamicBooleanElement create(String displayName, Material icon,
	                                           boolean currentValue,
	                                           Consumer<Boolean> toggleListener) {
		return new DynamicBooleanElement(displayName, icon, currentValue, toggleListener);
	}

	@Override
	public void draw(MatrixStack matrixStack, int prevX, int prevY, int prevMaxX, int prevMaxY,
	                 int mouseX, int mouseY) {
		super.draw(matrixStack, prevX, prevY, prevMaxX, prevMaxY, mouseX, mouseY);

		int buttonWidth = toggleButton.getWidth();

		toggleButton.active = false;
		toggleButton.x = maxX - buttonWidth - 1;
		toggleButton.y = y + 1;
		toggleButton.renderButton(matrixStack, x, y, 0F);
		toggleButton.active = true;

		String color = (this.currentValue ? ModColor.WHITE.toString() : ModColor.GRAY.toString());
		String displayString = (toggleButton.isMouseOver(mouseX, mouseY) ? ModColor.YELLOW.toString()
				: color) + (this.currentValue ? ENABLED : DISABLED);
		DRAW_UTILS.drawCenteredString(matrixStack, displayString,
				toggleButton.x + (buttonWidth - 4) / 2D + (currentValue ? 0 : 6),
				y + toggleButton.getHeightRealms() / 2D - 3);

		this.mc.getTextureManager().bindTexture(Widget.WIDGETS_LOCATION);
		int valuePosX = (this.currentValue ? maxX - 8 : maxX - buttonWidth) - 1;
		int red = (currentValue ? 85 : 255);
		int green = (currentValue ? 255 : 85);
		int blue = 85;
		RenderSystem.color4f(red / 255F, green / 255F, blue / 255F, 1F);
		DRAW_UTILS.drawTexturedModalRect(matrixStack, valuePosX, y + 1D, 0D, 66D, 4D, 20D);
		DRAW_UTILS.drawTexturedModalRect(matrixStack, valuePosX + 4D, y + 1D, 196.0D, 66.0D, 4.0D,
				20.0D);
		DRAW_UTILS.drawRectangle(matrixStack, x - 1, y, x, maxY,
				currentValue ? ModColor.toRGB(20, 120, 20, 120) : ModColor.toRGB(120, 20, 20, 120));
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (toggleButton.isMouseOver(mouseX, mouseY)) {
			toggleButton.onPress();
			toggleButton.playDownSound(Minecraft.getInstance().getSoundHandler());
		}
	}
}
