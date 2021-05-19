package de.jumpingpxl.labymod.customhitboxes.util;

import com.google.gson.JsonObject;
import de.jumpingpxl.labymod.customhitboxes.CustomHitboxes;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.ColorPickerElement;
import de.jumpingpxl.labymod.customhitboxes.util.dynamicelements.DynamicBooleanElement;
import de.jumpingpxl.labymod.customhitboxes.util.dynamicelements.DynamicHeaderElement;
import de.jumpingpxl.labymod.customhitboxes.util.dynamicelements.DynamicSettingsElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.List;

public class Settings {

	private final CustomHitboxes customHitboxes;

	private boolean enabled;
	private boolean eyeHeightBoxEnabled;
	private boolean selfEnabled;
	private boolean playersEnabled;
	private boolean animalsEnabled;
	private boolean mobsEnabled;
	private boolean dropsEnabled;
	private boolean throwablesEnabled;

	private boolean ownColorPlayers;
	private boolean ownColorAnimals;
	private boolean ownColorMobs;
	private boolean ownColorDrops;
	private boolean ownColorThrowables;

	private Color color;
	private Color eyeHeightBoxColor;
	private Color playerColor;
	private Color animalColor;
	private Color mobColor;
	private Color dropColor;
	private Color throwableColor;

	public Settings(CustomHitboxes customHitboxes) {
		this.customHitboxes = customHitboxes;
	}

	public void loadConfig() {
		enabled = !getConfig().has("enabled") || getConfig().get("enabled").getAsBoolean();
		eyeHeightBoxEnabled = getConfig().has("eyeHeightBoxEnabled") && getConfig().get(
				"eyeHeightBoxEnabled").getAsBoolean();
		selfEnabled = !getConfig().has("selfEnabled") || getConfig().get("selfEnabled").getAsBoolean();
		playersEnabled = !getConfig().has("playersEnabled") || getConfig().get("playersEnabled")
				.getAsBoolean();
		animalsEnabled = !getConfig().has("animalsEnabled") || getConfig().get("animalsEnabled")
				.getAsBoolean();
		mobsEnabled = !getConfig().has("mobsEnabled") || getConfig().get("mobsEnabled").getAsBoolean();
		dropsEnabled = !getConfig().has("dropsEnabled") || getConfig().get("dropsEnabled")
				.getAsBoolean();
		throwablesEnabled = !getConfig().has("throwablesEnabled") || getConfig().get(
				"throwablesEnabled").getAsBoolean();

		ownColorPlayers = getConfig().has("ownColorPlayers") && getConfig().get("ownColorPlayers")
				.getAsBoolean();
		ownColorAnimals = getConfig().has("ownColorAnimals") && getConfig().get("ownColorAnimals")
				.getAsBoolean();
		ownColorMobs = getConfig().has("ownColorMobs") && getConfig().get("ownColorMobs")
				.getAsBoolean();
		ownColorDrops = getConfig().has("ownColorDrops") && getConfig().get("ownColorDrops")
				.getAsBoolean();
		ownColorThrowables = getConfig().has("ownColorThrowables") && getConfig().get(
				"ownColorThrowables").getAsBoolean();

		color = getConfig().has("color") ? Color.fromRgb(getConfig().get("color").getAsInt())
				: Color.fromRgba(81, 179, 204, 255);
		eyeHeightBoxColor = getConfig().has("eyeHeightBoxColor") ? Color.fromRgb(
				getConfig().get("eyeHeightBoxColor").getAsInt()) : Color.fromRgba(255, 0, 0, 255);
		playerColor = getConfig().has("playerColor") ? Color.fromRgb(
				getConfig().get("playerColor").getAsInt()) : Color.fromRgba(81, 179, 204, 255);
		animalColor = getConfig().has("animalColor") ? Color.fromRgb(
				getConfig().get("animalColor").getAsInt()) : Color.fromRgba(81, 179, 204, 255);
		mobColor = getConfig().has("mobColor") ? Color.fromRgb(getConfig().get("mobColor").getAsInt())
				: Color.fromRgba(81, 179, 204, 255);
		dropColor = getConfig().has("dropColor") ? Color.fromRgb(
				getConfig().get("dropColor").getAsInt()) : Color.fromRgba(81, 179, 204, 255);
		throwableColor = getConfig().has("throwableColor") ? Color.fromRgb(
				getConfig().get("throwableColor").getAsInt()) : Color.fromRgba(81, 179, 204, 255);
	}

	public void fillSettings(List<SettingsElement> settingsElements) {
		settingsElements.add(new HeaderElement("§eCustomHitboxes v" + CustomHitboxes.VERSION));
		settingsElements.add(
				DynamicBooleanElement.create("§6Enable", Material.LEVER, enabled, newValue -> {
					enabled = newValue;
					getConfig().addProperty("enabled", newValue);
					saveConfig();
				}));
		settingsElements.add(DynamicHeaderElement.create(10, ""));
		settingsElements.add(
				DynamicBooleanElement.create("§6Eye Height Box", Material.LEVER, eyeHeightBoxEnabled,
						newValue -> {
							eyeHeightBoxEnabled = newValue;
							getConfig().addProperty("eyeHeightBoxEnabled", newValue);
							saveConfig();
						}));
		settingsElements.add(
				ColorPickerElement.create("§6Eye Height Box Color", Material.LIME_DYE, eyeHeightBoxColor,
						newColor -> {
							eyeHeightBoxColor = Color.fromRgb(newColor);
							getConfig().addProperty("eyeHeightBoxColor", newColor);
							saveConfig();
						}));
		settingsElements.add(DynamicHeaderElement.create(10, ""));
		settingsElements.add(
				ColorPickerElement.create("§6Hitbox Color", Material.LIME_DYE, color, newColor -> {
					color = Color.fromRgb(newColor);
					getConfig().addProperty("color", newColor);
					saveConfig();
				}));

		settingsElements.add(DynamicSettingsElement.create("§6Players", Material.DIAMOND_SWORD)
				.addSubSettings(
						DynamicBooleanElement.create("§6Enable Player Hitboxes", Material.DIAMOND_SWORD,
								playersEnabled, newValue -> {
									playersEnabled = newValue;
									getConfig().addProperty("playersEnabled", newValue);
									saveConfig();
								}), DynamicHeaderElement.create(10, ""),
						DynamicBooleanElement.create("§6Own Hitbox Visible", Material.LEVER, selfEnabled,
								newValue -> {
									selfEnabled = newValue;
									getConfig().addProperty("selfEnabled", newValue);
									saveConfig();
								}), DynamicBooleanElement.create("§6Custom Color", Material.LEVER, ownColorPlayers,
								newValue -> {
									ownColorPlayers = newValue;
									getConfig().addProperty("ownColorPlayers", newValue);
									saveConfig();
								}),
						ColorPickerElement.create("§6Custom Player Color", Material.LIME_DYE, getPlayerColor(),
								newColor -> {
									playerColor = Color.fromRgb(newColor);
									getConfig().addProperty("playerColor", newColor);
									saveConfig();
								})));

		settingsElements.add(DynamicSettingsElement.create("§6Animals", Material.PIG_SPAWN_EGG)
				.addSubSettings(
						DynamicBooleanElement.create("§6Enable Animal Hitboxes", Material.PIG_SPAWN_EGG,
								animalsEnabled, newValue -> {
									animalsEnabled = newValue;
									getConfig().addProperty("animalsEnabled", newValue);
									saveConfig();
								}), DynamicHeaderElement.create(10, ""),
						DynamicBooleanElement.create("§6Custom Color", Material.LEVER, ownColorAnimals,
								newValue -> {
									ownColorAnimals = newValue;
									getConfig().addProperty("ownColorAnimals", newValue);
									saveConfig();
								}),
						ColorPickerElement.create("§6Custom Animal Color", Material.LIME_DYE, getAnimalColor(),
								newColor -> {
									animalColor = Color.fromRgb(newColor);
									getConfig().addProperty("animalColor", newColor);
									saveConfig();
								})));

		settingsElements.add(DynamicSettingsElement.create("§6Mobs", Material.CREEPER_SPAWN_EGG)
				.addSubSettings(
						DynamicBooleanElement.create("§6Enable Mob Hitboxes", Material.CREEPER_SPAWN_EGG,
								mobsEnabled, newValue -> {
									mobsEnabled = newValue;
									getConfig().addProperty("mobsEnabled", newValue);
									saveConfig();
								}), DynamicHeaderElement.create(10, ""),
						DynamicBooleanElement.create("§6Custom Color", Material.LEVER, ownColorMobs,
								newValue -> {
									ownColorMobs = newValue;
									getConfig().addProperty("ownColorMobs", newValue);
									saveConfig();
								}),
						ColorPickerElement.create("§6Custom Mob Color", Material.LIME_DYE, getMobColor(),
								newColor -> {
									mobColor = Color.fromRgb(newColor);
									getConfig().addProperty("mobColor", newColor);
									saveConfig();
								})));

		settingsElements.add(DynamicSettingsElement.create("§6Drops", Material.APPLE)
				.addSubSettings(
						DynamicBooleanElement.create("§6Enable Drop Hitboxes", Material.APPLE, dropsEnabled,
								newValue -> {
									dropsEnabled = newValue;
									getConfig().addProperty("dropsEnabled", newValue);
									saveConfig();
								}), DynamicHeaderElement.create(10, ""),
						DynamicBooleanElement.create("§6Custom Color", Material.LEVER, ownColorDrops,
								newValue -> {
									ownColorDrops = newValue;
									getConfig().addProperty("ownColorDrops", newValue);
									saveConfig();
								}),
						ColorPickerElement.create("§6Custom Drop Color", Material.LIME_DYE, getDropColor(),
								newColor -> {
									dropColor = Color.fromRgb(newColor);
									getConfig().addProperty("dropColor", newColor);
									saveConfig();
								})));

		settingsElements.add(DynamicSettingsElement.create("§6Throwables", Material.SPLASH_POTION)
				.addSubSettings(
						DynamicBooleanElement.create("§6Enable Throwable Hitbox", Material.SPLASH_POTION,
								throwablesEnabled, newValue -> {
									throwablesEnabled = newValue;
									getConfig().addProperty("throwablesEnabled", newValue);
									saveConfig();
								}), DynamicHeaderElement.create(10, ""),
						DynamicBooleanElement.create("§6Custom Color", Material.LEVER, ownColorThrowables,
								newValue -> {
									ownColorThrowables = newValue;
									getConfig().addProperty("ownColorThrowables", newValue);
									saveConfig();
								}), ColorPickerElement.create("§6Custom Throwable Color", Material.LIME_DYE,
								getThrowableColor(), newColor -> {
									throwableColor = Color.fromRgb(newColor);
									getConfig().addProperty("throwableColor", newColor);
									saveConfig();
								})));
		settingsElements.add(DynamicHeaderElement.create(15, "", "§4§lIMPORTANT",
				"§cOnly a selection of entities is supported for\n§ccolored & permanent hitboxes.",
				"§cTo view colored hitboxes of all entities (for example\n§cArmorStands & MineCarts), "
						+ "press F3+B"));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isEyeHeightBoxEnabled() {
		return eyeHeightBoxEnabled;
	}

	public boolean isSelfEnabled() {
		return selfEnabled;
	}

	public boolean isPlayersEnabled() {
		return playersEnabled;
	}

	public boolean isAnimalsEnabled() {
		return animalsEnabled;
	}

	public boolean isMobsEnabled() {
		return mobsEnabled;
	}

	public boolean isDropsEnabled() {
		return dropsEnabled;
	}

	public boolean isThrowablesEnabled() {
		return throwablesEnabled;
	}

	public Color getColor() {
		return color;
	}

	public Color getEyeHeightBoxColor() {
		return eyeHeightBoxColor;
	}

	public Color getPlayerColor() {
		return playerColor;
	}

	public Color getAnimalColor() {
		return animalColor;
	}

	public Color getMobColor() {
		return mobColor;
	}

	public Color getDropColor() {
		return dropColor;
	}

	public Color getThrowableColor() {
		return throwableColor;
	}

	public boolean isOwnColorPlayers() {
		return ownColorPlayers;
	}

	public boolean isOwnColorAnimals() {
		return ownColorAnimals;
	}

	public boolean isOwnColorMobs() {
		return ownColorMobs;
	}

	public boolean isOwnColorDrops() {
		return ownColorDrops;
	}

	public boolean isOwnColorThrowables() {
		return ownColorThrowables;
	}

	private JsonObject getConfig() {
		return customHitboxes.getConfig();
	}

	private void saveConfig() {
		customHitboxes.saveConfig();
	}
}
