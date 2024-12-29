package it.ohalee.matchmaking.example.master;

import it.ohalee.ultimatematchmaking.UMProxy;
import it.ohalee.ultimatematchmaking.api.MapSupplier;
import it.ohalee.matchmaking.example.master.listeners.PlayerListener;
import it.ohalee.matchmaking.example.master.map.MapLoader;
import it.ohalee.matchmaking.example.master.player.BedwarsParticipator;
import it.ohalee.matchmaking.example.master.queue.BedwarsQueue;
import it.ohalee.matchmaking.example.master.queue.CustomQueue;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.ultimatematchmaking.server.ServerRancherConfiguration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static final boolean MAIN_HUB = true;

    @Getter
    private static UMProxy<BedwarsType, SharedBedwarsMatch, BedwarsParticipator, BedwarsQueue> UM_PROXY;

    @Override
    public void onEnable() {

        UMBedwars UMBedwars = new UMBedwars(this);
        UMBedwars.registerLogger(getLogger()).metadata(MAIN_HUB);
        UMBedwars.registerRancher(this, new ServerRancherConfiguration<BedwarsType, SharedBedwarsMatch>() {

            @Override
            public String modeName() {
                return "bedwars";
            }

            @Override
            public String instancePrefix() {
                return "bw";
            }

            @Override
            public int maxMatchesPerServer() {
                return 5;
            }

            @Override
            public double warningPercentage() {
                return 60;
            }

            @Override
            public Class<SharedBedwarsMatch> sharedMatchClass() {
                return SharedBedwarsMatch.class;
            }

            @Override
            public Class<BedwarsType> typeClass() {
                return BedwarsType.class;
            }

            @Override
            public ServerManagerConfiguration<BedwarsType, SharedBedwarsMatch> serverManager() {
                return new ServerManagerConfiguration<BedwarsType, SharedBedwarsMatch>() {
                    @Override
                    public boolean dynamicallyStartServers() {
                        return true;
                    }

                    @Override
                    public int maxAmountOfServers() {
                        return 100;
                    }
                };
            }

            @Override
            public LobbyConfiguration lobbyConfiguration() {
                return new LobbyConfiguration() {
                    @Override
                    public String genericModePrefix() {
                        return "bw";
                    }

                    @Override
                    public boolean enablePlaceholderAPI() {
                        return true;
                    }
                };
            }
        });

        MapSupplier supplier = new MapLoader(this);

        for (BedwarsType value : BedwarsType.defaults()) {
            UMBedwars.registerQueue(new BedwarsQueue("bedwars", value, supplier));
        }

        UMBedwars.registerQueue(new CustomQueue("bedwars", BedwarsType.PRIVATE, supplier));

        UM_PROXY = UMBedwars.process();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

    }

    @Override
    public void onDisable() {
        UMProxy.getRawProxy().shutdown();
    }

}
