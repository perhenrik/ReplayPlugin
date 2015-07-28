package no.perhenrik.replayplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Transporter {
	Player player;
	
	public Transporter(Player player) {
		this.player = player;
	}
	
	public void moveTo(Location location) {
		player.teleport(location);
	}
}
