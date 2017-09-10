package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.lib.LangKey;

/**
 * @author LatvianModder
 */
public class FTBLibLang
{
	public static final LangKey RELOAD_SERVER = get("reload_server");
	public static final LangKey RELOAD_CLIENT = get("reload_client");
	public static final LangKey RELOAD_CLIENT_CONFIG = get("reload_client_config");

	@Deprecated
	public static final LangKey RAW = LangKey.of("raw");
	public static final LangKey WIP = LangKey.of("wip");
	public static final LangKey EXAMPLE = LangKey.of("example");
	public static final LangKey CLICK_HERE = LangKey.of("click_here");
	public static final LangKey FEATURE_DISABLED = LangKey.of("feature_disabled");
	public static final LangKey MISSING_ARGS = LangKey.of("missing_args");
	public static final LangKey MISSING_ARGS_NUM = LangKey.of("missing_args_num");
	public static final LangKey COMMAND_PERMISSION = LangKey.of("commands.generic.permission");
	public static final LangKey COMMAND_USAGE = LangKey.of("commands.generic.usage");
	public static final LangKey CLIENT_CONFIG = LangKey.of("client_config");
	public static final LangKey OWNER = get("owner");
	public static final LangKey DELETE_ITEM = get("delete_item");
	public static final LangKey SERVER_FORCED = get("server_forced");
	public static final LangKey COMMANDS = get("commands");
	public static final LangKey MY_PERMISSIONS = get("my_permissions");
	public static final LangKey ALL_PERMISSIONS = get("all_permissions");
	public static final LangKey DIFFICULTY = get("difficulty");
	public static final LangKey MY_SERVER_SETTINGS = LangKey.of("sidebar_button.ftbl.my_server_settings");

	public static final LangKey TEAM_CONFIG = get("team.config");
	public static final LangKey TEAM_CREATED = get("team.created");
	public static final LangKey TEAM_DELETED = get("team.deleted");
	public static final LangKey TEAM_MEMBER_LEFT = get("team.member_left");
	public static final LangKey TEAM_MEMBER_JOINED = get("team.member_joined");
	public static final LangKey TEAM_TRANSFERRED_OWNERSHIP = get("team.transferred_ownership");
	public static final LangKey TEAM_INVITED = get("team.invited");
	public static final LangKey TEAM_INVITED_YOU = get("team.invited_you");
	public static final LangKey TEAM_NOT_FOUND = get("team.error.not_found");
	public static final LangKey TEAM_NO_TEAM = get("team.error.no_team");
	public static final LangKey TEAM_MUST_LEAVE = get("team.error.must_leave");
	public static final LangKey TEAM_NOT_OWNER = get("team.error.not_owner");
	public static final LangKey TEAM_NOT_MEMBER = get("team.error.not_member");
	public static final LangKey TEAM_MUST_TRANSFER_OWNERSHIP = get("team.error.must_transfer_ownership");
	public static final LangKey TEAM_ALREADY_INVITED = get("team.error.already_invited");
	public static final LangKey TEAM_FAILED_TO_JOIN = get("team.error.failed_to_join");
	public static final LangKey TEAM_STATUS_SET = get("team.status.set");
	public static final LangKey TEAM_PERMISSION_OWNER = get("team.permission.owner");
	public static final LangKey TEAM_NOTIFICATION = get("team.notification");
	public static final LangKey TEAM_NOTIFICATION_HIDE = get("team.notification.hide");
	public static final LangKey TEAM_CHAT_MESSAGE = get("team.chat_message");

	private static LangKey get(String s)
	{
		return LangKey.of("ftbl.lang." + s);
	}
}
