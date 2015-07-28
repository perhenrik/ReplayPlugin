package no.perhenrik.replayplugin;

import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplayCommandExecutor implements CommandExecutor {
	
	private final ReplayPlugin plugin;
	 
	public ReplayCommandExecutor(ReplayPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		
    	if (cmd.getName().equalsIgnoreCase("replay")) {
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			player = (Player) sender;
    			//player.sendMessage("Looks good :)");
    			//player.getLocation().set
    		}
    		//player.sendMessage("Label is " + label);
    		//for(int i=0; i<args.length;i++) {
    			//player.sendMessage("arg " + i + " = " + args[i]);
    		//}
    		
    		if(args.length < 1) {
    			usage(player);
    		} else {   
    			ReturnValue ret = null;
    			Transporter transporter = new Transporter(player);
	    		switch(args[0]) {
	    			case "list":
	    				if(args.length != 1) {
	    					usageList(player);
	    				} else {
		    				player.sendMessage("List of all replays:");
		    				Set<String> replays = plugin.getReplayRepository(player).list();
		    				for (String id : replays) {
								sendText(player, id);
		    				}
	    				}
	    				break;
	    			case "create":
	    			case "new":
	    				if(args.length != 2) {
	    					usageNew(player);
	    				} else {
	    					plugin.getReplayRepository(player).create(args[1]);
	    					ret = plugin.getReplayRepository(player).select(args[1]);
	    				}
	    				break;
	    			case "sel":
	    			case "select":
	    				if(args.length != 2) {
	    					usageSelect(player);
	    				} else {
	    					ret = plugin.getReplayRepository(player).select(args[1]);
	    				}
	    				break;
	    			case "del":
	    			case "delete":
	    				if(args.length != 2) {
	    					usageDelete(player);
	    				} else {
	    					plugin.getReplayRepository(player).delete(args[1]);
	    				}
	    				break;
	    			case "steps":
	    				if(args.length != 1) {
	    					usageSteps(player);
	    				} else {
		    				List<Step> steps = plugin.getReplayRepository(player).steps();
		    				for (int i=0; i<steps.size(); i++) {
		    					sendText(player, i + ": " + steps.get(i).toString());
							}
		    			}
	    				break;
	    			case "addstep":
	    				if(args.length != 1) {
	    					usageAddstep(player);
	    				} else {
	    					ret = plugin.getReplayRepository(player).addStep(player.getLocation());
	    				}
	    				break;
	    			case "insertstep":
	    				if(args.length != 2) {
	    					usageInsertstep(player);
	    				} else {
	    					ret = plugin.getReplayRepository(player).insertStep(args[1], player.getLocation());
	    				}
	    				break;
	    			case "deletestep":
	    				if(args.length != 2) {
	    					usageDeletestep(player);
	    				} else {
	    					ret = plugin.getReplayRepository(player).deleteStep(args[1]);
	    				}
	    				break;
	    			case "next":
	    				if(args.length != 1) {
	    					usageNext(player);
	    				}
	    				ret = plugin.getReplayRepository(player).next();
	    				if(ret.isOk()) {
	    					transporter.moveTo(plugin.getReplayRepository(player).getCurrentStep());
	    				}
	    				break;
	    			case "prev":
	    			case "previous":
	    				if(args.length != 1) {
	    					usagePrevious(player);
	    				}
	    				ret = plugin.getReplayRepository(player).previous();
	    				if(ret.isOk()) {
	    					transporter.moveTo(plugin.getReplayRepository(player).getCurrentStep());
	    				}
	    				break;
	    			case "first":
	    				if(args.length != 1) {
	    					usageFirst(player);
	    				}
	    				ret = plugin.getReplayRepository(player).first();
	    				if(ret.isOk()) {
	    					transporter.moveTo(plugin.getReplayRepository(player).getCurrentStep());
	    				}
	    				break;
	    			case "back":
	    				if(args.length != 1) {
	    					usageBack(player);
	    				}
    					transporter.moveTo(plugin.getReplayRepository(player).getCurrentStep());
	    				break;
	    			default:
	    				usage(player);   		
	    		}
				if(ret != null) {
					if(!ret.isOk()) {
						sendError(player, ret.getMessage());
					} else {
						sendText(player, ret.getMessage());
					}
				}
				plugin.saveConfig();
    		}
    		return true;
    	}
    	return false;
	}

	private void sendError(CommandSender sender, String message) {
		sender.sendMessage(message);
		
	}

	private void sendText(CommandSender sender, String text) {
		sender.sendMessage(text);
	}

	private void usage(CommandSender sender) {
		sender.sendMessage("Replay help:");
		sender.sendMessage("------------");
		usageList(sender);
		usageNew(sender);
		usageSelect(sender);
		usageDelete(sender);
		usageSteps(sender);
		usageAddstep(sender);
		usageInsertstep(sender);
		usageDeletestep(sender);
		usageNext(sender);
		usagePrevious(sender);
		usageFirst(sender);
		usageBack(sender);
	} 
	
	private void usageList(CommandSender sender) {
		sender.sendMessage("/replay list");				
	}
	
	private void usageNew(CommandSender sender) {
		sender.sendMessage("/replay new <id>");		
	}

	private void usageBack(CommandSender sender) {
		sender.sendMessage("/replay back");
	}

	private void usageFirst(CommandSender sender) {
		sender.sendMessage("/replay first");
	}

	private void usagePrevious(CommandSender sender) {
		sender.sendMessage("/replay previous");
	}

	private void usageNext(CommandSender sender) {
		sender.sendMessage("/replay next");
	}

	private void usageDeletestep(CommandSender sender) {
		sender.sendMessage("/replay deletestep <id>");
	}

	private void usageInsertstep(CommandSender sender) {
		sender.sendMessage("/replay insertstep <id>");
	}

	private void usageAddstep(CommandSender sender) {
		sender.sendMessage("/replay addstep");
	}

	private void usageSteps(CommandSender sender) {
		sender.sendMessage("/replay steps");
	}

	private void usageDelete(CommandSender sender) {
		sender.sendMessage("/replay delete <id>");
	}

	private void usageSelect(CommandSender sender) {
		sender.sendMessage("/replay select <id>");
	}
}
