/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.duckchat.command;

import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cyberiantiger.minecraft.duckchat.Main;
import org.cyberiantiger.minecraft.duckchat.PlayerState;

/**
 *
 * @author antony
 */
public class ChannelListSubCommand extends SubCommand {

    public ChannelListSubCommand(Main plugin) {
        super(plugin);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String... args) {
        if (args.length == 1) {
            return plugin.getChannelCompletions(args[0]);
        } else {
            return null;
        }

    }

    @Override
    public void onCommand(CommandSender sender, String... args) throws SubCommandException {
        if (!sender.hasPermission("duckchat.channellist")) {
            throw new PermissionException("duckchat.channellist");
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerState state = plugin.getPlayerState(player);
            String channel;
            if (args.length == 0) {
                channel = state.getCurrentChannel();
                if (!plugin.getAvailableChannels(player).contains(channel)) {
                    player.sendMessage(plugin.translate("channellist.notfound", channel));
                    return;
                }
            } else if (args.length == 1) {
                channel = findIgnoringCase(args[0], plugin.getAvailableChannels(player));
                if (channel == null) {
                    player.sendMessage(plugin.translate("channellist.notfound", args[0]));
                    return;
                }
            } else {
                throw new UsageException();
            }
            List<String> members = plugin.getMembers(channel);
            Collections.sort(members);
            StringBuilder memberList = new StringBuilder();
            for (int i = 0; i < members.size(); i++) {
                if (i != 0) {
                    memberList.append(", ");
                }
                memberList.append(members.get(i));
            }
            player.sendMessage(plugin.translate("channellist.header", channel));
            player.sendMessage(memberList.toString());
        } else {
            throw new SenderTypeException();
        }
    }

    @Override
    public String getName() {
        return "channellist";
    }
    
}