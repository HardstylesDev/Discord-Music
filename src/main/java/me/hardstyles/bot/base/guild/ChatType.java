package me.hardstyles.bot.base.guild;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatType {
    DEFAULT,
    SIMPLE,
    REACTION;

    private static final Map<String, ChatType> nameToValueMap =
            new HashMap<String, ChatType>();

    static {
        for (ChatType value : EnumSet.allOf(ChatType.class)) {
            nameToValueMap.put(value.name(), value);
        }
    }

    public static ChatType fromName(String name) {
        return nameToValueMap.get(name);
    }

}
