package it.ohalee.matchmaking.example.master.map;

import it.ohalee.ultimatematchmaking.api.MapSupplier;
import it.ohalee.matchmaking.example.master.Main;
import it.ohalee.ultimatematchmaking.match.UMType;
import it.ohalee.ultimatematchmaking.util.Basement;
import org.redisson.api.RMapCache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MapLoader implements MapSupplier {

    private static final Map<Integer, List<String>> names = new HashMap<>();
    private static RMapCache<String, String> maps;

    private final Main plugin;

    public MapLoader(Main plugin) {
        this.plugin = plugin;

        maps = Basement.rclient().getMapCache("bedwars_maps");
        maps.clear();

        findAll();
    }

    public static String validateMap(int maxInTeam, String map) {
        if (map == null || names.values().stream().noneMatch(s -> s.stream().noneMatch(s2 -> s2.equalsIgnoreCase(map)))) {
            return getRandomName(maxInTeam);
        }
        return map;
    }

    public static String getRandomName(int k) {
        List<String> l = names.get(k);
        if (l != null)
            return l.get(new Random().nextInt(l.size()));
        return null;
    }

    public static Set<String> getRandomNames(int num, int max, String blacklist) {
        List<String> availableMaps = new ArrayList<>(names.get(max));
        availableMaps.remove(blacklist);

        Set<String> maps = new HashSet<>();

        Random rand = new Random();
        for (int i = 0; i < num; i++)
            maps.add(availableMaps.get(rand.nextInt(availableMaps.size())));

        return maps;
    }

    @Override
    public String getMap(UMType type) {
        return getRandomName(type.teamSize());
    }

    private void findAll() {

        File folder = this.plugin.getDataFolder();
        folder.mkdirs();

        File arenaFolder = new File(folder, "arenas");
        arenaFolder.mkdirs();

        for (File modes : arenaFolder.listFiles()) {
            int teamSize = Integer.parseInt(modes.getName());

            for (File arena : modes.listFiles()) {
                if (!arena.getName().endsWith(".json")) continue;
                String noJson = arena.getName().replace(".json", "");
                String arenaName = noJson.substring(0, 1).toUpperCase() + noJson.substring(1).toLowerCase();

                try {
                    String json = new String(Files.readAllBytes(Paths.get(arena.getAbsolutePath())));
                    maps.fastPut(teamSize + "_" + arenaName.toLowerCase(), json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (names.containsKey(teamSize)) {
                    List<String> list = names.get(teamSize);
                    list.add(arenaName);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(arenaName);
                    names.put(teamSize, list);
                }
            }
        }

        this.plugin.getLogger().info(" Found " + maps.size() + " maps.");
    }

}