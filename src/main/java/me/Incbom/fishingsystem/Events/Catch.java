package me.Incbom.fishingsystem.Events;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Catch implements Listener {
    private Plugin plugin;
    private boolean isFishOnHook = false;
    private Entity fishEntity = null;
    private Location originalFishLocation = null;

    public Catch(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Item caughtFish = (Item) event.getCaught();
            ItemStack fishItem = caughtFish.getItemStack();

            // Spawn a new armor stand with a fish entity riding it
            ArmorStand fishArmorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
            fishArmorStand.setSmall(true);
            fishArmorStand.setInvulnerable(true);
            fishArmorStand.setVisible(false);
            fishArmorStand.setGravity(false);
            fishArmorStand.setCustomNameVisible(false);

            Entity fish = player.getWorld().spawnEntity(fishArmorStand.getLocation(), EntityType.COD);
            fish.addPassenger(fishArmorStand);

            // Set the original fish location and entity
            originalFishLocation = fishArmorStand.getLocation();
            fishEntity = fish;

            // Start the fish swimming animation
            startFishSwimming(fishArmorStand, fish, player);

            // Set the isFishOnHook flag to true
            isFishOnHook = true;
            event.setCancelled(true);
        } else if (event.getState() == PlayerFishEvent.State.REEL_IN) {
            if (isFishOnHook) {
                // Stop the fish swimming animation
                stopFishSwimming();

                // Move the fish closer to the player slowly until it's 2 blocks away
                new BukkitRunnable() {
                    int ticks = 0;
                    Location playerLocation = player.getLocation();

                    @Override
                    public void run() {
                        if (fishEntity == null || fishEntity.isDead()) {
                            cancel();
                            return;
                        }

                        double distanceToPlayer = fishEntity.getLocation().distance(playerLocation);

                        if (distanceToPlayer > 2) {
                            // Move the armor stand closer to the player slowly
                            Location fishLocation = fishEntity.getLocation();
                            Vector direction = playerLocation.subtract(fishLocation).toVector().normalize().multiply(0.1);
                            fishEntity.setVelocity(direction);

                            // Make the fish entity look at the player
                            fishEntity.setVelocity(direction);
                            fishEntity.getLocation().setDirection(playerLocation.subtract(fishLocation).toVector());

                            ticks++;
                        } else {
                            // Stop the reel in animation
                            fishEntity.setVelocity(new Vector(0, 0, 0));
                            fishEntity.getLocation().setDirection(playerLocation.subtract(fishEntity.getLocation()).toVector());

                            // Make the armor stand jump out of the water
                            fishEntity.teleport(fishEntity.getLocation().add(0, 1, 0));

                            // Despawn the armor stand
                            fishEntity.getPassengers().get(0).remove();
                            fishEntity.getPassengers().clear();

                            // Drop the fish item on the ground
                            player.getWorld().dropItem(fishEntity.getLocation().add(0, 1, 0), fishItem);
                        }
                    }
                };
            }
        }
    }
}

