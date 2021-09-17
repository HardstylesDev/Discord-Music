package me.hardstyles.bot.base.guild;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.guild.guildSettings.Setting;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GuildSettingsHandler {
    private final Bot bot;
    private final File file;
    private FileReader reader;

    public GuildSettingsHandler(@NotNull final Bot bot) {
        this.bot = bot;
        this.file = new File("GuildConfig.json");

        try {
            this.reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }

    }


    public void read() {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = content.length() > 0 ? new JsonParser().parse(content).getAsJsonObject() : new JsonObject();


        for (String s : jsonObject.keySet()) {

            JsonObject guildJson = jsonObject.get(s).getAsJsonObject();
            GuildObj obj = new GuildObj(bot, s);
            for (Setting setting : obj.getSettings()) {
                if (guildJson.has(setting.getName())) {
                    setting.set(guildJson.get(setting.getName()).getAsString());
                }
            }
            bot.getGuildManager().getNewGuilds().put(obj.getId(), obj);

        }

    }

    public void write() {
        JsonObject jsonObject = new JsonObject();
        for (GuildObj guild : bot.getGuildManager().getNewGuilds().values()) {
            JsonObject guildObject = new JsonObject();
            for (Setting setting : guild.getSettings()) {
                if (setting.defaultValue().equalsIgnoreCase(setting.get())) {
                    continue;
                }
                guildObject.addProperty(setting.getName(), setting.get());
            }
            jsonObject.add(guild.getId(), guildObject);
        }
        System.out.println(jsonObject);
        try {
            String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
            Path path = Paths.get(file.getPath());
            Files.write(path, jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
