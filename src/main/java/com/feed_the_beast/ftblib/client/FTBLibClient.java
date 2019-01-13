package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.command.client.CmdClientConfig;
import com.feed_the_beast.ftblib.command.client.CmdPrintItem;
import com.feed_the_beast.ftblib.command.client.CmdPrintState;
import com.feed_the_beast.ftblib.command.client.CmdSimulateButton;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.client.ParticleColoredDust;
import com.feed_the_beast.ftblib.lib.gui.misc.ChunkSelectorMap;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class FTBLibClient extends FTBLibCommon
{
	public static final Map<String, ClientConfig> CLIENT_CONFIG_MAP = new HashMap<>();

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		FTBLibClientConfig.sync();
		ClientUtils.localPlayerHead = new PlayerHeadIcon(Minecraft.getMinecraft().getSession().getProfile().getId());
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(FTBLibClientConfigManager.INSTANCE);
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(SidebarButtonManager.INSTANCE);
		ChunkSelectorMap.setMap(new BuiltinChunkMap());
	}

	@Override
	public void postInit()
	{
		super.postInit();

		ClientCommandHandler.instance.registerCommand(new CmdClientConfig());
		ClientCommandHandler.instance.registerCommand(new CmdSimulateButton());
		ClientCommandHandler.instance.registerCommand(new CmdPrintItem());
		ClientCommandHandler.instance.registerCommand(new CmdPrintState());
	}

	@Override
	public void handleClientMessage(MessageToClient message)
	{
		if (FTBLibConfig.debugging.log_network)
		{
			FTBLib.LOGGER.info("Net RX: " + message.getClass().getName());
		}

		message.onMessage();
	}

	@Override
	public void spawnDust(World world, double x, double y, double z, float r, float g, float b, float a)
	{
		ClientUtils.spawnParticle(new ParticleColoredDust(world, x, y, z, r, g, b, a));
	}

	@Override
	public long getWorldTime()
	{
		return Minecraft.getMinecraft().world == null ? super.getWorldTime() : Minecraft.getMinecraft().world.getTotalWorldTime();
	}
}