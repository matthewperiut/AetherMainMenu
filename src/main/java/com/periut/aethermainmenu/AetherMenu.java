package com.periut.aethermainmenu;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.world.storage.WorldSaveInfo;
import net.minecraft.world.storage.WorldStorageSource;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class AetherMenu {

    //public static final Namespace MODID = Namespace.resolve();
    @Entrypoint.Namespace
    public static Namespace MODID = Null.get();

//    @Entrypoint.Logger
//    public static final Logger LOGGER = Null.get();

    // Identifiers
    public static Identifier modular;

    // Utility fields
    public static boolean modmenu;
    public static boolean gambac;
    public static boolean captureSoundId;
    public static String musicId;
    public static long musicStartTimestamp;
    public static boolean needsPlayerCreation;
    public static boolean shouldWorldLoad = true;
    public static boolean replaceBgTile = true;
    public static boolean renderPlayer = true;
    public static boolean hideSettingsButton = false;

    public static boolean visibleWorldButton = false;

    public static String lastLevel;
    public static String toolTip = "";

    @EventListener
    public static void init(InitEvent event)
    {
        if (FabricLoader.getInstance().isModLoaded("modmenu"))
            modmenu = true;

        if (FabricLoader.getInstance().isModLoaded("gambac"))
            gambac = true;

        modular = Identifier.of(MODID, "mainmenu.aether");

        CreateConfigFolder();
        AttemptLoadingSettings();
        AttemptLoadingLastLevel();
        AttemptLoadingConfig();
    }

    private static void CreateConfigFolder()
    {
        String filePath = FabricLoader.getInstance().getConfigDir().toString() + "/aether/";
        File folder = new File(filePath);

        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public static void SaveCurrentSettings()
    {
        String filePath = FabricLoader.getInstance().getConfigDir().toString() + "/aether/settings.txt";
        try {
            File file = new File(filePath);
            String fileContent  = "P " + renderPlayer + "\nW " + shouldWorldLoad + "\nT " + replaceBgTile;
            
            file.getParentFile().mkdirs();
            
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void AttemptLoadingSettings()
    {
        String filePath = FabricLoader.getInstance().getConfigDir().toString() + "/aether/settings.txt";
        File file = new File(filePath);

        // Check if the file exists and read it first
        if (file.exists())
        {
            try {
                List<String> lines = Files.readAllLines(Path.of(filePath));
                for (String line : lines) {

                    if (line.length() < 2)
                        continue;

                    switch (line.charAt(0)) {
                        case 'P':
                            renderPlayer = line.charAt(2) == 't';
                            break;
                        case 'W':
                            shouldWorldLoad = line.charAt(2) == 't';
                            break;
                        case 'T':
                            replaceBgTile = line.charAt(2) == 't';
                            break;
                        default:
                            System.out.println("Invalid line in settings.txt: " + line);
                            break;
                    }
                }
            } catch (IOException e) {
                shouldWorldLoad = false;
            }
        }
        else
        {
            // File doesn't exist, create it with current settings
            SaveCurrentSettings();
        }
    }

    public static void SaveLastLevel()
    {
        String filePath = FabricLoader.getInstance().getConfigDir().toString() + "/aether/lastsave.txt";
        try {
            File file = new File(filePath);
            
            file.getParentFile().mkdirs();
            
            FileWriter writer = new FileWriter(file);
            writer.write(lastLevel != null ? lastLevel : "");
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println("An error occurred saving last level: " + e.getMessage());
        }
    }

    public static void AttemptLoadingLastLevel()
    {
        Path configFolder = FabricLoader.getInstance().getConfigDir();
        Path lastsave = Path.of(configFolder.toString() + "/aether/lastsave.txt");
        try {
            List<String> lines = Files.readAllLines(lastsave);
            for (String line : lines) {
                lastLevel = line;
            }
        } catch (IOException e) {
            shouldWorldLoad = false;
        }
    }

    public static void SaveConfig()
    {
        String filePath = FabricLoader.getInstance().getConfigDir().toString() + "/aether/config.txt";
        try {
            File file = new File(filePath);
            String fileContent = "HideSettingsButton " + hideSettingsButton;
            
            file.getParentFile().mkdirs();
            
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println("An error occurred saving config: " + e.getMessage());
        }
    }

    private static void AttemptLoadingConfig()
    {
        String filePath = FabricLoader.getInstance().getConfigDir().toString() + "/aether/config.txt";
        File file = new File(filePath);

        if (file.exists())
        {
            try {
                List<String> lines = Files.readAllLines(Path.of(filePath));
                for (String line : lines) {
                    if (line.startsWith("HideSettingsButton ")) {
                        hideSettingsButton = line.substring("HideSettingsButton ".length()).equals("true");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading config file: " + e.getMessage());
            }
        }
        else
        {
            SaveConfig();
        }
    }

    public static void LoadWorld(Minecraft minecraft) {
        List worlds;
        WorldStorageSource var1 = minecraft.getWorldStorageSource();
        worlds = var1.getAll();

        // Check if there are worlds to load
        if (worlds.size() > 0)
            visibleWorldButton = true;
        else
        {
            visibleWorldButton = false;
            return;
        }

        boolean found = false;
        // Preventing from generating new worlds
        if (shouldWorldLoad)
        {
            for (Object world : worlds)
            {
                if (((WorldSaveInfo)world).getName().equals(lastLevel))
                {
                    found = true;
                    break;
                }
            }
        }

        // Load a world if previous world not found
        if (!found)
        {
            lastLevel = ((WorldSaveInfo)worlds.get(0)).getSaveName();
            SaveLastLevel();
        }

        if (minecraft.world == null && shouldWorldLoad)
        {
            minecraft.interactionManager = new SingleplayerInteractionManager(minecraft);

            if(lastLevel != null)
            {
                minecraft.interactionManager = new SingleplayerInteractionManager(minecraft);
                String name = lastLevel;//((LevelMetadata)worlds.get(0)).getFileName();
                minecraft.startGame(name, name, new Random().nextLong());
                SaveLastLevel();
            }
        }
    }

    public static void quickLoad(Minecraft minecraft)
    {
        minecraft.setScreen(null);
        minecraft.interactionManager = new SingleplayerInteractionManager(minecraft);
        minecraft.camera = minecraft.player;
    }

    public static void startOrStopMenuWorld(Minecraft minecraft)
    {
        if (minecraft.world == null)
        {
            // loading
            needsPlayerCreation = true;
            shouldWorldLoad = true;
            LoadWorld(minecraft);
        }
        else
        {
            // quitting
            shouldWorldLoad = false;
            if (minecraft.isWorldRemote())
            {

                minecraft.world.disconnect();
            }

            minecraft.setWorld(null);
            minecraft.setScreen(new TitleScreen());
        }
    }
}
