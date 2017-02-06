package com.feed_the_beast.ftbl.client.teamsgui;

import com.mojang.authlib.GameProfile;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class PlayerInst
{
    public final GameProfile profile;
    public final TeamInst team;

    public PlayerInst(GameProfile p, @Nullable TeamInst t)
    {
        profile = p;
        team = t;
    }
}