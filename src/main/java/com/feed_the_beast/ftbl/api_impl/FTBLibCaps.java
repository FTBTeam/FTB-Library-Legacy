package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.security.ISecure;
import com.feed_the_beast.ftbl.api.security.ISecureStorage;
import com.feed_the_beast.ftbl.api.security.Security;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by LatvianModder on 15.05.2016.
 */
public class FTBLibCaps
{
    @CapabilityInject(ISecure.class)
    public static Capability<ISecure> SECURE = null;

    public static void init()
    {
        CapabilityManager.INSTANCE.register(ISecure.class, ISecureStorage.INSTANCE, () -> new Security(ISecure.SAVE_OWNER));
    }
}