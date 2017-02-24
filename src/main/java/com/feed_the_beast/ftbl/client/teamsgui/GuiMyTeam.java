package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.PanelLM;
import com.feed_the_beast.ftbl.lib.gui.PlayerHeadImage;
import com.feed_the_beast.ftbl.lib.gui.SliderLM;
import com.feed_the_beast.ftbl.lib.gui.TextFieldLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.List;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class GuiMyTeam extends GuiLM
{
    private static final int COLOR = 0xFFC0C0C0;
    private static final int TOP_PANEL_HEIGHT = 20;
    private static final int BOTTOM_PANEL_HEIGHT = 20;
    private static final int LEFT_PANEL_WIDTH = 90;

    static final IDrawableObject BACKGROUND = new TexturelessRectangle(0xC8333333).setLineColor(COLOR).setRoundEdges();

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

            LMColorUtils.GL_COLOR.set(COLOR);
            //GuiHelper.render(ENTRY_TEX, ax, ay, getWidth(), getHeight());
            GuiHelper.drawBlankRect(ax, ay + getHeight(), getWidth() + 3, 1);
            GlStateManager.color(1F, 1F, 1F, 1F);
            getIcon(gui).draw(ax + 2, ay + 2, 8, 8);
            gui.getFont().drawString(playerInst.displayName, ax + 12, ay + 2, COLOR);

            if(isMouseOver(this))
            {
                LMColorUtils.GL_COLOR.set(0xFFE4FFFF, 100);
                GuiHelper.drawBlankRect(ax, ay, getWidth() + 3, getHeight());
            }

            GlStateManager.color(1F, 1F, 1F, 1F);
        }

        @Override
        public int compareTo(ButtonPlayer o)
        {
            return LMUtils.removeFormatting(getTitle(GuiMyTeam.this)).compareTo(LMUtils.removeFormatting(o.getTitle(GuiMyTeam.this)));
        }
    }

    private final Collection<String> permissions;
    private final TeamInst teamInfo;
    private final PanelLM panelPlayers, panelText;
    private final ButtonLM buttonTeamsGui, buttonTeamTitle, buttonTeamSettings, buttonExitTeam;
    private PlayerInst selectedPlayer;
    private final SliderLM scrollPlayers, scrollText;
    private int playersHeight, textHeight;

    public GuiMyTeam(TeamInst t, Collection<String> p)
    {
        super(0, 0);
        teamInfo = t;
        permissions = p;

        panelPlayers = new PanelLM(1, TOP_PANEL_HEIGHT, LEFT_PANEL_WIDTH - 1, 0)
        {
            @Override
            public void addWidgets()
            {
                for(PlayerInst p : teamInfo.players)
                {
                    add(new ButtonPlayer(p));
                }

                if(permissions.contains(FTBLibPerms.TEAM_MANAGE_MEMBERS) || permissions.contains(FTBLibPerms.TEAM_MANAGE_ALLIES) || permissions.contains(FTBLibPerms.TEAM_MANAGE_ENEMIES))
                {
                    add(new ButtonPlayer(PlayerInst.ADD_NEW));
                }

                updateWidgetPositions();
            }

            @Override
            public void updateWidgetPositions()
            {
                playersHeight = alignWidgetsByHeight();
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
                    add(new TextFieldLM(1, 0, getWidth() - 5, -1, getFont(), "Insane Chat! Wow! Here's some Latin: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam iaculis, velit sed laoreet iaculis, elit nisl commodo turpis, a faucibus quam augue at ipsum. Nam ante nunc, posuere quis quam eget, luctus commodo ipsum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Cras eget aliquet eros. Morbi lobortis faucibus ex eget pellentesque. Integer quis malesuada dui. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus velit tortor, cursus sed tellus nec, egestas dictum tortor. In sagittis, leo non volutpat varius, nisi leo posuere diam, et porttitor tellus mi vitae velit. Nulla porta nulla leo. Pellentesque odio massa, luctus non enim vel, luctus malesuada quam. \n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nulla eros, placerat nec mattis sed, tincidunt id ex. Proin posuere malesuada metus non bibendum. Cras vel quam sed lectus maximus lacinia sit amet vel mauris. Donec et mi eget lacus rutrum pulvinar. Etiam at pellentesque ante, porta laoreet elit. Nulla sit amet semper neque. Quisque efficitur massa ipsum, quis bibendum purus placerat nec. Morbi ac est gravida, condimentum lorem ultrices, accumsan justo. Phasellus faucibus nibh a tellus tempus, in aliquam odio porta. Quisque mi magna, ornare eget pulvinar non, pulvinar vehicula dolor. "));
                }
                else if(selectedPlayer == PlayerInst.ADD_NEW)
                {
                }
                else
                {
                }

                updateWidgetPositions();
            }

            @Override
            public void updateWidgetPositions()
            {
                textHeight = alignWidgetsByHeight();
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
            public int renderTitleInCenter()
            {
                return COLOR;
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
            public int renderTitleInCenter()
            {
                return COLOR;
            }
        };

        buttonTeamSettings = new ButtonLM(0, 2, 16, 16)
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                FTBLibClient.execClientCommand("/ftb team config", false);
            }
        };

        buttonTeamSettings.setTitle("Team Settings");
        buttonTeamSettings.setIcon(GuiIcons.SETTINGS);

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
            public int renderTitleInCenter()
            {
                return 0xFFEA8383;
            }
        };

        buttonExitTeam.setTitle("Exit Team");

        scrollPlayers = new SliderLM(LEFT_PANEL_WIDTH - 3, TOP_PANEL_HEIGHT, 3, 0, 14)
        {
            @Override
            public void onMoved(IGui gui)
            {
                panelPlayers.setScrollY(getValue(gui), playersHeight);
            }

            @Override
            public boolean canMouseScroll(IGui gui)
            {
                return gui.isMouseOver(panelPlayers);
            }
        };

        scrollText = new SliderLM(0, TOP_PANEL_HEIGHT, 3, 0, 14)
        {
            @Override
            public void onMoved(IGui gui)
            {
                panelText.setScrollY(getValue(gui), textHeight);
            }

            @Override
            public boolean canMouseScroll(IGui gui)
            {
                return gui.isMouseOver(panelText);
            }
        };
    }

    @Override
    public void onInit()
    {
        setWidth(getScreen().getScaledWidth() - 30);
        setHeight(getScreen().getScaledHeight() - 30);

        int width = getWidth();
        int height = getHeight();

        buttonTeamSettings.setX(width - 16 - 4);
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
        add(buttonTeamSettings);
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
        LMColorUtils.GL_COLOR.set(COLOR);
        GuiHelper.drawBlankRect(ax, ay + TOP_PANEL_HEIGHT - 1, width, 1);
        GuiHelper.drawBlankRect(ax, ay + height - BOTTOM_PANEL_HEIGHT, playerGui ? LEFT_PANEL_WIDTH : width, 1);
        GuiHelper.drawBlankRect(ax + width - 23, ay, 1, TOP_PANEL_HEIGHT);
        GuiHelper.drawBlankRect(ax + LEFT_PANEL_WIDTH, ay, 1, height);

        LMColorUtils.GL_COLOR.set(0xFF666666, 120);
        GuiHelper.drawBlankRect(ax + LEFT_PANEL_WIDTH - 3, ay + TOP_PANEL_HEIGHT, 3, height - TOP_PANEL_HEIGHT - BOTTOM_PANEL_HEIGHT);

        if(!playerGui)
        {
            GuiHelper.drawBlankRect(ax + width - 4, ay + TOP_PANEL_HEIGHT, 3, height - TOP_PANEL_HEIGHT - BOTTOM_PANEL_HEIGHT);
        }

        LMColorUtils.GL_COLOR.set(0xFFFFFFFF, 80);
        GuiHelper.drawBlankRect(scrollPlayers.getAX(), scrollPlayers.getAY() + scrollPlayers.getValueI(this, scrollPlayers.getHeight()), scrollPlayers.getWidth(), scrollPlayers.sliderSize);

        if(!playerGui)
        {
            GuiHelper.drawBlankRect(scrollText.getAX(), scrollText.getAY() + scrollText.getValueI(this, scrollText.getHeight()), scrollText.getWidth(), scrollText.sliderSize);
            getFont().drawString(TextFormatting.ITALIC + "Chat...", ax + LEFT_PANEL_WIDTH + 6, ay + height - 14, 0x33FFFFFF);
        }

        if(isMouseOver(buttonExitTeam))
        {
            LMColorUtils.GL_COLOR.set(0xFFE4FFFF, 100);
            GuiHelper.drawBlankRect(buttonExitTeam.getAX(), buttonExitTeam.getAY(), buttonExitTeam.getWidth(), buttonExitTeam.getHeight());
        }

        if(isMouseOver(buttonTeamsGui))
        {
            LMColorUtils.GL_COLOR.set(0xFFE4FFFF, 100);
            GuiHelper.drawBlankRect(buttonTeamsGui.getAX(), buttonTeamsGui.getAY(), buttonTeamsGui.getWidth(), buttonTeamsGui.getHeight());
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public IDrawableObject getIcon(IGui gui)
    {
        return BACKGROUND;
    }
}