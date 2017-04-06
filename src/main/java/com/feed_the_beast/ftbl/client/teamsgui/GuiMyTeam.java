package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.ITeamMessage;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api_impl.ForgePlayerFake;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.ImmutableColor4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.CentredTextButton;
import com.feed_the_beast.ftbl.lib.gui.CheckBoxList;
import com.feed_the_beast.ftbl.lib.gui.EnumDirection;
import com.feed_the_beast.ftbl.lib.gui.ExtendedTextField;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.PlayerHeadImage;
import com.feed_the_beast.ftbl.lib.gui.TextBox;
import com.feed_the_beast.ftbl.lib.gui.TextField;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class GuiMyTeam extends GuiBase
{
    private static final int TOP_PANEL_HEIGHT = 20;
    private static final int BOTTOM_PANEL_HEIGHT = 20;
    private static final int LEFT_PANEL_WIDTH = 90;
    private static final Color4I EXIT_TEAM_COLOR = new ImmutableColor4I(0xFFEA8383);

    private class ButtonPlayer extends Button
    {
        private final MyTeamPlayerData playerInst;

        public ButtonPlayer(MyTeamPlayerData p)
        {
            super(0, 0, LEFT_PANEL_WIDTH - 4, 12);
            playerInst = p;
            setIcon(new PlayerHeadImage(p.playerName));
        }

        @Override
        public void onClicked(GuiBase gui, IMouseButton button)
        {
            selectedPlayer = playerInst;
            buttonTeamTitle.setTitle(selectedPlayer.playerName);
            scrollText.setValue(gui, 0D);
            gui.refreshWidgets();
        }

        @Override
        public void addMouseOverText(GuiBase gui, List<String> list)
        {
        }

        @Override
        public void renderWidget(GuiBase gui)
        {
            int ax = getAX();
            int ay = getAY();

            LMColorUtils.GL_COLOR.set(DEFAULT_BACKGROUND.lineColor);
            //GuiHelper.render(ENTRY_TEX, ax, ay, width, getHeight());
            GuiHelper.drawBlankRect(ax, ay + height, width + 3, 1, Color4I.WHITE);
            getIcon(gui).draw(ax + 2, ay + 2, 8, 8, Color4I.NONE);
            gui.drawString(playerInst.status.getColor() + playerInst.playerName, ax + 12, ay + 2);

            if(isMouseOver(this))
            {
                Button.DEFAULT_MOUSE_OVER.draw(ax, ay, width + 3, height, Color4I.NONE);
            }

            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }

    public static GuiMyTeam INSTANCE = null;

    private final MyTeamData teamInfo;
    private final Panel panelPlayers, panelText;
    private final Button buttonTeamsGui, buttonTeamTitle, buttonExitTeam;
    private MyTeamPlayerData selectedPlayer;
    private final PanelScrollBar scrollPlayers, scrollText;
    private final List<Widget> topPanelButtons;
    private final Map<UUID, MyTeamPlayerData> loadedProfiles;
    private final TextBox chatBox;

    public GuiMyTeam(MyTeamData t)
    {
        super(0, 0);
        INSTANCE = this;
        teamInfo = t;
        Collections.sort(teamInfo.players);

        loadedProfiles = new HashMap<>();

        for(MyTeamPlayerData p : teamInfo.players)
        {
            loadedProfiles.put(p.playerId, p);
        }

        panelPlayers = new Panel(1, TOP_PANEL_HEIGHT, LEFT_PANEL_WIDTH - 1, 0)
        {
            @Override
            public void addWidgets()
            {
                for(MyTeamPlayerData p : teamInfo.players)
                {
                    add(new ButtonPlayer(p));
                }
            }

            @Override
            public void updateWidgetPositions()
            {
                scrollPlayers.setElementSize(alignWidgets(EnumDirection.VERTICAL, 0, 1, 0));
                scrollPlayers.setSrollStepFromOneElementSize(13);
            }
        };

        panelPlayers.addFlags(Panel.FLAG_DEFAULTS);

        panelText = new Panel(LEFT_PANEL_WIDTH + 1, TOP_PANEL_HEIGHT, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                if(selectedPlayer == null)
                {
                    int messageID = 0;

                    for(ITeamMessage msg : teamInfo.chatHistory)
                    {
                        boolean sentByServer = msg.getSender().equals(ForgePlayerFake.SERVER.getId());
                        ITextComponent c;

                        if(!sentByServer && loadedProfiles.get(msg.getSender()) == null)
                        {
                            c = new TextComponentString("<Removed>");
                            c.getStyle().setColor(TextFormatting.DARK_GRAY);
                        }
                        else
                        {
                            c = new TextComponentString(sentByServer ? "" : ("<" + loadedProfiles.get(msg.getSender()).playerName + "> "));
                        }

                        c.appendSibling(msg.getMessage());

                        if(sentByServer)
                        {
                            c.getStyle().setColor(TextFormatting.DARK_AQUA);
                        }
                        else if(++messageID % 2 == 0)
                        {
                            c.getStyle().setColor(TextFormatting.WHITE);
                        }

                        add(new ExtendedTextField(1, 0, width - 5, -1, getFont(), c));
                    }
                }
                else if(teamInfo.me.status.isEqualOrGreaterThan(EnumTeamStatus.MOD))
                {
                    if(selectedPlayer.playerId.equals(mc.player.getGameProfile().getId()))
                    {
                        add(new TextField(4, 0, width - 5, -1, getFont(), "You can't edit yourself!"));
                    }
                    else if(selectedPlayer.playerId.equals(teamInfo.owner.playerId))
                    {
                        add(new TextField(4, 0, width - 5, -1, getFont(), "You can't edit owner!"));
                    }
                    else
                    {
                        add(new TextField(4, 0, width - 5, -1, getFont(), "ID: " + LMStringUtils.fromUUID(selectedPlayer.playerId)));

                        CheckBoxList checkBoxes = new CheckBoxList(4, 1, true);

                        EnumTeamStatus[] VALUES;

                        if(selectedPlayer.status.isEqualOrGreaterThan(EnumTeamStatus.MEMBER))
                        {
                            VALUES = new EnumTeamStatus[] {EnumTeamStatus.MEMBER, EnumTeamStatus.MOD};
                        }
                        else
                        {
                            VALUES = new EnumTeamStatus[] {EnumTeamStatus.NONE, EnumTeamStatus.INVITED, EnumTeamStatus.ALLY, EnumTeamStatus.ENEMY};
                        }

                        for(EnumTeamStatus status : VALUES)
                        {
                            CheckBoxList.CheckBoxEntry entry = new CheckBoxList.CheckBoxEntry(status.getColor() + status.getLangKey().translate())
                            {
                                @Override
                                public void onValueChanged()
                                {
                                    if(value > 0)
                                    {
                                        FTBLibClient.execClientCommand("/ftb team set_status " + selectedPlayer.playerId + " " + status.getName());
                                        selectedPlayer.status = status;
                                    }
                                }
                            };

                            checkBoxes.addBox(GuiMyTeam.this, entry);

                            if(status == selectedPlayer.status)
                            {
                                entry.select(1);
                            }
                        }

                        add(checkBoxes);

                        if(selectedPlayer.status.isEqualOrGreaterThan(EnumTeamStatus.MEMBER))
                        {
                            add(new CentredTextButton(4, 0, 40, 16, "Kick")
                            {
                                @Override
                                public void onClicked(GuiBase gui, IMouseButton button)
                                {
                                    GuiHelper.playClickSound();
                                    mc.displayGuiScreen(new GuiYesNo((result, id) ->
                                    {
                                        if(result)
                                        {
                                            FTBLibClient.execClientCommand("/ftb team kick " + selectedPlayer.playerName);
                                            selectedPlayer.status = EnumTeamStatus.NONE;
                                        }

                                        openGui();
                                    }, "Kick " + selectedPlayer.playerName + "?", "", 0));
                                }
                            });
                        }
                    }
                }
                else
                {
                    add(new TextField(1, 0, width - 5, -1, getFont(), "You don't have permission to manage players!"));
                }
            }

            @Override
            public void updateWidgetPositions()
            {
                if(selectedPlayer == null)
                {
                    scrollText.setElementSize(alignWidgets(EnumDirection.VERTICAL, 2, 1, 2));
                    scrollText.setSrollStepFromOneElementSize(11);
                }
                else
                {
                    scrollText.setElementSize(alignWidgets(EnumDirection.VERTICAL, 2, 4, 2));
                }
            }
        };

        panelText.addFlags(Panel.FLAG_DEFAULTS);

        buttonTeamsGui = new Button(0, 0, LEFT_PANEL_WIDTH, TOP_PANEL_HEIGHT)
        {
            @Override
            public void onClicked(GuiBase gui, IMouseButton button)
            {
                selectedPlayer = null;
                buttonTeamTitle.setTitle(teamInfo.displayName);
                scrollText.setValue(gui, 1D);
                gui.refreshWidgets();
            }

            @Override
            public Color4I renderTitleInCenter(GuiBase gui)
            {
                return gui.getContentColor();
            }
        };

        buttonTeamsGui.setTitle("Teams GUI");

        buttonTeamTitle = new Button(LEFT_PANEL_WIDTH + 1, 1, 0, TOP_PANEL_HEIGHT - 2)
        {
            @Override
            public void onClicked(GuiBase gui, IMouseButton button)
            {
            }

            @Override
            public void addMouseOverText(GuiBase gui, List<String> list)
            {
                if(!teamInfo.description.isEmpty())
                {
                    list.add(teamInfo.description);
                }
            }

            @Override
            public Color4I renderTitleInCenter(GuiBase gui)
            {
                return gui.getContentColor();
            }
        };

        buttonExitTeam = new Button(1, 0, LEFT_PANEL_WIDTH - 1, BOTTOM_PANEL_HEIGHT - 2)
        {
            @Override
            public void onClicked(GuiBase gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                mc.displayGuiScreen(new GuiYesNo((result, id) ->
                {
                    if(result)
                    {
                        FTBLibClient.execClientCommand("/ftb team leave");
                        closeGui();
                    }
                    else
                    {
                        openGui();
                    }
                }, "Exit Team?", TextFormatting.RED + "Warning: You can't rejoin the team, unless you are re-invited!", 0));
            }

            @Override
            public Color4I renderTitleInCenter(GuiBase gui)
            {
                return EXIT_TEAM_COLOR;
            }
        };

        buttonExitTeam.setTitle("Exit Team");

        scrollPlayers = new PanelScrollBar(LEFT_PANEL_WIDTH - 3, TOP_PANEL_HEIGHT, 3, 0, 14, panelPlayers);
        scrollText = new PanelScrollBar(0, TOP_PANEL_HEIGHT, 3, 0, 14, panelText);

        scrollText.background = scrollPlayers.background = new TexturelessRectangle(new ImmutableColor4I(0x78666666));
        scrollText.slider = scrollPlayers.slider = new TexturelessRectangle(new ImmutableColor4I(0x50FFFFFF));

        topPanelButtons = new ArrayList<>();

        if(teamInfo.me.status.isEqualOrGreaterThan(EnumTeamStatus.MOD))
        {
            Button b;

            b = new Button(0, 2, 16, 16)
            {
                @Override
                public void onClicked(GuiBase gui, IMouseButton button)
                {
                    GuiHelper.playClickSound();
                    FTBLibClient.execClientCommand("/ftb team gui add_player");
                    setTitle(GuiLang.BUTTON_REFRESH.translate());
                    setIcon(GuiIcons.REFRESH);
                }
            };

            b.setTitle(GuiLang.BUTTON_ADD.translate());
            b.setIcon(GuiIcons.ADD);
            topPanelButtons.add(b);

            b = new Button(0, 2, 16, 16)
            {
                @Override
                public void onClicked(GuiBase gui, IMouseButton button)
                {
                    GuiHelper.playClickSound();
                    FTBLibClient.execClientCommand("/ftb team config");
                }
            };

            b.setTitle("Team Settings");
            b.setIcon(GuiIcons.SETTINGS);
            topPanelButtons.add(b);
        }

        chatBox = new TextBox(LEFT_PANEL_WIDTH, 0, 0, BOTTOM_PANEL_HEIGHT)
        {
            @Override
            public void onEnterPressed(GuiBase gui)
            {
                FTBLibClient.execClientCommand("/ftb team msg " + getText());
                setText(gui, "");
                setFocused(true);
            }
        };

        chatBox.ghostText = TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC + "Chat...";
        chatBox.charLimit = 86;

        buttonTeamsGui.onClicked(this, MouseButton.LEFT);
    }

    @Override
    public void onInit()
    {
        setWidth(getScreen().getScaledWidth() - 30);
        setHeight(getScreen().getScaledHeight() - 30);

        for(int i = 0; i < topPanelButtons.size(); i++)
        {
            topPanelButtons.get(i).setX(width + (i - topPanelButtons.size()) * 20);
        }

        buttonExitTeam.setY(height - BOTTOM_PANEL_HEIGHT + 1);
        buttonTeamTitle.setWidth(width - LEFT_PANEL_WIDTH - 3 - topPanelButtons.size() * 20);
        panelPlayers.setHeight(height - TOP_PANEL_HEIGHT - BOTTOM_PANEL_HEIGHT);

        scrollPlayers.setHeight(panelPlayers.height);
        scrollText.setHeight(panelPlayers.height);
        scrollText.setX(width - 4);

        panelText.setWidth(width - LEFT_PANEL_WIDTH - 1);
        panelText.setHeight(panelPlayers.height);

        chatBox.setWidth(panelText.width);
        chatBox.setY(height - BOTTOM_PANEL_HEIGHT + 1);
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

        if(selectedPlayer == null)
        {
            add(chatBox);
        }

        scrollText.onMoved(this);
    }

    @Override
    public void drawBackground()
    {
        int ax = getAX();
        int ay = getAY();

        boolean playerGui = selectedPlayer != null;

        getIcon(this).draw(ax, ay, width, height, Color4I.NONE);
        GuiHelper.drawBlankRect(ax, ay + TOP_PANEL_HEIGHT - 1, width, 1, DEFAULT_BACKGROUND.lineColor);
        GuiHelper.drawBlankRect(ax, ay + height - BOTTOM_PANEL_HEIGHT, playerGui ? LEFT_PANEL_WIDTH : width, 1, DEFAULT_BACKGROUND.lineColor);

        if(!topPanelButtons.isEmpty())
        {
            GuiHelper.drawBlankRect(ax + width - 3 - topPanelButtons.size() * 20, ay, 1, TOP_PANEL_HEIGHT, DEFAULT_BACKGROUND.lineColor);
        }

        GuiHelper.drawBlankRect(ax + LEFT_PANEL_WIDTH, ay, 1, height, DEFAULT_BACKGROUND.lineColor);

        if(!playerGui)
        {
            //getFont().drawString(, ax + LEFT_PANEL_WIDTH + 6, ay + height - 14, 0x33FFFFFF);
        }

        if(isMouseOver(buttonExitTeam))
        {
            Button.DEFAULT_MOUSE_OVER.draw(buttonExitTeam, Color4I.NONE);
        }

        if(isMouseOver(buttonTeamsGui))
        {
            Button.DEFAULT_MOUSE_OVER.draw(buttonTeamsGui, Color4I.NONE);
        }
    }

    @Override
    public IDrawableObject getIcon(GuiBase gui)
    {
        return DEFAULT_BACKGROUND;
    }

    @Override
    public Color4I getContentColor()
    {
        return DEFAULT_BACKGROUND.lineColor;
    }

    public void loadAllPlayers(Collection<MyTeamPlayerData> players)
    {
        for(MyTeamPlayerData d : players)
        {
            MyTeamPlayerData d1 = loadedProfiles.get(d.playerId);

            if(d1 == null)
            {
                loadedProfiles.put(d.playerId, d);
                teamInfo.players.add(d);
            }
            else
            {
                d1.isOnline = d.isOnline;
                d1.status = d.status;
            }
        }

        Collections.sort(teamInfo.players);
        refreshWidgets();
    }

    public void printMessage(ITeamMessage message)
    {
        teamInfo.chatHistory.add(message);

        if(selectedPlayer == null)
        {
            panelText.refreshWidgets();
            scrollText.onMoved(this);
        }
    }
}