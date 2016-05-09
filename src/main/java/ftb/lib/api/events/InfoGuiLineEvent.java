package ftb.lib.api.events;

import com.google.gson.JsonObject;
import ftb.lib.api.info.InfoExtendedTextLine;
import ftb.lib.api.info.InfoPage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 09.05.2016.
 */
@Cancelable
public class InfoGuiLineEvent extends Event
{
	public final InfoPage page;
	public final JsonObject json;
	public InfoExtendedTextLine line;
	
	public InfoGuiLineEvent(InfoPage p, JsonObject o)
	{
		page = p;
		json = o;
	}
}