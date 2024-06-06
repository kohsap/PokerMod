package net.mcreator.pokermod.game;

public class Card {
    private final String suit;
    private final String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        switch (rank) {
            case "2": case "3": case "4": case "5": case "6":
            case "7": case "8": case "9": case "10":
                return Integer.parseInt(rank);
            case "J": case "Q": case "K":
                return 10;
            case "A":
                return 11;
            default:
                return 0;
        }
    }
}
