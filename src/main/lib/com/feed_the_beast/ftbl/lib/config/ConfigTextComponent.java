package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ConfigTextComponent extends ConfigValue
{
	public static final String ID = "text_component";

	private ITextComponent value;

	public ConfigTextComponent()
	{
	}

	public ConfigTextComponent(@Nullable ITextComponent c)
	{
		value = c;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Nullable
	public ITextComponent getText()
	{
		return value;
	}

	public void setText(@Nullable ITextComponent c)
	{
		value = c;
	}

	@Nullable
	@Override
	public Object getValue()
	{
		return getText();
	}

	@Override
	public String getString()
	{
		ITextComponent c = getText();
		return c == null ? "" : c.getFormattedText();
	}

	@Override
	public String toString()
	{
		return getSerializableElement().toString();
	}

	@Override
	public boolean getBoolean()
	{
		return !getString().isEmpty();
	}

	@Override
	public int getInt()
	{
		return getString().length();
	}

	@Override
	public ConfigTextComponent copy()
	{
		return new ConfigTextComponent(getText());
	}

	@Override
	public void fromJson(JsonElement e)
	{
		setText(JsonUtils.deserializeTextComponent(e));
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return JsonUtils.serializeTextComponent(getText());
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeTextComponent(getText());
	}

	@Override
	public void readData(DataIn data)
	{
		setText(data.readTextComponent());
	}
}