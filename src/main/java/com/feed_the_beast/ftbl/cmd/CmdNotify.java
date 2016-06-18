package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.cmd.ICustomCommandInfo;
import com.feed_the_beast.ftbl.api.notification.ClickAction;
import com.feed_the_beast.ftbl.api.notification.ClickActionType;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.json.LMJsonUtils;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
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

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender ics)
    {
        return "/" + commandName + " <player|@a> <json...>";
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, "{\"id\":\"test\", \"text\":[\"Title\"], \"click\":null}");
        }

        return super.getTabCompletionOptions(server, ics, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender ics, @Nonnull String[] args) throws CommandException
    {
        checkArgs(args, 2);
        EntityPlayerMP ep = getPlayer(server, ics, args[0]);
        String s = LMStringUtils.unsplitSpaceUntilEnd(1, args);
        Notification.deserialize(LMJsonUtils.fromJson(s)).sendTo(ep);
    }

    @Override
    public void addInfo(MinecraftServer server, ICommandSender sender, List<ITextComponent> list)
    {
        list.add(new TextComponentString("/" + commandName));
        list.add(null);

        list.add(new TextComponentString("Example:"));
        list.add(null);

        Notification n = new Notification("example_id")
                .addText(new TextComponentString("Example title"))
                .addText(new TextComponentString("Example description"))
                .setTimer(6500)
                .setColor(0xFFFF0000)
                .setItem(new ItemStack(Items.APPLE, 10));

        n.setClickAction(new ClickAction(ClickActionType.CMD, new JsonPrimitive("ftb reload")));

        for(String s : LMJsonUtils.toJson(LMJsonUtils.GSON_PRETTY, n.getSerializableElement()).split("\n"))
        {
            list.add(new TextComponentString(s));
        }

        list.add(null);
        list.add(new TextComponentString("Only \"id\" and \"title\" are required, the rest is optional"));
        list.add(new TextComponentString("\"mouse\":{} will make it permanent"));
    }
}