package com.feed_the_beast.ftbl.api;

import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by LatvianModder on 17.04.2016.
 */
public final class LangKey extends FinalIDObject
{
    private static final Object[] EMPTY_FORMATTING = new Object[0];

    public LangKey(String s)
    {
        super(s);
    }

    public String translate()
    {
        return I18n.format(getID(), EMPTY_FORMATTING);
    }

    public String translateFormatted(Object... o)
    {
        return I18n.format(getID(), o);
    }

    public ITextComponent textComponent(Object... o)
    {
        return new TextComponentTranslation(getID(), o);
    }

    public void printChat(ICommandSender ics, Object... o)
    {
        if(ics != null)
        {
            ics.addChatMessage(textComponent(o));
        }
    }

    public CommandException commandError(Object... o)
    {
        return new CommandException(getID(), o);
    }
}
