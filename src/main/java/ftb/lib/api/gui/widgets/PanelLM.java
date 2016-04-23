package ftb.lib.api.gui.widgets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.gui.IGuiLM;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class PanelLM extends WidgetLM // GuiLM
{
	public final List<WidgetLM> widgets;
	protected final List<PanelLM> childPanels;
	
	public PanelLM(IGuiLM g, int x, int y, int w, int h)
	{
		super(g, x, y, w, h);
		widgets = new ArrayList<>();
		childPanels = new ArrayList<>();
	}
	
	public abstract void addWidgets();
	
	public void add(WidgetLM w)
	{
		if(w == null) return;
		w.parentPanel = this;
		widgets.add(w);
		
		if(w instanceof PanelLM)
		{
			PanelLM p = (PanelLM) w;
			childPanels.add(p);
			p.refreshWidgets();
		}
	}
	
	public void addAll(Iterable<? extends WidgetLM> l)
	{
		if(l != null)
		{
			for(WidgetLM w : l)
			{
				add(w);
			}
		}
	}
	
	public void refreshWidgets()
	{
		widgets.clear();
		addWidgets();
	}
	
	@Override
	public void addMouseOverText(List<String> l)
	{
		if(title != null) l.add(title);
		
		for(WidgetLM w : widgets)
			if(w.isEnabled() && w.mouseOver()) w.addMouseOverText(l);
	}
	
	@Override
	public void mousePressed(int b)
	{
		for(WidgetLM w : widgets)
			if(w.isEnabled()) w.mousePressed(b);
	}
	
	@Override
	public boolean keyPressed(int key, char keyChar)
	{
		for(WidgetLM w : widgets)
			if(w.isEnabled() && w.keyPressed(key, keyChar)) return true;
		return false;
	}
	
	@Override
	public void renderWidget()
	{
		for(WidgetLM w : widgets)
		{
			if(w.isVisible())
			{
				w.renderWidget();
			}
		}
	}
}