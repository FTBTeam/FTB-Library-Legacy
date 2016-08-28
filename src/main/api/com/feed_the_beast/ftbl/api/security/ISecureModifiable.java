package com.feed_the_beast.ftbl.api.security;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by LatvianModder on 12.07.2016.
 */
public interface ISecureModifiable extends ISecure
{
    void setOwner(@Nullable UUID id);

    void setPrivacyLevel(EnumPrivacyLevel level);
}
