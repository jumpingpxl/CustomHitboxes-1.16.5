package de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.jumpingpxl.labymod.customhitboxes.util.Color;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.ColorUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class HueSelector extends ColorSelector {

	private HueSelector(ColorUtils colorUtils, Color color, int x, int y, int width, int height,
	                    int textFieldX, List<ColorSelector> colorSelectorList) {
		super(colorUtils, color, x, y, width, height, textFieldX, colorSelectorList);
	}

	public static HueSelector create(ColorUtils colorUtils, Color color, int x, int y, int width,
	                                 int height, int textFieldX,
	                                 List<ColorSelector> colorSelectorList) {
		return new HueSelector(colorUtils, color, x, y, width, height, textFieldX, colorSelectorList);
	}

	@Override
	public void initTextField(Screen screen) {
		getTextField().setText(String.valueOf(getColor().getHue()));
		getTextField().setValidator(new NumberPredicate(359));
		getTextField().setResponder(getTextFieldResponse(newValue -> {
			if (getColor().getHue() != newValue) {
				getColor().setHue(newValue);
				getColor().setRgb();
			}
		}));
	}

	@Override
	public void updateTextField(Color previousColor) {
		if (getColor().getHue() != previousColor.getHue()) {
			getTextField().setText(String.valueOf(getColor().getHue()));
		}
	}

	@Override
	public void draw(MatrixStack matrix, Tessellator tessellator, BufferBuilder buffer) {
		renderText(matrix, "Hue");

		int rgbLowHue;
		int rgbHighHue;
		for (int i = 0; i < 64; i += 8) {
			rgbLowHue = getColor().hsbToRgb(i * 360 / 64, 100, 100) | 0xff000000;
			rgbHighHue = getColor().hsbToRgb((i + 8) * 360 / 64, 100, 100) | 0xff000000;
			getColorUtils().drawGradientRect(getX() + i, getY(), getX() + i + 8, getY() + 20, rgbLowHue,
					rgbHighHue, rgbLowHue, rgbHighHue);
		}
	}

	@Override
	public void drawMarker(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer) {
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		buffer.pos(getX() + getColor().getHue() * 64 / 360, getY(), 0).endVertex();
		buffer.pos(getX() + getColor().getHue() * 64 / 360, getMaxY(), 0).endVertex();
		tessellator.draw();
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY) {
		update(mouseX - getX());
	}

	@Override
	public void mouseDragged(double mouseX, double mouseY) {
		update(mouseX - getX());
	}

	private void update(double dx) {
		getColor().setHue((int) MathHelper.clamp(dx * 360 / 64, 0, 360));
		getColor().setRgb();
	}
}
