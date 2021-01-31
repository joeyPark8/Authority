package authority;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin implements Listener {
    List<String> myPermissions = new ArrayList<>();
    List<String> permissions = new ArrayList<>(Arrays.asList("everything", "enter", "exit",
            "setBlock", "breakBlock", "dropItem",
            "getItem", "shoot", "explode",
            "interact", "fly", "swim", "run"));

    HashMap<String, String> descrip = new HashMap<>(Map.of("everything", "모든 권한",
                                                           "enter", "들어가기",
                                                           "exit", "나가기",
                                                           "setBlock", "블럭 설치하기",
                                                           "breakBlock", "블럭 부수기",
                                                           "dropItem", "아이템 버리기",
                                                           "getItem", "아이템 줍기",
                                                           "shoot", "발사체 쏘기",
                                                           "explode", "폭발시키기",
                                                          "interact", "상호작용 하기"));

    @Override
    public void onEnable() {
        System.out.println("Authority is activated");

        getCommand("access").setExecutor(this::onCommand);
        getCommand("access").setTabCompleter(this::onTabComplete);

        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        System.out.println("Authority is deactivated");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("access")) { // /<command> <mode1>0 <world>1 <target>2 <mode2>3 <role>4
            if (args[0].equalsIgnoreCase("modify")) {
                if (args[3].equalsIgnoreCase("add")) {
                    if (found(permissions, args[4])) {
                        if (!found(myPermissions, args[4])) {
                            myPermissions.add(args[4]);
                            sender.sendMessage("[" + args[1] + "]에 있는 [" + args[2] + "]에게 [" + args[4] + "] 권한을 추가 했습니다");
                            return false;
                        }
                        else {
                            sender.sendMessage(ChatColor.YELLOW + "[" + args[2] + "]는 이미 [" + args[4] + "] 권한을 가지고 있습니다");
                            return false;
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "[" + args[4] + "] 권한은 존재하지 않습니다");
                        return false;
                    }
                }
                else if (args[3].equalsIgnoreCase("remove")) {
                    if (found(permissions, args[4])) {
                        if (found(myPermissions, args[4])) {
                            myPermissions.remove(args[4]);
                            sender.sendMessage("[" + args[1] + "]에 있는 [" + args[2] + "]에게 [" + args[4] + "] 권한을 제거 했습니다");
                            return false;
                        }
                        else {
                            sender.sendMessage(ChatColor.YELLOW + "[" + args[2] + "]는 [" + args[4] + "] 권한이 없습니다");
                            return false;
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "[" + args[4] + "] 권한은 존재하지 않습니다");
                        return false;
                    }
                }
                else {
                    sender.sendMessage("존재 하지 않는 argument 입니다");
                    return false;
                }
            }
            else if (args[0].equalsIgnoreCase("show")) {
                if (args[1].equalsIgnoreCase("@local")) {
                    sender.sendMessage("[" + args[1] + "]이 가지고 있는 권한들");
                    for (String i : myPermissions) {
                        sender.sendMessage(i + " : " + descrip.get(i));
                    }
                    return false;
                }
                else if (args[1].equalsIgnoreCase("@random")) {
                    return false;
                }


            }
            else {
                return false;
            }
        }
        else if (command.getName().equalsIgnoreCase("protect")) {
            return false;
        }
        else {
            return false;
        }
        return true;
    }

    boolean found(List<String> array, String str) {
        for (String i : array) {
            if (i.equals(str)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("access")) {
            if (args.length == 1) {
                List<String> mode = new ArrayList<>();
                mode.add("modify");
                mode.add("show");

                List<String> result = new ArrayList<>();
                for (String a : mode) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(a);
                    }
                }

                return result;
            }
            if (args.length == 2) {
                List<String> worldTypes = new ArrayList<>();
                worldTypes.add("world:overworld");
                worldTypes.add("world:nether");
                worldTypes.add("world:end");

                List<String> result = new ArrayList<>();
                for (String a : worldTypes) {
                    if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                        result.add(a);
                    }
                }

                return result;
            }
            if (args.length == 3) {
                List<String> targets = new ArrayList<>();
                Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                Bukkit.getServer().getOnlinePlayers().toArray(players);

                for (Player i : players) {
                    targets.add(i.getName());
                }

                targets.add("@all");
                targets.add("@local");
                targets.add("@random");

                List<String> result = new ArrayList<>();
                for (String a : targets) {
                    if (a.toLowerCase().startsWith(args[2].toLowerCase())) {
                        result.add(a);
                    }
                }

                return result;
            }
            if (args.length == 4) {
                List<String> mode = new ArrayList<>();
                mode.add("add");
                mode.add("remove");

                List<String> result = new ArrayList<>();
                for (String a : mode) {
                    if (a.toLowerCase().startsWith(args[3].toLowerCase())) {
                        result.add(a);
                    }
                }

                return result;
            }
            if (args.length == 5) {
                List<String> accessTypes = new ArrayList<>();
                accessTypes.add("everything");
                accessTypes.add("enter");
                accessTypes.add("exit");
                accessTypes.add("setBlock");
                accessTypes.add("breakBlock");
                accessTypes.add("dropItem");
                accessTypes.add("getItem");
                accessTypes.add("shoot");
                accessTypes.add("explode");
                accessTypes.add("interact");
                accessTypes.add("fly");
                accessTypes.add("swim");
                accessTypes.add("run");

                List<String> result = new ArrayList<>();
                for (String a : accessTypes) {
                    if (a.toLowerCase().startsWith(args[4].toLowerCase())) {
                        result.add(a);
                    }
                }

                return result;
            }
        }
        return null;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().isOp() && !found(myPermissions, "setBlock")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.getPlayer().isOp() && !found(myPermissions, "breakBlock")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!e.getPlayer().isOp() && !found(myPermissions, "dropItem")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (!e.getPlayer().isOp() && !found(myPermissions, "getItem")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            if (!e.getEntity().isOp() && !found(myPermissions, "shoot")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.PRIMED_TNT) {
            if (!e.getEntity().isOp() && !found(myPermissions, "explode")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().isOp() && !found(myPermissions, "interact")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRun(PlayerToggleSprintEvent e) {
        if (!e.getPlayer().isOp() && !found(myPermissions, "run")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        if (!e.getPlayer().isOp() && !found(myPermissions, "fly")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntitySwim(EntityToggleSwimEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            if (e.getEntity().isOp() && !found(myPermissions, "swim")) {
                e.setCancelled(true);
            }
        }
    }
}
