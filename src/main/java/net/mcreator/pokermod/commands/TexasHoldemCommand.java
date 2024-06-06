package net.mcreator.pokermod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.mcreator.pokermod.procedures.TexasHoldemProcedures;
import net.mcreator.pokermod.game.PokerPlayer;

import java.util.ArrayList;
import java.util.List;

public class TexasHoldemCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("texasholdem")
                .then(Commands.argument("players", StringArgumentType.string())
                        .executes(TexasHoldemCommand::startGame))
                .then(Commands.literal("check")
                        .executes(TexasHoldemCommand::check))
                .then(Commands.literal("fold")
                        .executes(TexasHoldemCommand::fold))
                .then(Commands.literal("bet")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(TexasHoldemCommand::bet)))
                .then(Commands.literal("raise")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(TexasHoldemCommand::raise)))
                .then(Commands.literal("allin")
                        .executes(TexasHoldemCommand::allIn))
                .then(Commands.literal("nextphase")
                        .executes(TexasHoldemCommand::nextPhase))
                .then(Commands.literal("end")
                        .executes(TexasHoldemCommand::endGame)));
    }

    private static int startGame(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String playersArg = StringArgumentType.getString(context, "players");
        String[] playerNames = playersArg.split(",");
        List<PokerPlayer> players = new ArrayList<>();

        for (String playerName : playerNames) {
            ServerPlayer player = source.getServer().getPlayerList().getPlayerByName(playerName);
            if (player != null) {
                players.add(new PokerPlayer(player));
            }
        }

        TexasHoldemProcedures.startGame(null, players);
        source.sendSuccess(() -> Component.literal("Texas Hold'em game started with players: " + playersArg), false);
        return 1;
    }

    private static int check(CommandContext<CommandSourceStack> context) {
        if (!TexasHoldemProcedures.isGameStarted()) {
            context.getSource().sendFailure(Component.literal("No game is currently running."));
            return 0;
        }

        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            TexasHoldemProcedures.check(player);
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
        }
        return 1;
    }

    private static int fold(CommandContext<CommandSourceStack> context) {
        if (!TexasHoldemProcedures.isGameStarted()) {
            context.getSource().sendFailure(Component.literal("No game is currently running."));
            return 0;
        }

        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            TexasHoldemProcedures.fold(player);
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
        }
        return 1;
    }

    private static int bet(CommandContext<CommandSourceStack> context) {
        if (!TexasHoldemProcedures.isGameStarted()) {
            context.getSource().sendFailure(Component.literal("No game is currently running."));
            return 0;
        }

        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            int amount = IntegerArgumentType.getInteger(context, "amount");
            TexasHoldemProcedures.bet(player, amount);
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
        }
        return 1;
    }

    private static int raise(CommandContext<CommandSourceStack> context) {
        if (!TexasHoldemProcedures.isGameStarted()) {
            context.getSource().sendFailure(Component.literal("No game is currently running."));
            return 0;
        }

        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            int amount = IntegerArgumentType.getInteger(context, "amount");
            TexasHoldemProcedures.raise(player, amount);
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
        }
        return 1;
    }

    private static int allIn(CommandContext<CommandSourceStack> context) {
        if (!TexasHoldemProcedures.isGameStarted()) {
            context.getSource().sendFailure(Component.literal("No game is currently running."));
            return 0;
        }

        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            TexasHoldemProcedures.allIn(player);
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
        }
        return 1;
    }

    private static int nextPhase(CommandContext<CommandSourceStack> context) {
        if (!TexasHoldemProcedures.isGameStarted()) {
            context.getSource().sendFailure(Component.literal("No game is currently running."));
            return 0;
        }

        TexasHoldemProcedures.nextPhase();
        context.getSource().sendSuccess(() -> Component.literal("Proceeding to the next phase."), false);
        return 1;
    }

    private static int endGame(CommandContext<CommandSourceStack> context) {
        if (!TexasHoldemProcedures.isGameStarted()) {
            context.getSource().sendFailure(Component.literal("No game is currently running."));
            return 0;
        }

        TexasHoldemProcedures.endGame();
        context.getSource().sendSuccess(() -> Component.literal("Game ended."), false);
        return 1;
    }
}
