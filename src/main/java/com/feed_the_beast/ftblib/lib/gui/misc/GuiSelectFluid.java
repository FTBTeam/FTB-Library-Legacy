package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigFluid;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public class GuiSelectFluid extends GuiButtonListBase
{
	private final ConfigFluid value;
	private final IOpenableGui callbackGui;

	public GuiSelectFluid(ConfigFluid v, IOpenableGui c)
	{
		setHasSearchBox(true);
		value = v;
		callbackGui = c;
	}

	@Override
	public void addButtons(Panel panel)
	{
		if (value.getDefaultFluid() == null)
		{
			panel.add(new SimpleTextButton(panel, "None", GuiIcons.BARRIER)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					value.setFluid(null);
					callbackGui.openGui();
				}
			});
		}

		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
		{
			FluidStack fluidStack = new FluidStack(fluid, Fluid.BUCKET_VOLUME);

			panel.add(new SimpleTextButton(panel, fluid.getLocalizedName(fluidStack), Icon.getIcon(fluid.getStill(fluidStack).toString()))
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					value.setFluid(fluid);
					callbackGui.openGui();
				}
			});
		}
	}
}