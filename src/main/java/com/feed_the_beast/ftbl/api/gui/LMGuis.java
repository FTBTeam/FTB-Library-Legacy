package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.callback.IColorCallback;
import com.feed_the_beast.ftbl.api.gui.callback.IFieldCallback;
import com.feed_the_beast.ftbl.gui.GuiSelectColor;
import com.feed_the_beast.ftbl.gui.GuiSelectField;
import latmod.lib.Converter;
import latmod.lib.LMColor;

public class LMGuis
{
	public static void displayColorSelector(IColorCallback cb, LMColor col, Object id, boolean instant)
	{ FTBLibClient.openGui(new GuiSelectColor(cb, col, id, instant)); }
	
	public static void displayFieldSelector(Object id, FieldType typ, Object d, IFieldCallback c)
	{ FTBLibClient.openGui(new GuiSelectField(id, typ, String.valueOf(d), c)); }
	
	public enum FieldType
	{
		STRING
				{
					@Override
					public boolean isValid(String s)
					{ return true; }
				},
		INTEGER
				{
					@Override
					public boolean isValid(String s)
					{ return Converter.canParseInt(s); }
				},
		DOUBLE
				{
					@Override
					public boolean isValid(String s)
					{ return Converter.canParseDouble(s); }
				};
		
		public abstract boolean isValid(String s);
	}
}