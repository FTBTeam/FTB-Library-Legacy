package ftb.lib.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

/**
 * Created by LatvianModder on 29.03.2016.
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
	
	public IChatComponent chatComponent(Object... o)
	{ return new ChatComponentTranslation(getID(), o); }
	
	public void printChat(ICommandSender ics, Object... o)
	{ if(ics != null) ics.addChatMessage(chatComponent(o)); }
	
	public void commandError(Object... o)
	{ throw new CommandException(getID(), o); }
}