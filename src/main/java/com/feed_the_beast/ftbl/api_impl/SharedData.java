package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.api.IPackMode;
import com.feed_the_beast.ftbl.api.ISharedData;
import com.feed_the_beast.ftbl.lib.reg.ResourceLocationIDRegistry;
import com.feed_the_beast.ftbl.lib.reg.StringIDRegistry;
import com.feed_the_beast.ftbl.lib.util.LMServerUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class SharedData implements ISharedData, IJsonSerializable
{
    public static final SharedData SERVER = new SharedData(Side.SERVER);
    public static final SharedData CLIENT = new SharedData(Side.CLIENT);

    public static boolean hasServer, isClientPlayerOP, useFTBPrefix;
    public static GameProfile clientGameProfile;

    private final Side side;
    public IPackMode currentMode;
    public UUID universeID;
    public final Collection<String> optionalServerMods;
    private final StringIDRegistry configIDs;
    public final ResourceLocationIDRegistry guiIDs;
    public final StringIDRegistry notificationIDs;

    private SharedData(Side s)
    {
        side = s;
        currentMode = null;
        universeID = null;
        optionalServerMods = new HashSet<>();
        configIDs = new StringIDRegistry();
        guiIDs = new ResourceLocationIDRegistry();
        notificationIDs = new StringIDRegistry();
    }

    @Override
    public Side getSide()
    {
        return side;
    }

    public void reset()
    {
        currentMode = null;
        universeID = null;
        optionalServerMods.clear();

        if(side == Side.CLIENT)
        {
            hasServer = false;
            isClientPlayerOP = false;
            useFTBPrefix = false;
        }
    }

    @Override
    public IPackMode getPackMode()
    {
        if(currentMode == null)
        {
            currentMode = side.isClient() ? new PackMode("default") : FTBLibIntegrationInternal.API.getPackModes().getDefault();
        }

        return currentMode;
    }

    @Override
    public UUID getUniverseID()
    {
        if(universeID == null || (universeID.getLeastSignificantBits() == 0L && universeID.getMostSignificantBits() == 0L))
        {
            universeID = UUID.randomUUID();
        }

        return universeID;
    }

    public int setMode(String mode)
    {
        IPackMode m = FTBLibIntegrationInternal.API.getPackModes().getRawMode(mode);

        if(m == null)
        {
            return 1;
        }
        if(m.equals(getPackMode()))
        {
            return 2;
        }

        currentMode = m;
        return 0;
    }

    @Override
    public boolean hasOptionalServerMod(@Nullable String id)
    {
        return (id == null || id.isEmpty()) ? hasServer : optionalServerMods.contains(id);
    }

    @Override
    public boolean isOP(@Nullable GameProfile profile)
    {
        if(side == Side.SERVER)
        {
            return profile != null && LMServerUtils.isOP(profile);
        }

        return isClientPlayerOP && (profile == null || profile == clientGameProfile || (clientGameProfile != null && profile.equals(clientGameProfile)));
    }

    @Override
    public boolean useFTBPrefix()
    {
        return side == Side.CLIENT ? useFTBPrefix : FTBLibConfig.USE_FTB_COMMAND_PREFIX.getBoolean();
    }

    @Override
    public StringIDRegistry getConfigIDs()
    {
        return configIDs;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        JsonObject group = json.getAsJsonObject();
        universeID = group.has("world_id") ? LMStringUtils.fromString(group.get("world_id").getAsString()) : null;
        currentMode = group.has("mode") ? FTBLibIntegrationInternal.API.getPackModes().getMode(group.get("mode").getAsString()) : FTBLibIntegrationInternal.API.getPackModes().getDefault();
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        o.add("world_id", new JsonPrimitive(LMStringUtils.fromUUID(getUniverseID())));
        o.add("mode", new JsonPrimitive(getPackMode().getID()));
        return o;
    }
}