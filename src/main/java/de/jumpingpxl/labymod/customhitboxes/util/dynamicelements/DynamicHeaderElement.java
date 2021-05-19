package de.jumpingpxl.labymod.customhitboxes.util.dynamicelements;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.labymod.main.LabyMod;

import java.util.Arrays;
import java.util.List;

public class DynamicHeaderElement extends DynamicSettingsElement {

	private final List<Line> lines;
	private final int lineHeight;

	private DynamicHeaderElement(int lineHeight, String... lines) {
		super(null, null);

		this.lineHeight = lineHeight;

		this.lines = Lists.newArrayList();
		for (String line : lines) {
			this.lines.add(new Line(line));
		}
	}

	private DynamicHeaderElement(String... lines) {
		this(22, lines);
	}

	public static DynamicHeaderElement create(int lineHeight, String... lines) {
		return new DynamicHeaderElement(lineHeight, lines);
	}

	public static DynamicHeaderElement create(String... lines) {
		return new DynamicHeaderElement(lines);
	}

	@Override
	public void draw(MatrixStack matrixStack, int preX, int preY, int preMaxX, int preMaxY,
	                 int mouseX, int mouseY) {
		super.draw(matrixStack, preX, preY, preMaxX, preMaxY, mouseX, mouseY);

		double absoluteY = y - lineHeight / (double) lines.size();
		for (Line line : lines) {
			List<String> subLines = line.getLines();
			LabyMod.getInstance().getDrawUtils().drawCenteredString(matrixStack, subLines.get(0),
					x + (maxX - x) / 2D, absoluteY, 1D);
			for (int i = 1; i < subLines.size(); i++) {
				absoluteY += 10;
				LabyMod.getInstance().getDrawUtils().drawCenteredString(matrixStack, subLines.get(1),
						x + (maxX - x) / 2D, absoluteY, 1D);
			}

			absoluteY += lineHeight;
		}
	}

	@Override
	public int getEntryHeight() {
		int height = 0;
		for (Line line : lines) {
			height += lineHeight + 10 * (line.getLines().size() - 1);
		}

		return height;
	}

	private static class Line {

		private final List<String> lines;

		public Line(String text) {
			lines = Arrays.asList(text.split("\n"));
		}

		public List<String> getLines() {
			return lines;
		}

		public int getEntryHeight() {
			return 10 * (lines.size() - 1);
		}
	}
}
