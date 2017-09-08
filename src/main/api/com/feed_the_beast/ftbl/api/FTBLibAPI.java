package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.config.RankConfigKey;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * @author LatvianModder
 */
public abstract class FTBLibAPI
{
	public static FTBLibAPI API;

	public abstract Collection<ITickable> ticking();

	public abstract ISharedServerData getServerData();

	public abstract ISharedClientData getClientData();

	public ISharedData getSidedData(Side side)
	{
		return side.isServer() ? getServerData() : getClientData();
	}

	public abstract IUniverse getUniverse();

	public abstract void addServerCallback(int timer, Runnable runnable);

	public abstract void loadWorldData(MinecraftServer server);

	public abstract void reload(Side side, ICommandSender sender, EnumReloadType type, ResourceLocation id);

	public abstract void openGui(ResourceLocation guiID, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data);

	public abstract void editServerConfig(EntityPlayerMP player, ConfigTree tree, ITextComponent title, @Nullable NBTTagCompound data, IConfigCallback callback);

	public abstract void displayGuide(EntityPlayer player, GuidePage page);

	public abstract ConfigValue getConfigValueFromID(String id);

	public abstract Map<String, RankConfigKey> getRankConfigRegistry();

	public abstract void handleMessage(MessageBase<?> message, MessageContext context, Side side);
}