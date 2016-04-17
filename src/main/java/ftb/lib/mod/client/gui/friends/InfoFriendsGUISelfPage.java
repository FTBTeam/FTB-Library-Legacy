package ftb.lib.mod.client.gui.friends;

import ftb.lib.api.*;
import ftb.lib.api.gui.PlayerActionRegistry;
import ftb.lib.api.info.InfoPage;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 24.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUISelfPage extends InfoFriendsGUIPage
{
	public InfoFriendsGUISelfPage()
	{
		super(ForgeWorldSP.inst.clientPlayer);
		onClientDataChanged();
	}
	
	public void onClientDataChanged()
	{
		clear();
		
		for(PlayerAction a : PlayerActionRegistry.getPlayerActions(PlayerAction.Type.SELF, playerLM, playerLM, true, true))
		{
			text.add(new InfoPlayerActionLine(this, playerLM, a));
		}
		
		InfoPage page = getSub("info").setTitle(new TextComponentTranslation("ftbl.button.info"));
		
		page.text.add(new InfoPlayerViewLine(this, playerLM));
		
		if(!playerLM.clientInfo.isEmpty())
		{
			for(String s : playerLM.clientInfo)
				page.printlnText(s);
			
			page.text.add(null);
		}
		
		page = new InfoPage("settings")
		{
			public void onClientDataChanged()
			{
				clear();
				
				/*
				FTBUPlayerDataSP data = FTBUPlayerDataSP.get(playerLM);
				
				booleanCommand("render_badge", data.getFlag(FTBUPlayerData.RENDER_BADGE));
				booleanCommand("chat_links", data.getFlag(FTBUPlayerData.CHAT_LINKS));
				booleanCommand("explosions", data.getFlag(FTBUPlayerData.EXPLOSIONS));
				booleanCommand("fake_players", data.getFlag(FTBUPlayerData.FAKE_PLAYERS));
				
				IChatComponent text1 = FTBLibMod.mod.chatComponent("security." + data.blocks.uname);
				text1.getChatStyle().setColor(data.blocks == PrivacyLevel.FRIENDS ? EnumChatFormatting.BLUE : (data.blocks == PrivacyLevel.PUBLIC ? EnumChatFormatting.GREEN : EnumChatFormatting.RED));
				InfoExtendedTextLine line = new InfoExtendedTextLine(this, FTBU.mod.chatComponent("player_setting.security_level").appendText(": ").appendSibling(text1));
				line.setClickAction(new ClickAction(ClickActionType.CMD, new JsonPrimitive("lmplayer_settings block_security toggle")));
				text.add(line);
				*/
			}
			
			/*
			private void booleanCommand(String s, boolean current)
			{
				ChatComponentText text1 = new ChatComponentText(Boolean.toString(current));
				text1.getChatStyle().setColor(current ? EnumChatFormatting.GREEN : EnumChatFormatting.RED);
				InfoExtendedTextLine line = new InfoExtendedTextLine(this, FTBU.mod.chatComponent("player_setting." + s).appendText(": ").appendSibling(text1));
				line.setClickAction(new ClickAction(ClickActionType.CMD, new JsonPrimitive("lmplayer_settings " + s + " toggle")));
				text.add(line);
			}*/
		};
		
		page.setTitle(new TextComponentTranslation("ftbl.button.settings"));
		page.onClientDataChanged();
		addSub(page);
	}
}