package ftb.lib.mod.client.gui.friends;

import ftb.lib.api.EnumSelf;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.PlayerAction;
import ftb.lib.api.gui.PlayerActionRegistry;
import ftb.lib.mod.client.FTBLibActions;
import ftb.lib.mod.client.gui.info.GuiInfo;
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
		
		text.add(new InfoPlayerActionLine(this, playerLM, FTBLibActions.my_server_settings));
		text.add(null);
		
		for(PlayerAction a : PlayerActionRegistry.getPlayerActions(EnumSelf.SELF, ForgeWorldSP.inst.clientPlayer, ForgeWorldSP.inst.clientPlayer, true, true))
		{
			if(a != FTBLibActions.my_server_settings)
			{
				text.add(new InfoPlayerActionLine(this, playerLM, a));
			}
		}
	}
}