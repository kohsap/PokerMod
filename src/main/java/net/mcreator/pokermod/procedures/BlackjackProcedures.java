package net.mcreator.pokermod.procedures;

import net.mcreator.pokermod.game.BlackjackGame;
import net.mcreator.pokermod.init.PokermodModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackjackProcedures {
    private static BlackjackGame game;
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
        cardItems.put("2 of hearts", PokermodModItems.CARD_CHERVI_2);
        cardItems.put("2 of diamonds", PokermodModItems.CARD_BUBI_2);
        cardItems.put("2 of clubs", PokermodModItems.CARD_KRESTI_2);
        cardItems.put("2 of spades", PokermodModItems.CARD_PIKI_2);
        cardItems.put("3 of hearts", PokermodModItems.CARD_CHERVI_3);
        cardItems.put("3 of diamonds", PokermodModItems.CARD_BUBI_3);
        cardItems.put("3 of clubs", PokermodModItems.CARD_KRESTI_3);
        cardItems.put("3 of spades", PokermodModItems.CARD_PIKI_3);
        cardItems.put("4 of hearts", PokermodModItems.CARD_CHERVI_4);
        cardItems.put("4 of diamonds", PokermodModItems.CARD_BUBI_4);
        cardItems.put("4 of clubs", PokermodModItems.CARD_KRESTI_4);
        cardItems.put("4 of spades", PokermodModItems.CARD_PIKI_4);
        cardItems.put("5 of hearts", PokermodModItems.CARD_CHERVI_5);
        cardItems.put("5 of diamonds", PokermodModItems.CARD_BUBI_5);
        cardItems.put("5 of clubs", PokermodModItems.CARD_KRESTI_5);
        cardItems.put("5 of spades", PokermodModItems.CARD_PIKI_5);
        cardItems.put("6 of hearts", PokermodModItems.CARD_CHERVI_6);
        cardItems.put("6 of diamonds", PokermodModItems.CARD_BUBI_6);
        cardItems.put("6 of clubs", PokermodModItems.CARD_KRESTI_6);
        cardItems.put("6 of spades", PokermodModItems.CARD_PIKI_6);
        cardItems.put("7 of hearts", PokermodModItems.CARD_CHERVI_7);
        cardItems.put("7 of diamonds", PokermodModItems.CARD_BUBI_7);
        cardItems.put("7 of clubs", PokermodModItems.CARD_KRESTI_7);
        cardItems.put("7 of spades", PokermodModItems.CARD_PIKI_7);
        cardItems.put("8 of hearts", PokermodModItems.CARD_CHERVI_8);
        cardItems.put("8 of diamonds", PokermodModItems.CARD_BUBI_8);
        cardItems.put("8 of clubs", PokermodModItems.CARD_KRESTI_8);
        cardItems.put("8 of spades", PokermodModItems.CARD_PIKI_8);
        cardItems.put("9 of hearts", PokermodModItems.CARD_CHERVI_9);
        cardItems.put("9 of diamonds", PokermodModItems.CARD_BUBI_9);
        cardItems.put("9 of clubs", PokermodModItems.CARD_KRESTI_9);
        cardItems.put("9 of spades", PokermodModItems.CARD_PIKI_9);
        cardItems.put("10 of hearts", PokermodModItems.CARD_CHERVI_10);
        cardItems.put("10 of diamonds", PokermodModItems.CARD_BUBI_10);
        cardItems.put("10 of clubs", PokermodModItems.CARD_KRESTI_10);
        cardItems.put("10 of spades", PokermodModItems.CARD_PIKI_10);
    }

    public static void startGame(ServerPlayer player) {
        if (gameStarted) return;

        game = new BlackjackGame();
        game.dealInitialCards();
        gameStarted = true;

        // Добавляем предметы в инвентарь
        ItemStack stopGameItem = new ItemStack(PokermodModItems.STOP_GAME.get());
        ItemStack drawCardItem = new ItemStack(PokermodModItems.DRAW_CARD.get());

        player.getInventory().add(stopGameItem);
        player.getInventory().add(drawCardItem);

        // Добавляем карты в инвентарь
        for (String card : getPlayerHand()) {
            ItemStack cardItem = getCardItem(card);
            if (cardItem != null) {
                player.getInventory().add(cardItem);
                System.out.println("Added card to inventory: " + card + " (" + cardItem + ")");
            } else {
                System.out.println("Card not found: " + card);
            }
        }

        // Отображаем текущие карты игрока
        player.sendSystemMessage(Component.literal("Game started. Your hand: " + handToString(getPlayerHand(), getPlayerHandValue())));
    }

    public static void playerHit(ServerPlayer player) {
        game.hitPlayer();

        // Добавляем новую карту в инвентарь
        String newCard = game.getPlayerHand().get(game.getPlayerHand().size() - 1);
        ItemStack cardItem = getCardItem(newCard);
        if (cardItem != null) {
            player.getInventory().add(cardItem);
            System.out.println("Added card to inventory: " + newCard + " (" + cardItem + ")");
        } else {
            System.out.println("Card not found: " + newCard);
        }

        // Отображаем текущие карты игрока
        player.sendSystemMessage(Component.literal("Your hand: " + handToString(getPlayerHand(), getPlayerHandValue())));
    }

    public static void dealerHit(ServerPlayer player) {
        game.hitDealer();
        // Добавляем карты дилера в инвентарь
        for (String card : getDealerHand()) {
            ItemStack cardItem = getCardItem(card);
            if (cardItem != null) {
                player.getInventory().add(cardItem);
                System.out.println("Added dealer's card to inventory: " + card + " (" + cardItem + ")");
            } else {
                System.out.println("Dealer's card not found: " + card);
            }
        }
    }

    public static ItemStack getCardItem(String cardName) {
        RegistryObject<Item> cardItemRegistry = cardItems.get(cardName.toLowerCase());
        if (cardItemRegistry != null) {
            return new ItemStack(cardItemRegistry.get());
        }
        System.out.println("Card item not found for: " + cardName);
        return null;
    }

    public static void endGame(ServerPlayer player) {
        gameStarted = false;

        // Очищаем инвентарь от всех карт, кроме ColodaCardItem
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (!itemStack.isEmpty() && itemStack.getItem() != PokermodModItems.COLODA_CARD.get()) {
                player.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }

        player.sendSystemMessage(Component.literal("Game ended."));
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static List<String> getPlayerHand() {
        return game.getPlayerHand();
    }

    public static List<String> getDealerHand() {
        return game.getDealerHand();
    }

    public static int getPlayerHandValue() {
        return game.calculateHandValue(game.getPlayerHand());
    }

    public static int getDealerHandValue() {
        return game.calculateHandValue(game.getDealerHand());
    }

    public static boolean isPlayerBust() {
        return game.isPlayerBust();
    }

    public static boolean isDealerBust() {
        return game.isDealerBust();
    }

    public static String determineWinner() {
        return game.determineWinner();
    }

    public static String handToString(List<String> hand, int handValue) {
        return String.join(", ", hand) + " (Total: " + handValue + ")";
    }
}
