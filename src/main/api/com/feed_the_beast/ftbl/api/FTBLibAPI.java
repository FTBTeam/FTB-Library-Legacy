package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.config.RankConfigValueInfo;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public abstract class FTBLibAPI
{
	public static FTBLibAPI API;

	public abstract ISharedServerData getServerData();

	public abstract ISharedClientData getClientData();

	public ISharedData getSidedData(Side side)
	{
		return side.isServer() ? getServerData() : getClientData();
	}

	public abstract IUniverse getUniverse();

	public abstract boolean hasUniverse();

	public abstract void reload(Side side, ICommandSender sender, EnumReloadType type, ResourceLocation id);

	public abstract void openGui(ResourceLocation guiID, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data);

	public abstract void editServerConfig(EntityPlayerMP player, ConfigGroup group, IConfigCallback callback);

	public abstract ConfigValue getConfigValueFromID(String id);

	public abstract Map<String, RankConfigValueInfo> getRankConfigRegistry();

	public abstract void handleMessage(MessageBase<?> message, MessageContext context, Side side);

	@SideOnly(Side.CLIENT)
	public abstract List<ISidebarButton> getSidebarButtons(boolean ignoreConfig);
}