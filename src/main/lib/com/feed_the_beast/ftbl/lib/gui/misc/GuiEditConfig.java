package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.EnumDirection;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GuiEditConfig extends GuiBase implements IGuiEditConfig
{
    public static final Comparator<Map.Entry<IConfigKey, IConfigValue>> COMPARATOR = Comparator.comparing(o -> o.getKey().getDisplayName());
    public static final Color4I COLOR_BACKGROUND = new Color4I(false, 0x99333333);

    public class ButtonConfigEntry extends Button
    {
        public final IConfigKey key;
        public final IConfigValue value;
        public String keyText;
        public List<String> info;

        public ButtonConfigEntry(IConfigKey id, IConfigValue e)
        {
            super(0, 0, 0, 16);
            key = id;
            value = e;
            keyText = id.getDisplayName();
            String infoText = id.getInfo();

            if(!infoText.isEmpty())
            {
                info = new ArrayList<>();

                for(String s : infoText.split("\\\\n"))
                {
                    info.addAll(getFont().listFormattedStringToWidth(s, 230));
                }
            }

            if(info == null || info.isEmpty())
            {
                info = Collections.emptyList();
            }
        }

        @Override
        public void renderWidget(GuiBase gui)
        {
            boolean mouseOver = gui.getMouseY() >= 20 && gui.isMouseOver(this);

            int ax = getAX();
            int ay = getAY();

            if(mouseOver)
            {
                GuiHelper.drawBlankRect(ax, ay, width, height, Color4I.WHITE_A33);
            }

            gui.drawString(keyText, ax + 4, ay + 4, mouseOver ? Color4I.WHITE : Color4I.GRAY);
            GlStateManager.color(1F, 1F, 1F, 1F);

            String s = value.getString();

            int slen = gui.getFont().getStringWidth(s);

            if(slen > 150)
            {
                s = gui.getFont().trimStringToWidth(s, 150) + "...";
                slen = 152;
            }

            Color4I textCol = new Color4I(true, value.getColor(), 255);

            if(mouseOver)
            {
                textCol.addBrightness(60);
            }

            if(mouseOver && gui.getMouseX() > ax + width - slen - 9)
            {
                GuiHelper.drawBlankRect(ax + width - slen - 8, ay, slen + 8, height, Color4I.WHITE_A33);
            }

            gui.drawString(s, gui.width - (slen + 20), ay + 4, textCol);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }

        @Override
        public void onClicked(GuiBase gui, IMouseButton button)
        {
            if(gui.getMouseY() >= 20 && !key.getFlag(IConfigKey.CANT_EDIT))
            {
                GuiHelper.playClickSound();
                value.onClicked(GuiEditConfig.this, key, button);
            }
        }

        @Override
        public void addMouseOverText(GuiBase gui, List<String> list)
        {
            if(gui.getMouseY() > 18)
            {
                if(!info.isEmpty() && gui.getMouseX() < getAX() + gui.getFont().getStringWidth(keyText) + 10)
                {
                    list.addAll(info);
                }

                if(gui.getMouseX() > gui.width - (Math.min(150, gui.getFont().getStringWidth(value.getString())) + 25))
                {
                    value.addInfo(key, list);
                }
            }
        }
    }

    private final IConfigContainer configContainer;
    private final NBTTagCompound extraNBT;
    private final JsonObject modifiedConfig;

    private final String title;
    private final List<ButtonConfigEntry> configEntryButtons;
    private final Panel configPanel;
    private final Button buttonAccept, buttonCancel;
    private final PanelScrollBar scroll;
    private int shouldClose = 0;

    public GuiEditConfig(@Nullable NBTTagCompound nbt, IConfigContainer cc)
    {
        super(0, 0);
        configContainer = cc;

        ITextComponent title0 = configContainer.getTitle().createCopy();
        title0.getStyle().setBold(true);
        title = title0.getFormattedText();// + TextFormatting.DARK_GRAY + " [WIP GUI]";
        extraNBT = nbt;
        modifiedConfig = new JsonObject();

        configEntryButtons = new ArrayList<>();

        List<Map.Entry<IConfigKey, IConfigValue>> list = new ArrayList<>();
        list.addAll(configContainer.getConfigTree().getTree().entrySet());
        Collections.sort(list, COMPARATOR);

        for(Map.Entry<IConfigKey, IConfigValue> entry : list)
        {
            if(!entry.getKey().getFlag(IConfigKey.HIDDEN))
            {
                configEntryButtons.add(new ButtonConfigEntry(entry.getKey(), entry.getValue().copy()));
            }
        }

        configPanel = new Panel(0, 20, 0, 20)
        {
            @Override
            public void addWidgets()
            {
                addAll(configEntryButtons);
            }

            @Override
            public void updateWidgetPositions()
            {
                scroll.setElementSize(alignWidgets(EnumDirection.VERTICAL));
            }
        };

        configPanel.addFlags(Panel.FLAG_DEFAULTS);

        buttonAccept = new Button(0, 2, 16, 16, GuiLang.BUTTON_ACCEPT.translate())
        {
            @Override
            public void onClicked(GuiBase gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                shouldClose = 1;
                gui.closeGui();
            }
        };

        buttonAccept.setIcon(GuiIcons.ACCEPT);

        buttonCancel = new Button(0, 2, 16, 16, GuiLang.BUTTON_CANCEL.translate())
        {
            @Override
            public void onClicked(GuiBase gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                shouldClose = 2;
                gui.closeGui();
            }
        };

        buttonCancel.setIcon(GuiIcons.CANCEL);

        scroll = new PanelScrollBar(-16, 20, 16, 0, 10, configPanel);
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

        configPanel.setHeight(height - 20);
        configPanel.setWidth(width);

        scroll.posX = width - 16;
        scroll.setHeight(configPanel.height);

        for(ButtonConfigEntry b : configEntryButtons)
        {
            b.setWidth(scroll.posX);
        }
    }

    @Override
    public void addWidgets()
    {
        add(scroll);
        add(buttonAccept);
        add(buttonCancel);
        add(configPanel);
    }

    @Override
    public void onClosed()
    {
        if(shouldClose == 1 && !modifiedConfig.entrySet().isEmpty())
        {
            configContainer.saveConfig(mc.player, extraNBT, modifiedConfig);
        }
    }

    @Override
    public boolean onClosedByKey()
    {
        buttonCancel.onClicked(this, MouseButton.LEFT);
        return false;
    }

    @Override
    public void onChanged(IConfigKey key, JsonElement val)
    {
        modifiedConfig.add(key.getName(), val);
    }

    @Override
    public void drawBackground()
    {
        GuiHelper.drawBlankRect(0, 0, width, 20, COLOR_BACKGROUND);
        getFont().drawString(getTitle(this), 6, 6, 0xFFFFFFFF);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public String getTitle(GuiBase gui)
    {
        return title;
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}