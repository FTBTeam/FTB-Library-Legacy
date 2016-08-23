package com.feed_the_beast.ftbl.api.security;

import com.latmod.lib.EnumNameMap;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.UUID;

/**
 * Created by LatvianModder on 12.07.2016.
 */
public enum ISecureStorage implements Capability.IStorage<ISecure>
{
    INSTANCE;

    @Override
    public NBTBase writeNBT(Capability<ISecure> capability, ISecure instance, EnumFacing side)
    {
        int flags = instance.getFlags();
        boolean saveOwner = (flags & ISecure.SAVE_OWNER) != 0;
        boolean saveLevel = (flags & ISecure.SAVE_PRIVACY_LEVEL) != 0;
        UUID ownerID = instance.getOwner();

        if(saveOwner && !saveLevel)
        {
            return new NBTTagString(ownerID == null ? "" : LMStringUtils.fromUUID(ownerID));
        }

        NBTTagCompound tag = new NBTTagCompound();

        if(saveOwner && ownerID != null)
        {
            tag.setString("Owner", LMStringUtils.fromUUID(ownerID));
        }

        if(saveLevel)
        {
            tag.setString("Level", EnumNameMap.getEnumName(instance.getPrivacyLevel()));
        }

        return tag;
    }

    @Override
    public void readNBT(Capability<ISecure> capability, ISecure instance, EnumFacing side, NBTBase nbt)
    {
        if(!(instance instanceof ISecureModifiable))
        {
            throw new RuntimeException("ISecure instance does not implement ISecureModifiable");
        }

        ISecureModifiable sm = (ISecureModifiable) instance;

        if(nbt instanceof NBTTagString)
        {
            sm.setOwner(((instance.getFlags() & ISecure.SAVE_OWNER) != 0) ? LMStringUtils.fromString(((NBTTagString) nbt).getString()) : null);
        }
        else
        {
            NBTTagCompound tag = (NBTTagCompound) nbt;
            sm.setOwner(tag.hasKey("Owner") ? LMStringUtils.fromString(tag.getString("Owner")) : null);
            sm.setPrivacyLevel((((instance.getFlags() & ISecure.SAVE_PRIVACY_LEVEL) != 0) && tag.hasKey("Level")) ? EnumPrivacyLevel.NAME_MAP.get(tag.getString("Level")) : EnumPrivacyLevel.PUBLIC);
        }
    }
}
