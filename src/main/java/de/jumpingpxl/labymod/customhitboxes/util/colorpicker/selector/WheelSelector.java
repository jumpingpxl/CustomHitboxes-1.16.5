package de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.jumpingpxl.labymod.customhitboxes.util.Color;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.ColorUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class WheelSelector extends ColorSelector {

	private WheelSelector(ColorUtils colorUtils, Color color, int x, int y) {
		super(colorUtils, color, x, y);
	}

	public static WheelSelector create(ColorUtils colorUtils, Color color, int x, int y) {
		return new WheelSelector(colorUtils, color, x, y);
	}

	@Override
	public void initTextField(Screen screen) {

	}

	@Override
	public void updateTextField(Color previousColor) {

	}

	@Override
	public void draw(MatrixStack matrix, Tessellator tessellator, BufferBuilder buffer) {
		int rgb;
		for (float f = 0; f < 360; f += 0.25) {
			buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			float fRads = (float) Math.toRadians(f);
			rgb = getColor().hsbToRgb((int) f, 100, 100);
			buffer.pos(getX(), getY(), 0).color(1f, 1f, 1f, 1f).endVertex();
			buffer.pos(getX() + Math.cos(fRads) * 50, getY() + Math.sin(fRads) * 50, 0).color(
					(rgb & 0xff0000) >> 16, (rgb & 0x00ff00) >> 8, rgb & 0x0000ff, 255).endVertex();
			tessellator.draw();
		}
	}

	@Override
	public void drawMarker(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer) {
		int dist = getColor().getSaturation() / 2;
		int x = getX() + (int) (Math.cos(Math.toRadians(getColor().getHue())) * dist - 7) + 5;
		int y = getY() + (int) (Math.sin(Math.toRadians(getColor().getHue())) * dist) - 7 + 5;
		double height = 4;
		double width = 4;

		buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		buffer.pos(x, y + height, 0).endVertex();
		buffer.pos(x + width, y + height, 0).endVertex();
		buffer.pos(x + width, y, 0).endVertex();
		buffer.pos(x, y, 0).endVertex();
		tessellator.draw();
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY) {
		update(mouseX - getX(), mouseY - getY());
	}

	@Override
	public void mouseDragged(double mouseX, double mouseY) {
		update(mouseX - getX(), mouseY - getY());
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		double dx = mouseX - getX();
		double dy = mouseY - getY();
		return dx * dx + dy * dy <= 50 * 50;
	}

	public void update(double dx, double dy) {
		getColor().setHue((int) Math.toDegrees(Math.atan2(dy, dx)));
		if (getColor().getHue() < 0) {
			getColor().setHue(getColor().getHue() + 360);
		}

		int dist = (int) Math.sqrt(dx * dx + dy * dy);
		if (dist > 50) {
			dist = 50;
		}

		getColor().setSaturation(dist * 2);
		getColor().setRgb();
	}
}
