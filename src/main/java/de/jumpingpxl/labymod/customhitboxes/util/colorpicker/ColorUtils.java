package de.jumpingpxl.labymod.customhitboxes.util.colorpicker;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ColorUtils {

	private static final ResourceLocation TRANSPARENT_BACKGROUND = new ResourceLocation(
			"customhitboxes/textures/transparent.png");
	private static final DrawUtils DRAW_UTILS = LabyMod.getInstance().getDrawUtils();

	public void drawColorPreview(MatrixStack matrixStack, int x, int y, int maxX, int maxY,
	                             int color) {
		DRAW_UTILS.drawRect(matrixStack, x, y, maxX, maxY, color);
		DRAW_UTILS.drawRectBorder(matrixStack, x, y, maxX, maxY, java.awt.Color.LIGHT_GRAY.getRGB(),
				1);
	}

	public void drawGradientRect(int left, int top, int right, int bottom, int coltl, int coltr,
	                             int colbl, int colbr) {
		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		RenderSystem.shadeModel(GL11.GL_SMOOTH);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(right, top, 0).color((coltr & 0x00ff0000) >> 16, (coltr & 0x0000ff00) >> 8,
				(coltr & 0x000000ff), (coltr & 0xff000000) >>> 24).endVertex();
		buffer.pos(left, top, 0).color((coltl & 0x00ff0000) >> 16, (coltl & 0x0000ff00) >> 8,
				(coltl & 0x000000ff), (coltl & 0xff000000) >>> 24).endVertex();
		buffer.pos(left, bottom, 0).color((colbl & 0x00ff0000) >> 16, (colbl & 0x0000ff00) >> 8,
				(colbl & 0x000000ff), (colbl & 0xff000000) >>> 24).endVertex();
		buffer.pos(right, bottom, 0).color((colbr & 0x00ff0000) >> 16, (colbr & 0x0000ff00) >> 8,
				(colbr & 0x000000ff), (colbr & 0xff000000) >>> 24).endVertex();
		tessellator.draw();

		RenderSystem.shadeModel(GL11.GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.disableTexture();
	}

	public void drawTransparentBackground(Tessellator tessellator, BufferBuilder buffer, int x,
	                                      int y,
	                                      double width, double height) {
		RenderSystem.enableTexture();
		Minecraft.getInstance().getTextureManager().bindTexture(TRANSPARENT_BACKGROUND);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + height, 0).tex(0, (float) height / 16).endVertex();
		buffer.pos(x + width, y + height, 0).tex((float) width / 16, (float) height / 16).endVertex();
		buffer.pos(x + width, y, 0).tex((float) width / 16, 0).endVertex();
		buffer.pos(x, y, 0).tex(0, 0).endVertex();
		tessellator.draw();

		RenderSystem.disableTexture();
		Minecraft.getInstance().getTextureManager().deleteTexture(TRANSPARENT_BACKGROUND);
	}

	public DrawUtils getDrawUtils() {
		return DRAW_UTILS;
	}
}
