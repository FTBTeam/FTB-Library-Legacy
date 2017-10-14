package com.feed_the_beast.ftbl.api.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public interface ITeamGuiAction
{
	ResourceLocation getId();

	ITextComponent getTitle();

	boolean getRequireConfirm();

	Icon getIcon();

	int getOrder();

	boolean isAvailable(IForgeTeam team, IForgePlayer player);

	void onAction(IForgeTeam team, IForgePlayer player);
}