package ftb.lib.api.gui.widgets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.TextureCoords;
import ftb.lib.api.gui.GuiLM;
import ftb.lib.api.gui.IGuiLM;

import java.util.List;

@SideOnly(Side.CLIENT)
public class WidgetLM
{
	public final IGuiLM gui;
	public int posX, posY, width, height;
	public PanelLM parentPanel = null;
	public String title = null;
	
	public WidgetLM(IGuiLM g, int x, int y, int w, int h)
	{
		gui = g;
		posX = x;
		posY = y;
		width = w;
		height = h;
	}
	
	public boolean isEnabled()
	{ return true; }
	
	public boolean isVisible()
	{
		PanelLM p = gui.getMainPanel();
		int a0 = getAY();
		int a1 = p.getAY();
		
		if(a0 < a1 + p.height && a0 + height > a1)
		{
			a0 = getAX();
			a1 = p.getAX();
			return (a0 < a1 + p.width && a0 + width > a1);
		}
		
		return false;
	}
	
	public int getAX()
	{ return (parentPanel == null) ? posX : (parentPanel.getAX() + posX); }
	
	public int getAY()
	{ return (parentPanel == null) ? posY : (parentPanel.getAY() + posY); }
	
	protected boolean mouseOver(int ax, int ay)
	{ return gui.mouse().isInside(ax, ay, width, height); }
	
	public boolean mouseOver()
	{ return mouseOver(getAX(), getAY()); }
	
	public final void render(TextureCoords icon, double rw, double rh)
	{ GuiLM.render(icon, getAX(), getAY(), gui.getZLevel(), (int) (width * rw), (int) (height * rh)); }
	
	public final void render(TextureCoords icon)
	{ GuiLM.render(icon, getAX(), getAY(), gui.getZLevel(), width, height); }
	
	public void mousePressed(int b)
	{
	}
	
	public boolean keyPressed(int key, char keyChar)
	{
		return false;
	}
	
	public void addMouseOverText(List<String> l)
	{
		if(title != null) l.add(title);
	}
	
	public void renderWidget()
	{
	}
}