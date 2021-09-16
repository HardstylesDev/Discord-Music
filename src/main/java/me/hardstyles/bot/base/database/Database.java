package me.hardstyles.bot.base.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Scanner;


public class Database {
    String[] details = getDetails();

    private String host = details[0]; // The IP-address of the database host.
    private String database = details[1]; // The name of the database.
    private String user = details[2]; // The name of the database user.
    private String pass = details[3]; // The password of the database user.


    private String jbdcUrl = String.format("jdbc:mysql://%s:3306/%s?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET", host, database);
    //Call the get connection method.
    private static DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    //Get the DataSource. If not available create the new one
    //It is not threadsafe. I didn't wanted to complicate things.
    public DataSource getDataSource() {
        if (null == dataSource) {
            System.out.println("No DataSource is available. We will create a new one.");
            createDataSource();
        }
        return dataSource;
    }

    //To create a DataSource and assigning it to variable dataSource.
    public void createDataSource() {
        HikariConfig hikariConfig = getHikariConfig();
        System.out.println("Configuration is ready.");
        System.out.println("Creating the HiakriDataSource and assigning it as the global");

        dataSource = new HikariDataSource(hikariConfig);
    }

    public HikariConfig getHikariConfig() {
        System.out.println("Creating the config with HikariConfig with maximum pool size of 5");
        HikariConfig hikaConfig = new HikariConfig();
        hikaConfig.setJdbcUrl(jbdcUrl);
        hikaConfig.setUsername(user);
        hikaConfig.setPassword(pass);
        hikaConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikaConfig.setPoolName("MysqlPool-1");
        hikaConfig.setMaximumPoolSize(5);
        hikaConfig.setConnectionTimeout(Duration.ofSeconds(30).toMillis());
        hikaConfig.setIdleTimeout(Duration.ofMinutes(2).toMillis());
        return hikaConfig;
    }

    private String[] getDetails() {
        String[] details = new String[4];
        try {
            File file = new File("Database.txt");
            Scanner scanner = new Scanner(file);
            if (!scanner.useDelimiter("\\A").hasNext()) {
                scanner.close();
                return details;
            }
            String content = scanner.useDelimiter("\\A").next();


            String[] contents = content.split("\n");
            details[0] = contents[0].substring(6);
            details[1] = contents[1].substring(10);
            details[2] = contents[2].substring(10);
            details[3] = contents[3].substring(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return details;
    }
}
