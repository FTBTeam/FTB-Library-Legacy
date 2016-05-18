package com.feed_the_beast.ftbl.api;

import latmod.lib.util.FinalIDObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

/**
 * Created by LatvianModder on 17.04.2016.
 */
public final class LangKey extends FinalIDObject
{
    public LangKey(String s)
    { super(s); }
    
    public LangKey sub(String id)
    { return new LangKey(getID() + '.' + id); }
    
    public String translate()
    { return I18n.translateToLocal(getID()); }
    
    public String translateFormatted(Object... o)
    { return I18n.translateToLocalFormatted(getID(), o); }
    
    public ITextComponent textComponent(Object... o)
    { return new TextComponentTranslation(getID(), o); }
    
    public void printChat(ICommandSender ics, Object... o)
    { if(ics != null) { ics.addChatMessage(textComponent(o)); } }
    
    public CommandException commandError(Object... o)
    { return new CommandException(getID(), o); }
}
