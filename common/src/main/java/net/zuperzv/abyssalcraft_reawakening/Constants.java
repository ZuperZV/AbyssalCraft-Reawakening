package net.zuperzv.abyssalcraft_reawakening;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "abyssalcraft_reawakening";
	public static final String MOD_NAME = "AbyssalCraft Reawakening";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier id(String name) {
		return Identifier.fromNamespaceAndPath(MOD_ID, name);
	}
	public static Identifier entityId(String name) {return Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/" + name + ".png");}
	public static Identifier idWithDefaultNamespace(String name) {return Identifier.withDefaultNamespace(name);
	}
}
