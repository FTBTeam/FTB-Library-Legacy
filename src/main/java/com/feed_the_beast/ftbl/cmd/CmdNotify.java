package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.cmd.ICustomCommandInfo;
import com.feed_the_beast.ftbl.api.notification.ClickAction;
import com.feed_the_beast.ftbl.api.notification.ClickActionType;
import com.feed_the_beast.ftbl.api.notification.MouseAction;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.google.gson.JsonPrimitive;
import latmod.lib.LMJsonUtils;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class CmdNotify extends CommandLM implements ICustomCommandInfo
{
    public CmdNotify()
    {
        super("notify");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return "/" + commandName + " <player|@a> <json...>";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, "{\"id\":\"test\", \"title\":\"Title\", \"mouse\":{}}");
        }

        return super.getTabCompletionOptions(server, ics, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
        checkArgs(args, 2);
        String s = LMStringUtils.unsplitSpaceUntilEnd(1, args);
        Notification n = Notification.deserialize(LMJsonUtils.fromJson(s));
        FTBLib.notifyPlayer(ep, n);
    }

    @Override
    public void addInfo(MinecraftServer server, ICommandSender sender, List<ITextComponent> list)
    {
        list.add(new TextComponentString("/" + commandName));
        list.add(null);

        list.add(new TextComponentString("Example:"));
        list.add(null);

        Notification n = new Notification("example_id", new TextComponentString("Example title"), 6500);
        n.setColor(0xFFFF0000);
        n.setItem(new ItemStack(Items.APPLE, 10));
        MouseAction ma = new MouseAction();
        ma.click = new ClickAction(ClickActionType.CMD, new JsonPrimitive("reload"));
        n.setMouseAction(ma);
        n.setDesc(new TextComponentString("Example description"));

        for(String s : LMJsonUtils.toJson(LMJsonUtils.getGson(true), n.getSerializableElement()).split("\n"))
        {
            list.add(new TextComponentString(s));
        }

        list.add(null);
        list.add(new TextComponentString("Only \"id\" and \"title\" are required, the rest is optional"));
        list.add(new TextComponentString("\"mouse\":{} will make it permanent"));
    }
}