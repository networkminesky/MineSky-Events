package minesky.msne.events;

import minesky.msne.MineSkyEvents;
import minesky.msne.addons.Vault;
import minesky.msne.bot.MineSkyBot;
import minesky.msne.config.Config;
import minesky.msne.config.DataManager;
import minesky.msne.config.Locations;
import minesky.msne.utils.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TijolãoWarsEvent {
    private Map<Player, Integer> contagemRegressiva = new HashMap<>();
    public static boolean contagem;
    public static boolean contagemI = false;
    public static Set<Player> playerson = new HashSet<>();
    public static List<Player> mortos = new ArrayList<>();
    public static BukkitRunnable temporizador;
    public static void iniciarEvento() {
        MineSkyEvents.event = "TijolãoWars";
        Util.sendMessageBGMSNE("TijolãoWars");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("mineskyevents.bypass.join")) {
                Bukkit.dispatchCommand(player, "event entrar");
            }
        }
        temporizador = new BukkitRunnable() {
            int tempoRestante = 600;
            @Override
            public void run() {
                if (tempoRestante == 0) {
                    MineSkyEvents.event = "OFF";
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (Util.PDVE(player) || Util.PDVES(player)) {
                            Util.sendConectionBCMSNE(player);
                            File file = DataManager.getFile(player.getName().toLowerCase(), "playerdata");
                            FileConfiguration config = DataManager.getConfiguration(file);
                            config.set("Event", false);
                            config.set("EventSpect", false);
                            try {
                                config.save(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    TextComponent menorplayer = new TextComponent("§6§lTijolãoWars §8| §aInfelizmente o evento não teve §l4§a players para iniciar.");
                    Util.sendMessageBCMSNE(menorplayer);
                }
                tempoRestante--;
            }
        };
        temporizador.runTaskTimer(MineSkyEvents.get(), 0, 20);
    }
    public static void comtagemEvento() {
        if (!contagemI && playerson.size() >= 4) {
            temporizador.cancel();
            new BukkitRunnable() {
                int tempoRestante = 180;

                @Override
                public void run() {
                    contagemI = true;
                    contagem = true;
                    if (tempoRestante == 180 ||tempoRestante == 60 || tempoRestante == 30 || tempoRestante == 15 || tempoRestante == 10 || tempoRestante == 5 || tempoRestante == 4 || tempoRestante == 3 || tempoRestante == 2 || tempoRestante == 1) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (!Util.PDVE(player)) return;
                            if (tempoRestante == 180) {
                                player.sendTitle("§a3m", "", 10, 70, 20);
                            }
                            player.sendTitle(ChatColor.RED + String.valueOf(tempoRestante) + "s", "", 10, 70, 20);
                        }
                    }

                    if (tempoRestante == 0) {
                        contagem = false;
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (!Util.PDVE(p)) return;
                            p.teleport(Locations.tijolaoA, PlayerTeleportEvent.TeleportCause.COMMAND);
                            p.getInventory().removeItem(Util.BedLeave);
                            p.getInventory().removeItem(Util.Head);
                            ItemStack bricks = new ItemStack(Material.BRICK, 64);
                            PlayerInventory inventory = p.getInventory();

                            for (int i = 0; i < inventory.getSize(); i++) {
                                if (i == 40 || (i >= 36 && i <= 39)) {
                                    continue;
                                }
                                inventory.setItem(i, bricks.clone());
                            }
                            this.cancel();
                        }
                        this.cancel();
                    }

                    tempoRestante--;
                }
            }.runTaskTimer(MineSkyEvents.get(), 0, 20);
        }
    }
    public static void finalizar() {
        Player vencedor = playerson.stream()
                .filter(player -> !mortos.contains(player))
                .findFirst()
                .orElse(null);
        Player[] vencedores = mortos.stream()
                .sorted(Comparator.comparing(Player::getName).reversed()) // Classifique os jogadores mortos por nome em ordem reversa
                .limit(2)
                .toArray(Player[]::new);
        MineSkyEvents.event = "OFF";
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Util.PDVE(player) || Util.PDVES(player)) {
                Util.sendConectionBCMSNE(player);
                File file = DataManager.getFile(player.getName().toLowerCase(), "playerdata");
                FileConfiguration config = DataManager.getConfiguration(file);
                config.set("Event", false);
                config.set("EventSpect", false);
                try {
                    config.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            TextComponent encerrar = new TextComponent("§6§lTijolãoWars §8| §a1º Lugar §8- §a§l" + vencedor.getName() + " §8| §a2º Lugar §8- §a§l" + vencedores[0].getName() + " §8| §a3º Lugar §8- §a§l" + vencedores[1].getName());
            Util.sendMessageBCMSNE(encerrar);
            double premio1 = Math.random() * (5500 - 4500) + 4500;
            double premio2 = Math.random() * (3500 - 2500) + 2500;
            double premio3 = Math.random() * (2500 - 1500) + 1500;
            OfflinePlayer p1 = Bukkit.getOfflinePlayer(vencedor.getName());
            OfflinePlayer p2 = Bukkit.getOfflinePlayer(vencedores[0].getName());
            OfflinePlayer p3 = Bukkit.getOfflinePlayer(vencedores[1].getName());
            Vault.economy.depositPlayer(p1, premio1);
            Vault.economy.depositPlayer(p2, premio2);
            Vault.economy.depositPlayer(p3, premio3);
            TextComponent text1 = new TextComponent("§6§lTijolãoWars §8| §aVocê ganhou o §lTijolãoWars §ae como prêmio você ganhou: §l" + premio1);
            TextComponent text2 = new TextComponent("§6§lTijolãoWars §8| §aVocê ganhou o §lTijolãoWars §ae como prêmio você ganhou: §l" + premio2);
            TextComponent text3 = new TextComponent("§6§lTijolãoWars §8| §aVocê ganhou o §lTijolãoWars §ae como prêmio você ganhou: §l" + premio3);
            Util.sendPlayermessage(vencedor, text1);
            Util.sendPlayermessage(vencedores[0], text2);
            Util.sendPlayermessage(vencedores[1], text3);
            if (Config.Bot) {
                MineSkyBot.sendLogEvent("TijolãoWars", vencedor, vencedores, premio1, premio2, premio3);
            }
            playerson.clear();
            for (Player player1 : vencedores) {
                File file = DataManager.getFile(player1.getName().toLowerCase(), "playerdata");
                FileConfiguration config = DataManager.getConfiguration(file);
                File file2 = DataManager.getFile(vencedor.getName().toLowerCase(), "playerdata");
                FileConfiguration config2 = DataManager.getConfiguration(file2);

                config.set("Events.Tijolao.win", config.getInt("Events.Tijolao.win")+1);
                config2.set("Events.Tijolao.win", config.getInt("Events.Tijolao.win")+1);
                try {
                    config.save(file);
                    config2.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
