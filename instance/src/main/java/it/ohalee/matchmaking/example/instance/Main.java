package it.ohalee.matchmaking.example.instance;

import it.ohalee.matchmaking.example.instance.arena.StandardArenaManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private UMBedwarsInstance umInstance;

    @Override
    public void onEnable() {
        instance = this;

        StandardArenaManager.init();

        this.umInstance = new UMBedwarsInstance(this);
    }

    @Override
    public void onDisable() {
        this.umInstance.shutdown();
    }

}
