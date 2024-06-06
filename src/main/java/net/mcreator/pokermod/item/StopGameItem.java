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
import net.mcreator.pokermod.procedures.BlackjackProcedures;

import java.util.List;

public class StopGameItem extends Item {
    public StopGameItem() {
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
        if (!world.isClientSide) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) entity;

                // Ход дилера до тех пор, пока его сумма карт не станет >= 17
                while (BlackjackProcedures.getDealerHandValue() < 17) {
                    BlackjackProcedures.dealerHit(serverPlayer);
                }

                // Определение победителя
                String result = BlackjackProcedures.determineWinner();
                entity.sendSystemMessage(Component.literal("Dealer hand: " + handToString(BlackjackProcedures.getDealerHand(), BlackjackProcedures.getDealerHandValue()) + ". " + result));

                // Завершение игры и очистка карт из инвентаря
                BlackjackProcedures.endGame(serverPlayer);
            }
        }
        return InteractionResultHolder.success(entity.getItemInHand(hand));
    }

    private String handToString(List<String> hand, int handValue) {
        return String.join(", ", hand) + " (Total: " + handValue + ")";
    }
}
