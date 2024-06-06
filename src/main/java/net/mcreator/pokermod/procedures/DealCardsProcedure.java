package net.mcreator.pokermod.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.pokermod.network.PokermodModVariables;
import net.mcreator.pokermod.init.PokermodModItems;

public class DealCardsProcedure {
	public static void execute(LevelAccessor world) {
		ItemStack randomCardPlayer = ItemStack.EMPTY;
		ItemStack randomCardDealer = ItemStack.EMPTY;
		if (Mth.nextInt(RandomSource.create(), 0, 1) == 0) {
			randomCardPlayer = new ItemStack(PokermodModItems.CARD_CHERVI_2.get());
		} else {
			randomCardPlayer = new ItemStack(PokermodModItems.CARD_CHERVI_3.get());
		}
		if (Mth.nextInt(RandomSource.create(), 0, 1) == 0) {
			randomCardDealer = new ItemStack(PokermodModItems.CARD_BUBI_2.get());
		} else {
			randomCardDealer = new ItemStack(PokermodModItems.CARD_BUBI_3.get());
		}
		PokermodModVariables.MapVariables.get(world).playerHand = randomCardPlayer;
		PokermodModVariables.MapVariables.get(world).syncData(world);
		PokermodModVariables.MapVariables.get(world).dealerHand = randomCardDealer;
		PokermodModVariables.MapVariables.get(world).syncData(world);
	}
}
