package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.ITeamMessage;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamSettingsEvent;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.net.MessageDisplayTeamMsg;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public final class ForgeTeam extends FinalIDObject implements IForgeTeam
{
	public static class Message implements ITeamMessage
	{
		private final UUID sender;
		private final long time;
		private final ITextComponent text;

		public Message(UUID s, long t, ITextComponent txt)
		{
			sender = s;
			time = t;
			text = txt;
		}

		public Message(ITextComponent txt)
		{
			this(ForgePlayerFake.SERVER.getId(), System.currentTimeMillis(), txt);
		}

		public Message(NBTTagCompound nbt)
		{
			sender = nbt.getUniqueId("Sender");
			time = nbt.getLong("Time");

			String m = nbt.getString("Msg");

			if (m.isEmpty())
			{
				text = ForgeHooks.newChatWithLinks(nbt.getString("Text"));
			}
			else
			{
				text = ITextComponent.Serializer.jsonToComponent(m);
			}
		}

		public Message(ByteBuf io)
		{
			sender = NetUtils.readUUID(io);
			time = io.readLong();
			text = NetUtils.readTextComponent(io);
		}

		public static NBTTagCompound toNBT(ITeamMessage msg)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setUniqueId("Sender", msg.getSender());
			nbt.setLong("Time", msg.getTime());
			nbt.setString("Msg", ITextComponent.Serializer.componentToJson(msg.getMessage()));
			return nbt;
		}

		public static void write(ByteBuf io, ITeamMessage msg)
		{
			NetUtils.writeUUID(io, msg.getSender());
			io.writeLong(msg.getTime());
			NetUtils.writeTextComponent(io, msg.getMessage());
		}

		@Override
		public int compareTo(ITeamMessage o)
		{
			return Long.compare(getTime(), o.getTime());
		}

		@Override
		public UUID getSender()
		{
			return sender;
		}

		@Override
		public long getTime()
		{
			return time;
		}

		@Override
		public ITextComponent getMessage()
		{
			return text;
		}
	}

	private boolean isValid;
	private final NBTDataStorage dataStorage;
	private final PropertyEnum<EnumTeamColor> color;
	private IForgePlayer owner;
	private final PropertyString title;
	private final PropertyString desc;
	private final PropertyBool freeToJoin;
	private List<ITeamMessage> chatHistory;
	private Map<UUID, EnumTeamStatus> players;
	private final IConfigTree cachedConfig;

	public ForgeTeam(String id)
	{
		super(id);
		isValid = true;
		color = new PropertyEnum<>(EnumTeamColor.NAME_MAP);
		title = new PropertyString("");
		desc = new PropertyString("");
		freeToJoin = new PropertyBool(false);
		dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_TEAM);

		cachedConfig = new ConfigTree();
		ForgeTeamSettingsEvent event = new ForgeTeamSettingsEvent(this, cachedConfig);
		event.post();

		String group = FTBLibFinals.MOD_ID;
		event.add(group, "free_to_join", freeToJoin);
		group = FTBLibFinals.MOD_ID + ".display";
		event.add(group, "color", color);
		event.add(group, "title", title);
		event.add(group, "desc", desc);
	}

	@Override
	@Nullable
	public INBTSerializable<?> getData(ResourceLocation id)
	{
		return dataStorage == null ? null : dataStorage.get(id);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Owner", StringUtils.fromUUID(owner.getId()));
		nbt.setString("Color", color.getString());

		if (!title.isEmpty())
		{
			nbt.setString("Title", title.getString());
		}

		if (!desc.isEmpty())
		{
			nbt.setString("Desc", desc.getString());
		}

		nbt.setBoolean("FreeToJoin", freeToJoin.getBoolean());

		if (players != null && !players.isEmpty())
		{
			NBTTagCompound nbt1 = new NBTTagCompound();

			for (Map.Entry<UUID, EnumTeamStatus> entry : players.entrySet())
			{
				nbt1.setString(StringUtils.fromUUID(entry.getKey()), entry.getValue().getName());
			}

			nbt.setTag("Players", nbt1);
		}

		if (dataStorage != null)
		{
			nbt.setTag("Data", dataStorage.serializeNBT());
		}

		if (chatHistory != null && !chatHistory.isEmpty())
		{
			NBTTagList list = new NBTTagList();

			for (ITeamMessage msg : chatHistory)
			{
				list.appendTag(Message.toNBT(msg));
			}

			nbt.setTag("Chat", list);
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		owner = Universe.INSTANCE.getPlayer(StringUtils.fromString(nbt.getString("Owner")));
		color.setValueFromString(nbt.getString("Color"), false);
		title.setString(nbt.getString("Title"));
		desc.setString(nbt.getString("Desc"));

		if (nbt.hasKey("Flags"))
		{
			int flags = nbt.getInteger("Flags");
			freeToJoin.setBoolean(Bits.getFlag(flags, 1));
			isValid = !Bits.getFlag(flags, 2);
		}
		else
		{
			freeToJoin.setBoolean(nbt.getBoolean("FreeToJoin"));
		}

		if (players != null)
		{
			players.clear();
		}

		if (nbt.hasKey("Players"))
		{
			if (players == null)
			{
				players = new HashMap<>();
			}

			NBTTagCompound nbt1 = nbt.getCompoundTag("Players");

			for (String s : nbt1.getKeySet())
			{
				UUID id = StringUtils.fromString(s);

				if (id != null)
				{
					EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(nbt1.getString(s));

					if (status != null && status.canBeSet())
					{
						players.put(id, status);
					}
				}
			}
		}

		if (nbt.hasKey("Invited"))
		{
			if (players == null)
			{
				players = new HashMap<>();
			}

			NBTTagList list = nbt.getTagList("Invited", Constants.NBT.TAG_STRING);

			for (int i = 0; i < list.tagCount(); i++)
			{
				UUID id = StringUtils.fromString(list.getStringTagAt(i));

				if (id != null && !players.containsKey(id))
				{
					players.put(id, EnumTeamStatus.INVITED);
				}
			}
		}

		if (dataStorage != null)
		{
			dataStorage.deserializeNBT(nbt.hasKey("Caps") ? nbt.getCompoundTag("Caps") : nbt.getCompoundTag("Data"));
		}

		if (chatHistory != null)
		{
			chatHistory.clear();
		}

		if (nbt.hasKey("Chat"))
		{
			if (chatHistory == null)
			{
				chatHistory = new ArrayList<>();
			}

			NBTTagList list = nbt.getTagList("Chat", Constants.NBT.TAG_COMPOUND);

			for (int i = 0; i < list.tagCount(); i++)
			{
				chatHistory.add(new Message(list.getCompoundTagAt(i)));
			}
		}
	}

	@Override
	public IForgePlayer getOwner()
	{
		return owner;
	}

	@Override
	public String getTitle()
	{
		return title.isEmpty() ? (owner.getName() + (owner.getName().endsWith("s") ? "' Team" : "'s Team")) : title.getString();
	}

	@Override
	public String getDesc()
	{
		return desc.getString();
	}

	@Override
	public EnumTeamColor getColor()
	{
		return color.getValue();
	}

	public void setColor(EnumTeamColor col)
	{
		color.setValue(col);
	}

	@Override
	public EnumTeamStatus getHighestStatus(UUID playerId)
	{
		if (owner.getId().equals(playerId))
		{
			return EnumTeamStatus.OWNER;
		}

		EnumTeamStatus status = getSetStatus(playerId);

		if (status == EnumTeamStatus.MOD)
		{
			if (!isMember(playerId))
			{
				status = EnumTeamStatus.NONE;
			}
		}
		else if (!status.isEqualOrGreaterThan(EnumTeamStatus.MEMBER) && isMember(playerId))
		{
			status = EnumTeamStatus.MEMBER;
		}

		return status;
	}

	private boolean isMember(UUID playerId)
	{
		IForgePlayer player = FTBLibAPI.API.getUniverse().getPlayer(playerId);
		return player != null && equals(player.getTeam());

	}

	private EnumTeamStatus getSetStatus(UUID playerId)
	{
		if (players == null || players.isEmpty())
		{
			return EnumTeamStatus.NONE;
		}

		EnumTeamStatus status = players.get(playerId);

		if (status == null)
		{
			status = EnumTeamStatus.NONE;

			if (freeToJoin.getBoolean())
			{
				status = EnumTeamStatus.INVITED;
			}
		}

		return status;
	}

	@Override
	public boolean hasStatus(UUID playerId, EnumTeamStatus status)
	{
		if (status.isNone())
		{
			return true;
		}

		EnumTeamStatus status1 = getHighestStatus(playerId);
		return status1.isEqualOrGreaterThan(status);
	}

	@Override
	public void setStatus(UUID playerId, EnumTeamStatus status)
	{
		if (status.canBeSet() && !status.isNone())
		{
			if (players == null)
			{
				players = new HashMap<>();
			}

			players.put(playerId, status);
		}
		else if (players != null)
		{
			players.remove(playerId);
		}
	}

	@Override
	public Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> c, EnumTeamStatus status)
	{
		for (IForgePlayer p : Universe.INSTANCE.getPlayers())
		{
			if (hasStatus(p, status))
			{
				c.add(p);
			}
		}

		return c;
	}

	@Override
	public boolean addPlayer(IForgePlayer player)
	{
		if (hasStatus(player, EnumTeamStatus.INVITED))
		{
			player.setTeamID(getName());

			if (!hasStatus(player, EnumTeamStatus.MEMBER))
			{
				new ForgeTeamPlayerJoinedEvent(this, player).post();
				setStatus(player.getId(), EnumTeamStatus.MEMBER);
				printMessage(new Message(FTBLibLang.TEAM_MEMBER_JOINED.textComponent(player.getName())));
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean removePlayer(IForgePlayer player)
	{
		if (getPlayersWithStatus(new ArrayList<>(), EnumTeamStatus.MEMBER).size() == 1)
		{
			printMessage(new Message(FTBLibLang.TEAM_DELETED.textComponent(getTitle())));
			new ForgeTeamDeletedEvent(this).post();
			removePlayer0(player);
			Universe.INSTANCE.teams.remove(getName());
		}
		else
		{
			if (hasStatus(player, EnumTeamStatus.OWNER))
			{
				return false;
			}

			removePlayer0(player);
		}

		return true;
	}

	private void removePlayer0(IForgePlayer player)
	{
		if (hasStatus(player, EnumTeamStatus.MEMBER))
		{
			player.setTeamID("");
			new ForgeTeamPlayerLeftEvent(this, player).post();
			printMessage(new Message(FTBLibLang.TEAM_MEMBER_LEFT.textComponent(player.getName())));
		}
	}

	@Override
	public void changeOwner(IForgePlayer newOwner)
	{
		if (owner == null)
		{
			owner = newOwner;
			newOwner.setTeamID(getName());
		}
		else
		{
			IForgePlayer oldOwner = owner;

			if (!oldOwner.equalsPlayer(newOwner) && hasStatus(newOwner, EnumTeamStatus.MEMBER))
			{
				owner = newOwner;
				new ForgeTeamOwnerChangedEvent(this, oldOwner, newOwner).post();
			}
		}
	}

	@Override
	public IConfigTree getSettings()
	{
		return cachedConfig;
	}

	@Override
	public void printMessage(ITeamMessage message)
	{
		if (chatHistory == null)
		{
			chatHistory = new ArrayList<>();
		}

		while (chatHistory.size() >= FTBLibConfig.teams.max_team_chat_history)
		{
			chatHistory.remove(0);
		}

		chatHistory.add(message);

		MessageDisplayTeamMsg m = new MessageDisplayTeamMsg(message);
		ITextComponent name = StringUtils.color(new TextComponentString(Universe.INSTANCE.getPlayer(message.getSender()).getProfile().getName()), color.getValue().getTextFormatting());
		ITextComponent msg = FTBLibLang.TEAM_CHAT_MESSAGE.textComponent(name, message.getMessage());
		msg.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, StringUtils.color(FTBLibLang.CLICK_HERE.textComponent(), TextFormatting.GOLD)));
		msg.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/team msg "));

		for (EntityPlayerMP ep : getOnlineTeamPlayers(EnumTeamStatus.MEMBER))
		{
			ep.sendMessage(msg);
			m.sendTo(ep);
		}
	}

	@Override
	public List<ITeamMessage> getMessages()
	{
		return chatHistory == null ? Collections.emptyList() : chatHistory;
	}

	public Collection<EntityPlayerMP> getOnlineTeamPlayers(EnumTeamStatus status)
	{
		Collection<EntityPlayerMP> list = new ArrayList<>();

		for (EntityPlayerMP ep : ServerUtils.getServer().getPlayerList().getPlayers())
		{
			if (hasStatus(ep.getGameProfile().getId(), status))
			{
				list.add(ep);
			}
		}

		return list;
	}

	@Override
	public boolean isValid()
	{
		return isValid;
	}

	@Override
	public boolean freeToJoin()
	{
		return freeToJoin.getBoolean();
	}
}