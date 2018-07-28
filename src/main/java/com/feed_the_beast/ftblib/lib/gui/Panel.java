package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends Widget
{
	public final List<Widget> widgets;
	private int scrollX = 0, scrollY = 0;
	private int offsetX = 0, offsetY = 0;
	private boolean unicode = false, onlyRenderWidgetsInside = true, onlyInteractWithWidgetsInside = true;

	public Panel(Panel panel)
	{
		super(panel);
		widgets = new ArrayList<>();
	}

	public boolean getUnicode()
	{
		return unicode;
	}

	public void setUnicode(boolean value)
	{
		unicode = value;
	}

	public boolean getOnlyRenderWidgetsInside()
	{
		return onlyRenderWidgetsInside;
	}

	public void setOnlyRenderWidgetsInside(boolean value)
	{
		onlyRenderWidgetsInside = value;
	}

	public boolean getOnlyInteractWithWidgetsInside()
	{
		return onlyInteractWithWidgetsInside;
	}

	public void setOnlyInteractWithWidgetsInside(boolean value)
	{
		onlyInteractWithWidgetsInside = value;
	}

	public abstract void addWidgets();

	public abstract void alignWidgets();

	public void refreshWidgets()
	{
		widgets.clear();
		pushFontUnicode(getUnicode());

		try
		{
			addWidgets();
		}
		catch (MismatchingParentPanelException ex)
		{
			FTBLib.LOGGER.error(ex.getMessage());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		alignWidgets();

		for (Widget widget : widgets)
		{
			if (widget instanceof Panel)
			{
				((Panel) widget).refreshWidgets();
			}
		}

		alignWidgets();
		popFontUnicode();
	}

	public void add(Widget widget)
	{
		if (widget.parent != this)
		{
			throw new MismatchingParentPanelException(this, widget);
		}

		widgets.add(widget);
	}

	public void addAll(Iterable<? extends Widget> list)
	{
		for (Widget w : list)
		{
			add(w);
		}
	}

	protected final int align(WidgetLayout layout)
	{
		return layout.align(this);
	}

	@Override
	public int getAX()
	{
		return super.getAX() + offsetX;
	}

	@Override
	public int getAY()
	{
		return super.getAY() + offsetY;
	}

	public void setOffset(boolean flag)
	{
		if (flag)
		{
			offsetX = -scrollX;
			offsetY = -scrollY;
		}
		else
		{
			offsetX = offsetY = 0;
		}
	}

	public boolean isOffset()
	{
		return offsetX != 0 || offsetY != 0;
	}

	public void setScrollX(int scroll)
	{
		scrollX = scroll;
	}

	public void setScrollY(int scroll)
	{
		scrollY = scroll;
	}

	public int getScrollX()
	{
		return scrollX;
	}

	public int getScrollY()
	{
		return scrollY;
	}

	@Override
	public void draw()
	{
		boolean renderInside = getOnlyRenderWidgetsInside();
		pushFontUnicode(getUnicode());

		int ax = getAX();
		int ay = getAY();

		drawPanelBackground(ax, ay);

		if (renderInside)
		{
			GuiHelper.pushScissor(getScreen(), ax, ay, width, height);
		}

		setOffset(true);
		drawOffsetPanelBackground(ax + offsetX, ay + offsetY);

		for (int i = 0; i < widgets.size(); i++)
		{
			Widget widget = widgets.get(i);

			if (widget.shouldDraw() && (!renderInside || widget.collidesWith(ax, ay, width, height)))
			{
				drawWidget(widget, i, ax + offsetX, ay + offsetY, width, height);
			}
		}

		setOffset(false);

		if (renderInside)
		{
			GuiHelper.popScissor(getScreen());
		}

		popFontUnicode();
	}

	protected void drawPanelBackground(int ax, int ay)
	{
		getIcon().draw(ax, ay, width, height);
	}

	protected void drawOffsetPanelBackground(int ax, int ay)
	{
	}

	protected void drawWidget(Widget widget, int index, int ax, int ay, int w, int h)
	{
		widget.draw();

		if (GuiBase.renderDebugBoxes)
		{
			Color4I col = Color4I.rgb(java.awt.Color.HSBtoRGB((widget.hashCode() & 255) / 255F, 1F, 1F));
			GuiHelper.drawHollowRect(widget.getAX(), widget.getAY(), widget.width, widget.height, col.withAlpha(150), false);
			col.withAlpha(30).draw(widget.getAX() + 1, widget.getAY() + 1, widget.width - 2, widget.height - 2);
		}
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
		if (!shouldAddMouseOverText() || getOnlyInteractWithWidgetsInside() && !isMouseOver())
		{
			return;
		}

		pushFontUnicode(getUnicode());
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget widget = widgets.get(i);

			if (widget.shouldAddMouseOverText())
			{
				widget.addMouseOverText(list);

				if (GuiBase.renderDebugBoxes)
				{
					String s = widget.getClass().getSimpleName();

					if (s.isEmpty())
					{
						s = widget.getClass().getSuperclass().getSimpleName();
					}

					list.add(TextFormatting.DARK_GRAY + s + ": " + widget.width + "x" + widget.height);
				}
			}
		}

		setOffset(false);
		popFontUnicode();
	}

	@Override
	public void updateMouseOver(int mouseX, int mouseY)
	{
		super.updateMouseOver(mouseX, mouseY);
		setOffset(true);

		for (Widget widget : widgets)
		{
			widget.updateMouseOver(mouseX, mouseY);
		}

		setOffset(false);
	}

	@Override
	public boolean mousePressed(MouseButton button)
	{
		if (getOnlyInteractWithWidgetsInside() && !isMouseOver())
		{
			return false;
		}

		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget widget = widgets.get(i);

			if (widget.isEnabled())
			{
				if (widget.mousePressed(button))
				{
					setOffset(false);
					return true;
				}
			}
		}

		setOffset(false);
		return false;
	}

	@Override
	public void mouseReleased(MouseButton button)
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget widget = widgets.get(i);

			if (widget.isEnabled())
			{
				widget.mouseReleased(button);
			}
		}

		setOffset(false);
	}

	@Override
	public boolean mouseScrolled(int scroll)
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget widget = widgets.get(i);

			if (widget.isEnabled())
			{
				if (widget.mouseScrolled(scroll))
				{
					setOffset(false);
					return true;
				}
			}
		}

		setOffset(false);
		return false;
	}

	@Override
	public boolean keyPressed(int key, char keyChar)
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget widget = widgets.get(i);

			if (widget.isEnabled() && widget.keyPressed(key, keyChar))
			{
				setOffset(false);
				return true;
			}
		}

		setOffset(false);
		return false;
	}

	@Override
	public void keyReleased(int key)
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget widget = widgets.get(i);

			if (widget.isEnabled())
			{
				widget.keyReleased(key);
			}
		}

		setOffset(false);
	}

	@Override
	public void onClosed()
	{
		for (Widget widget : widgets)
		{
			widget.onClosed();
		}
	}

	@Nullable
	public Widget getWidget(int index)
	{
		return index < 0 || index >= widgets.size() ? null : widgets.get(index);
	}
}