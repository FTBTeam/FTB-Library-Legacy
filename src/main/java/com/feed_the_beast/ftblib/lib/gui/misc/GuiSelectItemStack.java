package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.config.ConfigItemStack;
import com.feed_the_beast.ftblib.lib.config.ConfigValueInstance;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.TextField;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.ItemIcon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author LatvianModder
 */
public class GuiSelectItemStack extends GuiButtonListBase
{
	private class ItemStackButton extends SimpleTextButton
	{
		private final ItemStack stack;
		private Icon icon;
		private String name;

		private ItemStackButton(Panel panel, ItemStack is)
		{
			super(panel, "-", GuiIcons.BARRIER);
			setWidth(170);
			stack = is;
		}

		@Override
		public SimpleTextButton setTitle(String txt)
		{
			return this;
		}

		@Override
		public String getTitle()
		{
			if (name == null)
			{
				name = stack.getDisplayName();
			}

			return name;
		}

		@Override
		public Icon getIcon()
		{
			if (icon == null)
			{
				icon = ItemIcon.getItemIcon(stack);
			}

			return icon;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			((ConfigItemStack) value.getValue()).setItem(stack.copy());
			callbackGui.openGui();
		}
	}

	private final ConfigValueInstance value;
	private final IOpenableGui callbackGui;

	public GuiSelectItemStack(ConfigValueInstance v, IOpenableGui g)
	{
		setHasSearchBox(true);
		value = v;
		callbackGui = g;
	}

	@Override
	public void addButtons(Panel panel)
	{
		panel.add(new ItemStackButton(panel, ItemStack.EMPTY));

		panel.add(new SimpleTextButton(panel, "{...}", GuiIcons.ADD)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();

				new GuiEditConfigValue(value, (value1, set) ->
				{
					if (set)
					{
						value.getValue().setValueFromOtherValue(value1);
					}

					callbackGui.openGui();
				}).openGui();
			}
		});

		panel.add(new TextField(panel, "\n" + ClientUtils.MC.player.inventory.getDisplayName().getUnformattedText() + "\n ", CENTERED));

		for (int i = 0; i < ClientUtils.MC.player.inventory.getSizeInventory(); i++)
		{
			ItemStack stack = ClientUtils.MC.player.inventory.getStackInSlot(i);

			if (!stack.isEmpty())
			{
				panel.add(new ItemStackButton(panel, stack));
			}
		}

		panel.add(new TextField(panel, "\n" + I18n.format("itemGroup.search") + "\n ", CENTERED));
		NonNullList<ItemStack> list = NonNullList.create();

		for (Item item : Item.REGISTRY)
		{
			item.getSubItems(CreativeTabs.SEARCH, list);

			for (ItemStack stack : list)
			{
				if (!stack.isEmpty())
				{
					panel.add(new ItemStackButton(panel, stack));
				}
			}

			list.clear();
		}
	}
}