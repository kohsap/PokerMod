package net.mcreator.pokermod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mcreator.pokermod.procedures.BlackjackProcedures;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class BlackjackCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("blackjack")
                .then(Commands.literal("start").executes(BlackjackCommand::startGame))
                .then(Commands.literal("hit").executes(BlackjackCommand::playerHit))
                .then(Commands.literal("stand").executes(BlackjackCommand::dealerTurn))
                .then(Commands.literal("end").executes(BlackjackCommand::endGame))
        );
    }

    private static int startGame(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        BlackjackProcedures.startGame(player);
        return 1;
    }

    private static int playerHit(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        BlackjackProcedures.playerHit(player);
        if (BlackjackProcedures.isPlayerBust()) {
            player.sendSystemMessage(Component.literal("You busted! Hand: " + BlackjackProcedures.handToString(BlackjackProcedures.getPlayerHand(), BlackjackProcedures.getPlayerHandValue())));
        } else {
            player.sendSystemMessage(Component.literal("Your hand: " + BlackjackProcedures.handToString(BlackjackProcedures.getPlayerHand(), BlackjackProcedures.getPlayerHandValue())));
        }
        return 1;
    }

    private static int dealerTurn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        while (BlackjackProcedures.getDealerHandValue() < 17) {
            BlackjackProcedures.dealerHit(player);
        }
        String result = BlackjackProcedures.determineWinner();
        player.sendSystemMessage(Component.literal("Dealer hand: " + BlackjackProcedures.handToString(BlackjackProcedures.getDealerHand(), BlackjackProcedures.getDealerHandValue()) + ". " + result));
        return 1;
    }

    private static int endGame(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        BlackjackProcedures.endGame(player);
        return 1;
    }
}
