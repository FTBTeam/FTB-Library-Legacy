package com.feed_the_beast.ftbl.api;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgePlayerFake extends ForgePlayerMP
{
    public ForgePlayerFake(FakePlayer p)
    {
        super(p.getGameProfile());
        setPlayer(p);
    }

    @Override
    public boolean isFake()
    {
        return true;
    }

    @Override
    public void sendUpdate()
    {
    }

    @Override
    public void sendInfoUpdate(ForgePlayer p)
    {
    }

    @Override
    public boolean isOP()
    {
        return false;
    }

    @Override
    public void getInfo(ForgePlayerMP owner, List<ITextComponent> info)
    {
    }
}