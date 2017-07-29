package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.client.DrawableItem;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.Iterator;
import java.util.List;

/**
 * @author LatvianModder
 */
public class Notification implements INotification
{
	private final ResourceLocation id;
	private final ITextComponent text;
	private int timer;
	private IDrawableObject icon;

	public Notification(ResourceLocation i)
	{
		id = i;
		text = StringUtils.text("");
		setDefaults();
	}

	public Notification(ResourceLocation i, ITextComponent c)
	{
		this(i);
		addLine(c);
	}

	public Notification(INotification n)
	{
		this(n.getId());
		addLine(n.getText().createCopy());
		setTimer(n.getTimer());
		setIcon(n.getIcon());
	}

	public Notification setError(ITextComponent title)
	{
		setDefaults();
		addLine(StringUtils.color(title.createCopy(), TextFormatting.DARK_RED));
		setIcon(new DrawableItem(new ItemStack(Blocks.BARRIER)));
		return this;
	}

	public void setDefaults()
	{
		text.getSiblings().clear();
		timer = 3000;
		icon = ImageProvider.NULL;
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	public boolean equals(Object o)
	{
		return o == this || (o instanceof INotification && ((INotification) o).getId().equals(getId()));
	}

	public String toString()
	{
		return getId() + ", text:" + getText() + ", timer:" + getTimer() + ", icon:" + getIcon().getJson();
	}

	public Notification addLine(ITextComponent component)
	{
		if (!text.getSiblings().isEmpty())
		{
			appendText("\n");
		}

		appendSibling(component);
		return this;
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public ITextComponent getText()
	{
		return text;
	}

	@Override
	public IDrawableObject getIcon()
	{
		return icon;
	}

	public Notification setIcon(IDrawableObject i)
	{
		icon = i;
		return this;
	}

	@Override
	public int getTimer()
	{
		return timer;
	}

	public Notification setTimer(int t)
	{
		timer = t;
		return this;
	}

	@Override
	public ITextComponent setStyle(Style style)
	{
		text.setStyle(style);
		return this;
	}

	@Override
	public Style getStyle()
	{
		return text.getStyle();
	}

	@Override
	public ITextComponent appendText(String string)
	{
		text.appendText(string);
		return this;
	}

	@Override
	public ITextComponent appendSibling(ITextComponent component)
	{
		text.appendSibling(component);
		return this;
	}

	@Override
	public String getUnformattedComponentText()
	{
		return text.getUnformattedComponentText();
	}

	@Override
	public String getUnformattedText()
	{
		return text.getUnformattedText();
	}

	@Override
	public String getFormattedText()
	{
		return text.getFormattedText();
	}

	@Override
	public List<ITextComponent> getSiblings()
	{
		return text.getSiblings();
	}

	@Override
	public ITextComponent createCopy()
	{
		return new Notification(this);
	}

	@Override
	public Iterator<ITextComponent> iterator()
	{
		return text.iterator();
	}
}