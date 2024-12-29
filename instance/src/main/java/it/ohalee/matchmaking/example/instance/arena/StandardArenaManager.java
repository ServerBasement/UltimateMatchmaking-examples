package it.ohalee.matchmaking.example.instance.arena;

import it.ohalee.matchmaking.example.instance.Main;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.ultimatematchmaking.util.Basement;
import org.bukkit.World;
import org.redisson.api.RMapCache;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class StandardArenaManager {

    private static RMapCache<String, String> maps;
    private static final Map<String, Arena> arenas = new ConcurrentHashMap<>();

    private StandardArenaManager() {
    }

    public static void init() {
        maps = Basement.rclient().getMapCache("bedwars_maps");
    }

    public static CompletableFuture<Arena> loadArena(Main plugin, SharedBedwarsMatch shared, String mapName) {
        return CompletableFuture.supplyAsync(() -> {
            plugin.getLogger().info("Loading Arena... " + mapName);

            Arena arena = new Arena(plugin, shared, maps.get(shared.getTeamSize() + "_" + mapName.toLowerCase()), mapName.toLowerCase());

            arenas.put(arena.getWorldName().toLowerCase(), arena);

            return arena;
        });
    }

    public static Arena arena(World world) {
        return arenas.get(world.getName().toLowerCase());
    }

}