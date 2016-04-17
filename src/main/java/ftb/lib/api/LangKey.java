package ftb.lib.api;

import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 17.04.2016.
 */
public final class LangKey extends FinalIDObject
{
	public LangKey(String s)
	{ super(s); }
	
	public LangKey sub(String id)
	{ return new LangKey(getID() + '.' + id); }
	
	@SideOnly(Side.CLIENT)
	public String format(Object... o)
	{ return I18n.format(getID(), o); }
	
	public ITextComponent chatComponent(Object... o)
	{ return new TextComponentTranslation(getID(), o); }
	
	public void printChat(ICommandSender ics, Object... o)
	{ if(ics != null) ics.addChatMessage(chatComponent(o)); }
	
	public void commandError(Object... o) throws CommandException
	{ throw new CommandException(getID(), o); }
}
