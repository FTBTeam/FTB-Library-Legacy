package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiSelectTeamValue;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.FinalIDObject;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ConfigTeamClient extends ConfigString
{
	public static class TeamInst extends FinalIDObject
	{
		public ITextComponent title;
		public Icon icon;

		public TeamInst(String id)
		{
			super(id);
		}
	}

	public final Map<String, TeamInst> map;

	public ConfigTeamClient(String v)
	{
		super(v, ConfigTeam.PATTERN);
		map = new HashMap<>();
	}

	@Override
	public String getName()
	{
		return ConfigTeam.TEAM_ID;
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
	public ConfigTeamClient copy()
	{
		ConfigTeamClient config = new ConfigTeamClient(getString());

		for (TeamInst inst : map.values())
		{
			TeamInst inst1 = new TeamInst(inst.getName());
			inst1.title = inst.title.createCopy();
			inst1.icon = inst.icon.copy();
			config.map.put(inst1.getName(), inst1);
		}

		return config;
	}

	@Override
	public Color4I getColor()
	{
		return ConfigEnum.COLOR;
	}

	@Override
	public void addInfo(ConfigValueInstance inst, List<String> list)
	{
	}

	@Override
	public List<String> getVariants()
	{
		return new ArrayList<>(map.keySet());
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
		if (inst.getCanEdit())
		{
			new GuiSelectTeamValue(this, gui).openGui();
		}
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
		throw new IllegalStateException("Can't write Team client property!");
	}

	@Override
	public void readData(DataIn data)
	{
		int s = data.readUnsignedShort();
		map.clear();

		for (int i = 0; i < s; i++)
		{
			TeamInst inst = new TeamInst(data.readString());
			inst.title = data.readTextComponent();
			inst.icon = data.readIcon();
			map.put(inst.getName(), inst);
		}

		setString(data.readString());
	}
}