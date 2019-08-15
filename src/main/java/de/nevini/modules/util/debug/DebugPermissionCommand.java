package de.nevini.modules.util.debug;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

@Slf4j
class DebugPermissionCommand extends Command {

    private final long[] masks = {
            Permission.ADMINISTRATOR.getRawValue(),
            Permission.VIEW_AUDIT_LOGS.getRawValue(),
            Permission.MANAGE_SERVER.getRawValue(),
            Permission.MANAGE_ROLES.getRawValue(),
            Permission.MANAGE_CHANNEL.getRawValue(),
            Permission.KICK_MEMBERS.getRawValue(),
            Permission.BAN_MEMBERS.getRawValue(),
            Permission.CREATE_INSTANT_INVITE.getRawValue(),
            Permission.MANAGE_CHANNEL.getRawValue(),
            Permission.MANAGE_PERMISSIONS.getRawValue(),
            Permission.NICKNAME_CHANGE.getRawValue(),
            Permission.NICKNAME_MANAGE.getRawValue(),
            Permission.MANAGE_EMOTES.getRawValue(),
            Permission.MANAGE_WEBHOOKS.getRawValue(),
            Permission.MESSAGE_READ.getRawValue(),
            Permission.MESSAGE_WRITE.getRawValue(),
            Permission.MESSAGE_TTS.getRawValue(),
            Permission.MESSAGE_MANAGE.getRawValue(),
            Permission.MESSAGE_EMBED_LINKS.getRawValue(),
            Permission.MESSAGE_ATTACH_FILES.getRawValue(),
            Permission.MESSAGE_HISTORY.getRawValue(),
            Permission.MESSAGE_MENTION_EVERYONE.getRawValue(),
            Permission.MESSAGE_EXT_EMOJI.getRawValue(),
            Permission.MESSAGE_ADD_REACTION.getRawValue(),
            Permission.VOICE_CONNECT.getRawValue(),
            Permission.VOICE_SPEAK.getRawValue(),
            Permission.VOICE_MUTE_OTHERS.getRawValue(),
            Permission.VOICE_DEAF_OTHERS.getRawValue(),
            Permission.VOICE_MOVE_OTHERS.getRawValue(),
            Permission.VOICE_USE_VAD.getRawValue(),
            Permission.PRIORITY_SPEAKER.getRawValue()
    };

    DebugPermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .aliases(new String[]{"permissions", "perm", "perms"})
                .node(Node.UTIL_DEBUG_PERMISSION)
                .description("creates a data dump of all relevant permissions and overrides on the server")
                .minimumBotPermissions(Permissions.sum(
                        Permissions.BOT_EMBED,
                        new Permission[]{Permission.MESSAGE_ATTACH_FILES}
                ))
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER_OR_BOT.describe(false, true),
                        Resolvers.ROLE.describe(false, true),
                        Resolvers.GUILD_FLAG.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (Resolvers.MEMBER_OR_BOT.isOptionPresent(event)) {
            Resolvers.MEMBER_OR_BOT.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
        } else if (Resolvers.ROLE.isOptionPresent(event)) {
            Resolvers.ROLE.resolveOptionOrInputIfExists(event, role -> acceptRole(event, role));
        } else if (Resolvers.GUILD_FLAG.isOptionPresent(event)) {
            acceptGuild(event);
        } else {
            Resolvers.MEMBER_OR_BOT.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
        }
    }

    private void acceptGuild(CommandEvent event) {
        export(event, event.getGuild().getMembers());
    }

    private void acceptRole(CommandEvent event, Role role) {
        export(event, role.getGuild().getMembersWithRoles(role));
    }

    private void acceptMember(CommandEvent event, Member member) {
        export(event, Collections.singleton(member));
    }

    private void export(CommandEvent event, Collection<Member> members) {
        try {
            File export = File.createTempFile("export", ".csv");
            try (PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(export), StandardCharsets.UTF_8)
            ))) {
                out.println("Server,User,Channel,Role,Administrator,View Audit Log,Manage Server,Manage Roles,"
                        + "Manage Channels,Kick Members,Ban Members,Create Instant Invite,Manage Channel,"
                        + "Manage Permissions,Change Nickname,Manage Nicknames,Manage Emojis,Manage Webhooks,"
                        + "Read Text Channels & See Voice Channels,Send Messages,Send TTS Messages,Manage Messages,"
                        + "Embed Links,Attach Files,Read Message History,Mention Everyone,Use External Emojis,"
                        + "Add Reactions,Connect,Speak,Mute Members,Deafen Members,Move Members,Use Voice Activity,"
                        + "Priority Speaker");

                TreeSet<Role> roles = new TreeSet<>();
                members.forEach(member -> roles.addAll(member.getRoles()));

                final String server = event.getGuild().getName();

                // 1. server permissions
                println(out, server, "(*)", "(server)", "(everyone)",
                        event.getGuild().getPublicRole().getPermissionsRaw());

                // 2. role server permissions
                for (Role role : roles) {
                    println(out, server, "(*)", "(server)", role.getName(),
                            role.getPermissionsRaw());
                }

                // 3. effective server permissions
                for (Member member : members) {
                    println(out, server, member.getEffectiveName(), "(server)", "(effective)",
                            PermissionUtil.getEffectivePermission(member));
                }

                for (GuildChannel channel : event.getGuild().getChannels(true)) {
                    if (channel instanceof TextChannel || channel instanceof VoiceChannel) {
                        // 4. channel permissions
                        println(out, server, "(*)", channel.getName(), "(everyone)",
                                channel.getPermissionOverride(event.getGuild().getPublicRole()));

                        // 5. role channel permissions
                        for (Role role : roles) {
                            println(out, server, "(*)", channel.getName(), role.getName(),
                                    channel.getPermissionOverride(role));
                        }

                        // 6. user channel permissions
                        for (Member member : members) {
                            println(out, server, member.getEffectiveName(), channel.getName(), "(user)",
                                    channel.getPermissionOverride(member));
                        }

                        // 7. effective channel permissions
                        for (Member member : members) {
                            println(out, server, member.getEffectiveName(), channel.getName(), "(effective)",
                                    PermissionUtil.getEffectivePermission(channel, member));
                        }
                    }
                }
            }

            event.getTextChannel().sendMessage("Here is the data you requested:").addFile(export)
                    .queue(event::complete);
        } catch (IOException e) {
            log.error("Something went wrong while exporting data.", e);
            event.reply(CommandReaction.ERROR, event::complete);
        }
    }

    private void println(PrintWriter out, String server, String user, String channel, String role, long permissions) {
        if (permissions != 0) {
            out.append(server).append(',').append(user).append(',').append(channel).append(',').append(role);
            for (long mask : masks) {
                out.append(',').append((permissions & mask) > 0 ? "ON" : "");
            }
            out.println();
        }
    }

    private void println(
            PrintWriter out, String server, String user, String channel, String role, PermissionOverride override
    ) {
        long allowed = override == null ? 0 : override.getAllowedRaw();
        long denied = override == null ? 0 : override.getDeniedRaw();
        if (allowed != 0 || denied != 0) {
            out.append(server).append(',').append(user).append(',').append(channel).append(',').append(role);
            for (long mask : masks) {
                out.append(',').append((allowed & mask) > 0 ? "ON" : (denied & mask) > 0 ? "NO" : "");
            }
            out.println();
        }
    }

}
