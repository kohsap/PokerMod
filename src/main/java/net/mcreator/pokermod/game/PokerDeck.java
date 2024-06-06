package net.mcreator.pokermod.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokerDeck {
    private List<PokerCard> cards;

    public PokerDeck() {
        cards = new ArrayList<>();
        for (PokerCard.Suit suit : PokerCard.Suit.values()) {
            for (PokerCard.Rank rank : PokerCard.Rank.values()) {
                cards.add(new PokerCard(suit, rank));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public PokerCard draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.remove(cards.size() - 1);
    }
}
