package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IPackMode;
import com.feed_the_beast.ftbl.api.ISharedData;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class SharedData implements ISharedData, IJsonSerializable
{
    private final Side side;
    private IPackMode currentMode;
    private UUID universeID;

    public SharedData(Side s)
    {
        side = s;
    }

    @Override
    public Side getSide()
    {
        return side;
    }

    @Override
    public IPackMode getPackMode()
    {
        if(currentMode == null)
        {
            currentMode = side.isClient() ? new PackMode("default") : FTBLibAPI_Impl.INSTANCE.getPackModes().getDefault();
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

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("M", getPackMode().getID());
        nbt.setLong("ID_M", getUniverseID().getMostSignificantBits());
        nbt.setLong("ID_L", getUniverseID().getLeastSignificantBits());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        currentMode = new PackMode(nbt.getString("M"));
        universeID = new UUID(nbt.getLong("ID_M"), nbt.getLong("ID_L"));
    }

    public final int setMode(String mode)
    {
        IPackMode m = FTBLibAPI_Impl.INSTANCE.getPackModes().getRawMode(mode);

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
    public void fromJson(JsonElement json)
    {
        JsonObject group = json.getAsJsonObject();
        universeID = group.has("world_id") ? LMStringUtils.fromString(group.get("world_id").getAsString()) : null;
        currentMode = group.has("mode") ? FTBLibAPI_Impl.INSTANCE.getPackModes().getMode(group.get("mode").getAsString()) : FTBLibAPI_Impl.INSTANCE.getPackModes().getDefault();
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