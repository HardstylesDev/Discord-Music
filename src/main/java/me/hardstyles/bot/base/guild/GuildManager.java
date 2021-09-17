package me.hardstyles.bot.base.guild;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.hardstyles.bot.Bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;


public class GuildManager {

    private HashSet<Guild> guilds;
    @Getter
    private HashSet<GuildObj> newGuilds;
    private final Bot bot;

    public GuildManager(Bot bot) {
        this.bot = bot;
        guilds = new HashSet<>();
        this.newGuilds = new HashSet<>();

    }

    public Guild getGuildFromId(String id) {
        for (Guild guild : guilds) {
            if (("" + guild.getGuildID()).equalsIgnoreCase(id))
                return guild;
        }
        return null;
    }

    public GuildObj getGuildObj(String id){
        for (GuildObj newGuild : newGuilds) {
            if(newGuild.getId().equalsIgnoreCase(id))
                return newGuild;
        }
        return null;
    }
    public void loadGuilds() {
        try {
            ArrayList<Long> checkedGuildIds = new ArrayList<>();

            Connection conn = bot.getDatabase().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM `data` WHERE id");
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                Guild newGuild = getEmptyGuildObject();
                JsonObject jsonObject = new JsonParser().parse(r.getString("data")).getAsJsonObject();
                newGuild.setGuildJson(jsonObject);
                newGuild.setGuildID(Long.parseLong(r.getString("id")));
                checkedGuildIds.add(Long.parseLong(r.getString("id")));
                this.addGuild(newGuild);
                this.guilds.add(newGuild);
            }
            r.close();
            ps.close();
            conn.close();

            for (net.dv8tion.jda.api.entities.Guild guild : bot.getShardManager().getGuilds()) {
                if (!checkedGuildIds.contains(guild.getIdLong())) {
                    System.out.println(guild.getName() + " is null");
                    createGuildInDatabase(guild);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void loadGuild(net.dv8tion.jda.api.entities.Guild guild) {
        try {
            this.removeGuild(getGuildFromId(guild.getId()));
            Connection conn = bot.getDatabase().getConnection();
            PreparedStatement ps = conn.prepareStatement(String.format("SELECT * FROM `data` WHERE id = \"%s\"", guild.getId()));
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                Guild newGuild = getEmptyGuildObject();
                JsonObject jsonObject = new JsonParser().parse(r.getString("data")).getAsJsonObject();
                newGuild.setGuildID(guild.getIdLong());
                newGuild.setGuildJson(jsonObject);
                this.addGuild(newGuild);
                System.out.println("added guild...");
            }
            r.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Guild getEmptyGuildObject() {
        return new Guild(0, null);
    }


    public ArrayList<Guild> getGuilds() {
        ArrayList<Guild> gs = new ArrayList<Guild>();
        for (Guild g : guilds) {
            gs.add(g);
        }
        return gs;
    }

    public void addGuild(Guild g) {
        guilds.add(g);
    }

    public void removeGuild(Guild g) {
        guilds.remove(g);
    }


    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void update(String uuid, JsonObject jsonObject) {
        try {
            Connection connection = bot.getDatabase().getConnection();
            String command = String.format("REPLACE INTO `data`(`id`, `data`) VALUES (?,?)");
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, gson.toJson(jsonObject));
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createGuildInDatabase(net.dv8tion.jda.api.entities.Guild guild) {

        try {
            Connection connection = bot.getDatabase().getConnection();
            PreparedStatement psa = connection.prepareStatement(String.format("INSERT INTO `data`(`id`, `data`) VALUES (\"%s\", \"%s\")", guild.getId(), "{}"));
            psa.execute();
            connection.close();
            psa.close();
        } catch (SQLException e) {
        }
        this.loadGuild(guild);
    }
}
