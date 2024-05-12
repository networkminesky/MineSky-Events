package minesky.msne;

import minesky.msne.addons.Vault;
import minesky.msne.commands.EventCommand;
import minesky.msne.commands.MSNECommand;
import minesky.msne.config.*;
import minesky.msne.enums.JarType;
import minesky.msne.enums.Version;
import minesky.msne.system.PlayerData;
import minesky.msne.system.PlayerInfo;
import minesky.msne.utils.Command;
import minesky.msne.utils.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

public final class MineSkyEvents extends JavaPlugin {

    public static JDA jda;
    private static MineSkyEvents mineSkyEvents;
    public static FileConfiguration Blacklist;
    public static String event = "OFF";
    private static Version version;
    private static JarType jarType;
    // private String palavraParaCompetir;
  //  private static final List<String> eventos = Arrays.asList("Spleef", "TijolãoWars", "Runners", "Corrida", "Corrida de barco", "Pega Bandeira", "TNTRUN");
   // private static final Random random = new Random();
    //private static int eventosIniciadosHoje = 0;
   // private static LocalDate ultimoDia = LocalDate.parse(Config.UltimoDia);

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §7-------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §7Initializing plugin...");
        enablePlugin();
        gerarConfigs();
        carregarConfigs();
        Commands();
        System();
        Bot();
        Vault.setupEconomy();
        Util.BedLeave = Util.createBed();
        Util.StoneShovel = Util.shovel();
        Util.SumoItem = Util.sumo();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "custom:msne");
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §aConfig loaded.");
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §aPlugin successfully initialized!");
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §7-------------------------------------------------");
    }

    private void enablePlugin() {
        mineSkyEvents = this;
        version = Version.getServerVersion();
        jarType = JarType.getJarType();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §7-------------------------------------------------");
        if (Config.Bot) {
            jda.shutdown();
            Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §cBOT DISCORD successfully disabled!");
            /*try {
                jda.awaitShutdown();
                Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §cBOT DISCORD successfully disabled!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        } else {
            Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §cBOT DISCORD successfully disabled!");
        }
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §cPlugin successfully disabled!");
        Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §7-------------------------------------------------");
    }

    private void Bot() {
        if (Config.Bot) {
            jda = JDABuilder.createLight(Config.Token, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                    .build();
            try {
                jda.awaitReady();
                Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §aBOT DISCORD successfully initialized!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("§8[§6MineSky Events§8] §cBOT DISCORD successfully disabled!");
        }
    }

    private void Commands() {
        new Command("msne", new MSNECommand());
        new Command("event", new EventCommand());
    }

    private void System() {
        Bukkit.getConsoleSender().sendMessage("§8[§6Minesky Events§8] §7Identified server version: " + jarType + " " + version.toString().replace("_", ".").replace("v", ""));
        if (!isRecomendedVersion()) {
            getServer().getConsoleSender().sendMessage("§8[§6Minesky Events§8] §cAttention! You are using a version of Minecraft that does not support all the features of MineSky-Events!");
        }
        PluginManager system = Bukkit.getServer().getPluginManager();
        system.registerEvents(new PlayerData(), this);
        system.registerEvents(new PlayerInfo(), this);
    }

    private void gerarConfigs() {
        DataManager.createFolder("playerdata");
        ConfigManager.createConfig("config.yml");
        ConfigManager.createConfig("commands.yml");
        ConfigManager.createConfig("messages.yml");
        ConfigManager.createConfig("locations.yml");
    }
    public static void carregarConfigs() {
        Messages.loadMessages();
        Config.loadConfig();
        Locations.loadLocations();
        Blacklist = ConfigManager.getConfig("config.yml");
    }


    static boolean isRecomendedVersion() {
        if (version == Version.v1_17)
            return true;
        if (version == Version.v1_18)
            return true;
        if (version == Version.v1_19)
            return true;
        if (version == Version.v1_20)
            return true;
        return false;
    }
    public static JarType getTypeJar() {
        return jarType;
    }

    public static Version getVersion() {
        return version;
    }

    public static MineSkyEvents get() {
        return (MineSkyEvents) Bukkit.getServer().getPluginManager().getPlugin("MineSky-Events");
    }
}