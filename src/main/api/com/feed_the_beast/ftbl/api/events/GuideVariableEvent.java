package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Cancelable
public class GuideVariableEvent extends FTBLibEvent
{
	private final Side side;
	private final GuidePage page;
	private final ResourceLocation variable;
	private String value;

	public GuideVariableEvent(Side s, GuidePage p, ResourceLocation id)
	{
		side = s;
		page = p;
		variable = id;
		value = "default";
	}

	public Side getSide()
	{
		return side;
	}

	public GuidePage getPage()
	{
		return page;
	}

	public ResourceLocation getVariable()
	{
		return variable;
	}

	public void setValue(String val)
	{
		value = val;
	}

	public String getValue()
	{
		return value;
	}
}