package ftb.lib.mod.client.gui.friends;

import ftb.lib.api.players.*;
import ftb.lib.mod.client.FTBLibModClient;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.*;

public class PanelPlayerList extends PanelFriendsGui
{
	private static final ArrayList<ForgePlayer> tempPlayerList = new ArrayList<>();
	
	public final ArrayList<ButtonPlayer> playerButtons;
	
	public PanelPlayerList(GuiFriends g)
	{
		super(g);
		width = 120;
		
		playerButtons = new ArrayList<>();
	}
	
	public boolean isEnabled()
	{ return gui.panelPopupMenu == null; }
	
	public void addWidgets()
	{
		tempPlayerList.clear();
		tempPlayerList.addAll(ForgeWorldSP.inst.playerMap.values());
		
		tempPlayerList.remove(ForgeWorldSP.inst.clientPlayer);
		
		if(FTBLibModClient.sort_friends_az.get()) Collections.sort(tempPlayerList, new ForgePlayerNameComparator());
		else Collections.sort(tempPlayerList, new ForgePlayerStatusComparator(ForgeWorldSP.inst.clientPlayer));
		
		playerButtons.clear();
		playerButtons.add(new ButtonPlayer(this, ForgeWorldSP.inst.clientPlayer));
		
		width = playerButtons.get(0).width;
		for(ForgePlayer aTempPlayerList : tempPlayerList)
		{
			ButtonPlayer b = new ButtonPlayer(this, aTempPlayerList.toPlayerSP());
			playerButtons.add(b);
			width = Math.max(width, b.width);
		}
		
		for(ButtonPlayer b : playerButtons)
		{
			b.width = width;
			add(b);
		}
	}
	
	public void renderWidget()
	{
		int size = playerButtons.size();
		if(size == 0) return;
		
		if(gui.mouse().x <= getAX() + width)
		{
			int scroll = 0;
			
			if(gui.mouse().dwheel != 0) scroll = ((gui.mouse().dwheel > 0) ? 28 : -28);
			
			if(Mouse.isButtonDown(0)) scroll += gui.mouse().dy;
			
			//if(Mouse.isButtonDown(1))
			//	scroll -= (int)((gui.mouseY - gui.lastClickY) * 0.1D);
			
			if(scroll != 0)
			{
				int newPos = posY + scroll;
				newPos = Math.min(newPos, 0);
				newPos = Math.max(newPos, (height) - size * 21);
				if(posY != newPos) posY = newPos;
			}
		}
		
		if(playerButtons.size() * 21 < height) posY = 0;
		
		for(ButtonPlayer playerButton : playerButtons) playerButton.renderWidget();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
	}
}