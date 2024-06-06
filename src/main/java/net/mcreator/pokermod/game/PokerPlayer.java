package net.mcreator.pokermod.game;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class PokerPlayer {
    private Player entity;
    private List<PokerCard> hand;
    private int chips;
    private boolean active;
    private boolean checked;

    public PokerPlayer(Player entity) {
        this.entity = entity;
        this.hand = new ArrayList<>();
        this.chips = 1000; // Начальное количество фишек
        this.active = true;
        this.checked = false;
    }

    public Player getEntity() {
        return entity;
    }

    public List<PokerCard> getHand() {
        return hand;
    }

    public void addCard(PokerCard card) {
        hand.add(card);
    }

    public void bet(int amount) {
        if (amount <= chips) {
            chips -= amount;
            entity.sendSystemMessage(Component.literal("You bet " + amount + " chips. Remaining chips: " + chips));
        } else {
            entity.sendSystemMessage(Component.literal("Insufficient chips to bet " + amount + ". You only have " + chips + " chips."));
            throw new IllegalStateException("Insufficient chips to bet " + amount);
        }
    }

    public void allIn() {
        int amount = chips;
        chips = 0;
        entity.sendSystemMessage(Component.literal("You went all-in with " + amount + " chips."));
        // Обработка all-in логики, если нужно
    }

    public int getChips() {
        return chips;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void sendSystemMessage(Component message) {
        entity.sendSystemMessage(message);
    }

    public int getHandValue() {
        // Пример логики расчета значения руки
        int value = 0;
        for (PokerCard card : hand) {
            value += card.getValue();
        }
        return value;
    }
}
