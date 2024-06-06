package net.mcreator.pokermod.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;

import net.mcreator.pokermod.network.PokermodModVariables;

public class StartGameProcedure {
	public static void execute(LevelAccessor world) {
		PokermodModVariables.MapVariables.get(world).playerHand = ItemStack.EMPTY;
		PokermodModVariables.MapVariables.get(world).syncData(world);
		PokermodModVariables.MapVariables.get(world).dealerHand = ItemStack.EMPTY;
		PokermodModVariables.MapVariables.get(world).syncData(world);
		PokermodModVariables.MapVariables.get(world).playerScore = 0;
		PokermodModVariables.MapVariables.get(world).syncData(world);
		PokermodModVariables.MapVariables.get(world).dealerScore = 0;
		PokermodModVariables.MapVariables.get(world).syncData(world);
	}
}
