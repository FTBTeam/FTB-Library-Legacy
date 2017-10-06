package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.FinalIDObject;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class SidebarButton extends FinalIDObject implements ISidebarButton
{
	private final ISidebarButtonGroup group;
	private Icon icon = Icon.EMPTY;
	private int x = 0;
	private Boolean defaultConfig = null;
	private boolean configValue = true;
	private final List<String> requiredServerMods = new ArrayList<>();
	private final List<ClickEvent> clickEvents = new ArrayList<>();
	private final List<ClickEvent> shiftClickEvents = new ArrayList<>();
	private boolean requiresOp, hideWithNEI, loadingScreen, customText;

	public SidebarButton(ResourceLocation id, ISidebarButtonGroup g, JsonObject json)
	{
		super(id.toString().replace(':', '.'));

		group = g;

		if (json.has("icon"))
		{
			icon = Icon.getIcon(json.get("icon"));
		}

		if (icon.isEmpty())
		{
			icon = GuiIcons.ACCEPT_GRAY;
		}

		if (json.has("click"))
		{
			for (JsonElement e : JsonUtils.toArray(json.get("click")))
			{
				clickEvents.add(JsonUtils.deserializeClickEvent(e));
			}
		}
		if (json.has("shift_click"))
		{
			for (JsonElement e : JsonUtils.toArray(json.get("shift_click")))
			{
				shiftClickEvents.add(JsonUtils.deserializeClickEvent(e));
			}
		}
		if (json.has("config"))
		{
			defaultConfig = configValue = json.get("config").getAsBoolean();
		}
		if (json.has("required_server_mods"))
		{
			for (JsonElement e : JsonUtils.toArray(json.get("required_server_mods")))
			{
				requiredServerMods.add(e.getAsString());
			}
		}
		if (json.has("x"))
		{
			x = json.get("x").getAsInt();
		}

		requiresOp = json.has("requires_op") && json.get("requires_op").getAsBoolean();
		hideWithNEI = json.has("hide_with_nei") && json.get("hide_with_nei").getAsBoolean();
		loadingScreen = json.has("loading_screen") && json.get("loading_screen").getAsBoolean();
		customText = json.has("custom_text") && json.get("custom_text").getAsBoolean();
	}

	@Override
	public ISidebarButtonGroup getGroup()
	{
		return group;
	}

	@Override
	public Icon getIcon()
	{
		return icon;
	}

	@Override
	public int getX()
	{
		return x;
	}

	@Override
	@Nullable
	public Boolean getDefaultConfig()
	{
		return defaultConfig;
	}

	@Override
	public void onClicked(boolean shift)
	{
		if (loadingScreen)
		{
			new GuiLoading().openGui();
		}

		for (ClickEvent event : (shift ? shiftClickEvents : clickEvents))
		{
			GuiHelper.onClickEvent(event);
		}
	}

	@Override
	public boolean isVisible()
	{
		return configValue && FTBLibClientConfig.general.action_buttons != EnumSidebarButtonPlacement.DISABLED && isAvailable();
	}

	@Override
	public boolean isAvailable()
	{
		return (!hideWithNEI || !CommonUtils.isNEILoaded()) && (!requiresOp || FTBLibAPI.API.getClientData().isClientOP()) && (requiredServerMods.isEmpty() || FTBLibAPI.API.getClientData().optionalServerMods().containsAll(requiredServerMods));
	}

	@Override
	public boolean hasCustomText()
	{
		return customText;
	}

	@Override
	public boolean getConfig()
	{
		return configValue;
	}

	@Override
	public void setConfig(boolean value)
	{
		configValue = value;
	}
}