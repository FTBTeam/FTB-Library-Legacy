package com.feed_the_beast.ftbl.api.security;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by LatvianModder on 12.07.2016.
 */
public interface ISecure
{
    int SAVE_OWNER = 1;
    int SAVE_PRIVACY_LEVEL = 2;

    @Nullable
    UUID getOwner();

    @Nonnull
    EnumPrivacyLevel getPrivacyLevel();

    int getFlags();
}