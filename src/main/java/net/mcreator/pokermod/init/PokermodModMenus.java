
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.pokermod.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import net.mcreator.pokermod.world.inventory.GameMenu;
import net.mcreator.pokermod.PokermodMod;

public class PokermodModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PokermodMod.MODID);
	public static final RegistryObject<MenuType<GameMenu>> GAME = REGISTRY.register("game", () -> IForgeMenuType.create(GameMenu::new));
}
