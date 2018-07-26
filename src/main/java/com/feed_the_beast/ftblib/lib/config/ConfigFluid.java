package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiSelectFluid;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ConfigFluid extends ConfigValue
{
	public static final String ID = "fluid";

	private Fluid value;
	private Fluid defaultFluid;

	public ConfigFluid(@Nullable Fluid fluid, @Nullable Fluid def)
	{
		defaultFluid = def;
		value = fluid == null ? getDefaultFluid() : fluid;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public String getString()
	{
		value = getFluid();
		return value == null ? "null" : value.getName();
	}

	@Nullable
	public Fluid getFluid()
	{
		return value;
	}

	public void setFluid(@Nullable Fluid fluid)
	{
		value = fluid == null ? getDefaultFluid() : fluid;
	}

	@Nullable
	public Fluid getDefaultFluid()
	{
		return defaultFluid;
	}

	public void setDefaultFluid(@Nullable Fluid fluid)
	{
		defaultFluid = fluid;
	}

	@Override
	public boolean getBoolean()
	{
		return value != null;
	}

	@Override
	public int getInt()
	{
		return 0;
	}

	@Override
	public ConfigFluid copy()
	{
		return new ConfigFluid(getFluid(), getDefaultFluid());
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		value = getFluid();

		if (value == null)
		{
			return new TextComponentString("null");
		}

		return new TextComponentString(value.getLocalizedName(new FluidStack(value, Fluid.BUCKET_VOLUME)));  //TODO: PR Forge to fix this for WATER and LAVA
	}

	@Override
	public boolean setValueFromString(String string, boolean simulate)
	{
		if (string.equals("null"))
		{
			if (!simulate)
			{
				setFluid(null);
			}

			return true;
		}

		value = FluidRegistry.getFluid(string);

		if (value != null)
		{
			if (!simulate)
			{
				setFluid(value);
			}

			return true;
		}

		return false;
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
		new GuiSelectFluid(this, gui).openGui();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		value = getFluid();
		nbt.setString(key, value == null ? "" : value.getName());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		String s = nbt.getString(key);
		setFluid(s.isEmpty() ? null : FluidRegistry.getFluid(s));
	}

	@Override
	public void writeData(DataOut data)
	{
		defaultFluid = getDefaultFluid();
		data.writeString(defaultFluid == null ? "" : defaultFluid.getName());
		value = getFluid();
		data.writeString(value == null ? "" : value.getName());
	}

	@Override
	public void readData(DataIn data)
	{
		String s = data.readString();
		setDefaultFluid(s.isEmpty() ? null : FluidRegistry.getFluid(s));
		s = data.readString();
		setFluid(s.isEmpty() ? null : FluidRegistry.getFluid(s));
	}

	@Override
	public boolean isEmpty()
	{
		return getFluid() == null;
	}
}