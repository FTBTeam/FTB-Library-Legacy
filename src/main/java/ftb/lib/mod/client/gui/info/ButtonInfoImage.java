package ftb.lib.mod.client.gui.info;

import ftb.lib.TextureCoords;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.GuiLM;
import ftb.lib.api.info.InfoImageLine;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoImage extends ButtonInfoExtendedTextLine
{
	public TextureCoords texture;
	
	public ButtonInfoImage(GuiInfo g, InfoImageLine l)
	{
		super(g, l);
		
		texture = l.getDisplayImage();
		
		double w = Math.min(guiInfo.panelText.width, texture.width);
		double h = texture.getHeight(w);
		texture = new TextureCoords(texture.texture, 0, 0, w, h, w, h);
		
		width = texture.widthI();
		height = texture.heightI() + 1;
	}
	
	@Override
	public void renderWidget()
	{
		int ay = getAY();
		if(ay < -height || ay > guiInfo.mainPanel.height) { return; }
		int ax = getAX();
		
		boolean mouseOver = mouseOver();
		
		if(texture != null)
		{
			GlStateManager.color(1F, 1F, 1F, 1F);
			FTBLibClient.setTexture(texture.texture);
			GuiLM.render(texture, ax, ay, gui.getZLevel(), texture.width, texture.height);
		}
	}
}
