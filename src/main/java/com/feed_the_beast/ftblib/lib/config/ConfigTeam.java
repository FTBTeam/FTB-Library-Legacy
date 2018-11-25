package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ConfigTeam extends ConfigValue
{
	public static final String TEAM_ID = "team";

	private final Supplier<ForgeTeam> get;
	private final Consumer<ForgeTeam> set;

	public ConfigTeam(Supplier<ForgeTeam> g, Consumer<ForgeTeam> s)
	{
		get = g;
		set = s;
	}

	@Override
	public String getID()
	{
		return TEAM_ID;
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		return get.get().getTitle();
	}

	@Override
	public String getString()
	{
		return get.get().getID();
	}

	@Override
	public boolean getBoolean()
	{
		return get.get().isValid();
	}

	@Override
	public int getInt()
	{
		return get.get().getUID();
	}

	@Override
	public ConfigTeam copy()
	{
		throw new IllegalStateException("Not supported!");
	}

	@Override
	public Color4I getColor()
	{
		return get.get().getColor().getColor();
	}

	@Override
	public void addInfo(ConfigValueInstance inst, List<String> list)
	{
	}

	@Override
	public List<String> getVariants()
	{
		List<String> list = new ObjectArrayList<>();

		for (ForgeTeam team : get.get().universe.getTeams())
		{
			list.add(team.getID());
		}

		list.sort(null);
		return list;
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button, Runnable callback)
	{
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		nbt.setShort(key, (short) getInt());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		set.accept(get.get().universe.getTeam(nbt.getShort(key)));
	}

	@Override
	public void writeData(DataOut data)
	{
		ForgeTeam team = get.get();
		Collection<ForgeTeam> teams = team.universe.getTeams();
		data.writeVarInt(teams.size());

		for (ForgeTeam t : teams)
		{
			data.writeShort(t.getUID());
			data.writeString(t.getID());
			data.writeTextComponent(t.getTitle());
			data.writeIcon(t.getIcon());
		}

		data.writeString(getString());
	}

	@Override
	public void readData(DataIn data)
	{
		throw new IllegalStateException("Can't read Team property!");
	}
}