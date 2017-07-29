package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.TextBox;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiCreateTeam extends GuiBase
{
	private EnumTeamColor color;
	private final Button buttonAccept, buttonCancel;
	private final List<Button> colorButtons;
	private final TextBox textBoxId;

	public GuiCreateTeam()
	{
		super(154, 102);
		color = EnumTeamColor.NAME_MAP.getRandom(MathUtils.RAND);

		int bwidth = width / 2 - 6;
		buttonAccept = new Button(width - bwidth - 4, height - 20, bwidth, 16)
		{
			@Override
			public void onClicked(GuiBase gui, IMouseButton button)
			{
				GuiHelper.playClickSound();

				if (!textBoxId.getText().isEmpty())
				{
					FTBLibClient.execClientCommand("/ftb team create " + textBoxId.getText() + " " + color.getName());
					gui.closeGui();
				}
			}

			@Override
			public Color4I renderTitleInCenter(GuiBase gui)
			{
				return gui.getContentColor();
			}
		};

		buttonAccept.setTitle(GuiLang.BUTTON_ACCEPT.translate());
		buttonAccept.setIcon(Button.DEFAULT_BACKGROUND);

		buttonCancel = new Button(4, height - 20, bwidth, 16)
		{
			@Override
			public void onClicked(GuiBase gui, IMouseButton button)
			{
				GuiHelper.playClickSound();
				gui.closeGui();
			}

			@Override
			public Color4I renderTitleInCenter(GuiBase gui)
			{
				return gui.getContentColor();
			}
		};

		buttonCancel.setTitle(GuiLang.BUTTON_CANCEL.translate());
		buttonCancel.setIcon(Button.DEFAULT_BACKGROUND);

		textBoxId = new TextBox(4, 4, width - 8, 16)
		{
			@Override
			public void onTextChanged(GuiBase gui)
			{
				setText(gui, StringUtils.getId(getText(), StringUtils.FLAG_ID_DEFAULTS), false);
			}
		};

		textBoxId.writeText(this, FTBLibClient.MC.player.getGameProfile().getName().toLowerCase());
		textBoxId.background = Button.DEFAULT_BACKGROUND;
		textBoxId.ghostText = TextFormatting.ITALIC.toString() + TextFormatting.DARK_GRAY + "Enter ID";
		textBoxId.textColor = color.getColor();
		textBoxId.setFocused(true);
		textBoxId.charLimit = 35;

		colorButtons = new ArrayList<>();
		int i = 0;

		for (EnumTeamColor col : EnumTeamColor.NAME_MAP)
		{
			i++;

			Button b = new Button(4 + (i % 5) * 30, 24 + (i / 5) * 30, 25, 25)
			{
				@Override
				public void onClicked(GuiBase gui, IMouseButton button)
				{
					color = col;
					textBoxId.textColor = color.getColor();
				}
			};

			b.setIcon(new TexturelessRectangle(col.getColor()).setLineColor(DEFAULT_BACKGROUND.lineColor).setRoundEdges(true));
			b.setTitle(col.getTextFormatting() + col.getLangKey().translate());
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
	public IDrawableObject getIcon(GuiBase gui)
	{
		return DEFAULT_BACKGROUND;
	}
}