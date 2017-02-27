package com.feed_the_beast.ftbl.api;

import java.util.UUID;

/**
 * Created by LatvianModder on 27.02.2017.
 */
public interface ITeamMessage extends Comparable<ITeamMessage>
{
    UUID getSender();

    long getTime();

    String getMessage();
}