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

public class AlphaSelector extends ColorSelector {

	private AlphaSelector(ColorUtils colorUtils, Color color, int x, int y, int width, int height,
	                      int textFieldX, List<ColorSelector> colorSelectorList) {
		super(colorUtils, color, x, y, width, height, textFieldX, colorSelectorList);
	}

	public static AlphaSelector create(ColorUtils colorUtils, Color color, int x, int y, int width,
	                                   int height, int textFieldX,
	                                   List<ColorSelector> colorSelectorList) {
		return new AlphaSelector(colorUtils, color, x, y, width, height, textFieldX,
				colorSelectorList);
	}

	@Override
	public void initTextField(Screen screen) {
		getTextField().setText(String.valueOf(getColor().getAlpha()));
		getTextField().setValidator(new NumberPredicate(255));
		getTextField().setResponder(getTextFieldResponse(newValue -> {
			if (getColor().getAlpha() != newValue) {
				getColor().setAlpha(newValue);
			}
		}));
	}

	@Override
	public void updateTextField(Color previousColor) {
		if (getColor().getAlpha() != previousColor.getAlpha()) {
			getTextField().setText(String.valueOf(getColor().getAlpha()));
		}
	}

	@Override
	public void draw(MatrixStack matrix, Tessellator tessellator, BufferBuilder buffer) {
		renderText(matrix, "Alpha");

		getColorUtils().drawTransparentBackground(tessellator, buffer, getX(), getY(), getWidth(),
				getHeight());

		int rgb = getColor().getRgb();
		getColorUtils().drawGradientRect(getX(), getY(), getMaxX(), getMaxY(), rgb, rgb | 0xff000000,
				rgb, rgb | 0xff000000);
	}

	@Override
	public void drawMarker(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer) {
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		buffer.pos(getX() + getColor().getAlpha() * 64D / 255, getY(), 0).endVertex();
		buffer.pos(getX() + getColor().getAlpha() * 64D / 255, getMaxY(), 0).endVertex();
		tessellator.draw();
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY) {
		update((int) (mouseX - getX()));
	}

	@Override
	public void mouseDragged(double mouseX, double mouseY) {
		update((int) (mouseX - getX()));
	}

	private void update(int dx) {
		dx = MathHelper.clamp(dx, 0, 64);
		getColor().setAlpha(dx * 255 / 64);
	}
}