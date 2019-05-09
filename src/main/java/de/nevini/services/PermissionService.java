package de.nevini.services;

import de.nevini.db.permission.PermissionData;
import de.nevini.db.permission.PermissionId;
import de.nevini.db.permission.PermissionRepository;
import de.nevini.modules.Node;
import lombok.NonNull;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final static byte SERVER = 1;
    private final static byte PERMISSION = 2;
    private final static byte ROLE = 3;
    private final static byte USER = 4;
    private final static byte CHANNEL = 5;
    private final static byte CHANNEL_PERMISSION = 6;
    private final static byte CHANNEL_ROLE = 7;
    private final static byte CHANNEL_USER = 8;

    private final PermissionRepository permissionRepository;

    public PermissionService(@Autowired PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Optional<Boolean> hasChannelUserPermission(@NonNull TextChannel channel, @NonNull Member user, @NonNull Node node) {
        return hasPermission(channel.getGuild(), null, null, user, channel, node, CHANNEL_USER);
    }

    public Optional<Boolean> hasChannelRolePermission(@NonNull TextChannel channel, @NonNull Role role, @NonNull Node node) {
        return hasPermission(channel.getGuild(), null, role, null, channel, node, CHANNEL_ROLE);
    }

    public Optional<Boolean> hasChannelPermissionPermission(@NonNull TextChannel channel, @NonNull Permission permission, @NonNull Node node) {
        return hasPermission(channel.getGuild(), permission, null, null, channel, node, CHANNEL_PERMISSION);
    }

    public Optional<Boolean> hasChannelPermission(@NonNull TextChannel channel, @NonNull Node node) {
        return hasPermission(channel.getGuild(), null, null, null, channel, node, CHANNEL);
    }

    public Optional<Boolean> hasUserPermission(@NonNull Member user, @NonNull Node node) {
        return hasPermission(user.getGuild(), null, null, user, null, node, USER);
    }

    public Optional<Boolean> hasRolePermission(@NonNull Role role, @NonNull Node node) {
        return hasPermission(role.getGuild(), null, role, null, null, node, ROLE);
    }

    public Optional<Boolean> hasPermissionPermission(@NonNull Guild server, @NonNull Permission permission, @NonNull Node node) {
        return hasPermission(server, permission, null, null, null, node, PERMISSION);
    }

    public Optional<Boolean> hasServerPermission(@NonNull Guild server, @NonNull Node node) {
        return hasPermission(server, null, null, null, null, node, SERVER);
    }

    private Optional<Boolean> hasPermission(@NonNull Guild server, Permission permission, Role role, Member user, TextChannel channel, @NonNull Node node, byte scope) {
        if (scope >= CHANNEL_USER && channel != null && user != null) {
            Optional<Boolean> channelUserPermission = getChannelUserPermission(channel, user, node);
            if (channelUserPermission.isPresent()) {
                return channelUserPermission;
            }
        }
        if (scope >= CHANNEL_ROLE) {
            Optional<Boolean> channelRolePermission;
            if (channel != null && user != null) {
                channelRolePermission = getChannelRolePermission(channel, user, node);
            } else if (channel != null && role != null) {
                channelRolePermission = getChannelRolePermission(channel, role, node);
            } else {
                channelRolePermission = Optional.empty();
            }
            if (channelRolePermission.isPresent()) {
                return channelRolePermission;
            }
        }
        if (scope >= CHANNEL_PERMISSION) {
            Optional<Boolean> channelPermissionPermission;
            if (channel != null && user != null) {
                channelPermissionPermission = getChannelPermissionPermission(channel, user, node);
            } else if (channel != null && role != null) {
                channelPermissionPermission = getChannelPermissionPermission(channel, role, node);
            } else if (channel != null && permission != null) {
                channelPermissionPermission = getChannelPermissionPermission(channel, permission, node);
            } else {
                channelPermissionPermission = Optional.empty();
            }
            if (channelPermissionPermission.isPresent()) {
                return channelPermissionPermission;
            }
        }
        if (scope >= CHANNEL && channel != null) {
            Optional<Boolean> channelPermission;
            channelPermission = getChannelPermission(channel, node);
            if (channelPermission.isPresent()) {
                return channelPermission;
            }
        }
        if (scope >= USER && user != null) {
            Optional<Boolean> userPermission = getUserPermission(user, node);
            if (userPermission.isPresent()) {
                return userPermission;
            }
        }
        if (scope >= ROLE) {
            Optional<Boolean> rolePermission;
            if (user != null) {
                rolePermission = getRolePermission(user, node);
            } else if (role != null) {
                rolePermission = getRolePermission(role, node);
            } else {
                rolePermission = Optional.empty();
            }
            if (rolePermission.isPresent()) {
                return rolePermission;
            }
        }
        if (scope >= PERMISSION) {
            Optional<Boolean> permissionPermission;
            if (user != null) {
                permissionPermission = getPermissionPermission(user, node);
            } else if (role != null) {
                permissionPermission = getPermissionPermission(role, node);
            } else if (permission != null) {
                permissionPermission = getPermissionPermission(server, permission, node);
            } else {
                permissionPermission = Optional.empty();
            }
            if (permissionPermission.isPresent()) {
                return permissionPermission;
            }
        }
        if (scope >= SERVER) {
            Optional<Boolean> serverPermission = getServerPermission(server, node);
            if (serverPermission.isPresent()) {
                return serverPermission;
            }
        }
        return Optional.empty();
    }

    private Optional<Boolean> getChannelUserPermission(Channel channel, Member user, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_USER,
                user.getUser().getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getChannelRolePermission(Channel channel, Role role, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_ROLE,
                role.getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getChannelRolePermission(Channel channel, Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_ROLE,
                user.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getChannelPermissionPermission(Channel channel, Permission permission, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_PERMISSION,
                permission.getRawValue(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getChannelPermissionPermission(Channel channel, Role role, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_PERMISSION,
                Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, role)).stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getChannelPermissionPermission(Channel channel, Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_PERMISSION,
                user.getPermissions(channel).stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getChannelPermission(Channel channel, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL,
                channel.getGuild().getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getUserPermission(Member user, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                user.getGuild().getIdLong(),
                user.getGuild().getIdLong(),
                USER,
                user.getUser().getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getRolePermission(Role role, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                role.getGuild().getIdLong(),
                role.getGuild().getIdLong(),
                ROLE,
                role.getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getRolePermission(Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                user.getGuild().getIdLong(),
                user.getGuild().getIdLong(),
                ROLE,
                user.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getPermissionPermission(Guild guild, Permission permission, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                guild.getIdLong(),
                guild.getIdLong(),
                PERMISSION,
                permission.getRawValue(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getPermissionPermission(Role role, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                role.getGuild().getIdLong(),
                role.getGuild().getIdLong(),
                PERMISSION,
                role.getPermissions().stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getPermissionPermission(Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                user.getGuild().getIdLong(),
                user.getGuild().getIdLong(),
                PERMISSION,
                user.getPermissions().stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getServerPermission(Guild server, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                server.getIdLong(),
                server.getIdLong(),
                SERVER,
                server.getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> resolvePermission(PermissionData data) {
        if (data != null && data.getFlag() != 0) {
            if (data.getFlag() > 0) {
                return Optional.of(true);
            } else {
                return Optional.of(false);
            }
        } else {
            return Optional.empty();
        }
    }

    private Optional<Boolean> resolvePermissions(Collection<PermissionData> data) {
        boolean anyAllow = false;
        boolean anyDeny = false;
        for (PermissionData p : data) {
            if (p.getFlag() != 0) {
                if (p.getFlag() > 0) {
                    anyAllow = true;
                } else {
                    anyDeny = true;
                }
            }
        }
        if (anyAllow) {
            return Optional.of(true);
        } else if (anyDeny) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }

}
