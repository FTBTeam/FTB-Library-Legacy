package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.client.ClientNotifications;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.feed_the_beast.ftbl.lib.info.InfoPageTheme;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoTeamsGUI extends InfoPage
{
    public static final ITextComponent TITLE = new TextComponentString("TeamsGUI");

    public InfoTeamsGUI()
    {
        super("teams_gui");
        setTitle(TITLE);
        theme = InfoPageTheme.DARK_NON_UNICODE;

        if(!ClientNotifications.Perm.MAP.isEmpty())
        {
            addSub(new InfoNotificationsPage());
        }

        println("Not yet implemented!");
        
        /*

        List<ForgePlayer> tempPlayerList = new ArrayList<>();
        tempPlayerList.addAll(ForgeWorldSP.inst.playerMap.values());

        tempPlayerList.remove(ForgeWorldSP.inst.clientPlayer);

        //if(FTBUClient.sort_friends_az.getMode()) Collections.sort(tempPlayerList, LMPNameComparator.instance);
        //else Collections.sort(tempPlayerList, new LMPStatusComparator(LMWorldClient.inst.clientPlayer));

        Collections.sort(tempPlayerList, ForgePlayerNameComparator.INSTANCE);

        addSub(ForgeWorldSP.inst.clientPlayer.getProfile().getName(), new InfoFriendsGUISelfPage());

        for(ForgePlayer p : tempPlayerList)
        {
            addSub(p.getProfile().getName(), new InfoFriendsGUIPage(p.toSP()));
        }
        */
    }
}