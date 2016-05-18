package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.events.ForgePlayerEvent;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public abstract class ForgePlayer implements Comparable<ForgePlayer>, ICapabilityProvider
{
    private GameProfile gameProfile;
    public final Collection<UUID> friends;
    public final Map<EntityEquipmentSlot, ItemStack> lastArmor;
    CapabilityDispatcher capabilities;
    
    ForgePlayer(GameProfile p)
    {
        setProfile(p);
        friends = new HashSet<>();
        lastArmor = new HashMap<>();
    }
    
    public void init()
    {
        ForgePlayerEvent.AttachCapabilities event = new ForgePlayerEvent.AttachCapabilities(this);
        MinecraftForge.EVENT_BUS.post(event);
        capabilities = !event.getCapabilities().isEmpty() ? new CapabilityDispatcher(event.getCapabilities(), null) : null;
    }
    
    @Override
    public final boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing)
    {
        return capabilities != null && capabilities.hasCapability(capability, facing);
    }
    
    @Override
    public final <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
    {
        return capabilities == null ? null : capabilities.getCapability(capability, facing);
    }
    
    public abstract Side getSide();
    public abstract boolean isOnline();
    public abstract EntityPlayer getPlayer();
    public abstract ForgePlayerMP toPlayerMP();
    
    @SideOnly(Side.CLIENT)
    public abstract ForgePlayerSP toPlayerSP();
    
    public abstract ForgeWorld getWorld();
    
    public final void setProfile(GameProfile p)
    {
        if(p != null)
        {
            gameProfile = new GameProfile(p.getId(), p.getName());
        }
    }
    
    public final GameProfile getProfile()
    { return gameProfile; }
    
    public final String getStringUUID()
    { return LMUtils.fromUUID(gameProfile.getId()); }
    
    public boolean isFriendRaw(ForgePlayer p)
    { return p != null && (equalsPlayer(this) || friends.contains(p.getProfile().getId())); }
    
    public boolean isFriend(ForgePlayer p)
    { return p != null && isFriendRaw(p) && p.isFriendRaw(this); }
    
    @Override
    public final int compareTo(ForgePlayer o)
    { return getProfile().getName().compareToIgnoreCase(o.getProfile().getName()); }
    
    @Override
    public final String toString()
    { return gameProfile.getName(); }
    
    @Override
    public final int hashCode()
    { return gameProfile.getId().hashCode(); }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == null) { return false; }
        else if(o == this) { return true; }
        else if(o instanceof UUID) { return gameProfile.getId().equals(o); }
        else if(o instanceof ForgePlayer) { return equalsPlayer((ForgePlayer) o); }
        return equalsPlayer(getWorld().getPlayer(o));
    }
    
    public boolean equalsPlayer(ForgePlayer p)
    { return p != null && (p == this || gameProfile.getId().equals(p.gameProfile.getId())); }
    
    public Collection<ForgePlayer> getFriends()
    {
        Collection<ForgePlayer> c = new HashSet<>();
        
        for(UUID id : friends)
        {
            ForgePlayer p = getWorld().getPlayer(id);
            
            if(p != null)
            {
                c.add(p);
            }
        }
        
        return c;
    }
    
    public Collection<ForgePlayer> getOtherFriends()
    {
        Collection<ForgePlayer> c = new HashSet<>();
        
        for(ForgePlayer p : getWorld().playerMap.values())
        {
            if(!p.equalsPlayer(this) && p.isFriendRaw(this))
            {
                c.add(p);
            }
        }
        
        return c;
    }
    
    public boolean isMCPlayer()
    { return false; }
    
    public void updateArmor()
    {
        if(getSide().isServer() && isOnline())
        {
            lastArmor.clear();
            EntityPlayer ep = getPlayer();
            
            for(EntityEquipmentSlot e : EntityEquipmentSlot.values())
            {
                ItemStack is = ep.getItemStackFromSlot(e);
                
                if(is != null)
                {
                    lastArmor.put(e, is.copy());
                }
            }
        }
    }
    
    public void onLoggedIn(boolean firstTime)
    {
        updateArmor();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.LoggedIn(this, firstTime));
    }
    
    public void onLoggedOut()
    {
        updateArmor();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.LoggedOut(this));
    }
    
    public void onDeath()
    {
        updateArmor();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.OnDeath(this));
    }
    
    public void sendUpdate()
    {
    }
}