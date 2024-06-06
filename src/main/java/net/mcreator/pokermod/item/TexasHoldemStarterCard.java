package net.mcreator.pokermod.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.mcreator.pokermod.game.TexasHoldemGame;
import net.mcreator.pokermod.game.PokerPlayer;
import net.mcreator.pokermod.game.PokerCard;
import net.mcreator.pokermod.procedures.TexasHoldemProcedures;

import java.util.ArrayList;

public class TexasHoldemStarterCard extends Item {
    public TexasHoldemStarterCard() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        return new ItemStack(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        if (!world.isClientSide && entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) entity;

            // Очищаем инвентарь игрока от старых карт
            TexasHoldemProcedures.clearPlayerHand(serverPlayer);

            // Создаем список игроков, включая серверного игрока
            ArrayList<PokerPlayer> players = new ArrayList<>();
            players.add(new PokerPlayer(serverPlayer));  // Добавляем текущего игрока, можно расширить для включения других игроков

            // Создаем новую игру
            TexasHoldemGame game = new TexasHoldemGame(players);
            game.deal();  // Раздача карт
            serverPlayer.sendSystemMessage(Component.literal("Texas Hold'em game started!"));

            // Добавляем карты в инвентарь игрока
            for (PokerPlayer pokerPlayer : game.getPlayers()) {
                if (pokerPlayer.getEntity().equals(serverPlayer)) {
                    for (PokerCard card : pokerPlayer.getHand()) {
                        ItemStack cardItem = TexasHoldemProcedures.getCardItem(card);
                        if (cardItem != null) {
                            serverPlayer.getInventory().add(cardItem);
                            System.out.println("Added card to inventory: " + card.toString() + " (" + cardItem.toString() + ")");
                        } else {
                            System.out.println("Card not found: " + card.toString());
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.success(entity.getItemInHand(hand));
    }
}
