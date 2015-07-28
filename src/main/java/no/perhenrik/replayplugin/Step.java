package no.perhenrik.replayplugin;

import org.bukkit.Location;
import org.bukkit.World;

public class Step extends Location {

	public Step(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public Step(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}
	
	public Step(Location location) {
		super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
}
