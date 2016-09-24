package com.feed_the_beast.ftbl;

import com.latmod.lib.LangKey;

/**
 * Created by LatvianModder on 13.04.2016.
 */
public class FTBLibLang
{
    //@formatter:off
    public static final LangKey

            MODE_LOADED = get("mode.loaded"),
            MODE_NOT_FOUND = get("mode.not_found"),
            MODE_ALREADY_SET = get("mode.already_set"),
            MODE_CURRENT = get("mode.current"),
            MODE_LIST = get("mode.list"),
            RELOAD_SERVER = get("reload_server"),
            RELOAD_CLIENT = get("reload_client"),
            RELOAD_CLIENT_CONFIG_1 = get("reload_client_config_1"),
            RELOAD_CLIENT_CONFIG_2 = get("reload_client_config_2"),

    //@Deprecated
    RAW = get("raw"),
            COMMAND_PERMISSION = new LangKey("commands.generic.permission"),
            MISSING_ARGS = get("missing_args"),
            MISSING_ARGS_NUM = get("missing_args_num"),
            FEATURE_DISABLED = get("feature_disabled"),
            INVALID_SUBCMD = get("invalid_subcmd"),
            CLIENT_CONFIG = new LangKey("client_config"),
            OWNER = get("owner"),
            DELETE_ITEM = get("delete_item"),
            SERVER_FORCED = get("server_forced"),
            COMMANDS = get("commands"),
            MY_PERMISSIONS = get("my_permissions"),
            DIFFICULTY = get("difficulty"),

    TEAM_CONFIG = get("team.config"),
            TEAM_CREATED = get("team.created"),
            TEAM_DELETED = get("team.deleted"),
            TEAM_MEMBER_LEFT = get("team.member_left"),
            TEAM_MEMBER_JOINED = get("team.member_joined"),
            TEAM_TRANSFERED_OWNERSHIP = get("team.transfered_ownership"),
            TEAM_INVITED = get("team.invited"),
            TEAM_INVITED_YOU = get("team.invited_you"),
            TEAM_NOT_FOUND = get("team.error.not_found"),
            TEAM_NO_TEAM = get("team.error.no_team"),
            TEAM_MUST_LEAVE = get("team.error.must_leave"),
            TEAM_NOT_OWNER = get("team.error.not_owner"),
            TEAM_NOT_MEMBER = get("team.error.not_member"),
            TEAM_MUST_TRANSFER_OWNERSHIP = get("team.error.must_transfer_ownership"),
            TEAM_ALREADY_INVITED = get("team.error.already_invited"),
            TEAM_FAILED_TO_JOIN = get("team.error.failed_to_join");
    //@formatter:on

    private static LangKey get(String s)
    {
        return new LangKey("ftbl.lang." + s);
    }
}
