package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.lib.util.LangKey;

/**
 * @author LatvianModder
 */
public interface FTBLibLang
{
	LangKey WIP = LangKey.of("wip");
	LangKey DEPRECATED = LangKey.of("deprecated", String.class);
	LangKey EXAMPLE = LangKey.of("example");
	LangKey CLICK_HERE = LangKey.of("click_here");
	LangKey FEATURE_DISABLED = LangKey.of("feature_disabled");
	LangKey FEATURE_DISABLED_SERVER = LangKey.of("feature_disabled_server");
	LangKey ERROR = LangKey.of("error", String.class);
	LangKey OWNER = LangKey.of("owner", String.class);
	LangKey DELETE_ITEM = LangKey.of("delete_item", String.class);
	LangKey SERVER_FORCED = LangKey.of("server_forced", String.class);
	LangKey COMMANDS = LangKey.of("commands");
	LangKey MY_PERMISSIONS = LangKey.of("my_permissions");
	LangKey ALL_PERMISSIONS = LangKey.of("all_permissions");
	LangKey DIFFICULTY = LangKey.of("difficulty", String.class);
	LangKey MY_TEAM = LangKey.of("sidebar_button." + FTBLib.MOD_ID + ".my_team");

	LangKey RELOAD_SERVER = LangKey.of("ftblib.lang.reload_server", String.class);
	LangKey RELOAD_CLIENT = LangKey.of("ftblib.lang.reload_client", String.class);
	LangKey RELOAD_FAILED = LangKey.of("ftblib.lang.reload_failed");

	LangKey MY_SERVER_SETTINGS = LangKey.of("player_config");
	LangKey COMMAND_PERMISSION = LangKey.of("commands.generic.permission");
	LangKey COMMAND_USAGE = LangKey.of("commands.generic.usage", String.class);
	LangKey PLAYER_NOT_FOUND = LangKey.of("commands.generic.player.notFound", String.class);

	LangKey CONFIG_COMMAND_INVALID_KEY = LangKey.of("ftblib.lang.config_command.invalid_key", String.class);
	LangKey CONFIG_COMMAND_SET = LangKey.of("ftblib.lang.config_command.set", String.class, String.class);

	LangKey CONFIG_ADD_FAKE_PLAYER_INVALID_UUID = LangKey.of("ftblib.lang.add_fake_player.invalid_uuid");
	LangKey CONFIG_ADD_FAKE_PLAYER_PLAYER_EXISTS = LangKey.of("ftblib.lang.add_fake_player.player_exists");
	LangKey CONFIG_ADD_FAKE_PLAYER_ADDED = LangKey.of("ftblib.lang.add_fake_player.added", String.class);

	LangKey TEAM_CREATED = LangKey.of("ftblib.lang.team.created", String.class);
	LangKey TEAM_ID_ALREADY_EXISTS = LangKey.of("ftblib.lang.team.id_already_exists");
	LangKey TEAM_ID_INVALID = LangKey.of("ftblib.lang.team.id_invalid");
	LangKey TEAM_DELETED = LangKey.of("ftblib.lang.team.deleted", String.class);
	LangKey TEAM_MEMBER_LEFT = LangKey.of("ftblib.lang.team.member_left", String.class);
	LangKey TEAM_MEMBER_JOINED = LangKey.of("ftblib.lang.team.member_joined", String.class);
	LangKey TEAM_TRANSFERRED_OWNERSHIP = LangKey.of("ftblib.lang.team.transferred_ownership", String.class);
	LangKey TEAM_INVITED = LangKey.of("ftblib.lang.team.invited", String.class, String.class);
	LangKey TEAM_INVITED_YOU = LangKey.of("ftblib.lang.team.invited_you", String.class, String.class);
	LangKey TEAM_NOT_FOUND = LangKey.of("ftblib.lang.team.error.not_found");
	LangKey TEAM_NO_TEAM = LangKey.of("ftblib.lang.team.error.no_team");
	LangKey TEAM_MUST_LEAVE = LangKey.of("ftblib.lang.team.error.must_leave");
	LangKey TEAM_NOT_OWNER = LangKey.of("ftblib.lang.team.error.not_owner");
	LangKey TEAM_NOT_MEMBER = LangKey.of("ftblib.lang.team.error.not_member", String.class);
	LangKey TEAM_MUST_TRANSFER_OWNERSHIP = LangKey.of("ftblib.lang.team.error.must_transfer_ownership");
	LangKey TEAM_ALREADY_INVITED = LangKey.of("ftblib.lang.team.error.already_invited", String.class);
	LangKey TEAM_FAILED_TO_JOIN = LangKey.of("ftblib.lang.team.error.failed_to_join");
	LangKey TEAM_PERMISSION_SET = LangKey.of("ftblib.lang.team.permission.set", String.class, String.class, String.class);
	LangKey TEAM_PERMISSION_OWNER = LangKey.of("ftblib.lang.team.permission.owner");
	LangKey TEAM_NOTIFICATION = LangKey.of("ftblib.lang.team.notification", String.class, String.class);
	LangKey TEAM_NOTIFICATION_HIDE = LangKey.of("ftblib.lang.team.notification.hide");
	LangKey TEAM_CHAT_MESSAGE = LangKey.of("ftblib.lang.team.chat_message", String.class, String.class);

	LangKey TEAM_GUI_SELECT_TEAM = LangKey.of("ftblib.lang.team.gui.select_team");
	LangKey TEAM_GUI_CREATE_TEAM = LangKey.of("ftblib.lang.team.gui.create_team");
	LangKey TEAM_GUI_JOIN_TEAM = LangKey.of("ftblib.lang.team.gui.join_team", String.class);
	LangKey TEAM_GUI_REQUEST_INVITE = LangKey.of("ftblib.lang.team.gui.request_invite", String.class);
	LangKey TEAM_GUI_MEMBERS = LangKey.of("ftblib.lang.team.gui.members");
	LangKey TEAM_GUI_MEMBERS_INVITE = LangKey.of("ftblib.lang.team.gui.members.invite");
	LangKey TEAM_GUI_MEMBERS_REQUESTING_INVITE = LangKey.of("ftblib.lang.team.gui.members.requesting_invite");
	LangKey TEAM_GUI_MEMBERS_DENY_REQUEST = LangKey.of("ftblib.lang.team.gui.members.deny_request");
	LangKey TEAM_GUI_MEMBERS_CANCEL_INVITE = LangKey.of("ftblib.lang.team.gui.members.cancel_invite");
	LangKey TEAM_GUI_MEMBERS_KICK = LangKey.of("ftblib.lang.team.gui.members.kick");
	LangKey TEAM_GUI_ALLIES = LangKey.of("ftblib.lang.team.gui.allies");
	LangKey TEAM_GUI_MODS = LangKey.of("ftblib.lang.team.gui.mods");
	LangKey TEAM_GUI_ENEMIES = LangKey.of("ftblib.lang.team.gui.enemies");
	LangKey TEAM_GUI_LEAVE = LangKey.of("ftblib.lang.team.gui.leave");
	LangKey TEAM_GUI_TRANSFER_OWNERSHIP = LangKey.of("ftblib.lang.team.gui.transfer_ownership");
	LangKey TEAM_GUI_REQUESTING_INVITE = LangKey.of("ftblib.lang.team_status.requesting_invite");
}