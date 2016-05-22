package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.events.ForgePlayerEvent;
import com.feed_the_beast.ftbl.api.item.LMInvUtils;
import com.feed_the_beast.ftbl.api.notification.ClickAction;
import com.feed_the_beast.ftbl.api.notification.ClickActionType;
import com.feed_the_beast.ftbl.api.notification.MouseAction;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.net.MessageLMPlayerDied;
import com.feed_the_beast.ftbl.net.MessageLMPlayerInfo;
import com.feed_the_beast.ftbl.net.MessageLMPlayerLoggedIn;
import com.feed_the_beast.ftbl.net.MessageLMPlayerUpdate;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.feed_the_beast.ftbl.util.BlockDimPos;
import com.feed_the_beast.ftbl.util.EntityDimPos;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class ForgePlayerMP extends ForgePlayer implements INBTSerializable<NBTTagCompound>
{
    public final ForgePlayerStats stats;
    public BlockDimPos lastPos, lastDeath;
    private EntityPlayerMP entityPlayer;

    public ForgePlayerMP(GameProfile p)
    {
        super(p);
        stats = new ForgePlayerStats();
    }

    public static ForgePlayerMP get(Object o) throws CommandException
    {
        ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(o);
        if(p == null || p.isFake())
        {
            throw new PlayerNotFoundException();
        }
        return p;
    }

    @Override
    public Side getSide()
    {
        return Side.SERVER;
    }

    @Override
    public boolean isOnline()
    {
        return getPlayer() != null;
    }

    @Override
    public EntityPlayerMP getPlayer()
    {
        return entityPlayer;
    }

    public void setPlayer(EntityPlayerMP ep)
    {
        entityPlayer = ep;
    }

    @Override
    public final ForgePlayerMP toMP()
    {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final ForgePlayerSP toSP()
    {
        return null;
    }

    @Override
    public final ForgeWorld getWorld()
    {
        return ForgeWorldMP.inst;
    }

    public boolean isFake()
    {
        return getPlayer() instanceof FakePlayer;
    }

    @Override
    public void sendUpdate()
    {
        //new EventLMPlayerServer.UpdateSent(this).post();
        if(isOnline())
        {
            new MessageLMPlayerUpdate(this, true).sendTo(getPlayer());
        }

        for(EntityPlayerMP ep : FTBLib.getAllOnlinePlayers(getPlayer()))
        {
            new MessageLMPlayerUpdate(this, false).sendTo(ep);
        }
    }

    public void sendInfoUpdate(ForgePlayerMP p)
    {
        new MessageLMPlayerInfo(this, p).sendTo(getPlayer());
    }

    public boolean isOP()
    {
        return FTBLib.isOP(getProfile());
    }

    public BlockDimPos getPos()
    {
        EntityPlayerMP ep = getPlayer();
        if(ep != null)
        {
            lastPos = new EntityDimPos(ep).toBlockDimPos();
        }
        return lastPos;
    }

    // Reading / Writing //

    public void getInfo(ForgePlayerMP owner, List<ITextComponent> info)
    {
        refreshStats();
        long currentTime = System.currentTimeMillis();

        if(!equalsPlayer(owner))
        {
            boolean raw1 = isFriendRaw(owner);
            boolean raw2 = owner.isFriendRaw(this);

            if(raw1 && raw2)
            {
                ITextComponent c = GuiLang.label_friend.textComponent();
                c.getStyle().setColor(TextFormatting.GREEN);
                info.add(c);
            }
            else if(raw1 || raw2)
            {
                ITextComponent c = GuiLang.label_friend_pending.textComponent();
                c.getStyle().setColor(raw1 ? TextFormatting.GOLD : TextFormatting.BLUE);
                info.add(c);
            }
        }

		/*
        if(owner.getRank().config.show_rank.getMode())
		{
			Rank rank = getRank();
			IChatComponent rankC = new ChatComponentText("[" + rank.ID + "]");
			rankC.getChatStyle().setColor(rank.color.getMode());
			info.add(rankC);
		}
		*/

        stats.getInfo(this, info, currentTime);
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.AddInfo(this, info, currentTime));
    }

    public void refreshStats()
    {
        if(isOnline())
        {
            stats.refresh(this, false);
            getPos();
        }
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        friends.clear();

        if(tag.hasKey("Friends"))
        {
            NBTTagList friendsList = tag.getTagList("Friends", Constants.NBT.TAG_LIST);
            for(int i = 0; i < friendsList.tagCount(); i++)
            {
                UUID id = LMUtils.fromString(friendsList.getStringTagAt(i));
                if(id != null)
                {
                    friends.add(id);
                }
            }
        }

        if(capabilities != null)
        {
            capabilities.deserializeNBT(tag.getCompoundTag("Caps"));
        }

        lastArmor.clear();

        if(tag.hasKey("LastItems"))
        {
            ItemStack[] lastArmorItems = new ItemStack[EntityEquipmentSlot.values().length];
            LMInvUtils.readItemsFromNBT(lastArmorItems, tag, "Armor");

            for(int i = 0; i < lastArmorItems.length; i++)
            {
                if(lastArmorItems[i] != null)
                {
                    lastArmor.put(EntityEquipmentSlot.values()[i], lastArmorItems[i]);
                }
            }
        }

        stats.load(tag);

        lastPos = null;
        if(tag.hasKey("Pos"))
        {
            lastPos = new BlockDimPos(tag.getIntArray("LastPos"));
        }

        lastDeath = null;
        if(tag.hasKey("LastDeath"))
        {
            lastDeath = new BlockDimPos(tag.getIntArray("LastDeath"));
        }

        NBTTagCompound settingsTag = tag.getCompoundTag("Settings");
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        refreshStats();

        NBTTagCompound tag = new NBTTagCompound();

        if(!friends.isEmpty())
        {
            NBTTagList tagList = new NBTTagList();

            for(UUID id : friends)
            {
                tagList.appendTag(new NBTTagString(LMUtils.fromUUID(id)));
            }

            tag.setTag("Friends", tagList);
        }

        if(capabilities != null)
        {
            tag.setTag("Caps", capabilities.serializeNBT());
        }

        if(!lastArmor.isEmpty())
        {
            ItemStack[] lastArmorItems = new ItemStack[EntityEquipmentSlot.values().length];
            for(Map.Entry<EntityEquipmentSlot, ItemStack> e : lastArmor.entrySet())
            {
                lastArmorItems[e.getKey().ordinal()] = e.getValue();
            }

            LMInvUtils.writeItemsToNBT(lastArmorItems, tag, "Armor");
        }

        stats.save(tag);

        if(lastPos != null)
        {
            tag.setIntArray("Pos", lastPos.toIntArray());
        }

        if(lastDeath != null)
        {
            tag.setIntArray("LastDeath", lastDeath.toIntArray());
        }

        return tag;
    }

    public void writeToNet(NBTTagCompound tag, boolean self)
    {
        refreshStats();

        NBTTagCompound syncData = new NBTTagCompound();
        MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.Sync(this, syncData, self));

        if(!syncData.hasNoTags())
        {
            tag.setTag("SY", syncData);
        }

        //Rank rank = getRank();

        if(isOnline())
        {
            tag.setBoolean("O", true);
        }

        if(!friends.isEmpty())
        {
            NBTTagList list = new NBTTagList();

            for(UUID id : friends)
            {
                list.appendTag(new NBTTagString(LMUtils.fromUUID(id)));
            }

            tag.setTag("F", list);
        }

        Collection<ForgePlayer> otherFriends = getOtherFriends();

        if(!otherFriends.isEmpty())
        {
            NBTTagList list = new NBTTagList();

            for(ForgePlayer p : otherFriends)
            {
                list.appendTag(new NBTTagString(p.getStringUUID()));
            }

            tag.setTag("OF", list);
        }
    }

    public void checkNewFriends()
    {
        if(isOnline())
        {
            List<String> requests = getWorld().playerMap.values().stream().filter(p -> p.isFriendRaw(this) && !isFriendRaw(p)).map(p -> p.getProfile().getName()).collect(Collectors.toList());

            if(!requests.isEmpty())
            {
                ITextComponent cc = GuiLang.label_friend_new.textComponent();
                cc.getStyle().setColor(TextFormatting.GREEN);
                Notification n = new Notification("new_friend_requests", cc, 6000);
                n.setDesc(GuiLang.label_friend_new_click.textComponent());

                MouseAction mouse = new MouseAction();
                mouse.click = new ClickAction(ClickActionType.FRIEND_ADD_ALL, null);
                Collections.sort(requests, null);

                for(String s : requests) { mouse.hover.add(new TextComponentString(s)); }
                n.setMouseAction(mouse);

                FTBLib.notifyPlayer(getPlayer(), n);
            }
        }
    }

    @Override
    public void onLoggedIn(boolean firstLogin)
    {
        super.onLoggedIn(firstLogin);

        EntityPlayerMP ep = getPlayer();
        new MessageReload(ReloadType.CLIENT_ONLY, this, true).sendTo(ep);

        new MessageLMPlayerLoggedIn(this, firstLogin, true).sendTo(ep);
        for(EntityPlayerMP ep1 : FTBLib.getAllOnlinePlayers(ep))
        { new MessageLMPlayerLoggedIn(this, firstLogin, false).sendTo(ep1); }

        checkNewFriends();
    }

    @Override
    public void onDeath()
    {
        if(!isOnline())
        {
            return;
        }
        lastDeath = new EntityDimPos(getPlayer()).toBlockDimPos();
        stats.refresh(this, false);
        super.onDeath();
        new MessageLMPlayerDied().sendTo(getPlayer());
    }

    public StatisticsManagerServer getStatFile(boolean force)
    {
        if(isOnline())
        {
            return getPlayer().getStatFile();
        }
        return force ? FTBLib.getServer().getPlayerList().getPlayerStatsFile(new FakePlayer(FTBLib.getServerWorld(), getProfile())) : null;
    }
}