package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgePlayerFake extends ForgePlayer
{
    ForgePlayerFake(FakePlayer p)
    {
        super(p.getGameProfile());
    }

    @Override
    public boolean isFake()
    {
        return true;
    }

    @Override
    public boolean isOP()
    {
        return false;
    }

    @Override
    public void getInfo(IForgePlayer owner, List<ITextComponent> info)
    {
    }
}