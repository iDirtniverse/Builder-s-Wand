package de.False.BuildersWand;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.manager.WandManager;
import de.False.BuildersWand.utilities.MessageUtil;
import de.False.BuildersWand.utilities.UUIDItemTagType;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Commands implements CommandExecutor {
	
	private Main plugin;
    private Config config;
    private WandManager wandManager;

    Commands(Main plugin, Config config, WandManager wandManager) {
        this.plugin = plugin;
        this.config = config;
        this.wandManager = wandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length < 1) {
            helpCommand(sender);
            return true;
        }

        switch (args[0]) {
            case "reload":
                reloadCommand(sender);
                break;
            case "give":
                giveCommand(sender, args);
                break;
            default:
                helpCommand(sender);
        }

        return true;
    }

    private void reloadCommand(CommandSender player) {
        if (player instanceof Player && !player.hasPermission("buildersWand.reload")) {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }

        MessageUtil.sendMessage(player, "reload");
        config.load();
        wandManager.load();
    }

    private void giveCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("buildersWand.give")) {
            MessageUtil.sendMessage(sender, "noPermissions");
            return;
        }
        Wand wand;
        Player destPlayer;

        if (args.length < 1) {
            helpCommand(sender);
            return;
        } else if (args.length == 1 && sender instanceof Player) {
            wand = wandManager.getWandTier(1);
            destPlayer = (Player) sender;

        } else if (args.length == 2) {
            destPlayer = Bukkit.getPlayer(args[1]);
            wand = wandManager.getWandTier(1);
        } else {
            wand = wandManager.getWandTier(Integer.parseInt(args[2]));
            destPlayer = Bukkit.getPlayer(args[1]);
        }

        if (destPlayer == null) {
            MessageUtil.sendMessage(sender, "playerNotFound");
            return;
        } else if (wand == null) {
            MessageUtil.sendMessage(sender, "wandNotFound");
            return;
        }

        ItemStack itemStack = wand.getRecipeResult();
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "uuid");
        itemMeta.getCustomTagContainer().setCustomTag(key, new UUIDItemTagType(), UUID.randomUUID());
        itemStack.setItemMeta(itemMeta);
        destPlayer.getInventory().addItem(itemStack);
    }

    private void helpCommand(CommandSender player) {
        MessageUtil.sendSeparator(player);
        MessageUtil.sendRawMessage(player, "             &b&lBuildersWand help");
        player.sendMessage("");
        MessageUtil.sendRawMessage(player, "&e&l»&r&e /bw reload &7- Reloads the config file.");
        MessageUtil.sendRawMessage(player, "&e&l»&r&e /bw give <player> &7- Give the builderswand tier 1 to a player.");
        MessageUtil.sendRawMessage(player, "&e&l»&r&e /bw give <player> <tier> &7- Give the builderswand tier X to a player.");
        MessageUtil.sendSeparator(player);
    }
}
