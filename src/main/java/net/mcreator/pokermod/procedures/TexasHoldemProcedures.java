package net.mcreator.pokermod.procedures;

import net.mcreator.pokermod.init.PokermodModItems;
import net.mcreator.pokermod.game.TexasHoldemGame;
import net.mcreator.pokermod.game.PokerPlayer;
import net.mcreator.pokermod.game.PokerCard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TexasHoldemProcedures {
    private static TexasHoldemGame game;
    private static boolean gameStarted = false;
    private static final Map<String, RegistryObject<Item>> cardItems = new HashMap<>();

    static {
        cardItems.put("ace of hearts", PokermodModItems.TUZ_CHERVI);
        cardItems.put("ace of diamonds", PokermodModItems.TUZ_BU_BI);
        cardItems.put("ace of clubs", PokermodModItems.TUZ_KRESTI);
        cardItems.put("ace of spades", PokermodModItems.TUZ_PI_KI);
        cardItems.put("king of hearts", PokermodModItems.KOROL_CHERVI);
        cardItems.put("king of diamonds", PokermodModItems.KOROL_BUBI);
        cardItems.put("king of clubs", PokermodModItems.KOROL_KRESTI);
        cardItems.put("king of spades", PokermodModItems.KOROL_PIKI);
        cardItems.put("queen of hearts", PokermodModItems.DAMA_CHERVI);
        cardItems.put("queen of diamonds", PokermodModItems.DAMA_BUBI);
        cardItems.put("queen of clubs", PokermodModItems.DAMA_KRESTI);
        cardItems.put("queen of spades", PokermodModItems.DAMA_PIKI);
        cardItems.put("jack of hearts", PokermodModItems.VALET_CHERVI);
        cardItems.put("jack of diamonds", PokermodModItems.VALET_BUBI);
        cardItems.put("jack of clubs", PokermodModItems.VALET_KRESTI);
        cardItems.put("jack of spades", PokermodModItems.VALET_PIKI);
        cardItems.put("two of hearts", PokermodModItems.CARD_CHERVI_2);
        cardItems.put("two of diamonds", PokermodModItems.CARD_BUBI_2);
        cardItems.put("two of clubs", PokermodModItems.CARD_KRESTI_2);
        cardItems.put("two of spades", PokermodModItems.CARD_PIKI_2);
        cardItems.put("three of hearts", PokermodModItems.CARD_CHERVI_3);
        cardItems.put("three of diamonds", PokermodModItems.CARD_BUBI_3);
        cardItems.put("three of clubs", PokermodModItems.CARD_KRESTI_3);
        cardItems.put("three of spades", PokermodModItems.CARD_PIKI_3);
        cardItems.put("four of hearts", PokermodModItems.CARD_CHERVI_4);
        cardItems.put("four of diamonds", PokermodModItems.CARD_BUBI_4);
        cardItems.put("four of clubs", PokermodModItems.CARD_KRESTI_4);
        cardItems.put("four of spades", PokermodModItems.CARD_PIKI_4);
        cardItems.put("five of hearts", PokermodModItems.CARD_CHERVI_5);
        cardItems.put("five of diamonds", PokermodModItems.CARD_BUBI_5);
        cardItems.put("five of clubs", PokermodModItems.CARD_KRESTI_5);
        cardItems.put("five of spades", PokermodModItems.CARD_PIKI_5);
        cardItems.put("six of hearts", PokermodModItems.CARD_CHERVI_6);
        cardItems.put("six of diamonds", PokermodModItems.CARD_BUBI_6);
        cardItems.put("six of clubs", PokermodModItems.CARD_KRESTI_6);
        cardItems.put("six of spades", PokermodModItems.CARD_PIKI_6);
        cardItems.put("seven of hearts", PokermodModItems.CARD_CHERVI_7);
        cardItems.put("seven of diamonds", PokermodModItems.CARD_BUBI_7);
        cardItems.put("seven of clubs", PokermodModItems.CARD_KRESTI_7);
        cardItems.put("seven of spades", PokermodModItems.CARD_PIKI_7);
        cardItems.put("eight of hearts", PokermodModItems.CARD_CHERVI_8);
        cardItems.put("eight of diamonds", PokermodModItems.CARD_BUBI_8);
        cardItems.put("eight of clubs", PokermodModItems.CARD_KRESTI_8);
        cardItems.put("eight of spades", PokermodModItems.CARD_PIKI_8);
        cardItems.put("nine of hearts", PokermodModItems.CARD_CHERVI_9);
        cardItems.put("nine of diamonds", PokermodModItems.CARD_BUBI_9);
        cardItems.put("nine of clubs", PokermodModItems.CARD_KRESTI_9);
        cardItems.put("nine of spades", PokermodModItems.CARD_PIKI_9);
        cardItems.put("ten of hearts", PokermodModItems.CARD_CHERVI_10);
        cardItems.put("ten of diamonds", PokermodModItems.CARD_BUBI_10);
        cardItems.put("ten of clubs", PokermodModItems.CARD_KRESTI_10);
        cardItems.put("ten of spades", PokermodModItems.CARD_PIKI_10);
    }

    public static TexasHoldemGame getGame() {
        return game;
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static void startGame(ServerPlayer initiator, List<PokerPlayer> players) {
        for (PokerPlayer pokerPlayer : players) {
            if (pokerPlayer.getEntity() instanceof ServerPlayer) {
                clearPlayerHand((ServerPlayer) pokerPlayer.getEntity());
            }
        }

        // Создаем новую игру
        game = new TexasHoldemGame(players);
        game.deal();  // Раздача карт

        // Добавляем карты в инвентарь игрока
        for (PokerPlayer pokerPlayer : game.getPlayers()) {
            ServerPlayer serverPlayer = (ServerPlayer) pokerPlayer.getEntity();
            for (PokerCard card : pokerPlayer.getHand()) {
                ItemStack cardItem = getCardItem(card);
                if (cardItem != null) {
                    serverPlayer.getInventory().add(cardItem);
                    System.out.println("Added card to inventory: " + card.toString() + " (" + cardItem.toString() + ")");
                } else {
                    System.out.println("Card not found: " + card.toString());
                }
            }

            // Отображаем текущие карты игрока
            serverPlayer.sendSystemMessage(Component.literal("Texas Hold'em game started. Your hand: " + handToString(pokerPlayer.getHand(), pokerPlayer.getHandValue())));
        }

        gameStarted = true;
    }

    public static void clearPlayerHand(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (!itemStack.isEmpty() && isPokerCard(itemStack.getItem())) {
                player.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }
    }

    public static boolean isPokerCard(Item item) {
        return cardItems.values().stream().anyMatch(registryObject -> registryObject.get() == item);
    }

    public static void check(ServerPlayer player) {
        if (!gameStarted) {
            player.sendSystemMessage(Component.literal("No game is currently running."));
            return;
        }

        PokerPlayer pokerPlayer = game.getPlayer(player);
        if (pokerPlayer != null) {
            game.check(pokerPlayer);
        }
    }

    public static void fold(ServerPlayer player) {
        if (!gameStarted) {
            player.sendSystemMessage(Component.literal("No game is currently running."));
            return;
        }

        PokerPlayer pokerPlayer = game.getPlayer(player);
        if (pokerPlayer != null) {
            game.fold(pokerPlayer);
        }
    }

    public static void bet(ServerPlayer player, int amount) {
        if (!gameStarted) {
            player.sendSystemMessage(Component.literal("No game is currently running."));
            return;
        }

        PokerPlayer pokerPlayer = game.getPlayer(player);
        if (pokerPlayer != null) {
            game.placeBet(pokerPlayer, amount);
        }
    }

    public static void raise(ServerPlayer player, int amount) {
        if (!gameStarted) {
            player.sendSystemMessage(Component.literal("No game is currently running."));
            return;
        }

        PokerPlayer pokerPlayer = game.getPlayer(player);
        if (pokerPlayer != null) {
            game.raise(pokerPlayer, amount);
        }
    }

    public static void allIn(ServerPlayer player) {
        if (!gameStarted) {
            player.sendSystemMessage(Component.literal("No game is currently running."));
            return;
        }

        PokerPlayer pokerPlayer = game.getPlayer(player);
        if (pokerPlayer != null) {
            pokerPlayer.allIn();
        }
    }

    public static void nextPhase() {
        if (!gameStarted) {
            return;
        }
        game.nextPhase();
    }

    public static void endGame() {
        game = null;
        gameStarted = false;
    }

    public static ItemStack getCardItem(PokerCard card) {
        String cardName = card.getRank().toString().toLowerCase() + " of " + card.getSuit().toString().toLowerCase();
        cardName = cardName.replace("_", " ");
        RegistryObject<Item> cardItemRegistry = cardItems.get(cardName);
        if (cardItemRegistry != null) {
            return new ItemStack(cardItemRegistry.get());
        }
        System.out.println("Card item not found for: " + cardName);
        return null;
    }

    public static List<PokerCard> getPlayerHand(ServerPlayer player) {
        for (PokerPlayer pokerPlayer : game.getPlayers()) {
            if (pokerPlayer.getEntity().equals(player)) {
                return pokerPlayer.getHand();
            }
        }
        return new ArrayList<>();
    }

    public static int getPlayerHandValue(ServerPlayer player) {
        for (PokerPlayer pokerPlayer : game.getPlayers()) {
            if (pokerPlayer.getEntity().equals(player)) {
                return pokerPlayer.getHandValue();
            }
        }
        return 0;
    }

    public static String handToString(List<PokerCard> hand, int handValue) {
        StringBuilder handStr = new StringBuilder();
        for (PokerCard card : hand) {
            handStr.append(card.toString()).append(", ");
        }
        if (handStr.length() > 0) {
            handStr.setLength(handStr.length() - 2); // Удаляем последнюю запятую и пробел
        }
        return handStr.toString() + " (Total: " + handValue + ")";
    }
}