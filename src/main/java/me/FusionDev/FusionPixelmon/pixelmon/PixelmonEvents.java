package me.FusionDev.FusionPixelmon.pixelmon;

import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class PixelmonEvents {

    /**
     * On the server, after beating a wild pokemon while the player is
     * in flight (most likely riding a pokemon), the player will fall
     * to the ground and take full damage, often killing them.
     * The player shall be made invulnerable to fall damage for a total
     * of {@value #FALL_INVULNERABILITY} seconds after defeating a wild
     * pokemon in flight.
     */
    private List<EntityPlayerMP> invulnerableFall = new ArrayList<>();
    private static final int FALL_INVULNERABILITY = 5;

    @SubscribeEvent
    public void onBeatPokemon(BeatWildPixelmonEvent event) {
        if (!invulnerableFall.contains(event.player)) {
            invulnerableFall.add(event.player);
            new Thread(() -> {
                try {
                    Thread.sleep(FALL_INVULNERABILITY * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                invulnerableFall.remove(event.player);
            }).start();
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            if (invulnerableFall.contains(player)) {
                event.setDamageMultiplier(0);
                event.setCanceled(true);
            }
        }
    }
}
