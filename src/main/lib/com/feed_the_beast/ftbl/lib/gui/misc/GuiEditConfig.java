package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.PanelLM;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GuiEditConfig extends GuiLM implements IGuiEditConfig
{
    public static final Comparator<Map.Entry<IConfigKey, IConfigValue>> COMPARATOR = (o1, o2) -> o1.getKey().getDisplayName().getFormattedText().compareTo(o2.getKey().getDisplayName().getFormattedText());

    public class ButtonConfigEntry extends ButtonLM
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
            keyText = id.getDisplayName().getUnformattedText();

            if(keyText.startsWith("config.") && keyText.endsWith(".name"))
            {
                keyText = id.getName();
            }

            if(!id.getInfo().isEmpty())
            {
                info = getFont().listFormattedStringToWidth(id.getInfo(), 230);
            }
        }

        @Override
        public void renderWidget(IGui gui)
        {
            boolean mouseOver = getMouseY() >= 20 && gui.isMouseOver(this);

            int ax = getAX();
            int ay = getAY();

            if(mouseOver)
            {
                GlStateManager.color(1F, 1F, 1F, 0.13F);
                GuiHelper.drawBlankRect(ax, ay, getWidth(), getHeight());
                GlStateManager.color(1F, 1F, 1F, 1F);
            }

            getFont().drawString(keyText, ax + 4, ay + 4, mouseOver ? 0xFFFFFFFF : 0xFF999999);

            String s = value.getString();

            int slen = getFont().getStringWidth(s);

            if(slen > 150)
            {
                s = getFont().trimStringToWidth(s, 150) + "...";
                slen = 152;
            }

            int textCol = 0xFF000000 | value.getColor();
            if(mouseOver)
            {
                textCol = LMColorUtils.addBrightness(textCol, 60);
            }

            if(mouseOver && getMouseX() > ax + getWidth() - slen - 9)
            {
                GlStateManager.color(1F, 1F, 1F, 0.13F);
                GuiHelper.drawBlankRect(ax + getWidth() - slen - 8, ay, slen + 8, getHeight());
                GlStateManager.color(1F, 1F, 1F, 1F);
            }

            getFont().drawString(s, gui.getWidth() - (slen + 20), ay + 4, textCol);
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            if(getMouseY() >= 20 && !key.getFlag(IConfigKey.CANT_EDIT))
            {
                GuiHelper.playClickSound();
                value.onClicked(GuiEditConfig.this, key, button);
            }
        }

        @Override
        public void addMouseOverText(IGui gui, List<String> list)
        {
            if(getMouseY() > 18)
            {
                if(getMouseX() < getAX() + getFont().getStringWidth(keyText) + 10)
                {
                    if(info != null)
                    {
                        list.addAll(info);
                    }
                }

                if(getMouseX() > gui.getWidth() - (Math.min(150, getFont().getStringWidth(value.getString())) + 25))
                {
                    String min = value.getMinValueString();
                    String max = value.getMaxValueString();

                    list.add(TextFormatting.AQUA + "Def: " + key.getDefValue().getString());

                    if(min != null)
                    {
                        list.add(TextFormatting.AQUA + "Min: " + min);
                    }

                    if(max != null)
                    {
                        list.add(TextFormatting.AQUA + "Max: " + max);
                    }
                }
            }
        }
    }

    private final IConfigContainer configContainer;
    private final NBTTagCompound extraNBT;
    private final JsonObject modifiedConfig;

    private final String title;
    private final List<ButtonConfigEntry> configEntryButtons;
    private final PanelLM configPanel;
    private final ButtonLM buttonAccept, buttonCancel;
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

        configPanel = new PanelLM(0, 20, 0, 20)
        {
            @Override
            public void addWidgets()
            {
                addAll(configEntryButtons);
            }

            @Override
            public void updateWidgetPositions()
            {
                scroll.elementSize = alignWidgetsByHeight();
            }
        };

        configPanel.addFlags(IPanel.FLAG_DEFAULTS);

        buttonAccept = new ButtonLM(0, 2, 16, 16, GuiLang.BUTTON_ACCEPT.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                shouldClose = 1;
                gui.closeGui();
            }
        };

        buttonAccept.setIcon(GuiIcons.ACCEPT);

        buttonCancel = new ButtonLM(0, 2, 16, 16, GuiLang.BUTTON_CANCEL.translate())
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                shouldClose = 2;
                gui.closeGui();
            }
        };

        buttonCancel.setIcon(GuiIcons.CANCEL);

        scroll = new PanelScrollBar(-16, 20, 16, 0, 10, configPanel);
        scroll.oneElementSize = 16;
    }

    @Override
    public boolean isFullscreen()
    {
        return true;
    }

    @Override
    public void updateWidgetPositions()
    {
        buttonAccept.posX = getWidth() - 18;
        buttonCancel.posX = getWidth() - 38;

        configPanel.setHeight(getHeight() - 20);
        configPanel.setWidth(getWidth());

        scroll.posX = getWidth() - 16;
        scroll.setHeight(configPanel.getHeight());

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
            configContainer.saveConfig(mc.thePlayer, extraNBT, modifiedConfig);
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
        LMColorUtils.GL_COLOR.set(0x99333333);
        GuiHelper.drawBlankRect(0, 0, getWidth(), 20);
        getFont().drawString(getTitle(this), 6, 6, 0xFFFFFFFF);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public String getTitle(IGui gui)
    {
        return title;
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}