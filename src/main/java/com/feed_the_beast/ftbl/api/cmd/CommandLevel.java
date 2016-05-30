package com.feed_the_beast.ftbl.api.cmd;

public enum CommandLevel
{
    ALL,
    OP;

    public static CommandLevel get(String s)
    {
        switch(s.toLowerCase())
        {
            case "all":
                return ALL;
            case "player":
                return ALL;
            case "op":
                return OP;
            case "admin":
                return OP;
            default:
                return null;
        }
    }

    public boolean isOP()
    {
        return this == OP;
    }

    public int requiredPermsLevel()
    {
        if(this == ALL)
        {
            return 0;
        }
        if(this == OP)
        {
            return 2;
        }
        return 0;
    }
}
