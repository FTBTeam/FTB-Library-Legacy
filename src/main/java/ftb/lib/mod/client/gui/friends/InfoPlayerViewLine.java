package ftb.lib.mod.client.gui.friends;

import ftb.lib.api.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.info.*;
import ftb.lib.mod.client.gui.info.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Mouse;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoPlayerViewLine extends InfoTextLine
{
	public final ForgePlayerSP playerLM;
	
	public InfoPlayerViewLine(InfoPage c, ForgePlayerSP p)
	{
		super(c, null);
		playerLM = p;
	}
	
	@SideOnly(Side.CLIENT)
	public ButtonInfoTextLine createWidget(GuiInfo gui)
	{ return new ButtonInfoPlayerView(gui, this); }
	
	public class ButtonInfoPlayerView extends ButtonInfoTextLine
	{
		private Player player;
		
		public ButtonInfoPlayerView(GuiInfo g, InfoPlayerViewLine w)
		{
			super(g, null);
			height = 1;
		}
		
		public void renderWidget()
		{
			int ay = getAY();
			if(ay < -height || ay > guiInfo.mainPanel.height) return;
			int ax = getAX();
			
			if(player == null) player = new Player(ForgeWorldSP.inst.clientPlayer);
			
			if(mouseOver() && Mouse.isButtonDown(1))
			{
				for(int i = 0; i < player.inventory.armorInventory.length; i++)
				{
					player.inventory.armorInventory[i] = null;
				}
			}
			else
			{
				EntityPlayer ep1 = playerLM.getPlayer();
				
				if(ep1 != null)
				{
					System.arraycopy(ep1.inventory.mainInventory, 0, player.inventory.mainInventory, 0, player.inventory.mainInventory.length);
					System.arraycopy(ep1.inventory.armorInventory, 0, player.inventory.armorInventory, 0, player.inventory.armorInventory.length);
					player.inventory.currentItem = ep1.inventory.currentItem;
				}
				else
				{
					for(int i = 0; i < 4; i++)
						player.inventory.armorInventory[i] = playerLM.lastArmor.get(i);
					player.inventory.mainInventory[0] = playerLM.lastArmor.get(4);
					player.inventory.currentItem = 0;
				}
			}
			
			GlStateManager.pushMatrix();
			
			int pheight = 120;
			int pwidth = (int) (pheight / 1.625F);
			
			int playerX = guiInfo.mainPanel.width - pwidth / 2 - 30;
			int playerY = ay + pheight + 10;
			
			pheight = pheight / 2;
			
			FTBLibClient.setTexture(player.getLocationSkin());
			GlStateManager.translate(0F, 0F, 100F);
			GuiInventory.drawEntityOnScreen(playerX, playerY, pheight, playerX - gui.mouse().x, playerY - (pheight + (pheight / 1.625F)) - gui.mouse().y, player);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.popMatrix();
		}
		
		//FIXME
		public class Player extends AbstractClientPlayer
		{
			public Player(ForgePlayerSP p)
			{
				super(Minecraft.getMinecraft().theWorld, p.getProfile());
			}
			
			public boolean equals(Object o)
			{ return playerLM.equals(o); }
			
			public void addChatMessage(ITextComponent i) { }
			
			public boolean canCommandSenderUseCommand(int i, String s)
			{ return false; }
			
			public BlockPos getPos()
			{ return BlockPos.ORIGIN; }
			
			public boolean isInvisibleToPlayer(EntityPlayer ep)
			{ return true; }
			
			public ResourceLocation getLocationSkin()
			{ return playerLM.getSkin(); }
			
			public boolean hasCape()
			{ return false; }
			
			public ResourceLocation getLocationCape()
			{ return null; }
		}
	}
}
