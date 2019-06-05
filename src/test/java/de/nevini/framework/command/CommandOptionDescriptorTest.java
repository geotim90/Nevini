package de.nevini.framework.command;

import org.junit.Assert;
import org.junit.Test;

public class CommandOptionDescriptorTest {

    @Test
    public void testBuilder() {
        CommandOptionDescriptor commandOptionDescriptor = CommandOptionDescriptor.builder()
                .syntax("[--user] [<user>]")
                .description("user reference")
                .keyword("--user")
                .aliases(new String[]{"//user"})
                .build();
        Assert.assertEquals("[--user] [<user>]", commandOptionDescriptor.getSyntax());
        Assert.assertEquals("user reference", commandOptionDescriptor.getDescription());
        Assert.assertEquals("--user", commandOptionDescriptor.getKeyword());
        Assert.assertArrayEquals(new String[]{"//user"}, commandOptionDescriptor.getAliases());
    }

    @Test
    public void testBuilderConstructor() {
        CommandOptionDescriptor commandOptionDescriptor = new CommandOptionDescriptor.CommandOptionDescriptorBuilder()
                .syntax("[--user] [<user>]")
                .description("user reference")
                .keyword("--user")
                // test without aliases this time
                .build();
        Assert.assertEquals("[--user] [<user>]", commandOptionDescriptor.getSyntax());
        Assert.assertEquals("user reference", commandOptionDescriptor.getDescription());
        Assert.assertEquals("--user", commandOptionDescriptor.getKeyword());
        Assert.assertArrayEquals(new String[]{}, commandOptionDescriptor.getAliases());
    }

    @Test
    public void testConstructor() {
        CommandOptionDescriptor commandOptionDescriptor = new CommandOptionDescriptor(
                "[--user] [<user>]", "user reference", "--user", new String[]{"//user"}
        );
        Assert.assertEquals("[--user] [<user>]", commandOptionDescriptor.getSyntax());
        Assert.assertEquals("user reference", commandOptionDescriptor.getDescription());
        Assert.assertEquals("--user", commandOptionDescriptor.getKeyword());
        Assert.assertArrayEquals(new String[]{"//user"}, commandOptionDescriptor.getAliases());
    }

    @Test
    public void testEqualsAndHashCode() {
        CommandOptionDescriptor a = CommandOptionDescriptor.builder()
                .syntax("[--user] [<user>]")
                .description("user reference")
                .keyword("--user")
                .aliases(new String[]{"//user"})
                .build();
        CommandOptionDescriptor b = new CommandOptionDescriptor(
                "[--user] [<user>]", "user reference", "--user", new String[]{"//user"}
        );
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        CommandOptionDescriptor commandOptionDescriptor = new CommandOptionDescriptor(
                "[--user] [<user>]", "user reference", "--user", new String[]{"//user"}
        );
        Assert.assertEquals(
                "CommandOptionDescriptor(syntax=[--user] [<user>], description=user reference, keyword=--user, aliases=[//user])",
                commandOptionDescriptor.toString()
        );
    }

}
