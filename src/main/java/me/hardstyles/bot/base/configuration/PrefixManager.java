package me.hardstyles.bot.base.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.hardstyles.bot.Bot;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PrefixManager {
    private HashMap<String, String> prefixes;

    private final Bot bot;
    public PrefixManager(Bot bot){
        this.bot = bot;
    }

    public void setPrefix(Guild g, String p) {
        prefixes.put(g.getId(), p);
        savePrefixes();
    }

    public String getPrefix(Guild g) {
        return prefixes.getOrDefault(g.getId(), ">");
    }

    public void loadPrefixes() {
        File tempFile = new File("guildSettings.json");
        boolean exists = tempFile.exists();
        if (!exists) {
            try {
                File file = new File("guildSettings.json");
                if (file.createNewFile()) {
                    System.out.println("New Guild Settings Created " + file.getName());
                } else {
                    System.out.println("Settings Already Exist");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Path path = Paths.get("guildSettings.json");
                Files.write(path, "{\"1\": \"/\"}".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File("guildSettings.json");
        HashMap<String, String> newPrefixes = null;
        try {
            newPrefixes = new Gson().fromJson(new FileReader(file), HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        prefixes = newPrefixes;
    }


    public void savePrefixes() {
        try {
            String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(prefixes);
            Path path = Paths.get("guildSettings.json");
            Files.write(path, jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
