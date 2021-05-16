package de.jumpingpxl.labymod.customhitboxes.util;

public class Color {

	private int rgb;
	private int alpha;
	private int hue;
	private int saturation;
	private int brightness;

	private Color(int color) {
		setColor(color);
	}

	private Color(int rgb, int alpha, int hue, int saturation, int brightness) {
		this.rgb = rgb;
		this.alpha = alpha;
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}

	public static Color fromRgb(int color) {
		return new Color(color);
	}

	public static Color fromRgba(int red, int green, int blue, int alpha) {
		return new Color((alpha & 255) << 24 | (red & 255) << 16 | (green & 255) << 8 | (blue & 255));
	}

	public int getColor() {
		return (alpha << 24) | rgb;
	}

	public void setColor(int color) {
		rgb = color & 0x00ffffff;
		alpha = (color & 0xff000000) >>> 24;

		int[] hsv = rgbToHsb(rgb);
		hue = hsv[0] == -1 ? 0 : hsv[0];
		saturation = hsv[1];
		brightness = hsv[2];
	}

	public int getRgb() {
		return rgb;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
		setHsb();
	}

	public int getRed() {
		return (rgb & 0xff0000) >> 16;
	}

	public int getGreen() {
		return (rgb & 0x00ff00) >> 8;
	}

	public int getBlue() {
		return rgb & 0x0000ff;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getHue() {
		return hue;
	}

	public void setHue(int hue) {
		this.hue = hue;
	}

	public int getSaturation() {
		return saturation;
	}

	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public void setRgb() {
		rgb = hsbToRgb(hue, saturation, brightness);
	}

	private void setHsb() {
		int[] hsb = rgbToHsb(rgb);
		if (hsb[0] != -1) {
			hue = hsb[0];
		}

		saturation = hsb[1];
		brightness = hsb[2];
	}

	public Color createCopy() {
		return new Color(rgb, alpha, hue, saturation, brightness);
	}

	public int hsbToRgb(int hue, int saturation, int brightness) {
		hue %= 360;
		float s = (float) saturation / 100;
		float v = (float) brightness / 100;
		float c = v * s;
		float h = (float) hue / 60;
		float x = c * (1 - Math.abs(h % 2 - 1));
		float r;
		float g;
		float b;
		switch (hue / 60) {
			case 0:
				r = c;
				g = x;
				b = 0;
				break;
			case 1:
				r = x;
				g = c;
				b = 0;
				break;
			case 2:
				r = 0;
				g = c;
				b = x;
				break;
			case 3:
				r = 0;
				g = x;
				b = c;
				break;
			case 4:
				r = x;
				g = 0;
				b = c;
				break;
			case 5:
				r = c;
				g = 0;
				b = x;
				break;
			default:
				return 0;
		}

		float m = v - c;
		return ((int) ((r + m) * 255) << 16) | ((int) ((g + m) * 255) << 8) | ((int) ((b + m) * 255));
	}

	private int[] rgbToHsb(int rgb) {
		float r = (float) ((rgb & 0xff0000) >> 16) / 255;
		float g = (float) ((rgb & 0x00ff00) >> 8) / 255;
		float b = (float) (rgb & 0x0000ff) / 255;
		float M = r > g ? (Math.max(r, b)) : (Math.max(g, b));
		float m = r < g ? (Math.min(r, b)) : (Math.min(g, b));
		float c = M - m;
		float h;
		if (M == r) {
			h = ((g - b) / c);
			while (h < 0) {
				h += 6;
			}

			h %= 6;
		} else if (M == g) {
			h = ((b - r) / c) + 2;
		} else {
			h = ((r - g) / c) + 4;
		}

		h *= 60;
		float s = c / M;
		return new int[]{c == 0 ? -1 : (int) h, (int) (s * 100), (int) (M * 100)};
	}
}
