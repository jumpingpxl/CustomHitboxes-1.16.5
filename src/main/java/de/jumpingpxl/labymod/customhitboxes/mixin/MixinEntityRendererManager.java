package de.jumpingpxl.labymod.customhitboxes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.jumpingpxl.labymod.customhitboxes.CustomHitboxes;
import de.jumpingpxl.labymod.customhitboxes.util.Color;
import de.jumpingpxl.labymod.customhitboxes.util.EntityType;
import de.jumpingpxl.labymod.customhitboxes.util.Settings;
import net.labymod.api.permissions.Permissions;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(EntityRendererManager.class)
public class MixinEntityRendererManager {

	private static final Settings SETTINGS = CustomHitboxes.getSettings();

	@Redirect(method = "renderEntityStatic", at = @At(value = "FIELD",
			target = "Lnet/minecraft/client/renderer/entity/EntityRendererManager;debugBoundingBox:Z"))
	public boolean debugBoundingBox(EntityRendererManager rendererManager) {
		return rendererManager.isDebugBoundingBox() || SETTINGS.isEnabled();
	}

	@Redirect(method = "renderEntityStatic",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isReducedDebug()Z"))
	public boolean isReducedDebug(Minecraft minecraft) {
		return false;
	}

	@Redirect(method = "renderEntityStatic", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/EntityRendererManager;renderDebugBoundingBox"
					+ "(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;"
					+ "Lnet/minecraft/entity/Entity;F)V"))
	public void renderDebugBoundingBox(EntityRendererManager rendererManager,
	                                   MatrixStack matrixStack,
	                                   IVertexBuilder buffer, Entity entity, float partialTicks) {
		Color color = getColor(entity);
		if (Objects.isNull(color)) {
			if (rendererManager.isDebugBoundingBox()) {
				color = SETTINGS.getColor();
			} else {
				return;
			}
		}

		float f = entity.getWidth() / 2.0F;
		renderBoundingBox(matrixStack, buffer, entity, color.getRed() / 255F, color.getGreen() / 255F,
				color.getBlue() / 255F, color.getAlpha() / 255F);
		if (entity instanceof EnderDragonEntity) {
			double d0 = -MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.getPosX());
			double d1 = -MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.getPosY());
			double d2 = -MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.getPosZ());

			for (EnderDragonPartEntity enderdragonpartentity :
					((EnderDragonEntity) entity).getDragonParts()) {
				matrixStack.push();
				double d3 = d0 + MathHelper.lerp(partialTicks, enderdragonpartentity.lastTickPosX,
						enderdragonpartentity.getPosX());
				double d4 = d1 + MathHelper.lerp(partialTicks, enderdragonpartentity.lastTickPosY,
						enderdragonpartentity.getPosY());
				double d5 = d2 + MathHelper.lerp(partialTicks, enderdragonpartentity.lastTickPosZ,
						enderdragonpartentity.getPosZ());
				matrixStack.translate(d3, d4, d5);
				this.renderBoundingBox(matrixStack, buffer, enderdragonpartentity, color.getRed() / 255F,
						color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
				matrixStack.pop();
			}
		}

		if (entity instanceof LivingEntity && SETTINGS.isEyeHeightBoxEnabled()) {
			Color eyeHeightBoxColor = SETTINGS.getEyeHeightBoxColor();
			WorldRenderer.drawBoundingBox(matrixStack, buffer, -f, entity.getEyeHeight() - 0.01F, -f, f,
					entity.getEyeHeight() + 0.01F, f, eyeHeightBoxColor.getRed() / 255F,
					eyeHeightBoxColor.getGreen() / 255F, eyeHeightBoxColor.getBlue() / 255F,
					eyeHeightBoxColor.getAlpha() / 255F);
		}

		if (!(LabyMod.getSettings().playerAnimation && LabyMod.getSettings().oldHitbox
				&& Permissions.isAllowed(Permissions.Permission.ANIMATIONS))) {
			Vector3d vector3d = entity.getLook(partialTicks);
			Matrix4f matrix4f = matrixStack.getLast().getMatrix();
			buffer.pos(matrix4f, 0.0F, entity.getEyeHeight(), 0.0F).color(0, 0, 255, 255).endVertex();
			buffer.pos(matrix4f, (float) (vector3d.x * 2.0D),
					(float) ((double) entity.getEyeHeight() + vector3d.y * 2.0D),
					(float) (vector3d.z * 2.0D))
					.color(0, 0, 255, 255)
					.endVertex();
		}
	}

	private void renderBoundingBox(MatrixStack matrixStackIn, IVertexBuilder bufferIn,
	                               Entity entityIn, float red, float green, float blue,
	                               float alpha) {
		AxisAlignedBB axisAlignedBB = entityIn.getBoundingBox().offset(-entityIn.getPosX(),
				-entityIn.getPosY(), -entityIn.getPosZ());
		WorldRenderer.drawBoundingBox(matrixStackIn, bufferIn, axisAlignedBB, red, green, blue, alpha);
	}

	private Color getColor(Entity entity) {
		Color color = null;
		switch (EntityType.fromEntity(entity)) {
			case PLAYER:
				if (SETTINGS.isPlayersEnabled() && !(Minecraft.getInstance().player == entity
						&& !SETTINGS.isSelfEnabled())) {
					color = SETTINGS.isOwnColorPlayers() ? SETTINGS.getPlayerColor() : SETTINGS.getColor();
				}

				break;
			case ANIMAL:
				if (SETTINGS.isAnimalsEnabled()) {
					color = SETTINGS.isOwnColorAnimals() ? SETTINGS.getAnimalColor() : SETTINGS.getColor();
				}

				break;
			case MOB:
				if (SETTINGS.isMobsEnabled()) {
					color = SETTINGS.isOwnColorMobs() ? SETTINGS.getMobColor() : SETTINGS.getColor();
				}

				break;
			case DROP:
				if (SETTINGS.isDropsEnabled()) {
					color = SETTINGS.isOwnColorDrops() ? SETTINGS.getDropColor() : SETTINGS.getColor();
				}

				break;
			case THROWABLE:
				if (SETTINGS.isThrowablesEnabled()) {
					color =
							SETTINGS.isOwnColorThrowables() ? SETTINGS.getThrowableColor() : SETTINGS.getColor();
				}

				break;
			default:

				break;
		}

		return color;
	}
}