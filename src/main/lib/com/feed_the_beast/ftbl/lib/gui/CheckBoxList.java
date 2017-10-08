package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CheckBoxList extends Button
{
	public static class CheckBoxEntry
	{
		public String name;
		public int value = 0;
		private CheckBoxList checkBoxList;

		public CheckBoxEntry(String n)
		{
			name = n;
		}

		public void onClicked(MouseButton button, int index)
		{
			select((value + 1) % checkBoxList.getValueCount());
			GuiHelper.playClickSound();
		}

		public void addMouseOverText(List<String> list)
		{
		}

		public CheckBoxEntry select(int v)
		{
			if (checkBoxList.radioButtons)
			{
				if (v > 0)
				{
					for (CheckBoxEntry entry : checkBoxList.entries)
					{
						boolean old1 = entry.value > 0;
						entry.value = 0;

						if (old1)
						{
							entry.onValueChanged();
						}
					}
				}
				else
				{
					return this;
				}
			}

			int old = value;
			value = v;

			if (old != value)
			{
				onValueChanged();
			}

			return this;
		}

		public void onValueChanged()
		{
		}
	}

	public final boolean radioButtons;
	private final List<CheckBoxEntry> entries;

	public CheckBoxList(GuiBase gui, int x, int y, boolean radiobutton)
	{
		super(gui, x, y, 10, 2);
		radioButtons = radiobutton;
		entries = new ArrayList<>();
	}

	public int getValueCount()
	{
		return 2;
	}

	public Icon getCheckboxBackground()
	{
		return gui.getTheme().getCheckboxBackground(radioButtons);
	}

	public Icon getCheckboxIcon(int index, int value)
	{
		return gui.getTheme().getCheckbox(gui.isMouseOver(this), value != 0, radioButtons);
	}

	public void addBox(CheckBoxEntry checkBox)
	{
		checkBox.checkBoxList = this;
		entries.add(checkBox);
		setWidth(Math.max(width, gui.getStringWidth(checkBox.name)));
		setHeight(height + 11);
	}

	public CheckBoxEntry addBox(String name)
	{
		CheckBoxEntry entry = new CheckBoxEntry(name);
		addBox(entry);
		return entry;
	}

	@Override
	public void onClicked(MouseButton button)
	{
		int y = gui.getMouseY() - getAY();

		if (y % 11 == 10)
		{
			return;
		}

		int i = y / 11;

		if (i >= 0 && i < entries.size())
		{
			entries.get(i).onClicked(button, i);
		}
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
	}

	@Override
	public Icon getIcon()
	{
		return Icon.EMPTY;
	}

	@Override
	public void renderWidget()
	{
		int ax = getAX();
		int ay = getAY();
		getIcon().draw(ax, ay, width, height);
		Icon bg = getCheckboxBackground();

		for (int i = 0; i < entries.size(); i++)
		{
			CheckBoxEntry entry = entries.get(i);
			int y = ay + i * 11 + 1;
			bg.draw(ax, y, 10, 10);
			getCheckboxIcon(i, entry.value).draw(ax + 1, y + 1, 8, 8);
			gui.drawString(entry.name, ax + 12, y + 1);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}
}