package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.config.ConfigInt;
import com.feed_the_beast.ftblib.lib.config.ConfigItemStack;
import com.feed_the_beast.ftblib.lib.config.ConfigNBT;
import com.feed_the_beast.ftblib.lib.config.ConfigString;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.TextBox;
import com.feed_the_beast.ftblib.lib.gui.Theme;
import com.feed_the_beast.ftblib.lib.gui.Widget;
import com.feed_the_beast.ftblib.lib.gui.WidgetLayout;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.ItemIcon;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.feed_the_beast.ftblib.lib.util.NBTUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class GuiSelectItemStack extends GuiBase
{
	private static boolean allItems = true;

	private class ItemStackButton extends Button
	{
		private final ItemStack stack;

		private ItemStackButton(Panel panel, ItemStack is)
		{
			super(panel, "", GuiIcons.BARRIER);
			setSize(18, 18);
			stack = is;
			title = null;
			icon = null;
		}

		public boolean shouldAdd(String search, String mod)
		{
			if (search.isEmpty())
			{
				return true;
			}

			if (!mod.isEmpty())
			{
				return stack.getItem().getRegistryName().getNamespace().contains(mod);
			}

			return stack.getDisplayName().toLowerCase().contains(search);
		}

		@Override
		public String getTitle()
		{
			if (title == null)
			{
				title = stack.getDisplayName();
			}

			return title;
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			GuiHelper.addStackTooltip(stack, list, "");
		}

		@Override
		public WidgetType getWidgetType()
		{
			return InvUtils.stacksAreEqual(stack, selected) ? WidgetType.MOUSE_OVER : super.getWidgetType();
		}

		@Override
		public void drawBackground(Theme theme, int x, int y, int w, int h)
		{
			(getWidgetType() == WidgetType.MOUSE_OVER ? Color4I.LIGHT_GREEN.withAlpha(70) : Color4I.BLACK.withAlpha(50)).draw(x, y, w, h);
		}

		@Override
		public void drawIcon(Theme theme, int x, int y, int w, int h)
		{
			GuiHelper.drawItem(stack, x, y, w / 16D, h / 16D, true, Icon.EMPTY);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			selected = stack.copy();
		}
	}

	private class ButtonSwitchMode extends Button
	{
		private final Icon ICON_ALL = ItemIcon.getItemIcon(new ItemStack(Items.COMPASS));
		private final Icon ICON_INV = ItemIcon.getItemIcon(new ItemStack(Blocks.CHEST));

		public ButtonSwitchMode(Panel panel)
		{
			super(panel);
		}

		@Override
		public void drawIcon(Theme theme, int x, int y, int w, int h)
		{
			(allItems ? ICON_ALL : ICON_INV).draw(x, y, w, h);
		}

		@Override
		public String getTitle()
		{
			return I18n.format("ftblib.select_item.list_mode", TextFormatting.GRAY + (allItems ? I18n.format("ftblib.select_item.list_mode.all") : I18n.format("ftblib.select_item.list_mode.inv"))) + TextFormatting.DARK_GRAY + " [" + (panelStacks.widgets.size() - 1) + "]";
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			allItems = !allItems;
			panelStacks.refreshWidgets();
		}
	}

	private abstract class ButtonStackConfig extends Button implements IConfigValueEditCallback
	{
		public ButtonStackConfig(Panel panel, String title, Icon icon)
		{
			super(panel, title, icon);
		}

		@Override
		public WidgetType getWidgetType()
		{
			return selected.isEmpty() ? WidgetType.DISABLED : super.getWidgetType();
		}
	}

	private class ButtonEditData extends Button implements IConfigValueEditCallback
	{
		public ButtonEditData(Panel panel)
		{
			super(panel, "", GuiIcons.BUG);
		}

		@Override
		public void drawIcon(Theme theme, int x, int y, int w, int h)
		{
			GuiHelper.drawItem(selected, x, y, w / 16D, h / 16D, true, Icon.EMPTY);
		}

		@Override
		public String getTitle()
		{
			return selected.getDisplayName();
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			new GuiEditConfigValue("itemstack", new ConfigItemStack(selected.copy(), single), this).openGui();
		}

		@Override
		public void onCallback(ConfigValue value, boolean set)
		{
			if (set)
			{
				selected = ((ConfigItemStack) value).getStack();
			}

			openGui();
		}
	}

	private class ButtonCount extends ButtonStackConfig
	{
		public ButtonCount(Panel panel)
		{
			super(panel, I18n.format("ftblib.select_item.count"), ItemIcon.getItemIcon(new ItemStack(Items.PAPER)));
		}

		@Override
		public WidgetType getWidgetType()
		{
			return single ? WidgetType.DISABLED : super.getWidgetType();
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			new GuiEditConfigValue("count", new ConfigInt(selected.getCount(), 1, selected.getMaxStackSize()), this).openGui();
		}

		@Override
		public void onCallback(ConfigValue value, boolean set)
		{
			if (set)
			{
				selected.setCount(value.getInt());
			}

			openGui();
		}
	}

	private class ButtonMeta extends ButtonStackConfig
	{
		public ButtonMeta(Panel panel)
		{
			super(panel, I18n.format("ftblib.select_item.meta"), ItemIcon.getItemIcon(new ItemStack(Blocks.STONEBRICK, 1, 2)));
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			new GuiEditConfigValue("meta", new ConfigInt(selected.getMetadata(), 0, Short.MAX_VALUE), this).openGui();
		}

		@Override
		public void onCallback(ConfigValue value, boolean set)
		{
			if (set)
			{
				selected.setItemDamage(value.getInt());
			}

			openGui();
		}
	}

	private class ButtonNBT extends ButtonStackConfig
	{
		public ButtonNBT(Panel panel)
		{
			super(panel, I18n.format("ftblib.select_item.nbt"), ItemIcon.getItemIcon(new ItemStack(Items.NAME_TAG)));
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			new GuiEditConfigValue("nbt", new ConfigNBT(selected.getTagCompound()), this).openGui();
		}

		@Override
		public void onCallback(ConfigValue value, boolean set)
		{
			if (set)
			{
				selected.setTagCompound(((ConfigNBT) value).getNBT());
			}

			openGui();
		}
	}

	private class ButtonCaps extends ButtonStackConfig
	{
		public ButtonCaps(Panel panel)
		{
			super(panel, I18n.format("ftblib.select_item.caps"), ItemIcon.getItemIcon(new ItemStack(Blocks.ANVIL)));
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			NBTTagCompound nbt = selected.serializeNBT();
			new GuiEditConfigValue("caps", new ConfigNBT((NBTTagCompound) nbt.getTag("ForgeCaps")), this).openGui();
		}

		@Override
		public void onCallback(ConfigValue value, boolean set)
		{
			if (set)
			{
				NBTTagCompound caps = ((ConfigNBT) value).getNBT();
				NBTTagCompound nbt = selected.serializeNBT();

				if (caps == null)
				{
					nbt.removeTag("ForgeCaps");
				}
				else
				{
					nbt.setTag("ForgeCaps", caps);
				}

				selected = new ItemStack(nbt);
			}

			openGui();
		}
	}

	private class ButtonDisplayName extends ButtonStackConfig
	{
		public ButtonDisplayName(Panel panel)
		{
			super(panel, I18n.format("ftblib.select_item.display_name"), ItemIcon.getItemIcon(new ItemStack(Items.SIGN)));
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			new GuiEditConfigValue("name", new ConfigString(selected.hasDisplayName() ? selected.getDisplayName() : ""), this).openGui();
		}

		@Override
		public void onCallback(ConfigValue value, boolean set)
		{
			if (set)
			{
				if (!value.isEmpty())
				{
					selected.setStackDisplayName(value.getString());
				}
				else if (selected.hasTagCompound())
				{
					selected.getTagCompound().getCompoundTag("display").removeTag("Name");
					selected.setTagCompound(NBTUtils.minimize(selected.getTagCompound()));
				}
			}

			openGui();
		}
	}

	private final IOpenableGui callbackGui;
	private final Button buttonCancel, buttonAccept;
	private final Panel panelStacks;
	private final PanelScrollBar scrollBar;
	private TextBox searchBox;
	private ItemStack selected;
	private final boolean single;
	private final Consumer<ItemStack> callback;
	private final Panel tabs;

	public GuiSelectItemStack(IOpenableGui g, ItemStack is, boolean s, Consumer<ItemStack> c)
	{
		setSize(211, 150);
		callbackGui = g;
		selected = is;
		single = s;
		callback = c;

		int bsize = width / 2 - 10;

		buttonCancel = new SimpleTextButton(this, I18n.format("gui.cancel"), Icon.EMPTY)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				callbackGui.openGui();
			}

			@Override
			public boolean renderTitleInCenter()
			{
				return true;
			}
		};

		buttonCancel.setPosAndSize(8, height - 24, bsize, 16);

		buttonAccept = new SimpleTextButton(this, I18n.format("gui.accept"), Icon.EMPTY)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				callbackGui.openGui();
				callback.accept(selected);
			}

			@Override
			public boolean renderTitleInCenter()
			{
				return true;
			}
		};

		buttonAccept.setPosAndSize(width - bsize - 8, height - 24, bsize, 16);

		panelStacks = new Panel(this)
		{
			@Override
			public void addWidgets()
			{
				NonNullList<ItemStack> list = NonNullList.create();

				if (allItems)
				{
					for (Item item : Item.REGISTRY)
					{
						item.getSubItems(CreativeTabs.SEARCH, list);
					}

					list.add(new ItemStack(Blocks.COMMAND_BLOCK));
					list.add(new ItemStack(Blocks.BARRIER));
					list.add(new ItemStack(Blocks.STRUCTURE_VOID));
				}
				else
				{
					for (int i = 0; i < ClientUtils.MC.player.inventory.getSizeInventory(); i++)
					{
						ItemStack stack = ClientUtils.MC.player.inventory.getStackInSlot(i);

						if (!stack.isEmpty())
						{
							list.add(stack);
						}
					}
				}

				String search = searchBox.getText().toLowerCase();
				String mod = "";

				if (search.startsWith("@"))
				{
					mod = search.substring(1);
				}

				ItemStackButton button = new ItemStackButton(this, ItemStack.EMPTY);

				if (button.shouldAdd(search, mod))
				{
					add(new ItemStackButton(this, ItemStack.EMPTY));
				}

				for (ItemStack stack : list)
				{
					if (!stack.isEmpty())
					{
						button = new ItemStackButton(this, stack);

						if (button.shouldAdd(search, mod))
						{
							add(button);
						}
					}
				}
			}

			@Override
			public void alignWidgets()
			{
				for (int i = 0; i < widgets.size(); i++)
				{
					widgets.get(i).setPos(1 + (i % 9) * 19, 1 + (i / 9) * 19);
				}

				scrollBar.setPosAndSize(posX + width + 6, posY - 1, 16, height + 2);
				scrollBar.setMaxValue(1 + MathHelper.ceil(widgets.size() / 9F) * 19);
			}

			@Override
			public void drawBackground(Theme theme, int x, int y, int w, int h)
			{
				theme.drawPanelBackground(x, y, w, h);
			}
		};

		panelStacks.setPosAndSize(9, 24, 9 * 19 + 1, 5 * 19 + 1);

		scrollBar = new PanelScrollBar(this, panelStacks);
		scrollBar.setCanAlwaysScroll(true);
		scrollBar.setScrollStep(20);

		searchBox = new TextBox(this)
		{
			@Override
			public void onTextChanged()
			{
				panelStacks.refreshWidgets();
			}
		};

		searchBox.setPosAndSize(8, 7, width - 16, 12);
		searchBox.ghostText = I18n.format("gui.search_box");

		tabs = new Panel(this)
		{
			@Override
			public void addWidgets()
			{
				add(new ButtonSwitchMode(this));
				add(new ButtonEditData(this));
				add(new ButtonCount(this));
				add(new ButtonMeta(this));
				add(new ButtonNBT(this));
				add(new ButtonCaps(this));
				add(new ButtonDisplayName(this));
			}

			@Override
			public void alignWidgets()
			{
				for (Widget widget : widgets)
				{
					widget.setSize(20, 20);
				}

				setHeight(align(WidgetLayout.VERTICAL));
			}
		};

		tabs.setPosAndSize(-19, 8, 20, 0);
	}

	public GuiSelectItemStack(IOpenableGui g, Consumer<ItemStack> c)
	{
		this(g, ItemStack.EMPTY, false, c);
	}

	@Override
	public void addWidgets()
	{
		add(tabs);
		add(panelStacks);
		add(scrollBar);
		add(searchBox);
		add(buttonCancel);
		add(buttonAccept);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		GuiScreen screen = getPrevScreen();
		return screen != null && screen.doesGuiPauseGame();
	}
}