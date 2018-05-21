package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.events.RegisterAdminPanelActionsEvent;
import com.feed_the_beast.ftblib.events.RegisterConfigValueProvidersEvent;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.events.team.RegisterTeamGuiActionsEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.config.ConfigBlockState;
import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.config.ConfigColor;
import com.feed_the_beast.ftblib.lib.config.ConfigDouble;
import com.feed_the_beast.ftblib.lib.config.ConfigEnum;
import com.feed_the_beast.ftblib.lib.config.ConfigInt;
import com.feed_the_beast.ftblib.lib.config.ConfigItemStack;
import com.feed_the_beast.ftblib.lib.config.ConfigList;
import com.feed_the_beast.ftblib.lib.config.ConfigNull;
import com.feed_the_beast.ftblib.lib.config.ConfigString;
import com.feed_the_beast.ftblib.lib.config.ConfigStringEnum;
import com.feed_the_beast.ftblib.lib.config.ConfigTextComponent;
import com.feed_the_beast.ftblib.lib.config.ConfigTimer;
import com.feed_the_beast.ftblib.lib.config.ConfigTristate;
import com.feed_the_beast.ftblib.lib.data.AdminPanelAction;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class FTBLibEventHandler
{
	public static final ResourceLocation RELOAD_CONFIG = new ResourceLocation(FTBLib.MOD_ID, "config");

	@SubscribeEvent
	public static void registerConfigValueProviders(RegisterConfigValueProvidersEvent event)
	{
		event.register(ConfigNull.ID, () -> ConfigNull.INSTANCE);
		event.register(ConfigList.ID, () -> new ConfigList(ConfigNull.ID));
		event.register(ConfigBoolean.ID, ConfigBoolean::new);
		event.register(ConfigTristate.ID, ConfigTristate::new);
		event.register(ConfigInt.ID, ConfigInt::new);
		event.register(ConfigDouble.ID, ConfigDouble::new);
		event.register(ConfigString.ID, ConfigString::new);
		event.register(ConfigColor.ID, ConfigColor::new);
		event.register(ConfigEnum.ID, ConfigStringEnum::new);
		event.register(ConfigBlockState.ID, ConfigBlockState::new);
		event.register(ConfigItemStack.ID, ConfigItemStack::new);
		event.register(ConfigTextComponent.ID, ConfigTextComponent::new);
		event.register(ConfigTimer.ID, ConfigTimer::new);
	}

	@SubscribeEvent
	public static void registerTeamGuiActions(RegisterTeamGuiActionsEvent event)
	{
		event.register(FTBLibTeamGuiActions.CONFIG);
		event.register(FTBLibTeamGuiActions.INFO);
		event.register(FTBLibTeamGuiActions.MEMBERS);
		event.register(FTBLibTeamGuiActions.ALLIES);
		event.register(FTBLibTeamGuiActions.MODERATORS);
		event.register(FTBLibTeamGuiActions.ENEMIES);
		event.register(FTBLibTeamGuiActions.LEAVE);
		event.register(FTBLibTeamGuiActions.TRANSFER_OWNERSHIP);
	}

	@SubscribeEvent
	public static void registerAdminPanelActions(RegisterAdminPanelActionsEvent event)
	{
		event.register(new AdminPanelAction(FTBLib.MOD_ID, "reload", GuiIcons.REFRESH, -1000)
		{
			@Override
			public Type getType(ForgePlayer player, NBTTagCompound data)
			{
				return Type.fromBoolean(player.isOP());
			}

			@Override
			public void onAction(ForgePlayer player, NBTTagCompound data)
			{
				FTBLibAPI.reloadServer(player.team.universe, player.getPlayer(), EnumReloadType.RELOAD_COMMAND, ServerReloadEvent.ALL);
			}
		}.setTitle(new TextComponentTranslation("ftblib.lang.reload_server_button")));
	}

	@SubscribeEvent
	public static void registerReloadIds(ServerReloadEvent.RegisterIds event)
	{
		event.register(RELOAD_CONFIG);
	}

	@SubscribeEvent
	public static void onServerReload(ServerReloadEvent event)
	{
		if (event.reload(RELOAD_CONFIG))
		{
			FTBLibConfig.sync();
		}
	}

    /*
	@SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent e)
    {
    }
    */
}