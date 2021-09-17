package me.hardstyles.bot.base.commands.impl;

public enum Category {
    ADMIN("Administrator"),
    MUSIC("Music"),
    OTHER("Other");

    private String name;

    Category(String name) {
        this.name = name;
    }
}
