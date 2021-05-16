package de.jumpingpxl.labymod.customhitboxes.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import de.jumpingpxl.labymod.customhitboxes.CustomHitboxes;
import de.jumpingpxl.labymod.customhitboxes.util.colorpicker.ColorPickerElement;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.ListContainerElement;
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
				new BooleanElement("§6Enable", new ControlElement.IconData(Material.LEVER), newValue -> {
					enabled = newValue;
					getConfig().addProperty("enabled", newValue);
					saveConfig();
				}, enabled));
		settingsElements.add(new HeaderElement(""));
		settingsElements.add(
				new BooleanElement("§6Eye Height Box", new ControlElement.IconData(Material.LEVER),
						newValue -> {
							eyeHeightBoxEnabled = newValue;
							getConfig().addProperty("eyeHeightBoxEnabled", newValue);
							saveConfig();
						}, eyeHeightBoxEnabled));
		settingsElements.add(ColorPickerElement.create("§6Eye Height Box Color",
				new ControlElement.IconData(Material.LIME_DYE), eyeHeightBoxColor, newColor -> {
					eyeHeightBoxColor = Color.fromRgb(newColor);
					getConfig().addProperty("eyeHeightBoxColor", newColor);
					saveConfig();
				}));
		settingsElements.add(new HeaderElement(""));
		settingsElements.add(
				ColorPickerElement.create("§6Hitbox Color", new ControlElement.IconData(Material.LIME_DYE),
						color, newColor -> {
							color = Color.fromRgb(newColor);
							getConfig().addProperty("color", newColor);
							saveConfig();
						}));

		ListContainerElement playerContainer = new ListContainerElement("§6Players",
				new ControlElement.IconData(Material.DIAMOND_SWORD));
		List<SettingsElement> playerSubSettings = Lists.newArrayList();
		playerSubSettings.add(new HeaderElement("§eCustomHitboxes §7» §ePlayers"));
		playerSubSettings.add(new BooleanElement("§6Enable Player Hitboxes",
				new ControlElement.IconData(Material.DIAMOND_SWORD), newValue -> {
			playersEnabled = newValue;
			getConfig().addProperty("playersEnabled", newValue);
			saveConfig();
		}, playersEnabled));
		playerSubSettings.add(new HeaderElement(""));
		playerSubSettings.add(
				new BooleanElement("§6Own Hitbox Visible", new ControlElement.IconData(Material.LEVER),
						newValue -> {
							selfEnabled = newValue;
							getConfig().addProperty("selfEnabled", newValue);
							saveConfig();
						}, selfEnabled));
		playerSubSettings.add(
				new BooleanElement("§6Custom Color", new ControlElement.IconData(Material.LEVER),
						newValue -> {
							ownColorPlayers = newValue;
							getConfig().addProperty("ownColorPlayers", newValue);
							saveConfig();
						}, ownColorPlayers));
		playerSubSettings.add(ColorPickerElement.create("§6Custom Player Color",
				new ControlElement.IconData(Material.LIME_DYE), getPlayerColor(), newColor -> {
					playerColor = Color.fromRgb(newColor);
					getConfig().addProperty("playerColor", newColor);
					saveConfig();
				}));
		playerContainer.getSubSettings().getElements().addAll(playerSubSettings);
		settingsElements.add(playerContainer);

		ListContainerElement animalContainer = new ListContainerElement("§6Animals",
				new ControlElement.IconData(Material.PIG_SPAWN_EGG));
		List<SettingsElement> animalSubSettings = Lists.newArrayList();
		animalSubSettings.add(new HeaderElement("§eCustomHitboxes §7» §eAnimals"));
		animalSubSettings.add(new BooleanElement("§6Enable Animal Hitboxes",
				new ControlElement.IconData(Material.PIG_SPAWN_EGG), newValue -> {
			animalsEnabled = newValue;
			getConfig().addProperty("animalsEnabled", newValue);
			saveConfig();
		}, animalsEnabled));
		animalSubSettings.add(new HeaderElement(""));
		animalSubSettings.add(
				new BooleanElement("§6Custom Color", new ControlElement.IconData(Material.LEVER),
						newValue -> {
							ownColorAnimals = newValue;
							getConfig().addProperty("ownColorAnimals", newValue);
							saveConfig();
						}, ownColorAnimals));
		animalSubSettings.add(ColorPickerElement.create("§6Custom Animal Color",
				new ControlElement.IconData(Material.LIME_DYE), getAnimalColor(), newColor -> {
					animalColor = Color.fromRgb(newColor);
					getConfig().addProperty("animalColor", newColor);
					saveConfig();
				}));
		animalContainer.getSubSettings().getElements().addAll(animalSubSettings);
		settingsElements.add(animalContainer);

		ListContainerElement mobContainer = new ListContainerElement("§6Mobs",
				new ControlElement.IconData(Material.CREEPER_SPAWN_EGG));
		List<SettingsElement> mobSubSettings = Lists.newArrayList();
		mobSubSettings.add(new HeaderElement("§eCustomHitboxes §7» §eMobs"));
		mobSubSettings.add(new BooleanElement("§6Enable Mob Hitboxes",
				new ControlElement.IconData(Material.CREEPER_SPAWN_EGG), newValue -> {
			mobsEnabled = newValue;
			getConfig().addProperty("mobsEnabled", newValue);
			saveConfig();
		}, mobsEnabled));
		mobSubSettings.add(new HeaderElement(""));
		mobSubSettings.add(
				new BooleanElement("§6Custom Color", new ControlElement.IconData(Material.LEVER),
						newValue -> {
							ownColorMobs = newValue;
							getConfig().addProperty("ownColorMobs", newValue);
							saveConfig();
						}, ownColorMobs));
		mobSubSettings.add(ColorPickerElement.create("§6Custom Mob Color",
				new ControlElement.IconData(Material.LIME_DYE), getMobColor(), newColor -> {
					mobColor = Color.fromRgb(newColor);
					getConfig().addProperty("mobColor", newColor);
					saveConfig();
				}));
		mobContainer.getSubSettings().getElements().addAll(mobSubSettings);
		settingsElements.add(mobContainer);

		ListContainerElement dropContainer = new ListContainerElement("§6Drops",
				new ControlElement.IconData(Material.APPLE));
		List<SettingsElement> dropSubSettings = Lists.newArrayList();
		dropSubSettings.add(new HeaderElement("§eCustomHitboxes §7» §eDrops"));
		dropSubSettings.add(
				new BooleanElement("§6Enable Drop Hitboxes", new ControlElement.IconData(Material.APPLE),
						newValue -> {
							dropsEnabled = newValue;
							getConfig().addProperty("dropsEnabled", newValue);
							saveConfig();
						}, dropsEnabled));
		dropSubSettings.add(new HeaderElement(""));
		dropSubSettings.add(
				new BooleanElement("§6Custom Color", new ControlElement.IconData(Material.LEVER),
						newValue -> {
							ownColorDrops = newValue;
							getConfig().addProperty("ownColorDrops", newValue);
							saveConfig();
						}, ownColorDrops));
		dropSubSettings.add(ColorPickerElement.create("§6Custom Drop Color",
				new ControlElement.IconData(Material.LIME_DYE), getDropColor(), newColor -> {
					dropColor = Color.fromRgb(newColor);
					getConfig().addProperty("dropColor", newColor);
					saveConfig();
				}));
		dropContainer.getSubSettings().getElements().addAll(dropSubSettings);
		settingsElements.add(dropContainer);

		ListContainerElement throwableContainer = new ListContainerElement("§6Throwables",
				new ControlElement.IconData(Material.SPLASH_POTION));
		List<SettingsElement> throwableSubSettings = Lists.newArrayList();
		throwableSubSettings.add(new HeaderElement("§eCustomHitboxes §7» §eThrowables"));
		throwableSubSettings.add(new BooleanElement("§6Enable Throwable Hitbox",
				new ControlElement.IconData(Material.SPLASH_POTION), newValue -> {
			throwablesEnabled = newValue;
			getConfig().addProperty("throwablesEnabled", newValue);
			saveConfig();
		}, throwablesEnabled));
		throwableSubSettings.add(new HeaderElement(""));
		throwableSubSettings.add(
				new BooleanElement("§6Custom Color", new ControlElement.IconData(Material.LEVER),
						newValue -> {
							ownColorThrowables = newValue;
							getConfig().addProperty("ownColorThrowables", newValue);
							saveConfig();
						}, ownColorThrowables));
		throwableSubSettings.add(ColorPickerElement.create("§6Custom Throwable Color",
				new ControlElement.IconData(Material.LIME_DYE), getThrowableColor(), newColor -> {
					throwableColor = Color.fromRgb(newColor);
					getConfig().addProperty("throwableColor", newColor);
					saveConfig();
				}));
		throwableContainer.getSubSettings().getElements().addAll(throwableSubSettings);
		settingsElements.add(throwableContainer);
		settingsElements.add(new HeaderElement(""));
		settingsElements.add(new HeaderElement("§4§lIMPORTANT"));
		settingsElements.add(new HeaderElement(
				"§cOnly a selection of entities is supported for colored & permanent hitboxes."));
		settingsElements.add(new HeaderElement("§cTo view colored hitboxes of all entities (for "
				+ "example ArmorStands & MineCarts), press F3+B"));
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
