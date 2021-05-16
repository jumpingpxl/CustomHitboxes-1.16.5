package de.jumpingpxl.labymod.customhitboxes.util.colorpicker;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.jumpingpxl.labymod.customhitboxes.util.Color;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.AlphaSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.BlueSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.BrightnessSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.ColorSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.GreenSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.HexSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.HueSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.RedSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.SaturationSelector;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector.WheelSelector;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ColorPickerGui extends Screen {

	private static final ColorUtils COLOR_UTILS = new ColorUtils();
	private static final DrawUtils DRAW_UTILS = LabyMod.getInstance().getDrawUtils();
	private static final String TITLE_PREFIX = "Color Picker for ";

	private final List<ColorSelector> colorSelectorList;
	private final String colorPickerTitle;
	private final Consumer<Integer> callback;
	private final Screen backgroundScreen;
	private final Color color;
	private ColorSelector clickedSelector;

	private int x;
	private int y;
	private int maxX;
	private int maxY;
	private int centerX;

	private Button cancelButton;

	private ColorPickerGui(String colorPickerTitle, Color color, Screen backgroundScreen,
	                       Consumer<Integer> callback) {
		super(ITextComponent.getTextComponentOrEmpty(""));
		this.colorPickerTitle = TITLE_PREFIX + ModColor.removeColor(colorPickerTitle);
		this.color = color;
		this.callback = callback;
		this.backgroundScreen = backgroundScreen;

		colorSelectorList = Lists.newArrayList();
	}

	public static ColorPickerGui create(String title, Color color, Screen backgroundScreen,
	                                    Consumer<Integer> callback) {
		return new ColorPickerGui(title, color, backgroundScreen, callback);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);
		backgroundScreen.init(minecraft, width, height);

		centerX = this.width / 2;
		x = centerX - 175;
		y = this.height / 3 - 50;
		maxX = centerX + 175;
		maxY = this.height / 3 + 140;
		int centerY = this.height / 3 + 30;

		Button doneButton = new Button(centerX - 100, maxY - 25, 98, 20,
				new StringTextComponent("§aDone"), onPress -> {
			callback.accept(color.getColor());
			cancelButton.onPress();
		});

		cancelButton = new Button(centerX + 2, maxY - 25, 98, 20, new StringTextComponent("§cCancel"),
				onPress -> minecraft.displayGuiScreen(backgroundScreen));

		addButton(doneButton);
		addButton(cancelButton);

		colorSelectorList.clear();
		colorSelectorList.add(WheelSelector.create(COLOR_UTILS, color, centerX, y + 70));

		int leftX = centerX - 124;
		colorSelectorList.add(
				HexSelector.create(COLOR_UTILS, color, leftX - 44, centerY - 49, 0, 0, leftX - 44,
						colorSelectorList));
		colorSelectorList.add(
				HueSelector.create(COLOR_UTILS, color, leftX, centerY - 15, 64, 20, leftX - 45,
						colorSelectorList));
		colorSelectorList.add(
				SaturationSelector.create(COLOR_UTILS, color, leftX, centerY + 20, 64, 20, leftX - 45,
						colorSelectorList));
		colorSelectorList.add(
				BrightnessSelector.create(COLOR_UTILS, color, leftX, centerY + 55, 64, 20, leftX - 45,
						colorSelectorList));

		int rightX = centerX + 60;
		colorSelectorList.add(
				RedSelector.create(COLOR_UTILS, color, rightX, centerY - 49, 64, 20, rightX + 69,
						colorSelectorList));
		colorSelectorList.add(
				GreenSelector.create(COLOR_UTILS, color, rightX, centerY - 15, 64, 20, rightX + 69,
						colorSelectorList));
		colorSelectorList.add(
				BlueSelector.create(COLOR_UTILS, color, rightX, centerY + 20, 64, 20, rightX + 69,
						colorSelectorList));
		colorSelectorList.add(
				AlphaSelector.create(COLOR_UTILS, color, rightX, centerY + 55, 64, 20, rightX + 69,
						colorSelectorList));

		for (ColorSelector colorSelector : colorSelectorList) {
			TextFieldWidget textField = colorSelector.getTextField();
			if (Objects.nonNull(textField)) {
				colorSelector.initTextField(this);
				addListener(textField);
			}
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		backgroundScreen.render(matrixStack, 0, 0, partialTicks);
		RenderSystem.clear(256, Minecraft.IS_RUNNING_ON_MAC);

		fill(matrixStack, 0, 0, this.width, this.height, -2147483648);
		fill(matrixStack, x, y, maxX, maxY, -2147483648);
		DRAW_UTILS.drawCenteredString(matrixStack, colorPickerTitle, centerX, y + 5D);

		super.render(matrixStack, mouseX, mouseY, partialTicks);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		for (ColorSelector colorSelector : colorSelectorList) {
			colorSelector.renderTextField(tessellator, buffer, matrixStack, mouseX, mouseY,
					partialTicks);
		}

		RenderSystem.disableLighting();
		RenderSystem.disableFog();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		RenderSystem.disableTexture();
		RenderSystem.shadeModel(GL11.GL_SMOOTH);

		for (ColorSelector colorSelector : colorSelectorList) {
			colorSelector.draw(matrixStack, tessellator, buffer);
		}

		COLOR_UTILS.drawTransparentBackground(tessellator, buffer, centerX - 45, maxY - 60, 90, 25);
		COLOR_UTILS.drawColorPreview(matrixStack, centerX - 45, maxY - 60, centerX + 45, maxY - 35,
				color.getColor());

		RenderSystem.blendFuncSeparate(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
		RenderSystem.enableBlend();
		RenderSystem.blendColor(1, 1, 1, 1);
		RenderSystem.disableTexture();

		for (ColorSelector colorSelector : colorSelectorList) {
			colorSelector.drawMarker(matrixStack, tessellator, buffer);
		}

		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) {
			cancelButton.onPress();
			return false;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		Color previousColor = color.createCopy();
		if (button == 0) {
			for (ColorSelector colorSelector : colorSelectorList) {
				if (colorSelector.isMouseOver(mouseX, mouseY)) {
					clickedSelector = colorSelector;
					colorSelector.mouseClicked(mouseX, mouseY);
					break;
				}
			}
		}

		if (Objects.nonNull(clickedSelector)) {
			for (ColorSelector colorSelector : colorSelectorList) {
				colorSelector.updateTextField(previousColor);
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0 && Objects.nonNull(clickedSelector)) {
			clickedSelector = null;
		}

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
	                            double dragY) {
		Color previousColor = color.createCopy();
		if (Objects.nonNull(clickedSelector)) {
			clickedSelector.mouseDragged(mouseX, mouseY);

			for (ColorSelector colorSelector : colorSelectorList) {
				colorSelector.updateTextField(previousColor);
			}
		}

		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public void tick() {
		for (ColorSelector colorSelector : colorSelectorList) {
			colorSelector.tick();
		}
	}
}
