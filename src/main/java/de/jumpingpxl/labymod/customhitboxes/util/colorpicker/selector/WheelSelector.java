package de.jumpingpxl.labymod.customhitboxes.util.colorpicker.selector;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.jumpingpxl.labymod.customhitboxes.util.Color;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WheelSelector extends ColorSelector {

	private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");

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
		double x = getX() + (Math.cos(Math.toRadians(getColor().getHue())) * dist - 8);
		double y = getY() + (Math.sin(Math.toRadians(getColor().getHue())) * dist - 8);
		drawCrossHair(tessellator, buffer, x, y);
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

	private void update(double dx, double dy) {
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

	private void drawCrossHair(Tessellator tessellator, BufferBuilder buffer, double x, double y) {
		RenderSystem.enableTexture();
		Minecraft.getInstance().getTextureManager().bindTexture(ICONS);
		float offset = 0.00390625F;
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + 16D, 0D).tex(0F, 14 * offset).endVertex();
		buffer.pos(x + 16D, y + 16D, 0D).tex(14 * offset, 14 * offset).endVertex();
		buffer.pos(x + 16D, y, 0D).tex(14 * offset, 0F).endVertex();
		buffer.pos(x, y, 0D).tex(0F, 0F).endVertex();
		tessellator.draw();
		Minecraft.getInstance().getTextureManager().deleteTexture(ICONS);
	}
}
