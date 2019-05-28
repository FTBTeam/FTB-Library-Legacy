package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.command.client.CommandClientConfig;
import com.feed_the_beast.ftblib.command.client.CommandListAdvancements;
import com.feed_the_beast.ftblib.command.client.CommandPrintItem;
import com.feed_the_beast.ftblib.command.client.CommandPrintState;
import com.feed_the_beast.ftblib.command.client.CommandSimulateButton;
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
import org.lwjgl.opengl.Display;

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

		if (System.getProperty("ftbdevenvironment", "0").equals("1"))
		{
			Display.setTitle(Minecraft.getMinecraft().getVersion() + " Dev :: " + Minecraft.getMinecraft().getSession().getUsername());
		}
	}

	@Override
	public void postInit()
	{
		super.postInit();

		ClientCommandHandler.instance.registerCommand(new CommandClientConfig());
		ClientCommandHandler.instance.registerCommand(new CommandSimulateButton());
		ClientCommandHandler.instance.registerCommand(new CommandPrintItem());
		ClientCommandHandler.instance.registerCommand(new CommandPrintState());
		ClientCommandHandler.instance.registerCommand(new CommandListAdvancements());
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