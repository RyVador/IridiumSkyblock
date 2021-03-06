package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command which creates a new island for an user.
 */
public class UnInviteCommand extends Command {

    /**
     * The default constructor.
     */
    public UnInviteCommand() {
        super(Collections.singletonList("uninvite"), "Revoke an invitation to your island", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Tries to create a new island for the user.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();
        User unInvitedUser = IridiumSkyblockAPI.getInstance().getUser(Bukkit.getServer().getOfflinePlayer(args[1]));
        if (island.isPresent()) {
            Optional<IslandInvite> islandInvite = IridiumSkyblock.getInstance().getIslandManager().getIslandInvite(island.get(), unInvitedUser);
            if (islandInvite.isPresent()) {
                IridiumSkyblock.getInstance().getDatabaseManager().deleteInvite(islandInvite.get());
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inviteRevoked.replace("%player%", unInvitedUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inviteDoesntExist.replace("%player%", unInvitedUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }

}
