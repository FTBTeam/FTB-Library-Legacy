package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.EnumSelf;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.PlayerAction;
import com.feed_the_beast.ftbl.api.gui.PlayerActionRegistry;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 24.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUISelfPage extends InfoFriendsGUIPage
{
	public InfoFriendsGUISelfPage()
	{
		super(ForgeWorldSP.inst.clientPlayer);
	}
	
	@Override
	public void refreshGui(GuiInfo gui)
	{
		clear();
		
		text.add(new InfoPlayerViewLine(this, playerLM));
		
		if(!playerLM.clientInfo.isEmpty())
		{
			for(String s : playerLM.clientInfo)
				printlnText(s);
			
			text.add(null);
		}
		
		text.add(null);
		
		for(PlayerAction a : PlayerActionRegistry.getPlayerActions(EnumSelf.SELF, ForgeWorldSP.inst.clientPlayer, ForgeWorldSP.inst.clientPlayer, true, true))
		{
			text.add(new InfoPlayerActionLine(this, playerLM, a));
		}
	}
}