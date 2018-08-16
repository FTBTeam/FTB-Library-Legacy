package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author LatvianModder
 */
public class ConfigTeam extends ConfigString
{
	public static final Pattern PATTERN = Pattern.compile("");
	public static final String TEAM_ID = "team";

	public ConfigTeam(String v)
	{
		super(v, PATTERN);
	}

	@Override
	public String getName()
	{
		return TEAM_ID;
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		if (Universe.loaded())
		{
			return Universe.get().getTeam(getString()).getTitle();
		}

		return super.getStringForGUI();
	}

	@Override
	public ConfigTeam copy()
	{
		return new ConfigTeam(getString());
	}

	@Override
	public Color4I getColor()
	{
		if (Universe.loaded())
		{
			return Universe.get().getTeam(getString()).getColor().getColor();
		}

		return Color4I.getChatFormattingColor(TextFormatting.DARK_GREEN);
	}

	@Override
	public void addInfo(ConfigValueInstance inst, List<String> list)
	{
	}

	@Override
	public List<String> getVariants()
	{
		if (Universe.loaded())
		{
			List<String> list = new ArrayList<>();

			for (ForgeTeam team : Universe.get().getTeams())
			{
				list.add(team.getName());
			}

			list.sort(null);
			return list;
		}

		return Collections.emptyList();
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		nbt.setString(key, getString());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		setString(nbt.getString(key));
	}

	@Override
	public void writeData(DataOut data)
	{
		if (!Universe.loaded())
		{
			throw new IllegalStateException("Can't write Team property, world hasn't loaded!");
		}

		data.writeShort(Universe.get().getTeams().size());

		for (ForgeTeam team : Universe.get().getTeams())
		{
			data.writeString(team.getName());
			data.writeTextComponent(team.getTitle());
			data.writeIcon(team.getIcon());
		}

		data.writeString(getString());
	}

	@Override
	public void readData(DataIn data)
	{
		throw new IllegalStateException("Can't read Team property!");
	}
}