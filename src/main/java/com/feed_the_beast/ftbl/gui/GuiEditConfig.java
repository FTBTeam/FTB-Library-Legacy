package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiLang;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.PanelLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.SliderLM;
import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigEntry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryColor;
import com.feed_the_beast.ftbl.api.config.ConfigEntryDouble;
import com.feed_the_beast.ftbl.api.config.ConfigEntryInt;
import com.feed_the_beast.ftbl.api.config.ConfigEntryString;
import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.latmod.lib.LMColor;
import com.latmod.lib.annotations.Flags;
import com.latmod.lib.json.LMJsonUtils;
import com.latmod.lib.util.LMColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiEditConfig extends GuiLM
{
    public static final Comparator<Map.Entry<String, ConfigEntry>> COMPARATOR = (o1, o2) -> o1.getKey().compareTo(o2.getKey());

    public class ButtonConfigEntry extends ButtonLM
    {
        public final String ID;
        public final ConfigEntry entry;

        public ButtonConfigEntry(String id, ConfigEntry e)
        {
            super(0, 0, 0, 16);
            ID = id;
            entry = e;
            title = e.getDisplayName() == null ? ID : e.getDisplayName().getFormattedText();
        }

        @Override
        public boolean shouldRender(GuiLM gui)
        {
            double ay = getAY();
            return ay > -height && ay < screen.getScaledHeight();
        }

        @Override
        public void renderWidget(GuiLM gui)
        {
            boolean mouseOver = mouseY >= 20 && gui.isMouseOver(this);

            double ax = getAX();
            double ay = getAY();

            if(mouseOver)
            {
                GlStateManager.color(1F, 1F, 1F, 0.13F);
                drawBlankRect(ax, ay, width, height);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }

            font.drawString(title, (int) (ax + 4D), (int) (ay + 4D), mouseOver ? 0xFFFFFFFF : 0xFF999999);

            String s = entry.getAsString();

            int slen = font.getStringWidth(s);

            if(slen > 150)
            {
                s = font.trimStringToWidth(s, 150) + "...";
                slen = 152;
            }

            int textCol = 0xFF000000 | entry.getColor();
            if(mouseOver)
            {
                textCol = LMColorUtils.addBrightness(textCol, 60);
            }

            if(mouseOver && mouseX > ax + width - slen - 9)
            {
                GlStateManager.color(1F, 1F, 1F, 0.13F);
                drawBlankRect(ax + width - slen - 8, ay, slen + 8, height);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }

            font.drawString(s, screen.getScaledWidth() - (slen + 20), (int) (ay + 4D), textCol);
        }

        @Override
        public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
        {
            if(mouseY >= 20 && !entry.getFlag(Flags.CANT_EDIT))
            {
                FTBLibClient.playClickSound();

                ConfigEntryType type = entry.getConfigType();

                if(entry instanceof IClickable)
                {
                    ((IClickable) entry).onClicked(button);
                    GuiEditConfig.this.onChanged();
                }
                else
                {
                    switch(type)
                    {
                        case COLOR:
                        {
                            GuiSelectColor.display(null, ((ConfigEntryColor) entry).value, (id, val) ->
                            {
                                ((ConfigEntryColor) entry).value.set((LMColor) val);
                                GuiEditConfig.this.onChanged();
                                GuiEditConfig.this.openGui();
                            });

                            break;
                        }
                        case INT:
                        {
                            GuiSelectField.display(null, GuiSelectField.FieldType.INTEGER, entry.getAsInt(), (id, val) ->
                            {
                                ((ConfigEntryInt) entry).set((Integer) val);
                                GuiEditConfig.this.onChanged();
                                GuiEditConfig.this.openGui();
                            });

                            break;
                        }
                        case DOUBLE:
                        {
                            GuiSelectField.display(null, GuiSelectField.FieldType.DOUBLE, entry.getAsDouble(), (id, val) ->
                            {
                                ((ConfigEntryDouble) entry).set((Double) val);
                                GuiEditConfig.this.onChanged();
                                GuiEditConfig.this.openGui();
                            });

                            break;
                        }
                        case STRING:
                        {
                            GuiSelectField.display(null, GuiSelectField.FieldType.STRING, entry.getAsString(), (id, val) ->
                            {
                                ((ConfigEntryString) entry).set(val.toString());
                                GuiEditConfig.this.onChanged();
                                GuiEditConfig.this.openGui();
                            });

                            break;
                        }
                        case CUSTOM:
                        case INT_ARRAY:
                        case STRING_ARRAY:
                        {
                            GuiSelectField.display(null, GuiSelectField.FieldType.STRING, entry.getSerializableElement().toString(), (id, val) ->
                            {
                                entry.fromJson(LMJsonUtils.fromJson(val.toString()));
                                GuiEditConfig.this.onChanged();
                                GuiEditConfig.this.openGui();
                            });

                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void addMouseOverText(GuiLM gui, List<String> l)
        {
            if(mouseY > 18)
            {
                if(mouseX < getAX() + font.getStringWidth(title) + 10)
                {
                    String[] info = entry.getInfo();

                    if(info != null && info.length > 0)
                    {
                        for(String s : info)
                        {
                            l.addAll(font.listFormattedStringToWidth(s, 230));
                        }
                    }
                }

                if(entry.getAsGroup() == null && mouseX > screen.getScaledWidth() - (Math.min(150, font.getStringWidth(entry.getAsString())) + 25))
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
    }

    public final ConfigContainer configContainer;
    public final ConfigGroup configGroup;
    public final NBTTagCompound extraNBT;

    public final String title;
    public final List<ButtonConfigEntry> configEntryButtons;
    public final PanelLM configPanel;
    public final ButtonLM buttonAccept, buttonCancel;
    public final SliderLM scroll;
    private boolean changed = false;
    private int shouldClose = 0;

    public GuiEditConfig(NBTTagCompound nbt, ConfigContainer cc)
    {
        configContainer = cc;
        title = configContainer.getConfigTitle().getFormattedText();
        configGroup = (ConfigGroup) configContainer.createGroup().copy();
        extraNBT = nbt;

        configEntryButtons = new ArrayList<>();

        List<Map.Entry<String, ConfigEntry>> list = new ArrayList<>();
        list.addAll(configGroup.getFullEntryMap().entrySet());
        Collections.sort(list, COMPARATOR);

        for(Map.Entry<String, ConfigEntry> entry : list)
        {
            if(!entry.getValue().getFlag(Flags.HIDDEN))
            {
                configEntryButtons.add(new ButtonConfigEntry(entry.getKey(), entry.getValue()));
            }
        }

        configPanel = new PanelLM(0, 0, 0, 20)
        {
            @Override
            public void addWidgets()
            {
                height = 0;
                for(ButtonConfigEntry b : configEntryButtons)
                {
                    b.posY = height;
                    add(b);
                    height += b.height;
                }
            }
        };

        buttonAccept = new ButtonLM(0, 2, 16, 16)
        {
            @Override
            public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
            {
                FTBLibClient.playClickSound();
                shouldClose = 1;
                closeGui();
            }
        };

        buttonAccept.title = GuiLang.button_accept.translate();

        buttonCancel = new ButtonLM(0, 2, 16, 16)
        {
            @Override
            public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
            {
                FTBLibClient.playClickSound();
                shouldClose = 2;
                closeGui();
            }
        };

        buttonCancel.title = GuiLang.button_cancel.translate();

        scroll = new SliderLM(-16, 20, 16, 0, 10)
        {
            @Override
            public boolean canMouseScroll(GuiLM gui)
            {
                return true;
            }
        };

        scroll.isVertical = true;
        scroll.displayMin = scroll.displayMax = 0;
    }

    @Override
    public void onInit()
    {
        setFullscreen();
        buttonAccept.posX = width - 18;
        buttonCancel.posX = width - 38;
        scroll.posX = width - 16;
        scroll.height = height - 20;
        configPanel.posY = 20;
        scroll.value = 0F;

        for(ButtonConfigEntry b : configEntryButtons)
        {
            b.width = width - 16;
        }
    }

    @Override
    public void addWidgets()
    {
        configPanel.height = 20;
        configPanel.width = width;
        configPanel.posX = 0;
        configPanel.posY = 20;

        add(buttonAccept);
        add(buttonCancel);
        add(configPanel);
        add(scroll);
    }

    @Override
    public void onClosed()
    {
        if(shouldClose > 0)
        {
            if(changed && shouldClose == 1)
            {
                configContainer.saveConfig(mc.thePlayer, extraNBT, configGroup);
            }
        }
    }

    @Override
    public boolean onClosedByKey()
    {
        buttonCancel.onClicked(this, MouseButton.LEFT);
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

        if(configPanel.height + 20D > screen.getScaledHeight_double())
        {
            scroll.scrollStep = 40D / (configPanel.height + 20D);
            scroll.update(this);
            configPanel.posY = scroll.value * (screen.getScaledHeight_double() - configPanel.height - 20D) + 20D;
        }
        else
        {
            scroll.value = 0F;
            configPanel.posY = 20D;
        }

        configPanel.renderWidget(this);

        FTBLibClient.setGLColor(0x99333333);
        drawBlankRect(0, 0, (int) width, 20);
        GlStateManager.color(1F, 1F, 1F, 1F);
        drawCenteredString(font, title, width / 2D, 6, 0xFFFFFFFF);

        FTBLibClient.setGLColor(0x99333333);
        drawBlankRect(scroll.posX, scroll.posY, scroll.width, scroll.height);
        int sy = (int) (scroll.posY + scroll.value * (scroll.height - scroll.sliderSize));
        FTBLibClient.setGLColor(0x99666666);
        drawBlankRect(scroll.posX, sy, scroll.width, scroll.sliderSize);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);

        buttonAccept.render(GuiIcons.accept);
        buttonCancel.render(GuiIcons.cancel);
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}