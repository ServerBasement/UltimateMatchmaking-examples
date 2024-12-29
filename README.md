# Minecraft Matchmaking Library

UltimateMatchmaking is a Minecraft Matchmaking Library designed to facilitate matchmaking functionality 
 within Minecraft minigames. 
 It provides a set of tools and utilities to simplify the process of creating custom matchmaking
 systems for multiplayer servers.
 Whether you're creating a mini-game server or building a competitive environment,
 this library can help you manage player matchmaking efficiently.

## Features

* **Player matchmaking:**
This library offers a robust matchmaking system that allows players to be grouped together based
on customizable criteria, such as skill level, game mode preference, or party size.

* **Flexible:**
You can easily customize the matchmaking algorithm and rules to suit your specific needs.

* **Efficient matching:**
The matchmaking system is designed to be efficient and scalable, allowing for fast
matching of players in large queues.

* **Integration-ready:**
The library is designed to be easily integrated into existing Minecraft plugins.
It provides a clean API and clear documentation to simplify the development process.

* **Compatibility:**
The library is compatible with Bukkit, Spigot, Paper, and forks of these platforms.

## Getting Started

1. Download the library JAR files.
2. Include the JAR file in your Minecraft plugin's dependencies:

Master Plugin (plugin that goes in the lobby server)
```java
repositories {
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation ('UltimateMatchmaking-Master-<version>.jar')
}
```

Instance Plugin (plugin that goes in the game servers)
```java
repositories {
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation ('UltimateMatchmaking-Instance-<version>.jar')
}
```

3. Setup master

```java
public class ExamplePlugin extends JavaPlugin {

    private static final boolean MAIN_HUB = true;
    private static UMProxy<BedwarsType, SharedBedwarsMatch, BedwarsParticipator, BedwarsQueue> UM_PROXY;

    @Override
    public void onEnable() {
        UMBedwars UMBedwars = new UMBedwars(this);
        UMBedwars.registerLogger(getLogger()).metadata(MAIN_HUB);
        UMBedwars.registerRancher(this, new ServerRancherConfiguration<BedwarsType, SharedBedwarsMatch>() {
            ...
        });

        MapSupplier supplier = new MapLoader(this);
        for (BedwarsType value : BedwarsType.defaults()) {
            UMBedwars.registerQueue(new BedwarsQueue("bedwars", value, supplier));
        }

        UM_PROXY = UMBedwars.process();
    }

    @Override
    public void onDisable() {
        UMProxy.getRawProxy().shutdown();
    }
    
}

class UMBedwars extends UltimateMatchmaking<BedwarsType, SharedBedwarsMatch, BedwarsParticipator, BedwarsQueue> {

    public UMBedwars(JavaPlugin plugin) {
        super(plugin);
    }

}
```

```java
public class BedwarsQueue extends StandardQueue<BedwarsType, SharedBedwarsMatch, BedwarsParticipator> {

    public BedwarsQueue(String mode, BedwarsType queueType, MapSupplier supplier) {
        super(mode, queueType, supplier);
    }

    @Override
    public SharedBedwarsMatch seekMatch(int weight) {
        if (tunnels.isEmpty()) return createMatch();
        SharedBedwarsMatch best = null;
        for (SharedBedwarsMatch match : tunnels.values()) {
            if (match.getStatus() != SharedMatchStatus.OPEN || !match.canCarry(weight)) continue;

            best = match;
        }
        return best == null ? createMatch() : best;
    }

    @Override
    public SharedBedwarsMatch summonMatch() {
        return new SharedBedwarsMatch(UUID.randomUUID().toString().substring(0, 4) + mode + UUID.randomUUID().toString().substring(0, 3), queueType);
    }

    @Override
    public void task() {
        status = QueueStatus.TASK;
        StaticTask.runBukkitTaskTimer(summonStealer(), 2L, 2L, true);
    }

    @Override
    public QueueStealer<BedwarsType, SharedBedwarsMatch, BedwarsParticipator, BedwarsQueue> summonStealer() {
        return new BedwarsQueueStealer(
                Basement.rclient().getLock("lock_" + queueType.name()),
                this
        );
    }

}
```

_All the code required is in the repository. This is just some of the code.<br>_
_For a detailed example, see the code in the repository._

4. Setup instance

```java
public class ExamplePlugin extends JavaPlugin {

    private UMBedwarsInstance umInstance;

    @Override
    public void onEnable() {
        this.umInstance = new UMBedwarsInstance(this);
    }

    @Override
    public void onDisable() {
        this.umInstance.shutdown();
    }

}
```

```java
public class UMBedwarsInstance extends UMSub<BedwarsType, SharedBedwarsMatch, Arena> {

    public UMBedwarsInstance(Main plugin) {
        super(plugin, null);
    }

    @Override
    public UMMatchManager<BedwarsType, SharedBedwarsMatch, Arena> summonMatchManager() {
        return new BedwarsMatchManager(new BedwarsSharedListener());
    }

    @Override
    public PlayerReceiver<BedwarsType, SharedBedwarsMatch, Arena> summonPlayerReceiver() {
        return new ConnectionListener();
    }

}
```

```java
@Getter
public class Arena extends Match<BedwarsType, SharedBedwarsMatch> {

    private final Main plugin;

    private final List<Player> players = new ArrayList<>();
    private final String worldName;

    public Arena(Main plugin, SharedBedwarsMatch shared, String configInRedis, String mapName) {
        super(shared);
        this.plugin = plugin;
        this.worldName = mapName;
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    @Override
    public void letJoin(Player player) {
        switch (getStatus()) {
            case WAITING:
            case STARTING:
                addPlayer(player);

                this.joining.remove(player.getName());
                this.shared.setEffectivePlayers(this.players.size());
                this.shared.setJoiningPlayers(this.shared.getJoiningPlayers() - 1);
                break;
        }

    }

    @Override
    public void letQuit(Player player) {
        removePlayer(player);
        this.shared.setEffectivePlayers(this.players.size());
    }
    
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    @Override
    public int getMatchWeight() {
        return players.size();
    }

    public MatchStatus getStatus() {
        return this.shared.getInstanceStatus() != null ? this.shared.getInstanceStatus() : MatchStatus.WAITING;
    }

}
```

```java
@RequiredArgsConstructor
public enum BedwarsType implements UMType {
    SOLO("Solo", 1, 8),
    DOUBLES("Duo", 2, 8),
    TRIO("Trio", 3, 4),
    SQUAD("Squad", 4, 4),

    PRIVATE("Private", -1, -1);

    private final static BedwarsType[] defaults = new BedwarsType[]{SOLO, DOUBLES, TRIO, SQUAD};

    private final String visualName;
    private final int teamSize;
    private final int teams;

    public static BedwarsType[] defaults() {
        return defaults;
    }

    @Override
    public String visualName() {
        return visualName;
    }

    @Override
    public int teams() {
        return this.teams;
    }

    @Override
    public int teamSize() {
        return teamSize;
    }

    @Override
    public String toString() {
        return this.name();
    }

}
```
*_This must be implemented in both modules._

_All the code required is in the repository. This is just some of the code.<br>_
_For a detailed example, see the code in the repository._

## Contributing
Contributions to UltimateMatchmaking are welcome! 
If you encounter any issues, have suggestions for improvements, or would like to add new features,
please open an issue in the issue tracker.

## Contact
If you have any questions or need support regarding UltimateMatchmaking,
you can contact me through [Discord](https://discord.com/invite/u5pcHwWUxA)
or by opening an issue in the issue tracker.