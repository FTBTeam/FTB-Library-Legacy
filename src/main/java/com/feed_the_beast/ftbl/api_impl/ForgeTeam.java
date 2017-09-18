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
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamConfigEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigEnum;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigString;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.FileUtils;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.net.MessageDisplayTeamMsg;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
		private final ITextComponent text;

		public Message(UUID s, ITextComponent txt)
		{
			sender = s;
			text = txt;
		}

		public Message(ITextComponent txt)
		{
			this(ForgePlayerFake.SERVER.getId(), txt);
		}

		public Message(NBTTagCompound nbt)
		{
			sender = nbt.getUniqueId("Sender");
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
			text = NetUtils.readTextComponent(io);
		}

		public static NBTTagCompound toNBT(ITeamMessage msg)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setUniqueId("Sender", msg.getSender());
			nbt.setString("Msg", ITextComponent.Serializer.componentToJson(msg.getMessage()));
			return nbt;
		}

		public static void write(ByteBuf io, ITeamMessage msg)
		{
			NetUtils.writeUUID(io, msg.getSender());
			NetUtils.writeTextComponent(io, msg.getMessage());
		}

		@Override
		public UUID getSender()
		{
			return sender;
		}

		@Override
		public ITextComponent getMessage()
		{
			return text;
		}
	}

	public boolean isValid;
	public final NBTDataStorage dataStorage;
	public final ConfigEnum<EnumTeamColor> color;
	public IForgePlayer owner;
	public final ConfigString title;
	public final ConfigString desc;
	public final ConfigBoolean freeToJoin;
	public final List<ITeamMessage> chatHistory;
	public final Map<UUID, EnumTeamStatus> players;
	public final ConfigGroup cachedConfig;

	public ForgeTeam(String id, @Nullable IForgePlayer _owner)
	{
		super(id);
		isValid = true;
		color = new ConfigEnum<>(EnumTeamColor.NAME_MAP);
		owner = _owner;
		title = new ConfigString("");
		desc = new ConfigString("");
		freeToJoin = new ConfigBoolean(false);
		chatHistory = new ArrayList<>();
		players = new HashMap<>();

		dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_TEAM);

		cachedConfig = new ConfigGroup(FTBLibLang.MY_TEAM_SETTINGS.textComponent());
		cachedConfig.setSupergroup("team_config");
		ForgeTeamConfigEvent event = new ForgeTeamConfigEvent(this, cachedConfig);
		event.post();

		String group = FTBLibFinals.MOD_ID;
		event.getConfig().setGroupName(group, new TextComponentString(FTBLibFinals.MOD_NAME));
		event.getConfig().add(group, "free_to_join", freeToJoin);
		group = FTBLibFinals.MOD_ID + ".display";
		event.getConfig().add(group, "color", color);
		event.getConfig().add(group, "title", title);
		event.getConfig().add(group, "desc", desc);
	}

	@Override
	public NBTDataStorage getData()
	{
		return dataStorage;
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
			players.put(playerId, status);
		}
		else
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
			FileUtils.delete(new File(CommonUtils.folderWorld, "data/ftb_lib/teams/" + getName() + ".dat"));
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
	public void changeOwner(IForgePlayer player)
	{
		if (!hasStatus(player, EnumTeamStatus.MEMBER))
		{
			return;
		}

		IForgePlayer oldOwner = owner;
		owner = player;
		player.setTeamID(getName());

		if (!oldOwner.equalsPlayer(owner))
		{
			new ForgeTeamOwnerChangedEvent(this, oldOwner, player).post();
		}
	}

	@Override
	public ConfigGroup getSettings()
	{
		return cachedConfig;
	}

	@Override
	public void printMessage(ITeamMessage message)
	{
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
		return chatHistory;
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