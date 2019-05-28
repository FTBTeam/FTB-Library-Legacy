package com.feed_the_beast.ftblib.command.client;

import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.ItemIcon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandListAdvancements extends CmdBase
{
	public CommandListAdvancements()
	{
		super("list_advancements", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, final String[] args) throws CommandException
	{
		if (!(sender instanceof EntityPlayer))
		{
			return;
		}

		List<Advancement> list = new ArrayList<>();

		for (Advancement a : Minecraft.getMinecraft().player.connection.getAdvancementManager().getAdvancementList().getAdvancements())
		{
			if (a.getDisplay() != null)
			{
				list.add(a);
			}
		}

		list.sort((o1, o2) -> o1.getDisplay().getTitle().getUnformattedText().compareToIgnoreCase(o2.getDisplay().getTitle().getUnformattedText()));

		GuiButtonListBase gui = new GuiButtonListBase()
		{
			@Override
			public void addButtons(Panel panel)
			{
				for (Advancement advancement : list)
				{
					panel.add(new SimpleTextButton(panel, advancement.getDisplay().getTitle().getFormattedText(), ItemIcon.getItemIcon(advancement.getDisplay().getIcon()))
					{
						@Override
						public void onClicked(MouseButton button)
						{
							GuiHelper.playClickSound();
							GuiScreen.setClipboardString(advancement.getId().toString());
							closeGui();
						}

						@Override
						public void addMouseOverText(List<String> list)
						{
							super.addMouseOverText(list);
							list.add(TextFormatting.GRAY + advancement.getId().toString());

							for (Map.Entry<String, Criterion> entry : advancement.getCriteria().entrySet())
							{
								list.add(TextFormatting.DARK_GRAY + "- " + entry.getKey());
							}
						}
					});
				}
			}
		};

		gui.setTitle(I18n.format("gui.advancements"));
		gui.setHasSearchBox(true);
		gui.openGuiLater();
	}
}