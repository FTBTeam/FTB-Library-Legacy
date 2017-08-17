package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.lib.LangKey;

/**
 * @author LatvianModder
 */
public class FTBLibLang
{
	//@formatter:off
    public static final LangKey
    RELOAD_SERVER = get("reload_server"),
    RELOAD_CLIENT = get("reload_client"),
    RELOAD_CLIENT_CONFIG_1 = get("reload_client_config_1"),
    RELOAD_CLIENT_CONFIG_2 = get("reload_client_config_2"),

    //@Deprecated
    RAW = LangKey.of("raw"),
    WIP = LangKey.of("wip"),
	EXAMPLE = LangKey.of("example"),
	CLICK_HERE = LangKey.of("click_here"),
	FEATURE_DISABLED = LangKey.of("feature_disabled"),
	MISSING_ARGS = LangKey.of("missing_args"),
    MISSING_ARGS_NUM = LangKey.of("missing_args_num"),
	COMMAND_PERMISSION = LangKey.of("commands.generic.permission"),
	COMMAND_USAGE = LangKey.of("commands.generic.usage"),
    CLIENT_CONFIG = LangKey.of("client_config"),
    OWNER = get("owner"),
    DELETE_ITEM = get("delete_item"),
    SERVER_FORCED = get("server_forced"),
    COMMANDS = get("commands"),
    MY_PERMISSIONS = get("my_permissions"),
    ALL_PERMISSIONS = get("all_permissions"),
    DIFFICULTY = get("difficulty"),

    TEAM_CONFIG = get("team.config"),
    TEAM_CREATED = get("team.created"),
    TEAM_DELETED = get("team.deleted"),
    TEAM_MEMBER_LEFT = get("team.member_left"),
    TEAM_MEMBER_JOINED = get("team.member_joined"),
    TEAM_TRANSFERRED_OWNERSHIP = get("team.transferred_ownership"),
    TEAM_INVITED = get("team.invited"),
    TEAM_INVITED_YOU = get("team.invited_you"),
    TEAM_NOT_FOUND = get("team.error.not_found"),
    TEAM_NO_TEAM = get("team.error.no_team"),
    TEAM_MUST_LEAVE = get("team.error.must_leave"),
    TEAM_NOT_OWNER = get("team.error.not_owner"),
    TEAM_NOT_MEMBER = get("team.error.not_member"),
    TEAM_MUST_TRANSFER_OWNERSHIP = get("team.error.must_transfer_ownership"),
    TEAM_ALREADY_INVITED = get("team.error.already_invited"),
    TEAM_FAILED_TO_JOIN = get("team.error.failed_to_join"),
    TEAM_STATUS_SET = get("team.status.set"),
    TEAM_PERMISSION_OWNER = get("team.permission.owner"),
	TEAM_NOTIFICATION = get("team.notification"),
	TEAM_NOTIFICATION_HIDE = get("team.notification.hide");
    //@formatter:on

	private static LangKey get(String s)
	{
		return LangKey.of("ftbl.lang." + s);
	}
}
