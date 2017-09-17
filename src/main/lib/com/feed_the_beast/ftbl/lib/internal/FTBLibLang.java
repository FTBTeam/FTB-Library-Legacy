package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.lib.LangKey;

/**
 * @author LatvianModder
 */
public interface FTBLibLang
{
	LangKey RELOAD_SERVER = LangKey.of("ftbl.lang.reload_server");
	LangKey RELOAD_CLIENT = LangKey.of("ftbl.lang.reload_client");
	LangKey RELOAD_CLIENT_CONFIG = LangKey.of("ftbl.lang.reload_client_config");
	LangKey RELOAD_FAILED = LangKey.of("ftbl.lang.reload_failed");

	LangKey WIP = LangKey.of("wip");
	LangKey EXAMPLE = LangKey.of("example");
	LangKey CLICK_HERE = LangKey.of("click_here");
	LangKey FEATURE_DISABLED = LangKey.of("feature_disabled");
	LangKey MISSING_ARGS = LangKey.of("missing_args");
	LangKey MISSING_ARGS_NUM = LangKey.of("missing_args_num");
	LangKey COMMAND_PERMISSION = LangKey.of("commands.generic.permission");
	LangKey COMMAND_USAGE = LangKey.of("commands.generic.usage");
	LangKey CLIENT_CONFIG = LangKey.of("client_config");
	LangKey ERROR = LangKey.of("ftbl.lang.error");
	LangKey OWNER = LangKey.of("ftbl.lang.owner");
	LangKey DELETE_ITEM = LangKey.of("ftbl.lang.delete_item");
	LangKey SERVER_FORCED = LangKey.of("ftbl.lang.server_forced");
	LangKey COMMANDS = LangKey.of("ftbl.lang.commands");
	LangKey MY_PERMISSIONS = LangKey.of("ftbl.lang.my_permissions");
	LangKey ALL_PERMISSIONS = LangKey.of("ftbl.lang.all_permissions");
	LangKey DIFFICULTY = LangKey.of("ftbl.lang.difficulty");
	LangKey MY_SERVER_SETTINGS = LangKey.of("player_config");
	LangKey MY_TEAM_SETTINGS = LangKey.of("team_config");

	LangKey CONFIG_COMMAND_INVALID_KEY = LangKey.of("ftbl.lang.config_command.invalid_key");
	LangKey CONFIG_COMMAND_SETTING = LangKey.of("ftbl.lang.config_command.setting");
	LangKey CONFIG_COMMAND_SET = LangKey.of("ftbl.lang.config_command.set");

	LangKey CONFIG_ADD_FAKE_PLAYER_INVALID_UUID = LangKey.of("ftbl.lang.add_fake_player.invalid_uuid");
	LangKey CONFIG_ADD_FAKE_PLAYER_PLAYER_EXISTS = LangKey.of("ftbl.lang.add_fake_player.player_exists");
	LangKey CONFIG_ADD_FAKE_PLAYER_ADDED = LangKey.of("ftbl.lang.add_fake_player.added");

	LangKey TEAM_CREATED = LangKey.of("ftbl.lang.team.created");
	LangKey TEAM_ID_ALREADY_EXISTS = LangKey.of("ftbl.lang.team.id_already_exists");
	LangKey TEAM_ID_INVALID = LangKey.of("ftbl.lang.team.id_invalid");
	LangKey TEAM_DELETED = LangKey.of("ftbl.lang.team.deleted");
	LangKey TEAM_MEMBER_LEFT = LangKey.of("ftbl.lang.team.member_left");
	LangKey TEAM_MEMBER_JOINED = LangKey.of("ftbl.lang.team.member_joined");
	LangKey TEAM_TRANSFERRED_OWNERSHIP = LangKey.of("ftbl.lang.team.transferred_ownership");
	LangKey TEAM_INVITED = LangKey.of("ftbl.lang.team.invited");
	LangKey TEAM_INVITED_YOU = LangKey.of("ftbl.lang.team.invited_you");
	LangKey TEAM_NOT_FOUND = LangKey.of("ftbl.lang.team.error.not_found");
	LangKey TEAM_NO_TEAM = LangKey.of("ftbl.lang.team.error.no_team");
	LangKey TEAM_MUST_LEAVE = LangKey.of("ftbl.lang.team.error.must_leave");
	LangKey TEAM_NOT_OWNER = LangKey.of("ftbl.lang.team.error.not_owner");
	LangKey TEAM_NOT_MEMBER = LangKey.of("ftbl.lang.team.error.not_member");
	LangKey TEAM_MUST_TRANSFER_OWNERSHIP = LangKey.of("ftbl.lang.team.error.must_transfer_ownership");
	LangKey TEAM_ALREADY_INVITED = LangKey.of("ftbl.lang.team.error.already_invited");
	LangKey TEAM_FAILED_TO_JOIN = LangKey.of("ftbl.lang.team.error.failed_to_join");
	LangKey TEAM_STATUS_SET = LangKey.of("ftbl.lang.team.status.set");
	LangKey TEAM_PERMISSION_OWNER = LangKey.of("ftbl.lang.team.permission.owner");
	LangKey TEAM_NOTIFICATION = LangKey.of("ftbl.lang.team.notification");
	LangKey TEAM_NOTIFICATION_HIDE = LangKey.of("ftbl.lang.team.notification.hide");
	LangKey TEAM_CHAT_MESSAGE = LangKey.of("ftbl.lang.team.chat_message");
}