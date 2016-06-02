package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.config.ConfigEntry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryColor;
import com.feed_the_beast.ftbl.api.config.ConfigEntryDouble;
import com.feed_the_beast.ftbl.api.config.ConfigEntryInt;
import com.feed_the_beast.ftbl.api.config.ConfigEntryString;
import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.IConfigProvider;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IClickable;
import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.LMGuis;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.gui.widgets.PanelLM;
import com.feed_the_beast.ftbl.api.gui.widgets.SliderLM;
import latmod.lib.LMColor;
import latmod.lib.LMColorUtils;
import latmod.lib.LMJsonUtils;
import latmod.lib.annotations.Flags;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiEditConfig extends GuiLM implements IClientActionGui
{
    public static class ButtonConfigEntry extends ButtonLM
    {
        public final GuiEditConfig gui;
        public final ConfigEntry entry;
        public final ArrayList<ButtonConfigEntry> subButtons;
        public boolean expanded = false;

        public ButtonConfigEntry(GuiEditConfig g, ConfigEntry e)
        {
            super(g, 0, g.configPanel.height, g.width - 16, 16);
            gui = g;
            entry = e;
            title = g.provider.getEntryTitle(entry);
            subButtons = new ArrayList<>();
        }

        public boolean isVisible()
        {
            int ay = getAY();
            return ay > -height && ay < gui.height;
        }

        @Override
        public void renderWidget()
        {
            if(!isVisible())
            {
                return;
            }
            boolean mouseOver = gui.mouse().y >= 20 && mouseOver();

            int ax = getAX();
            int ay = getAY();
            boolean isGroup = entry.getAsGroup() != null;

            if(mouseOver)
            {
                GlStateManager.color(1F, 1F, 1F, 0.13F);
                drawBlankRect(ax, ay, gui.zLevel, width, height);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
            gui.drawString(gui.fontRendererObj, isGroup ? (((expanded ? "[-] " : "[+] ") + title)) : title, ax + 4, ay + 4, mouseOver ? 0xFFFFFFFF : (isGroup ? 0xFFCCCCCC : 0xFF999999));

            if(!isGroup)
            {
                String s = entry.getAsString();

                int slen = gui.fontRendererObj.getStringWidth(s);

                if(slen > 150)
                {
                    s = gui.fontRendererObj.trimStringToWidth(s, 150) + "...";
                    slen = 152;
                }

                int textCol = 0xFF000000 | entry.getColor();
                if(mouseOver)
                {
                    textCol = LMColorUtils.addBrightness(textCol, 60);
                }

                if(mouseOver && gui.mouse().x > ax + width - slen - 9)
                {
                    GlStateManager.color(1F, 1F, 1F, 0.13F);
                    drawBlankRect(ax + width - slen - 8, ay, gui.zLevel, slen + 8, height);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                }

                gui.drawString(gui.fontRendererObj, s, gui.width - (slen + 20), ay + 4, textCol);
            }
        }

        @Override
        public void onClicked(MouseButton button)
        {
            if(gui.mouse().y < 20)
            {
                return;
            }

            FTBLibClient.playClickSound();

            if(entry.getFlag(Flags.CANT_EDIT))
            {
                return;
            }

            ConfigEntryType type = entry.getConfigType();

            if(entry instanceof IClickable)
            {
                ((IClickable) entry).onClicked(button);
                gui.onChanged();
            }
            else if(entry.getAsGroup() != null)
            {
                expanded = !expanded;
                gui.refreshWidgets();
            }
            else if(type == ConfigEntryType.COLOR)
            {
                LMGuis.displayColorSelector(0, false, ((ConfigEntryColor) entry).value, c ->
                {
                    if(c.set)
                    {
                        ((ConfigEntryColor) entry).value.set((LMColor) c.object);
                        gui.onChanged();
                    }

                    if(c.close)
                    {
                        gui.mc.displayGuiScreen(gui);
                    }
                });
            }
            else if(type == ConfigEntryType.INT)
            {
                LMGuis.displayFieldSelector(entry.getFullID(), LMGuis.FieldType.INTEGER, entry.getAsInt(), c ->
                {
                    if(c.set)
                    {
                        ((ConfigEntryInt) entry).set((Integer) c.object);
                        gui.onChanged();
                    }

                    if(c.close)
                    {
                        gui.mc.displayGuiScreen(gui);
                    }
                });
            }
            else if(type == ConfigEntryType.DOUBLE)
            {
                LMGuis.displayFieldSelector(entry.getFullID(), LMGuis.FieldType.DOUBLE, entry.getAsDouble(), c ->
                {
                    if(c.set)
                    {
                        ((ConfigEntryDouble) entry).set((Double) c.object);
                        gui.onChanged();
                    }

                    if(c.close)
                    {
                        gui.mc.displayGuiScreen(gui);
                    }
                });
            }
            else if(type == ConfigEntryType.STRING)
            {
                LMGuis.displayFieldSelector(entry.getFullID(), LMGuis.FieldType.STRING, entry.getAsString(), c ->
                {
                    if(c.set)
                    {
                        ((ConfigEntryString) entry).set(c.toString());
                        gui.onChanged();
                    }

                    if(c.close)
                    {
                        gui.mc.displayGuiScreen(gui);
                    }
                });
            }
            else if(type == ConfigEntryType.CUSTOM || type == ConfigEntryType.INT_ARRAY || type == ConfigEntryType.STRING_ARRAY)
            {
                LMGuis.displayFieldSelector(entry.getFullID(), LMGuis.FieldType.STRING, entry.getSerializableElement().toString(), c ->
                {
                    if(c.set)
                    {
                        entry.fromJson(LMJsonUtils.fromJson(c.object.toString()));
                        gui.onChanged();
                    }

                    if(c.close)
                    {
                        gui.mc.displayGuiScreen(gui);
                    }
                });
            }
        }

        @Override
        public void addMouseOverText(List<String> l)
        {
            if(gui.mouse().x < gui.fontRendererObj.getStringWidth(title) + 10)
            {
                String[] info = entry.getInfo();

                if(info != null && info.length > 0)
                {
                    for(String s : info)
                    {
                        l.addAll(gui.mc.fontRendererObj.listFormattedStringToWidth(s, 230));
                    }
                }
            }

            if(entry.getAsGroup() == null && gui.mouse().x > gui.width - (Math.min(150, gui.fontRendererObj.getStringWidth(entry.getAsString())) + 25))
            {
                String def = entry.getDefValueString();
                String min = entry.getMinValueString();
                String max = entry.getMaxValueString();

                if(def != null)
                {
                    l.add(TextFormatting.AQUA + "Def: " + def);
                }
                if(min != null)
                {
                    l.add(TextFormatting.AQUA + "Min: " + min);
                }
                if(max != null)
                {
                    l.add(TextFormatting.AQUA + "Max: " + max);
                }
            }
        }
    }

    public final IConfigProvider provider;
    public final String title;
    public final List<ButtonConfigEntry> configEntryButtons;
    public final PanelLM configPanel;
    public final ButtonLM buttonClose, buttonExpandAll, buttonCollapseAll;
    public final SliderLM scroll;
    private boolean changed = false;
    private boolean shouldClose = false;

    public GuiEditConfig(GuiScreen g, IConfigProvider p)
    {
        super(g, null);
        provider = p;

        title = p.getConfigGroup().getDisplayName();

        configEntryButtons = new ArrayList<>();

        configPanel = new PanelLM(this, 0, 0, 0, 20)
        {
            @Override
            public void addWidgets()
            {
                height = 0;
                for(ButtonConfigEntry b : configEntryButtons)
                {
                    addCE(b);
                }
            }

            private void addCE(ButtonConfigEntry e)
            {
                add(e);
                e.posY = height;
                height += e.height;
                if(e.expanded)
                {
                    for(ButtonConfigEntry b : e.subButtons)
                    {
                        addCE(b);
                    }
                }
            }
        };

        buttonClose = new ButtonLM(this, 0, 2, 16, 16)
        {
            @Override
            public void onClicked(MouseButton button)
            {
                FTBLibClient.playClickSound();
                shouldClose = true;
                closeGui();
            }
        };

        buttonExpandAll = new ButtonLM(this, 2, 2, 16, 16)
        {
            @Override
            public void onClicked(MouseButton button)
            {
                FTBLibClient.playClickSound();
                for(ButtonConfigEntry e : configEntryButtons)
                {
                    expandAll(e);
                }
                gui.refreshWidgets();
            }

            private void expandAll(ButtonConfigEntry e)
            {
                if(e.entry.getAsGroup() != null)
                {
                    e.expanded = true;
                    for(ButtonConfigEntry e1 : e.subButtons)
                    {
                        expandAll(e1);
                    }
                }
            }
        };

        buttonCollapseAll = new ButtonLM(this, 20, 2, 16, 16)
        {
            @Override
            public void onClicked(MouseButton button)
            {
                FTBLibClient.playClickSound();
                for(ButtonConfigEntry e : configEntryButtons)
                {
                    collapseAll(e);
                }
                gui.refreshWidgets();
            }

            private void collapseAll(ButtonConfigEntry e)
            {
                if(e.entry.getAsGroup() != null)
                {
                    e.expanded = false;
                    for(ButtonConfigEntry e1 : e.subButtons)
                    {
                        collapseAll(e1);
                    }
                }
            }
        };

        scroll = new SliderLM(this, -16, 20, 16, 0, 10)
        {
            @Override
            public boolean canMouseScroll()
            {
                return true;
            }
        };
        scroll.isVertical = true;
        scroll.displayMin = scroll.displayMax = 0;
    }

    @Override
    public void initLMGui()
    {
        mainPanel.width = width;
        mainPanel.height = height;
        mainPanel.posX = mainPanel.posY = 0;
        buttonClose.posX = width - 18;
        scroll.posX = width - 16;
        scroll.height = height - 20;
        configPanel.posY = 20;
        scroll.value = 0F;

        if(configEntryButtons.isEmpty())
        {
            configEntryButtons.clear();

            for(ConfigEntry entry : provider.getConfigGroup().sortedEntries())
            {
                addCE(null, entry, 0);
            }
        }
    }

    private void addCE(ButtonConfigEntry parent, ConfigEntry e, int level)
    {
        if(!e.getFlag(Flags.HIDDEN))
        {
            ButtonConfigEntry b = new ButtonConfigEntry(this, e);
            b.posX += level * 12;
            b.width -= level * 12;
            if(parent == null)
            {
                b.expanded = true;
                configEntryButtons.add(b);
            }
            else
            {
                parent.subButtons.add(b);
            }

            ConfigGroup g = e.getAsGroup();
            if(g != null)
            {
                for(ConfigEntry entry : g.sortedEntries())
                {
                    addCE(b, entry, level + 1);
                }
            }
        }
    }

    @Override
    public void addWidgets()
    {
        configPanel.height = 20;
        configPanel.width = width;
        configPanel.posX = 0;
        configPanel.posY = 20;
        mainPanel.add(buttonClose);
        mainPanel.add(buttonExpandAll);
        mainPanel.add(buttonCollapseAll);
        mainPanel.add(configPanel);
        mainPanel.add(scroll);
    }

    @Override
    public void onLMGuiClosed()
    {
        if(shouldClose && changed)
        {
            provider.save();
        }
    }

    @Override
    public boolean onClosedByKey()
    {
        shouldClose = true;
        return false;
    }

    public void onChanged()
    {
        changed = true;
    }

    @Override
    public void drawBackground()
    {
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);

        if(configPanel.height + 20 > height)
        {
            scroll.scrollStep = 40F / (configPanel.height + 20F);
            scroll.update();
            configPanel.posY = (int) (scroll.value * (height - configPanel.height - 20)) + 20;
        }
        else
        {
            scroll.value = 0F;
            configPanel.posY = 20;
        }

        configPanel.renderWidget();

        drawRect(0, 0, width, 20, 0x99333333);
        drawCenteredString(fontRendererObj, title, width / 2, 6, 0xFFFFFFFF);

        drawRect(scroll.posX, scroll.posY, scroll.posX + scroll.width, scroll.posY + scroll.height, 0x99333333);
        int sy = (int) (scroll.posY + scroll.value * (scroll.height - scroll.sliderSize));
        drawRect(scroll.posX, sy, scroll.posX + scroll.width, sy + scroll.sliderSize, 0x99666666);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);

        buttonClose.render(GuiIcons.accept);
        buttonExpandAll.render(GuiIcons.add);
        buttonCollapseAll.render(GuiIcons.remove);
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}