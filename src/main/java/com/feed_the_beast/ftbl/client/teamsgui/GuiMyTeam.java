package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.CheckBoxListLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.PanelLM;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.PlayerHeadImage;
import com.feed_the_beast.ftbl.lib.gui.TextFieldLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class GuiMyTeam extends GuiLM
{
    private static final int TOP_PANEL_HEIGHT = 20;
    private static final int BOTTOM_PANEL_HEIGHT = 20;
    private static final int LEFT_PANEL_WIDTH = 90;

    private class ButtonPlayer extends ButtonLM implements Comparable<ButtonPlayer>
    {
        private final PlayerInst playerInst;

        public ButtonPlayer(PlayerInst p)
        {
            super(0, 0, LEFT_PANEL_WIDTH - 4, 12);
            playerInst = p;

            if(p == PlayerInst.ADD_NEW)
            {
                setIcon(GuiIcons.ADD);
            }
            else
            {
                setIcon(new PlayerHeadImage(p.profile.getName()));
            }
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            selectedPlayer = playerInst;
            buttonTeamTitle.setTitle(selectedPlayer.displayName);
            scrollText.setValue(gui, 0D);
            gui.refreshWidgets();
        }

        @Override
        public void addMouseOverText(IGui gui, List<String> list)
        {
        }

        @Override
        public void renderWidget(IGui gui)
        {
            int ax = getAX();
            int ay = getAY();

            LMColorUtils.GL_COLOR.set(DEFAULT_BACKGROUND.lineColor);
            //GuiHelper.render(ENTRY_TEX, ax, ay, getWidth(), getHeight());
            GuiHelper.drawBlankRect(ax, ay + getHeight(), getWidth() + 3, 1);
            GlStateManager.color(1F, 1F, 1F, 1F);
            getIcon(gui).draw(ax + 2, ay + 2, 8, 8);
            gui.drawString(playerInst.status.getColor() + playerInst.displayName, ax + 12, ay + 2);

            if(isMouseOver(this))
            {
                ButtonLM.DEFAULT_MOUSE_OVER.draw(ax, ay, getWidth() + 3, getHeight());
            }

            GlStateManager.color(1F, 1F, 1F, 1F);
        }

        @Override
        public int compareTo(ButtonPlayer o)
        {
            int i = o.playerInst.status.getStatus() - playerInst.status.getStatus();

            if(i == 0)
            {
                i = LMUtils.removeFormatting(playerInst.displayName).compareTo(LMUtils.removeFormatting(o.playerInst.displayName));
            }

            return i;
        }
    }

    private final Collection<String> permissions;
    private final Collection<String> allPermissions;
    private final TeamInst teamInfo;
    private final PanelLM panelPlayers, panelText;
    private final ButtonLM buttonTeamsGui, buttonTeamTitle, buttonExitTeam;
    private PlayerInst selectedPlayer;
    private final PanelScrollBar scrollPlayers, scrollText;
    private final List<IWidget> topPanelButtons;

    public GuiMyTeam(TeamInst t, Collection<String> p, Collection<String> ap)
    {
        super(0, 0);
        teamInfo = t;
        permissions = p;
        allPermissions = ap;

        panelPlayers = new PanelLM(1, TOP_PANEL_HEIGHT, LEFT_PANEL_WIDTH - 1, 0)
        {
            @Override
            public void addWidgets()
            {
                List<ButtonPlayer> buttonList = new ArrayList<>(teamInfo.players.size());

                for(PlayerInst p : teamInfo.players)
                {
                    buttonList.add(new ButtonPlayer(p));
                }

                Collections.sort(buttonList);
                addAll(buttonList);

                if(permissions.contains(FTBLibPerms.TEAM_MANAGE_MEMBERS) || permissions.contains(FTBLibPerms.TEAM_MANAGE_ALLIES) || permissions.contains(FTBLibPerms.TEAM_MANAGE_ENEMIES))
                {
                    add(new ButtonPlayer(PlayerInst.ADD_NEW));
                }

                updateWidgetPositions();
            }

            @Override
            public void updateWidgetPositions()
            {
                scrollPlayers.elementSize = alignWidgetsByHeight();
            }
        };

        panelPlayers.addFlags(IPanel.FLAG_DEFAULTS);

        panelText = new PanelLM(LEFT_PANEL_WIDTH + 1, TOP_PANEL_HEIGHT, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                if(selectedPlayer == null)
                {
                    add(new TextFieldLM(1, 0, getWidth() - 5, -1, getFont(), "Insane Chat! Wow!"));
                }
                else if(selectedPlayer == PlayerInst.ADD_NEW)
                {
                }
                else if(selectedPlayer.profile.equals(mc.thePlayer.getGameProfile()))
                {
                    add(new TextFieldLM(1, 0, getWidth() - 5, -1, getFont(), "You can't edit your own permissions!"));
                }
                else
                {
                    CheckBoxListLM checkBoxes = new CheckBoxListLM(1, 1, false);

                    for(String s : allPermissions)
                    {
                        CheckBoxListLM.CheckBoxEntry entry = new CheckBoxListLM.CheckBoxEntry(s)
                        {
                            @Override
                            public void onValueChanged()
                            {
                                FTBLibClient.execClientCommand("/ftb team set_has_permission " + selectedPlayer.profile.getName() + " " + name + " " + isSelected, false);
                            }
                        };

                        checkBoxes.addBox(GuiMyTeam.this, entry);
                    }

                    add(checkBoxes);
                }

                updateWidgetPositions();
            }

            @Override
            public void updateWidgetPositions()
            {
                scrollText.elementSize = alignWidgetsByHeight();
            }
        };

        panelText.addFlags(IPanel.FLAG_DEFAULTS | IPanel.FLAG_UNICODE_FONT);

        buttonTeamsGui = new ButtonLM(0, 0, LEFT_PANEL_WIDTH, TOP_PANEL_HEIGHT)
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                selectedPlayer = null;
                buttonTeamTitle.setTitle(teamInfo.displayName);
                scrollText.setValue(gui, 1D);
                gui.refreshWidgets();
            }

            @Override
            public int renderTitleInCenter(IGui gui)
            {
                return gui.getTextColor();
            }
        };

        buttonTeamsGui.setTitle("Teams GUI");

        buttonTeamTitle = new ButtonLM(LEFT_PANEL_WIDTH + 1, 1, 0, TOP_PANEL_HEIGHT - 2)
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
            }

            @Override
            public void addMouseOverText(IGui gui, List<String> list)
            {
                if(!teamInfo.description.isEmpty())
                {
                    list.add(teamInfo.description);
                }
            }

            @Override
            public int renderTitleInCenter(IGui gui)
            {
                return gui.getTextColor();
            }
        };

        buttonExitTeam = new ButtonLM(1, 0, LEFT_PANEL_WIDTH - 1, BOTTOM_PANEL_HEIGHT - 2)
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                mc.displayGuiScreen(new GuiYesNo((result, id) ->
                {
                    if(result)
                    {
                        FTBLibClient.execClientCommand("/ftb team leave", false);
                        closeGui();
                    }
                    else
                    {
                        openGui();
                    }
                }, "Exit Team?", TextFormatting.RED + "Warning: You can't rejoin the team, unless you are re-invited!", 0));

                GuiHelper.playClickSound();
            }

            @Override
            public int renderTitleInCenter(IGui gui)
            {
                return 0xFFEA8383;
            }
        };

        buttonExitTeam.setTitle("Exit Team");

        scrollPlayers = new PanelScrollBar(LEFT_PANEL_WIDTH - 3, TOP_PANEL_HEIGHT, 3, 0, 14, panelPlayers);
        scrollText = new PanelScrollBar(0, TOP_PANEL_HEIGHT, 3, 0, 14, panelText);

        scrollText.background = scrollPlayers.background = new TexturelessRectangle(0x78666666);
        scrollText.slider = scrollPlayers.slider = new TexturelessRectangle(0x50FFFFFF);

        topPanelButtons = new ArrayList<>();

        if(permissions.contains(FTBLibPerms.TEAM_EDIT_SETTINGS))
        {
            ButtonLM b = new ButtonLM(0, 2, 16, 16)
            {
                @Override
                public void onClicked(IGui gui, IMouseButton button)
                {
                    GuiHelper.playClickSound();
                    FTBLibClient.execClientCommand("/ftb team config", false);
                }
            };

            b.setTitle("Team Settings");
            b.setIcon(GuiIcons.SETTINGS);
            topPanelButtons.add(b);
        }
    }

    @Override
    public void onInit()
    {
        setWidth(getScreen().getScaledWidth() - 30);
        setHeight(getScreen().getScaledHeight() - 30);

        int width = getWidth();
        int height = getHeight();

        for(int i = 0; i < topPanelButtons.size(); i++)
        {
            topPanelButtons.get(i).setX(width - i * 20);
        }

        buttonExitTeam.setY(height - BOTTOM_PANEL_HEIGHT + 1);
        buttonTeamTitle.setWidth(width - LEFT_PANEL_WIDTH - 24);
        panelPlayers.setHeight(height - TOP_PANEL_HEIGHT - BOTTOM_PANEL_HEIGHT);

        scrollPlayers.setHeight(panelPlayers.getHeight());
        scrollText.setHeight(panelPlayers.getHeight());
        scrollText.setX(width - 4);

        panelText.setWidth(width - LEFT_PANEL_WIDTH - 1);
        panelText.setHeight(panelPlayers.getHeight());
    }

    @Override
    public void addWidgets()
    {
        add(scrollPlayers);
        add(scrollText);
        add(buttonTeamsGui);
        add(buttonTeamTitle);
        add(panelPlayers);
        add(panelText);
        addAll(topPanelButtons);
        add(buttonExitTeam);
    }

    @Override
    public void drawBackground()
    {
        int ax = getAX();
        int ay = getAY();
        int width = getWidth();
        int height = getHeight();

        boolean playerGui = selectedPlayer != null;

        getIcon(this).draw(ax, ay, width, height);
        LMColorUtils.GL_COLOR.set(DEFAULT_BACKGROUND.lineColor);
        GuiHelper.drawBlankRect(ax, ay + TOP_PANEL_HEIGHT - 1, width, 1);
        GuiHelper.drawBlankRect(ax, ay + height - BOTTOM_PANEL_HEIGHT, playerGui ? LEFT_PANEL_WIDTH : width, 1);

        if(!topPanelButtons.isEmpty())
        {
            GuiHelper.drawBlankRect(ax + width - 3 - topPanelButtons.size() * 20, ay, 1, TOP_PANEL_HEIGHT);
        }

        GuiHelper.drawBlankRect(ax + LEFT_PANEL_WIDTH, ay, 1, height);

        if(!playerGui)
        {
            getFont().drawString(TextFormatting.ITALIC + "Chat...", ax + LEFT_PANEL_WIDTH + 6, ay + height - 14, 0x33FFFFFF);
        }

        if(isMouseOver(buttonExitTeam))
        {
            ButtonLM.DEFAULT_MOUSE_OVER.draw(buttonExitTeam);
        }

        if(isMouseOver(buttonTeamsGui))
        {
            ButtonLM.DEFAULT_MOUSE_OVER.draw(buttonTeamsGui);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public IDrawableObject getIcon(IGui gui)
    {
        return DEFAULT_BACKGROUND;
    }

    @Override
    public int getTextColor()
    {
        return DEFAULT_BACKGROUND.lineColor;
    }
}