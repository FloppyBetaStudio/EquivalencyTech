package io.github.sefiraat.equivalencytech.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import io.github.sefiraat.equivalencytech.EquivalencyTech;
import io.github.sefiraat.equivalencytech.misc.Utils;
import io.github.sefiraat.equivalencytech.configuration.ConfigMain;
import io.github.sefiraat.equivalencytech.statics.ContainerStorage;
import io.github.sefiraat.equivalencytech.statics.Messages;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("EquivalencyTech|ET")
@Description("EquivalencyTech Main")
public class Commands extends BaseCommand {

    private final EquivalencyTech plugin;

    public EquivalencyTech getPlugin() {
        return plugin;
    }

    public Commands(EquivalencyTech plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage(Messages.msgCmdSubcommand(plugin));
        }
    }

    @Subcommand("ItemEmc")
    @Description("Displays the EMC value for the held item.")
    public class ItemEmc extends BaseCommand {

        @Default
        public void onDefault(CommandSender sender) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack i = player.getInventory().getItemInMainHand();
                SlimefunItem sfItem = null;
                if (EquivalencyTech.getInstance().getManagerSupportedPlugins().isInstalledSlimefun()) {
                    sfItem = SlimefunItem.getByItem(i);
                }
                if (sfItem != null) {
                    if (plugin.getEmcDefinitions().getEmcSlimefun().containsKey(sfItem.getId())) {
                        player.sendMessage(Messages.msgCmdEmcDisplay(sfItem.getId(), Utils.getEMC(plugin, i)));
                        player.sendMessage(Messages.msgCmdEmcDisplayStack(sfItem.getId(), i.getAmount(), Utils.getEMC(plugin, i) * i.getAmount()));
                    } else {
                        player.sendMessage(Messages.msgCmdEmcNone(plugin));
                    }
                    return;
                }
                if (i.getType() != Material.AIR) {
                    if (ContainerStorage.isCraftable(i, plugin)) {
                        if (plugin.getEmcDefinitions().getEmcEQ().containsKey(i.getItemMeta().getDisplayName())) {
                            player.sendMessage(Messages.msgCmdEmcDisplay(i.getItemMeta().getDisplayName(), Utils.getEMC(plugin, i)));
                            player.sendMessage(Messages.msgCmdEmcDisplayStack(i.getItemMeta().getDisplayName(), i.getAmount(), Utils.getEMC(plugin, i) * i.getAmount()));
                        } else {
                            player.sendMessage(Messages.msgCmdEmcNone(plugin));
                        }
                        return;
                    }
                    if (plugin.getEmcDefinitions().getEmcExtended().containsKey(i.getType())) {
                        player.sendMessage(Messages.msgCmdEmcDisplay(i.getType(), Utils.getEMC(plugin, i)));
                        player.sendMessage(Messages.msgCmdEmcDisplayStack(i.getType(), i.getAmount(), Utils.getEMC(plugin, i) * i.getAmount()));
                    } else {
                        player.sendMessage(Messages.msgCmdEmcNone(plugin));
                    }
                } else {
                    player.sendMessage(Messages.msgCmdEmcMustHold(plugin));
                }
            }
        }
    }

    @Subcommand("Emc")
    @Description("Displays the player's emc.")
    public class Emc extends BaseCommand {

        @Default
        public void onDefault(CommandSender sender) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(Messages.messageCommandEmc(plugin, player));
            }
        }
    }

    @Subcommand("GiveItem")
    @CommandPermission("EquiTech.Admin")
    @Description("Gives Debug Items")
    public class GiveItem extends BaseCommand {

        @Default
        public void onDefault(CommandSender sender) {
            if (sender instanceof Player) {
                sender.sendMessage(Messages.messageCommandSelectItem(plugin));
            }
        }

        @Subcommand("TransmutationOrb")
        @CommandCompletion("@players")
        public void onGiveItemOrb(CommandSender sender, OnlinePlayer player) {
            if (sender instanceof Player) {
                Utils.givePlayerOrb(plugin, player.getPlayer());
            }
        }

        @Subcommand("DissolutionChest")
        @CommandCompletion("@players")
        public void onGiveItemDChest(CommandSender sender, OnlinePlayer player) {
            if (sender instanceof Player) {
                Utils.givePlayerDChest(plugin, player.getPlayer());
            }
        }

        @Subcommand("CondensateChest")
        @CommandCompletion("@players")
        public void onGiveItemCChest(CommandSender sender, OnlinePlayer player) {
            if (sender instanceof Player) {
                Utils.givePlayerCChest(plugin, player.getPlayer());
            }
        }
    }


    @Subcommand("manEMC")
    @CommandPermission("EquiTech.Admin")
    @Description("EMC Management")
    public class GiveEMC extends BaseCommand {

        @Subcommand("addEMC")
        @CommandCompletion("* @amount")
        public void onAddEMC(CommandSender sender, OnlinePlayer player, double amount) {
            if (sender instanceof Player) {
                if(ConfigMain.getPlayerEmc(plugin, player.getPlayer())+amount > 0){
                    ConfigMain.addPlayerEmc(plugin, player.getPlayer().getUniqueId().toString(), new Double(amount));
                    sender.sendMessage("操作完成");
                }else{
                    sender.sendMessage("SUM结果小于0，停止操作");
                }
            }
        }

        @Subcommand("lookupEMC")
        @CommandCompletion("*")
        public void onAddEMC(CommandSender sender, OnlinePlayer player) {
            if (sender instanceof Player) {
                sender.sendMessage("EMC余额:"+toString(ConfigMain.getPlayerEmc(plugin, player.getPlayer())));
                }
            }
        }
    }

}