
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.pokermod.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.pokermod.PokermodMod;

public class PokermodModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PokermodMod.MODID);
	public static final RegistryObject<CreativeModeTab> POKER = REGISTRY.register("poker",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.pokermod.poker")).icon(() -> new ItemStack(PokermodModItems.COLODA_CARD.get())).displayItems((parameters, tabData) -> {
				tabData.accept(PokermodModItems.TEXAS_HOLDEM_STARTER_CARD.get());
				tabData.accept(PokermodModItems.COLODA_CARD.get());
				tabData.accept(PokermodModItems.STOP_GAME.get());
				tabData.accept(PokermodModItems.DRAW_CARD.get());
				tabData.accept(PokermodModItems.TUZ_CHERVI.get());
				tabData.accept(PokermodModItems.TUZ_BU_BI.get());
				tabData.accept(PokermodModItems.TUZ_KRESTI.get());
				tabData.accept(PokermodModItems.TUZ_PI_KI.get());
				tabData.accept(PokermodModItems.KOROL_CHERVI.get());
				tabData.accept(PokermodModItems.KOROL_BUBI.get());
				tabData.accept(PokermodModItems.KOROL_KRESTI.get());
				tabData.accept(PokermodModItems.KOROL_PIKI.get());
				tabData.accept(PokermodModItems.DAMA_CHERVI.get());
				tabData.accept(PokermodModItems.DAMA_BUBI.get());
				tabData.accept(PokermodModItems.DAMA_KRESTI.get());
				tabData.accept(PokermodModItems.DAMA_PIKI.get());
				tabData.accept(PokermodModItems.VALET_CHERVI.get());
				tabData.accept(PokermodModItems.VALET_BUBI.get());
				tabData.accept(PokermodModItems.VALET_KRESTI.get());
				tabData.accept(PokermodModItems.VALET_PIKI.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_2.get());
				tabData.accept(PokermodModItems.CARD_BUBI_2.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_2.get());
				tabData.accept(PokermodModItems.CARD_PIKI_2.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_3.get());
				tabData.accept(PokermodModItems.CARD_BUBI_3.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_3.get());
				tabData.accept(PokermodModItems.CARD_PIKI_3.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_4.get());
				tabData.accept(PokermodModItems.CARD_BUBI_4.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_4.get());
				tabData.accept(PokermodModItems.CARD_PIKI_4.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_5.get());
				tabData.accept(PokermodModItems.CARD_BUBI_5.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_5.get());
				tabData.accept(PokermodModItems.CARD_PIKI_5.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_6.get());
				tabData.accept(PokermodModItems.CARD_BUBI_6.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_6.get());
				tabData.accept(PokermodModItems.CARD_PIKI_6.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_7.get());
				tabData.accept(PokermodModItems.CARD_BUBI_7.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_7.get());
				tabData.accept(PokermodModItems.CARD_PIKI_7.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_8.get());
				tabData.accept(PokermodModItems.CARD_BUBI_8.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_8.get());
				tabData.accept(PokermodModItems.CARD_PIKI_8.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_9.get());
				tabData.accept(PokermodModItems.CARD_BUBI_9.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_9.get());
				tabData.accept(PokermodModItems.CARD_PIKI_9.get());
				tabData.accept(PokermodModItems.CARD_CHERVI_10.get());
				tabData.accept(PokermodModItems.CARD_BUBI_10.get());
				tabData.accept(PokermodModItems.CARD_KRESTI_10.get());
				tabData.accept(PokermodModItems.CARD_PIKI_10.get());
			})

					.build());
}
