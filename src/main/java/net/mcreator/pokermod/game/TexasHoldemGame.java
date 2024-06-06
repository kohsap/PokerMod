package net.mcreator.pokermod.game;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TexasHoldemGame {
    private final PokerDeck deck;
    private final List<PokerCard> communityCards;
    private final List<PokerPlayer> players;
    private int currentBet;
    private int pot;
    private int phase; // 0: Pre-flop, 1: Flop, 2: Turn, 3: River
    private int currentPlayerIndex;

    public TexasHoldemGame(List<PokerPlayer> players) {
        this.deck = new PokerDeck();
        this.communityCards = new ArrayList<>();
        this.players = players;
        this.currentBet = 0;
        this.pot = 0;
        this.phase = 0;
        this.currentPlayerIndex = 0;
        deck.shuffle();
    }

    public List<PokerPlayer> getPlayers() {
        return players;
    }

    public void deal() {
        for (PokerPlayer player : players) {
            player.addCard(deck.draw());
            player.addCard(deck.draw());
        }
        broadcastMessage("Dealing cards...");
        for (PokerPlayer player : players) {
            player.getEntity().sendSystemMessage(Component.literal("Your cards: " + player.getHand()));
        }
        promptNextPlayer();
    }

    public void flop() {
        communityCards.add(deck.draw());
        communityCards.add(deck.draw());
        communityCards.add(deck.draw());
        broadcastMessage("Flop: " + communityCards);
        promptNextPlayer();
    }

    public void turn() {
        communityCards.add(deck.draw());
        broadcastMessage("Turn: " + communityCards);
        promptNextPlayer();
    }

    public void river() {
        communityCards.add(deck.draw());
        broadcastMessage("River: " + communityCards);
        promptNextPlayer();
    }

    public List<PokerCard> getCommunityCards() {
        return communityCards;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int getPot() {
        return pot;
    }

    public void placeBet(PokerPlayer player, int amount) {
        if (amount <= player.getChips()) {
            player.bet(amount);
            currentBet = amount;
            pot += amount;
            player.setChecked(false); // Сбросить чек, так как игрок сделал ставку
            broadcastMessage(player.getEntity().getName().getString() + " bet " + amount + " chips. Remaining chips: " + player.getChips());
            if (players.size() == 1 || allPlayersCheckedOrFolded()) {
                nextPhase();
            } else {
                promptNextPlayer();
            }
        } else {
            throw new IllegalStateException("Insufficient chips to bet");
        }
    }

    public void check(PokerPlayer player) {
        if (currentBet == 0) {
            player.setChecked(true);
            broadcastMessage(player.getEntity().getName().getString() + " checks.");
            if (players.size() == 1 || allPlayersCheckedOrFolded()) {
                nextPhase();
            } else {
                promptNextPlayer();
            }
        } else {
            throw new IllegalStateException("Cannot check when there is an active bet");
        }
    }

    public void fold(PokerPlayer player) {
        player.setActive(false);
        broadcastMessage(player.getEntity().getName().getString() + " folds.");
        if (players.size() == 1 || allPlayersCheckedOrFolded()) {
            nextPhase();
        } else {
            promptNextPlayer();
        }
    }

    public void raise(PokerPlayer player, int amount) {
        if (amount > currentBet) {
            int raiseAmount = amount - currentBet;
            player.bet(raiseAmount);
            currentBet = amount;
            pot += raiseAmount;
            player.setChecked(false);
            broadcastMessage(player.getEntity().getName().getString() + " raises by " + raiseAmount + ".");
            if (players.size() == 1 || allPlayersCheckedOrFolded()) {
                nextPhase();
            } else {
                promptNextPlayer();
            }
        } else {
            throw new IllegalStateException("Raise must be greater than the current bet");
        }
    }

    public boolean allPlayersCheckedOrFolded() {
        for (PokerPlayer player : players) {
            if (player.isActive() && !player.isChecked()) {
                return false;
            }
        }
        return true;
    }

    public void resetChecks() {
        for (PokerPlayer player : players) {
            player.setChecked(false);
        }
    }

    public PokerPlayer getPlayer(ServerPlayer entity) {
        for (PokerPlayer player : players) {
            if (player.getEntity().equals(entity)) {
                return player;
            }
        }
        return null;
    }

    public void nextPhase() {
        phase++;
        resetChecks();
        currentBet = 0;

        switch (phase) {
            case 1:
                flop();
                break;
            case 2:
                turn();
                break;
            case 3:
                river();
                break;
            case 4:
                determineWinner();
                break;
        }
    }

    public void promptNextPlayer() {
        if (allPlayersCheckedOrFolded()) {
            nextPhase();
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            PokerPlayer currentPlayer = players.get(currentPlayerIndex);
            while (!currentPlayer.isActive()) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                currentPlayer = players.get(currentPlayerIndex);
            }
            broadcastMessage("It's " + currentPlayer.getEntity().getName().getString() + "'s turn.");
        }
    }

    private void broadcastMessage(String message) {
        for (PokerPlayer player : players) {
            player.getEntity().sendSystemMessage(Component.literal(message));
        }
    }

    private void determineWinner() {
        broadcastMessage("Determining the winner...");
        PokerPlayer winner = null;
        HandRank bestHandRank = null;

        for (PokerPlayer player : players) {
            if (player.isActive()) {
                List<PokerCard> combinedHand = new ArrayList<>(player.getHand());
                combinedHand.addAll(communityCards);
                HandRank handRank = evaluateHand(combinedHand);

                if (bestHandRank == null || handRank.compareTo(bestHandRank) > 0) {
                    bestHandRank = handRank;
                    winner = player;
                }
            }
        }

        if (winner != null) {
            broadcastMessage("The winner is " + winner.getEntity().getName().getString() + " with hand: " + bestHandRank);
        } else {
            broadcastMessage("No winner, something went wrong.");
        }
    }

    private HandRank evaluateHand(List<PokerCard> hand) {
        if (isRoyalFlush(hand)) return new HandRank(HandRankType.ROYAL_FLUSH);
        if (isStraightFlush(hand)) return new HandRank(HandRankType.STRAIGHT_FLUSH);
        if (isFourOfAKind(hand)) return new HandRank(HandRankType.FOUR_OF_A_KIND);
        if (isFullHouse(hand)) return new HandRank(HandRankType.FULL_HOUSE);
        if (isFlush(hand)) return new HandRank(HandRankType.FLUSH);
        if (isStraight(hand)) return new HandRank(HandRankType.STRAIGHT);
        if (isThreeOfAKind(hand)) return new HandRank(HandRankType.THREE_OF_A_KIND);
        if (isTwoPair(hand)) return new HandRank(HandRankType.TWO_PAIR);
        if (isOnePair(hand)) return new HandRank(HandRankType.ONE_PAIR);
        return new HandRank(HandRankType.HIGH_CARD, hand);
    }

    private boolean isRoyalFlush(List<PokerCard> hand) {
        return isStraightFlush(hand) && getHighestCard(hand).getRank() == PokerCard.Rank.ACE;
    }

    private boolean isStraightFlush(List<PokerCard> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private boolean isFourOfAKind(List<PokerCard> hand) {
        Map<PokerCard.Rank, Integer> rankCount = getRankCount(hand);
        return rankCount.containsValue(4);
    }

    private boolean isFullHouse(List<PokerCard> hand) {
        Map<PokerCard.Rank, Integer> rankCount = getRankCount(hand);
        return rankCount.containsValue(3) && rankCount.containsValue(2);
    }

    private boolean isFlush(List<PokerCard> hand) {
        Map<PokerCard.Suit, Integer> suitCount = getSuitCount(hand);
        return suitCount.containsValue(5);
    }

    private boolean isStraight(List<PokerCard> hand) {
        List<Integer> rankValues = getSortedRankValues(hand);
        for (int i = 0; i < rankValues.size() - 4; i++) {
                if (rankValues.get(i + 4) - rankValues.get(i) == 4) {
                return true;
             }
        }
        return false;
    }

    private boolean isThreeOfAKind(List<PokerCard> hand) {
        Map<PokerCard.Rank, Integer> rankCount = getRankCount(hand);
        return rankCount.containsValue(3);
    }

    private boolean isTwoPair(List<PokerCard> hand) {
        Map<PokerCard.Rank, Integer> rankCount = getRankCount(hand);
        int pairCount = 0;
        for (int count : rankCount.values()) {
            if (count == 2) pairCount++;
        }
        return pairCount == 2;
    }

    private boolean isOnePair(List<PokerCard> hand) {
        Map<PokerCard.Rank, Integer> rankCount = getRankCount(hand);
        return rankCount.containsValue(2);
    }

    private PokerCard getHighestCard(List<PokerCard> hand) {
        return Collections.max(hand, Comparator.comparingInt(PokerCard::getRankValue));
    }

    private Map<PokerCard.Rank, Integer> getRankCount(List<PokerCard> hand) {
        Map<PokerCard.Rank, Integer> rankCount = new HashMap<>();
        for (PokerCard card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCount;
    }

    private Map<PokerCard.Suit, Integer> getSuitCount(List<PokerCard> hand) {
        Map<PokerCard.Suit, Integer> suitCount = new HashMap<>();
        for (PokerCard card : hand) {
            suitCount.put(card.getSuit(), suitCount.getOrDefault(card.getSuit(), 0) + 1);
        }
        return suitCount;
    }

    private List<Integer> getSortedRankValues(List<PokerCard> hand) {
        List<Integer> rankValues = new ArrayList<>();
        for (PokerCard card : hand) {
            rankValues.add(card.getRankValue());
        }
        Collections.sort(rankValues);
        return rankValues;
    }

    private enum HandRankType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }

    private class HandRank implements Comparable<HandRank> {
        private final HandRankType type;
        private final List<PokerCard> hand;

        public HandRank(HandRankType type) {
            this(type, Collections.emptyList());
        }

        public HandRank(HandRankType type, List<PokerCard> hand) {
            this.type = type;
            this.hand = new ArrayList<>(hand);
            this.hand.sort(Comparator.comparingInt(PokerCard::getRankValue).reversed());
        }

        @Override
        public int compareTo(HandRank other) {
            if (this.type != other.type) {
                return this.type.ordinal() - other.type.ordinal();
            }
            for (int i = 0; i < this.hand.size(); i++) {
                int comparison = this.hand.get(i).getRankValue() - other.hand.get(i).getRankValue();
                if (comparison != 0) {
                    return comparison;
                }
            }
            return 0;
        }

        @Override
        public String toString() {
            return type + " " + hand;
        }
    }
}
