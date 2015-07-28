package no.perhenrik.replayplugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class ReplayPlugin extends JavaPlugin {
	//private HashMap<String, Object> repository;
	private ConfigAccessor config;
	
    @Override
    public void onLoad() {
//        repository = new HashMap<String, Object>(); // change to read from disk
        this.config = new ConfigAccessor(this, "replays");
//        FileConfiguration fc = config.getConfig();
//        fc.addDefaults(repository);
//        List<Map<?, ?>> cv = fc.getMapList("test");
//        config.saveConfig();
    }

    public void info(String msg) {
        getLogger().info(msg);
    }
    
    public void severe(String msg) {
        getLogger().severe(msg);
    }

    @Override
    public void onEnable() {
    	this.getCommand("replay").setExecutor(new ReplayCommandExecutor(this));
    }
 
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }

	public ReplayRepository getReplayRepository(Player player) {
		ReplayRepository repo = new ReplayRepository();
		
//		if(repository.containsKey(player.getUniqueId())) {
//			repo = (ReplayRepository) repository.get(player.getUniqueId());
//		} else {
//			repository.put(player.getUniqueId().toString(), repo);
//		}
		
		if(config.getConfig().contains(player.getUniqueId().toString())) {
			repo = (ReplayRepository) config.getConfig().get(player.getUniqueId().toString());
		} else {
			config.getConfig().set(player.getUniqueId().toString(), repo);
		}
		
		return repo;
	}

	public void saveConfig() {
		this.config.saveConfig();
	}

}
