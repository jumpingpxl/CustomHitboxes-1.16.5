package de.jumpingpxl.labymod.customhitboxes;

import net.labymod.addon.AddonTransformer;
import net.labymod.api.TransformerType;

public class CustomHitboxTransformer extends AddonTransformer {

	@Override
	public void registerTransformers() {
		this.registerTransformer(TransformerType.VANILLA, "customhitboxes.mixin.json");
	}
}
