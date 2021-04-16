package de.nevini.modules.util.debug;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
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

@Slf4j
class DebugPermissionCommand extends Command {

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
                        Resolvers.ROLE.describe(),
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

    private void acceptMember(CommandEvent event, Member member) {
        export(event, Collections.singleton(member), member.getRoles());
    }

    private void acceptRole(CommandEvent event, Role role) {
        export(event, role.getGuild().getMembersWithRoles(role), Collections.singletonList(role));
    }

    private void acceptGuild(CommandEvent event) {
        export(event, event.getGuild().getMembers(), event.getGuild().getRoles());
    }

    private void export(CommandEvent event, Collection<Member> members, Collection<Role> roles) {
        try {
            File export = File.createTempFile("export", ".csv");
            try (PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(export), StandardCharsets.UTF_8)
            ))) {
                out.print("Type,Server,User,Channel,Role");
                for (Permission p : Permission.values()) {
                    if (p.getOffset() > -1) {
                        out.print("," + p.getName());
                    }
                }
                out.println();

                final String server = event.getGuild().getName();

                // 1. server permissions
                println(out, event.getGuild(), server, "(*)", "(server)", "(everyone)",
                        event.getGuild().getPublicRole().getPermissionsRaw());

                // 2. role server permissions
                for (Role role : roles) {
                    println(out, event.getGuild(), server, "(*)", "(server)", role.getName(),
                            role.getPermissionsRaw());
                }

                // 3. effective server permissions
                for (Member member : members) {
                    println(out, event.getGuild(), server, member.getEffectiveName(), "(server)", "(effective)",
                            PermissionUtil.getEffectivePermission(member));
                }

                for (GuildChannel channel : event.getGuild().getChannels(true)) {
                    // 4. channel permissions
                    println(out, channel, server, "(*)", channel.getName(), "(everyone)",
                            channel.getPermissionOverride(event.getGuild().getPublicRole()));

                    // 5. role channel permissions
                    for (Role role : roles) {
                        println(out, channel, server, "(*)", channel.getName(), role.getName(),
                                channel.getPermissionOverride(role));
                    }

                    // 6. user channel permissions
                    for (Member member : members) {
                        println(out, channel, server, member.getEffectiveName(), channel.getName(), "(user)",
                                channel.getPermissionOverride(member));
                    }

                    // 7. effective channel permissions
                    for (Member member : members) {
                        println(out, channel, server, member.getEffectiveName(), channel.getName(), "(effective)",
                                PermissionUtil.getEffectivePermission(channel, member));
                    }
                }
            }

            event.getTextChannel().sendMessage("Here is the data you requested:").addFile(export)
                    .queue(event::complete);
        } catch (IOException e) {
            log.warn("Something went wrong while exporting data.", e);
            event.reply(CommandReaction.ERROR, event::complete);
        }
    }

    private void println(PrintWriter out, ISnowflake type, String server, String user, String channel, String role,
                         long permissions
    ) {
        long filteredPermissions = permissions & getTypeMask(type);
        if (filteredPermissions != 0) {
            out.append(getTypeName(type)).append(',').append(server).append(',').append(user).append(',')
                    .append(channel).append(',').append(role);
            for (Permission p : Permission.values()) {
                if (p.getOffset() > -1) {
                    out.append(',').append((permissions & p.getRawValue()) > 0 ? "ON" : "");
                }
            }
            out.println();
        }
    }

    private void println(PrintWriter out, ISnowflake type, String server, String user, String channel, String role,
                         PermissionOverride override
    ) {
        long allowed = override == null ? 0 : override.getAllowedRaw() & getTypeMask(type);
        long denied = override == null ? 0 : override.getDeniedRaw() & getTypeMask(type);
        if (allowed != 0 || denied != 0) {
            out.append(getTypeName(type)).append(',').append(server).append(',').append(user).append(',')
                    .append(channel).append(',').append(role);
            for (Permission p : Permission.values()) {
                if (p.getOffset() > -1) {
                    out.append(',').append((allowed & p.getRawValue()) > 0 ? "ON" : (denied & p.getRawValue()) > 0 ? "NO" : "");
                }
            }
            out.println();
        }
    }

    private long getTypeMask(ISnowflake type) {
        if (type instanceof TextChannel) {
            return Permission.ALL_TEXT_PERMISSIONS;
        } else if (type instanceof VoiceChannel) {
            return Permission.ALL_VOICE_PERMISSIONS;
        } else if (type instanceof Category) {
            return Permission.ALL_CHANNEL_PERMISSIONS;
        } else {
            return Permission.ALL_GUILD_PERMISSIONS;
        }
    }

    private String getTypeName(ISnowflake type) {
        if (type instanceof TextChannel) {
            return "Text Channel";
        } else if (type instanceof VoiceChannel) {
            return "Voice Channel";
        } else if (type instanceof Category) {
            return "Category";
        } else {
            return "Server";
        }
    }

}
