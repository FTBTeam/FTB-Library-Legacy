package ftb.lib.api.gui.widgets;

import ftb.lib.TextureCoords;
import ftb.lib.api.gui.*;
import latmod.lib.MathHelperLM;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Mouse;

import java.util.List;

@SideOnly(Side.CLIENT)
public class SliderLM extends WidgetLM
{
	public boolean isGrabbed;
	public float value;
	public final int sliderSize;
	public int displayMin = 0;
	public int displayMax = 0;
	public boolean isVertical = false;
	public float scrollStep = 0.1F;
	
	public SliderLM(IGuiLM g, int x, int y, int w, int h, int ss)
	{
		super(g, x, y, w, h);
		sliderSize = ss;
	}
	
	public boolean update()
	{
		float v0 = value;
		
		if(isGrabbed)
		{
			if(Mouse.isButtonDown(0))
			{
				if(isVertical)
					value = (float) (gui.mouse().y - (gui.getMainPanel().posY + posY + (sliderSize / 2))) / (float) (height - sliderSize);
				else
					value = (float) (gui.mouse().x - (gui.getMainPanel().posY + posX + (sliderSize / 2))) / (float) (width - sliderSize);
				value = MathHelperLM.clampFloat(value, 0F, 1F);
			}
			else isGrabbed = false;
		}
		
		if(gui.mouse().dwheel != 0 && canMouseScroll())
		{
			value += (gui.mouse().dwheel < 0) ? scrollStep : -scrollStep;
			value = MathHelperLM.clampFloat(value, 0F, 1F);
		}
		
		return v0 != value;
	}
	
	public boolean canMouseScroll()
	{ return mouseOver(); }
	
	public int getValueI()
	{ return (int) (value * ((isVertical ? height : width) - sliderSize)); }
	
	public void renderSlider(TextureCoords tc)
	{
		if(isVertical) GuiLM.render(tc, getAX(), getAY() + getValueI(), gui.getZLevel(), width, sliderSize);
		else GuiLM.render(tc, getAX() + getValueI(), getAY(), gui.getZLevel(), sliderSize, height);
	}
	
	@Override
	public void mousePressed(int b)
	{
		if(mouseOver() && b == 0) isGrabbed = true;
	}
	
	@Override
	public void addMouseOverText(List<String> l)
	{
		if(displayMin == 0 && displayMax == 0) return;
		String s = "" + (int) MathHelperLM.map(value, 0D, 1D, displayMin, displayMax);
		if(title != null) s = title + ": " + s;
		l.add(s);
	}
}