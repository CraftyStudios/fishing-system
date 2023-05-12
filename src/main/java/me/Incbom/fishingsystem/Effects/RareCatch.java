package me.Incbom.fishingsystem.Effects;

import me.Incbom.fishingsystem.utils.*;
import java.util.List;
import org.bukkit.entity.Player;

public class RareCatch {
    private Plugin plugin;


    public RareCatch(Plugin plugin) {
        this.plugin = plugin;
    }
    public boolean isItemRare(String item) {
        if (item == null) {
            return false;
        }
        List<String> rareItems = plugin.getConfig().getStringList("rare-items");
        return rareItems.contains(item);
    }
    public void playRareAnimation(Player player) {
        launchFirework(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = player.getLocation();
                location.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
                location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 100, 0.5, 0.5, 0.5, 0.5);
            }
        };

    }   
    public void launchFirework(Player player) {
        Location location = player.getLocation();
    
        Firework firework = (Firework) location.getWorld().spawn(location, Firework.class);
    
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
    
        FireworkEffect.Builder effect = FireworkEffect.builder();
        effect.withColor(Color.RED);
        effect.withFade(Color.ORANGE);
        effect.with(FireworkEffect.Type.BALL_LARGE);
    
        fireworkMeta.addEffect(effect.build());
        fireworkMeta.setPower(1);
    
        firework.setFireworkMeta(fireworkMeta);
    }
         
}

