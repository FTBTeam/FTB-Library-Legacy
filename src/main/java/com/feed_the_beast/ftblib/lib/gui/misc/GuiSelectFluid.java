package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class GuiSelectFluid extends GuiButtonListBase
{
	private final IOpenableGui callbackGui;
	private final Supplier<Fluid> defaultFluid;
	private final Consumer<Fluid> callback;

	public GuiSelectFluid(IOpenableGui g, Supplier<Fluid> def, Consumer<Fluid> c)
	{
		setTitle(I18n.format("ftblib.select_fluid.gui"));
		setHasSearchBox(true);
		callbackGui = g;
		defaultFluid = def;
		callback = c;
	}

	@Override
	public void addButtons(Panel panel)
	{
		if (defaultFluid.get() == null)
		{
			panel.add(new SimpleTextButton(panel, I18n.format("ftblib.select_fluid.none"), GuiIcons.BARRIER)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					callbackGui.openGui();
					callback.accept(null);
				}
			});
		}

		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
		{
			FluidStack fluidStack = new FluidStack(fluid, Fluid.BUCKET_VOLUME);

			panel.add(new SimpleTextButton(panel, fluid.getLocalizedName(fluidStack), Icon.getIcon(fluid.getStill(fluidStack).toString()).withTint(Color4I.rgb(fluid.getColor(fluidStack))))
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					callbackGui.openGui();
					callback.accept(fluid);
				}

				@Override
				public Object getJEIFocus()
				{
					return new FluidStack(fluid, Fluid.BUCKET_VOLUME);
				}
			});
		}
	}
}