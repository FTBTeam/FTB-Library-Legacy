package ftb.lib.mod.client.gui.friends;

import ftb.lib.api.ForgePlayer;
import ftb.lib.api.ForgePlayerComparators;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.info.InfoPage;
import latmod.lib.LMColor;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUI extends InfoPage
{
	public InfoFriendsGUI()
	{
		super("friends_gui");
		setTitle(new TextComponentString("FriendsGUI"));
		
		List<ForgePlayer> tempPlayerList = new ArrayList<>();
		tempPlayerList.addAll(ForgeWorldSP.inst.playerMap.values());
		
		tempPlayerList.remove(ForgeWorldSP.inst.clientPlayer);
		
		//if(FTBUClient.sort_friends_az.get()) Collections.sort(tempPlayerList, LMPNameComparator.instance);
		//else Collections.sort(tempPlayerList, new LMPStatusComparator(LMWorldClient.inst.clientPlayer));
		
		Collections.sort(tempPlayerList, new ForgePlayerComparators.ByStatus(ForgeWorldSP.inst.clientPlayer));
		
		addSub(new InfoFriendsGUISelfPage());
		
		for(ForgePlayer p : tempPlayerList)
		{
			addSub(new InfoFriendsGUIPage(p.toPlayerSP()));
		}
	}
	
	@Override
	public LMColor getBackgroundColor()
	{ return new LMColor.RGB(30, 30, 30); }
	
	@Override
	public LMColor getTextColor()
	{ return new LMColor.RGB(200, 200, 200); }
	
	@Override
	public Boolean useUnicodeFont()
	{ return Boolean.FALSE; }
}