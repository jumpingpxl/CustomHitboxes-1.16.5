package de.jumpingpxl.labymod.customhitboxes.util.dynamicelements;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Objects;

public class DynamicSettingsElement extends SettingsElement {

	private static final DrawUtils DRAW_UTILS = LabyMod.getInstance().getDrawUtils();
	private static final int OBJECT_WIDTH = 50;

	private final ItemStack iconItemStack;
	private final int entryHeight;
	private final SubSettingsButton subSettingsButton;
	private final List<SettingsElement> subSettingList;
	protected int x;
	protected int y;
	protected int maxX;
	protected int maxY;
	private boolean subSettingsExpanded;
	private boolean subSetting;

	protected DynamicSettingsElement(String displayName, Material iconMaterial, int entryHeight) {
		super(displayName, null);

		this.entryHeight = entryHeight;

		this.iconItemStack = Objects.isNull(iconMaterial) ? null : iconMaterial.createItemStack();
		subSettingList = Lists.newArrayList();

		subSettingsButton = new SubSettingsButton(this, 0, 0,
				onPress -> subSettingsExpanded = !subSettingsExpanded);
		subSetting = false;
	}

	protected DynamicSettingsElement(String displayName, Material icon) {
		this(displayName, icon, 22);
	}

	public static DynamicSettingsElement create(String displayName, Material iconMaterial,
	                                            int entryHeight) {
		return new DynamicSettingsElement(displayName, iconMaterial, entryHeight);
	}

	public static DynamicSettingsElement create(String displayName, Material icon) {
		return new DynamicSettingsElement(displayName, icon);
	}

	@Override
	public void draw(MatrixStack matrixStack, int preX, int preY, int preMaxX, int preMaxY,
	                 int mouseX, int mouseY) {
		x = preX;
		y = preY;
		maxX = preMaxX;
		maxY = preMaxY;

		if (!isSubSetting()) {
			x -= 20;
			maxX += 20;
		}

		super.draw(matrixStack, x, y, maxX, maxY, mouseX, mouseY);

		if (isSubSetting() || subSettingsExpanded) {
			DRAW_UTILS.drawRectangle(matrixStack, x - 1, y - 1, maxX + 1,
					y + (subSettingsExpanded ? entryHeight : getEntryHeight()) + 1,
					ModColor.toRGB(60, 60, 60, 60));
		}

		if (Objects.nonNull(displayName)) {
			DRAW_UTILS.drawRectangle(matrixStack, x, y, maxX, y + entryHeight,
					ModColor.toRGB(80, 80, 80, isSubSetting() || subSettingsExpanded ? 80 : 60));
			DRAW_UTILS.drawString(matrixStack, getDisplayName(), x + 24D, y + entryHeight / 2D - 3.5D);
		}

		if (Objects.nonNull(iconItemStack)) {
			DRAW_UTILS.drawItem(iconItemStack, x + 4D, y + 3D, null);
		}

		if (!subSettingList.isEmpty()) {
			subSettingsButton.x = maxX - subSettingsButton.getWidth() - 1;
			subSettingsButton.y = y + 1;
			subSettingsButton.render(matrixStack, mouseX, mouseY, 0F);
		}

		if (subSettingsExpanded) {
			drawSubSettings(matrixStack, mouseX, mouseY);
		}
	}

	@Override
	public void drawDescription(int x, int y, int screenWidth) {
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!subSettingList.isEmpty()) {
			if (subSettingsButton.isMouseOver(mouseX, mouseY)) {
				subSettingsButton.onPress();
			}

			for (SettingsElement settingsElement : subSettingList) {
				if (settingsElement.isMouseOver()) {
					settingsElement.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	@Override
	public void mouseRelease(int mouseX, int mouseY, int mouseButton) {
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int mouseButton) {
	}

	@Override
	public void charTyped(char codePoint, int modifiers) {
	}

	@Override
	public void unfocus(int mouseX, int mouseY, int mouseButton) {
	}

	@Override
	public int getEntryHeight() {
		int height = entryHeight;
		if (subSettingsExpanded) {
			height += subSettingList.size() * 2 + 8;
			for (SettingsElement settingsElement : subSettingList) {
				height += settingsElement.getEntryHeight();
			}
		}

		return height;
	}

	private void drawSubSettings(MatrixStack matrixStack, int mouseX, int mouseY) {
		int markerX = x + 13;
		int markerY = y + entryHeight + subSettingList.get(0).getEntryHeight() / 2 + 1;
		int entryY = y + entryHeight + 2;
		for (SettingsElement settingsElement : subSettingList) {
			if (Objects.nonNull(settingsElement.getDisplayName())) {
				DRAW_UTILS.drawRectangle(matrixStack, markerX, markerY, markerX + 11, markerY + 2,
						ModColor.toRGB(60, 60, 60, 120));
			}

			markerY += settingsElement.getEntryHeight() + 2;
			settingsElement.draw(matrixStack, x + 24, entryY, maxX,
					entryY + settingsElement.getEntryHeight(), mouseX, mouseY);
			entryY += settingsElement.getEntryHeight() + 2;
		}

		DRAW_UTILS.drawRectangle(matrixStack, x + 11, y + entryHeight, x + 13,
				markerY - subSettingList.get(subSettingList.size() - 1).getEntryHeight(),
				ModColor.toRGB(60, 60, 60, 120));
	}

	public DynamicSettingsElement addSubSettings(SettingsElement... settingsElements) {
		for (SettingsElement settingsElement : settingsElements) {
			if (settingsElement instanceof DynamicSettingsElement) {
				((DynamicSettingsElement) settingsElement).subSetting = true;
			}

			subSettingList.add(settingsElement);
		}

		return this;
	}

	public int getObjectWidth() {
		return OBJECT_WIDTH;
	}

	public boolean isSubSetting() {
		return subSetting;
	}

	private static class SubSettingsButton extends Button {

		private final DynamicSettingsElement settingsElement;

		public SubSettingsButton(DynamicSettingsElement settingsElement, int x, int y,
		                         IPressable pressedAction) {
			super(x, y, 20, 20, ITextComponent.getTextComponentOrEmpty(""), pressedAction);

			this.settingsElement = settingsElement;
		}

		@Override
		public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;

			Minecraft minecraft = Minecraft.getInstance();
			FontRenderer fontrenderer = minecraft.fontRenderer;
			minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			int i = this.getYImage(this.isHovered());
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			this.blit(matrixStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
			this.blit(matrixStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20,
					this.width / 2, this.height);
			this.renderBg(matrixStack, minecraft, mouseX, mouseY);
			int j = this.active ? 16777215 : 10526880;
			drawCenteredString(matrixStack, fontrenderer,
					ITextComponent.getTextComponentOrEmpty(settingsElement.subSettingsExpanded ? "V" : ">"),
					this.x + this.width / 2, this.y + (this.height - 8) / 2,
					j | MathHelper.ceil(this.alpha * 255.0F) << 24);
			if (this.isHovered()) {
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}
}
