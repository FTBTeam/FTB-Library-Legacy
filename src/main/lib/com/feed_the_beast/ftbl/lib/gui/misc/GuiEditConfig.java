package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.MutableColor4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigValueInfo;
import com.feed_the_beast.ftbl.lib.config.ConfigValueInstance;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.SimpleButton;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.feed_the_beast.ftbl.lib.icon.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiEditConfig extends GuiBase implements IGuiEditConfig
{
	public static final Color4I COLOR_BACKGROUND = Color4I.rgba(0x99333333);

	public class ButtonConfigGroup extends Button
	{
		public final String groupId;
		public String title = "-", info = "";
		public boolean collapsed = false;

		public ButtonConfigGroup(String id)
		{
			super(0, 0, 0, 16);
			groupId = id;

			if (!groupId.isEmpty())
			{
				StringBuilder current = new StringBuilder();
				StringBuilder text = new StringBuilder();

				String[] sa = groupId.split("\\.");

				for (int i = 0; i < sa.length; i++)
				{
					if (i != 0)
					{
						current.append(".");
					}

					current.append(sa[i]);
					String key = current.toString();

					text.append(group.getGroupName(key).getFormattedText());

					if (i != sa.length - 1)
					{
						text.append(" > ");
					}
				}

				title = text.toString();
				text.setLength(0);
				text.append(groupId);
				text.append(".info");
				String infoKey = text.toString();

				if (StringUtils.canTranslate(infoKey))
				{
					info = StringUtils.translate(infoKey);
				}
			}

			setCollapsed(collapsed);
		}

		public void setCollapsed(boolean v)
		{
			collapsed = v;
			setTitle((collapsed ? (TextFormatting.RED + "[-] ") : (TextFormatting.GREEN + "[v] ")) + TextFormatting.RESET + title);
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();

			GuiHelper.drawBlankRect(ax, ay, width, height, COLOR_BACKGROUND);
			gui.drawString(getTitle(gui), ax + 3, ay + 4, gui.getContentColor());
			GlStateManager.color(1F, 1F, 1F, 1F);

			if (gui.isMouseOver(this))
			{
				DEFAULT_MOUSE_OVER.draw(ax, ay, width, height, Color4I.NONE);
			}
		}

		@Override
		public void addMouseOverText(GuiBase gui, List<String> list)
		{
			if (!info.isEmpty())
			{
				list.add(info);
			}
		}

		@Override
		public void onClicked(GuiBase gui, MouseButton button)
		{
			setCollapsed(!collapsed);
			gui.refreshWidgets();
		}
	}

	public class ButtonConfigEntry extends Button
	{
		public final ButtonConfigGroup group;
		public final ConfigValueInfo info;
		public final ConfigValue value;
		public String keyText;
		public List<String> infoText;

		public ButtonConfigEntry(ButtonConfigGroup g, ConfigValueInfo i, ConfigValue e)
		{
			super(0, 0, 0, 16);
			group = g;
			info = i;
			value = e;
			String keyLang = GuiEditConfig.this.group.getNameKey(info);
			keyText = StringUtils.translate(keyLang);
			String infoText = StringUtils.canTranslate(keyLang + ".tooltip") ? StringUtils.translate(keyLang + ".tooltip") : "";

			if (!infoText.isEmpty())
			{
				this.infoText = new ArrayList<>();

				for (String s : infoText.split("\\\\n"))
				{
					this.infoText.addAll(getFont().listFormattedStringToWidth(s, 230));
				}
			}

			if (this.infoText == null || this.infoText.isEmpty())
			{
				this.infoText = Collections.emptyList();
			}
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			boolean mouseOver = gui.getMouseY() >= 20 && gui.isMouseOver(this);

			int ax = getAX();
			int ay = getAY();

			if (mouseOver)
			{
				GuiHelper.drawBlankRect(ax, ay, width, height, Color4I.WHITE_A[33]);
			}

			gui.drawString(keyText, ax + 4, ay + 4, mouseOver ? Color4I.WHITE : Color4I.GRAY);
			GlStateManager.color(1F, 1F, 1F, 1F);

			String s = value.getString();

			int slen = gui.getFont().getStringWidth(s);

			if (slen > 150)
			{
				s = gui.getFont().trimStringToWidth(s, 150) + "...";
				slen = 152;
			}

			MutableColor4I textCol = value.getColor().mutable();
			textCol.setAlpha(255);

			if (mouseOver)
			{
				textCol.addBrightness(60);

				if (gui.getMouseX() > ax + width - slen - 9)
				{
					GuiHelper.drawBlankRect(ax + width - slen - 8, ay, slen + 8, height, Color4I.WHITE_A[33]);
				}
			}

			gui.drawString(s, gui.width - (slen + 20), ay + 4, textCol);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}

		@Override
		public void onClicked(GuiBase gui, MouseButton button)
		{
			if (gui.getMouseY() >= 20 && !info.cantEdit)
			{
				GuiHelper.playClickSound();
				value.onClicked(GuiEditConfig.this, info, button);
			}
		}

		@Override
		public void addMouseOverText(GuiBase gui, List<String> list)
		{
			if (gui.getMouseY() > 18)
			{
				if (!infoText.isEmpty() && gui.getMouseX() < getAX() + gui.getFont().getStringWidth(keyText) + 10)
				{
					list.addAll(infoText);
				}

				if (gui.getMouseX() > gui.width - (Math.min(150, gui.getFont().getStringWidth(value.getString())) + 25))
				{
					value.addInfo(info, list);
				}
			}
		}
	}

	private final ConfigGroup group;
	private final IConfigCallback callback;
	private final JsonObject modifiedConfig;

	private final String title;
	private final List<Widget> configEntryButtons;
	private final Panel configPanel;
	private final Button buttonAccept, buttonCancel, buttonCollapseAll, buttonExpandAll;
	private final PanelScrollBar scroll;
	private int shouldClose = 0;

	public GuiEditConfig(ConfigGroup g, IConfigCallback c)
	{
		super(0, 0);
		group = g;
		callback = c;

		ITextComponent title0 = g.getTitle().createCopy();
		title0.getStyle().setBold(true);
		title = title0.getFormattedText();
		modifiedConfig = new JsonObject();

		configEntryButtons = new ArrayList<>();

		List<ConfigValueInstance> list = new ArrayList<>();

		for (ConfigValueInstance instance : group.getMap().values())
		{
			if (!instance.info.hidden)
			{
				list.add(instance);
			}
		}

		if (!list.isEmpty())
		{
			list.sort((o1, o2) ->
			{
				int i = o1.info.group.compareToIgnoreCase(o2.info.group);
				return i == 0 ? StringUtils.translate(group.getNameKey(o1.info)).compareToIgnoreCase(StringUtils.translate(group.getNameKey(o2.info))) : i;
			});

			ButtonConfigGroup group = null;

			for (ConfigValueInstance instance : list)
			{
				if (group == null || !group.groupId.equalsIgnoreCase(instance.info.group))
				{
					group = new ButtonConfigGroup(instance.info.group);
					configEntryButtons.add(group);
				}

				configEntryButtons.add(new ButtonConfigEntry(group, instance.info, instance.value.copy()));
			}
		}

		configPanel = new Panel(0, 20, 0, 20)
		{
			@Override
			public void addWidgets()
			{
				for (Widget w : configEntryButtons)
				{
					if (!(w instanceof ButtonConfigEntry) || !((ButtonConfigEntry) w).group.collapsed)
					{
						add(w);
					}
				}
			}

			@Override
			public void updateWidgetPositions()
			{
				scroll.setElementSize(align(WidgetLayout.VERTICAL));
			}
		};

		configPanel.addFlags(Panel.FLAG_DEFAULTS);

		scroll = new PanelScrollBar(-16, 20, 16, 0, 0, configPanel)
		{
			@Override
			public boolean shouldRender(GuiBase gui)
			{
				return true;
			}
		};

		scroll.slider = new TexturelessRectangle(0x99666666);
		scroll.background = new TexturelessRectangle(0x99333333);

		buttonAccept = new SimpleButton(0, 2, GuiLang.BUTTON_ACCEPT, GuiIcons.ACCEPT, (gui, button) ->
		{
			shouldClose = 1;
			gui.closeGui();
		});

		buttonCancel = new SimpleButton(0, 2, GuiLang.BUTTON_CANCEL, GuiIcons.CANCEL, (gui, button) ->
		{
			shouldClose = 2;
			gui.closeGui();
		});

		buttonCollapseAll = new SimpleButton(0, 2, GuiLang.BUTTON_COLLAPSE_ALL, GuiIcons.REMOVE, (gui, button) ->
		{
			for (Widget w : configEntryButtons)
			{
				if (w instanceof ButtonConfigGroup)
				{
					((ButtonConfigGroup) w).setCollapsed(true);
				}
			}

			scroll.setValue(gui, 0);
			gui.refreshWidgets();
		});

		buttonExpandAll = new SimpleButton(0, 2, GuiLang.BUTTON_EXPAND_ALL, GuiIcons.ADD, (gui, button) ->
		{
			for (Widget w : configEntryButtons)
			{
				if (w instanceof ButtonConfigGroup)
				{
					((ButtonConfigGroup) w).setCollapsed(false);
				}
			}

			scroll.setValue(gui, 0);
			gui.refreshWidgets();
		});
	}

	@Override
	public boolean isFullscreen()
	{
		return true;
	}

	@Override
	public void updateWidgetPositions()
	{
		buttonAccept.posX = width - 18;
		buttonCancel.posX = width - 38;
		buttonExpandAll.posX = width - 58;
		buttonCollapseAll.posX = width - 78;

		configPanel.setHeight(height - 20);
		configPanel.setWidth(width);

		scroll.posX = width - 16;
		scroll.setHeight(configPanel.height);

		for (Widget b : configEntryButtons)
		{
			b.setWidth(scroll.posX);
		}
	}

	@Override
	public void addWidgets()
	{
		addAll(buttonAccept, buttonCancel, buttonCollapseAll, buttonExpandAll);
		addAll(configPanel, scroll);
	}

	@Override
	public void onClosed()
	{
		if (shouldClose == 1 && !modifiedConfig.entrySet().isEmpty())
		{
			callback.saveConfig(group, ClientUtils.MC.player, modifiedConfig);
		}
	}

	@Override
	public boolean onClosedByKey()
	{
		buttonCancel.onClicked(this, MouseButton.LEFT);
		return false;
	}

	@Override
	public void onChanged(String key, JsonElement val)
	{
		modifiedConfig.add(key, val);
	}

	@Override
	public void drawBackground()
	{
		GuiHelper.drawBlankRect(0, 0, width, 20, COLOR_BACKGROUND);
		drawString(getTitle(this), 6, 6);
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	@Override
	public String getTitle(GuiBase gui)
	{
		return title;
	}
}