package me.FusionDev.FusionPixelmon.sponge.modules.antifall.listeners;

import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import me.fusiondev.fusionpixelmon.impl.TimeUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class PixelmonEvents {

    /**
     * On the server, after beating a wild pokemon while the player is
     * in flight (most likely riding a pokemon), the player will fall
     * to the ground and take full damage, often killing them.
     * The player shall be made invulnerable to fall damage for a total
     * of {@value #FALL_INVULNERABILITY} seconds after defeating a wild
     * pokemon in flight.
     */
    private final List<EntityPlayerMP> INVULNERABLE_FALL = new ArrayList<>();
    private static final int FALL_INVULNERABILITY = 5;

    @SubscribeEvent
    public void onBeatPokemon(BeatWildPixelmonEvent event) {
        if (!INVULNERABLE_FALL.contains(event.player)) {
            INVULNERABLE_FALL.add(event.player);
            TimeUtil.setTimeout(() -> INVULNERABLE_FALL.remove(event.player), FALL_INVULNERABILITY * 1000);
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            if (INVULNERABLE_FALL.contains(player)) {
                event.setDamageMultiplier(0);
                event.setCanceled(true);
            }
        }
    }
}
