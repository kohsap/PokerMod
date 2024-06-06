package net.mcreator.pokermod.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackjackGame {
    private List<String> deck;
    private List<String> playerHand;
    private List<String> dealerHand;

    public BlackjackGame() {
        deck = new ArrayList<>();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        initializeDeck();
        shuffleDeck();
    }

    private void initializeDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(rank + " of " + suit);
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public void dealInitialCards() {
        playerHand.add(deck.remove(0));
        playerHand.add(deck.remove(0));
        dealerHand.add(deck.remove(0));
        dealerHand.add(deck.remove(0));
    }

    public void hitPlayer() {
        playerHand.add(deck.remove(0));
    }

    public void hitDealer() {
        dealerHand.add(deck.remove(0));
    }

    public List<String> getPlayerHand() {
        return playerHand;
    }

    public List<String> getDealerHand() {
        return dealerHand;
    }

    public int calculateHandValue(List<String> hand) {
        int value = 0;
        int aceCount = 0;

        for (String card : hand) {
            String rank = card.split(" ")[0];
            if (rank.equals("Ace")) {
                aceCount++;
                value += 11;
            } else if (rank.equals("King") || rank.equals("Queen") || rank.equals("Jack")) {
                value += 10;
            } else {
                value += Integer.parseInt(rank);
            }
        }

        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }

        return value;
    }

    public boolean isPlayerBust() {
        return calculateHandValue(playerHand) > 21;
    }

    public boolean isDealerBust() {
        return calculateHandValue(dealerHand) > 21;
    }

    public String determineWinner() {
        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        if (playerValue > 21) {
            return "Dealer wins!";
        } else if (dealerValue > 21) {
            return "Player wins!";
        } else if (playerValue > dealerValue) {
            return "Player wins!";
        } else if (dealerValue > playerValue) {
            return "Dealer wins!";
        } else {
            return "It's a tie!";
        }
    }
}
