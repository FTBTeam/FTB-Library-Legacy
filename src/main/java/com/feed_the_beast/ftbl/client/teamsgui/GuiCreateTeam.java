package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftbl.lib.gui.TextBox;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
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
		super(162, 118);
		color = EnumTeamColor.NAME_MAP.getRandom(MathUtils.RAND);

		int bwidth = width / 2 - 10;
		buttonAccept = new SimpleTextButton(this, width - bwidth - 9, height - 24, GuiLang.ACCEPT.translate(), Icon.EMPTY)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();

				if (!textBoxId.getText().isEmpty())
				{
					gui.closeGui(false);
					ClientUtils.execClientCommand("/ftb team create " + textBoxId.getText() + " " + color.getName());
				}
			}

			@Override
			public boolean renderTitleInCenter()
			{
				return true;
			}
		};

		buttonAccept.setWidth(bwidth);
		buttonAccept.setHeight(16);

		buttonCancel = new SimpleTextButton(this, 8, height - 24, GuiLang.CANCEL.translate(), Icon.EMPTY)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				gui.closeGui();
			}

			@Override
			public boolean renderTitleInCenter()
			{
				return true;
			}
		};

		buttonCancel.setWidth(bwidth);
		buttonCancel.setHeight(16);

		textBoxId = new TextBox(this, 8, 8, width - 16, 16)
		{
			@Override
			public void onTextChanged()
			{
				setText(StringUtils.getId(getText(), StringUtils.FLAG_ID_DEFAULTS), false);
			}
		};

		textBoxId.writeText(ClientUtils.MC.player.getGameProfile().getName().toLowerCase());
		textBoxId.ghostText = TextFormatting.ITALIC.toString() + TextFormatting.DARK_GRAY + "Enter ID";
		textBoxId.textColor = color.getColor();
		textBoxId.setFocused(true);
		textBoxId.charLimit = 35;

		colorButtons = new ArrayList<>();
		int i = 0;

		for (EnumTeamColor col : EnumTeamColor.NAME_MAP)
		{
			Button b = new Button(this, 8 + (i % 5) * 30, 32 + (i / 5) * 30, 25, 25)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					color = col;
					textBoxId.textColor = color.getColor();
				}

				@Override
				public Icon getIcon()
				{
					return getTheme().getGui(false).withColor(col.getColor().mutable().setAlpha(color == col || gui.isMouseOver(this) ? 200 : 100));
				}
			};

			b.setTitle(col.getTextFormatting() + col.getLangKey().translate());
			colorButtons.add(b);
			i++;
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
}