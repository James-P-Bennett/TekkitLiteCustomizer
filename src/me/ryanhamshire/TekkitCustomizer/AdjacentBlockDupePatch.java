package me.ryanhamshire.TekkitCustomizer;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.Map;

public class AdjacentBlockDupePatch implements Listener {

    private static final Map<String, String> itemIdentifiers = new HashMap<>();

    static {
        // Now some Java mafs
        // Assign values to each block
        // Can add more blocks as needed
        // Block Breaker
        itemIdentifiers.put("763:1", "x");
        // Deep Storage Unit
        itemIdentifiers.put("3131:3", "y");
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent placeEvent) {
        Player player = placeEvent.getPlayer();
        Block placedBlock = placeEvent.getBlockPlaced();
        String placedIdentifier = getIdentifier(placedBlock.getTypeId(), placedBlock.getData());
        if (placedIdentifier != null) {

            // Check if the placed block is adjacent to another block with a different identifier
            if (isAdjacentToDifferentIdentifier(placedBlock, placedIdentifier)) {
                placeEvent.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot place these two blocks near each other due to an exploit.");
            }
        }
    }
    private String getIdentifier(int itemId, byte data) {
        String identifier = itemId + ":" + data;
        return itemIdentifiers.get(identifier);
    }
    private boolean isAdjacentToDifferentIdentifier(Block block, String placedIdentifier) {
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    if (xOffset == 0 && yOffset == 0 && zOffset == 0) {
                        continue;
                    }
                    Block adjacentBlock = block.getRelative(xOffset, yOffset, zOffset);
                    String adjacentIdentifier = getIdentifier(adjacentBlock.getTypeId(), adjacentBlock.getData());
                    if (adjacentIdentifier != null && !adjacentIdentifier.equals(placedIdentifier)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

