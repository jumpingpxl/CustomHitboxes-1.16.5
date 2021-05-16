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

public class BlueSelector extends ColorSelector {

	private BlueSelector(ColorUtils colorUtils, Color color, int x, int y, int width, int height,
	                     int textFieldX, List<ColorSelector> colorSelectorList) {
		super(colorUtils, color, x, y, width, height, textFieldX, colorSelectorList);
	}

	public static BlueSelector create(ColorUtils colorUtils, Color color, int x, int y, int width,
	                                  int height, int textFieldX,
	                                  List<ColorSelector> colorSelectorList) {
		return new BlueSelector(colorUtils, color, x, y, width, height, textFieldX, colorSelectorList);
	}

	@Override
	public void initTextField(Screen screen) {
		getTextField().setText(String.valueOf(getColor().getBlue()));
		getTextField().setValidator(new NumberPredicate(255));
		getTextField().setResponder(getTextFieldResponse(newValue -> {
			int rgb = getColor().getRgb();
			rgb &= 0xffff00;
			rgb |= newValue;
			if (getColor().getRgb() != rgb) {
				getColor().setRgb(rgb);
			}
		}));
	}

	@Override
	public void updateTextField(Color previousColor) {
		if (getColor().getBlue() != previousColor.getBlue()) {
			getTextField().setText(String.valueOf(getColor().getBlue()));
		}
	}

	@Override
	public void draw(MatrixStack matrix, Tessellator tessellator, BufferBuilder buffer) {
		renderText(matrix, "Blue");

		int rgb = getColor().getRgb() | 0xff000000;
		getColorUtils().drawGradientRect(getX(), getY(), getMaxX(), getMaxY(), rgb & 0xffffff00,
				rgb | 0x000000ff, rgb & 0xffffff00, rgb | 0x000000ff);
	}

	@Override
	public void drawMarker(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer) {
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		buffer.pos(getX() + (getColor().getRgb() & 0x0000ff) * 64D / 255, getY(), 0).endVertex();
		buffer.pos(getX() + (getColor().getRgb() & 0x0000ff) * 64D / 255, getMaxY(), 0).endVertex();
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
		int rgb = getColor().getRgb();
		rgb &= 0xffff00;
		rgb |= dx * 255 / 64;
		getColor().setRgb(rgb);
	}
}