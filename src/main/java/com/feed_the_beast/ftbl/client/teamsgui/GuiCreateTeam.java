package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.TextBoxLM;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class GuiCreateTeam extends GuiLM
{
    private EnumTeamColor color;
    private final ButtonLM buttonAccept, buttonCancel;
    private final List<ButtonLM> colorButtons;
    private final TextBoxLM textBoxId;

    public GuiCreateTeam()
    {
        super(154, 102);
        color = EnumTeamColor.VALUES[MathHelperLM.RAND.nextInt(EnumTeamColor.VALUES.length)];

        int bwidth = getWidth() / 2 - 6;
        buttonAccept = new ButtonLM(getWidth() - bwidth - 4, getHeight() - 20, bwidth, 16)
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();

                if(!textBoxId.getText().isEmpty())
                {
                    FTBLibClient.execClientCommand("/ftb team create " + textBoxId.getText() + " " + color.getName());
                    gui.closeGui();
                }
            }

            @Override
            public int renderTitleInCenter(IGui gui)
            {
                return gui.getTextColor();
            }
        };

        buttonAccept.setTitle(GuiLang.BUTTON_ACCEPT.translate());
        buttonAccept.setIcon(ButtonLM.DEFAULT_BACKGROUND);

        buttonCancel = new ButtonLM(4, getHeight() - 20, bwidth, 16)
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                gui.closeGui();
            }

            @Override
            public int renderTitleInCenter(IGui gui)
            {
                return gui.getTextColor();
            }
        };

        buttonCancel.setTitle(GuiLang.BUTTON_CANCEL.translate());
        buttonCancel.setIcon(ButtonLM.DEFAULT_BACKGROUND);

        textBoxId = new TextBoxLM(4, 4, getWidth() - 8, 16)
        {
            @Override
            public void onTextChanged(IGui gui)
            {
                setText(LMStringUtils.getID(getText(), LMStringUtils.FLAG_ID_DEFAULTS));
            }
        };

        textBoxId.setText(mc.thePlayer.getGameProfile().getName().toLowerCase());
        textBoxId.background = ButtonLM.DEFAULT_BACKGROUND;
        textBoxId.ghostText = TextFormatting.ITALIC.toString() + TextFormatting.DARK_GRAY + "Enter ID";
        textBoxId.textColor = color.getColor();
        textBoxId.setSelected(this, true);
        textBoxId.charLimit = 35;

        colorButtons = new ArrayList<>();

        for(int i = 0; i < EnumTeamColor.VALUES.length; i++)
        {
            final int i1 = i;

            ButtonLM b = new ButtonLM(4 + (i % 5) * 30, 24 + (i / 5) * 30, 25, 25)
            {
                @Override
                public void onClicked(IGui gui, IMouseButton button)
                {
                    color = EnumTeamColor.VALUES[i1];
                    textBoxId.textColor = color.getColor();
                }
            };

            b.setIcon(new TexturelessRectangle(EnumTeamColor.VALUES[i].getColor()).setLineColor(DEFAULT_BACKGROUND.lineColor).setRoundEdges(true));
            b.setTitle(EnumTeamColor.VALUES[i].getTextFormatting() + EnumTeamColor.VALUES[i].getLangKey().translate());
            colorButtons.add(b);
        }
    }

    @Override
    public void addWidgets()
    {
        add(buttonAccept);
        add(buttonCancel);
        addAll(colorButtons);
        add(textBoxId);
    }

    @Override
    public IDrawableObject getIcon(IGui gui)
    {
        return DEFAULT_BACKGROUND;
    }
}