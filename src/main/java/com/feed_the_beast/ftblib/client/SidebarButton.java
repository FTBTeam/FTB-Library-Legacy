package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.OtherMods;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.SidedUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class SidebarButton implements Comparable<SidebarButton>
{
	public final ResourceLocation id;
	public final SidebarButtonGroup group;
	private Icon icon = Icon.EMPTY;
	private int x = 0;
	private boolean defaultConfig = true;
	private boolean configValue = true;
	private final List<String> requiredServerMods = new ArrayList<>();
	private final List<String> clickEvents = new ArrayList<>();
	private final List<String> shiftClickEvents = new ArrayList<>();
	private boolean requiresOp, hideWithNEI, loadingScreen, customText;

	public SidebarButton(ResourceLocation _id, SidebarButtonGroup g, JsonObject json)
	{
		group = g;
		id = _id;

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
				if (e.isJsonPrimitive())
				{
					clickEvents.add(e.getAsString());
				}
				else
				{
					clickEvents.add(GuiHelper.clickEventToString(JsonUtils.deserializeClickEvent(e)));
				}
			}
		}
		if (json.has("shift_click"))
		{
			for (JsonElement e : JsonUtils.toArray(json.get("shift_click")))
			{
				if (e.isJsonPrimitive())
				{
					shiftClickEvents.add(e.getAsString());
				}
				else
				{
					shiftClickEvents.add(GuiHelper.clickEventToString(JsonUtils.deserializeClickEvent(e)));
				}
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

	public String getLangKey()
	{
		return "sidebar_button." + id.getNamespace() + '.' + id.getPath();
	}

	public String getTooltipLangKey()
	{
		return getLangKey() + ".tooltip";
	}

	@Override
	public String toString()
	{
		return id.toString();
	}

	@Override
	public final int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public final boolean equals(Object o)
	{
		return o == this || o instanceof SidebarButton && id.equals(((SidebarButton) o).id);
	}

	public Icon getIcon()
	{
		return icon;
	}

	public int getX()
	{
		return x;
	}

	public boolean getDefaultConfig()
	{
		return defaultConfig;
	}

	public void onClicked(boolean shift)
	{
		if (loadingScreen)
		{
			new GuiLoading(I18n.format(getLangKey())).openGui();
		}

		for (String event : (shift ? shiftClickEvents : clickEvents))
		{
			GuiHelper.BLANK_GUI.handleClick(event);
		}
	}

	public boolean isVisible()
	{
		return configValue && FTBLibClientConfig.action_buttons != EnumSidebarButtonPlacement.DISABLED && isAvailable();
	}

	public boolean isAvailable()
	{
		return (!hideWithNEI || !Loader.isModLoaded(OtherMods.NEI)) && (!requiresOp || ClientUtils.isClientOP()) && SidedUtils.areAllModsLoadedOnServer(requiredServerMods);
	}

	public boolean hasCustomText()
	{
		return customText;
	}

	public boolean getConfig()
	{
		return configValue;
	}

	public void setConfig(boolean value)
	{
		configValue = value;
	}

	@Override
	public int compareTo(SidebarButton button)
	{
		return getX() - button.getX();
	}
}