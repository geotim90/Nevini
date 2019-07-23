package de.nevini.util.command;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class CommandOptionsTest {

    @Test
    public void testEmptyArguments() {
        CommandOptions commandOptions = CommandOptions.parseArgument(StringUtils.EMPTY);
        // make sure no arguments are present
        Assert.assertTrue(commandOptions.getArguments().isEmpty());
        // make sure no argument is present
        Assert.assertFalse(commandOptions.getArgument().isPresent());
        // make sure no options are present
        Assert.assertTrue(commandOptions.getOptions().isEmpty());
    }

    @Test
    public void testArgumentWithoutOptions() {
        CommandOptions commandOptions = CommandOptions.parseArgument("test argument");
        // make sure the argument is the only thing present in arguments
        Assert.assertEquals(1, commandOptions.getArguments().size());
        Assert.assertEquals("test argument", commandOptions.getArguments().get(0));
        // make sure the argument is present
        Assert.assertEquals("test argument", commandOptions.getArgument().orElse(null));
        // make sure no options are present
        Assert.assertTrue(commandOptions.getOptions().isEmpty());
    }

    @Test
    public void testArgumentWithOption() {
        CommandOptions commandOptions = CommandOptions.parseArgument("test argument --flag value");
        // make sure the arguments were split correctly
        Assert.assertEquals(2, commandOptions.getArguments().size());
        Assert.assertEquals("test argument", commandOptions.getArguments().get(0));
        Assert.assertEquals("--flag value", commandOptions.getArguments().get(1));
        // make sure the argument is present
        Assert.assertEquals("test argument", commandOptions.getArgument().orElse(null));
        // make sure the option is present
        Assert.assertEquals(1, commandOptions.getOptions().size());
        Assert.assertEquals("--flag value", commandOptions.getOptions().get(0));
    }

    @Test
    public void testArgumentWithOptions() {
        CommandOptions commandOptions = CommandOptions.parseArgument("test argument --flag value --flag value two");
        // make sure the arguments were split correctly
        Assert.assertEquals(3, commandOptions.getArguments().size());
        Assert.assertEquals("test argument", commandOptions.getArguments().get(0));
        Assert.assertEquals("--flag value", commandOptions.getArguments().get(1));
        Assert.assertEquals("--flag value two", commandOptions.getArguments().get(2));
        // make sure the argument is present
        Assert.assertEquals("test argument", commandOptions.getArgument().orElse(null));
        // make sure the option is present
        Assert.assertEquals(2, commandOptions.getOptions().size());
        Assert.assertEquals("--flag value", commandOptions.getOptions().get(0));
        Assert.assertEquals("--flag value two", commandOptions.getOptions().get(1));
    }

    @Test
    public void testOptionWithoutArgument() {
        CommandOptions commandOptions = CommandOptions.parseArgument("--flag value");
        // make sure the option is the only thing present in arguments
        Assert.assertEquals(1, commandOptions.getArguments().size());
        Assert.assertEquals("--flag value", commandOptions.getArguments().get(0));
        // make sure no argument is present
        Assert.assertFalse(commandOptions.getArgument().isPresent());
        // make sure the option is present
        Assert.assertEquals(1, commandOptions.getOptions().size());
        Assert.assertEquals("--flag value", commandOptions.getOptions().get(0));
    }

    @Test
    public void testWithArgument() {
        CommandOptions commandOptions = CommandOptions.parseArgument("--flag value").withArgument("test argument");
        // make sure the arguments were split correctly
        Assert.assertEquals(2, commandOptions.getArguments().size());
        Assert.assertEquals("test argument", commandOptions.getArguments().get(0));
        Assert.assertEquals("--flag value", commandOptions.getArguments().get(1));
        // make sure the argument is present
        Assert.assertEquals("test argument", commandOptions.getArgument().orElse(null));
        // make sure the option is present
        Assert.assertEquals(1, commandOptions.getOptions().size());
        Assert.assertEquals("--flag value", commandOptions.getOptions().get(0));
    }

    @Test
    public void testWithArgumentEmpty() {
        CommandOptions commandOptions = CommandOptions.parseArgument("test argument").withArgument(StringUtils.EMPTY);
        // make sure no arguments are present
        Assert.assertTrue(commandOptions.getArguments().isEmpty());
        // make sure no argument is present
        Assert.assertFalse(commandOptions.getArgument().isPresent());
        // make sure no options are present
        Assert.assertTrue(commandOptions.getOptions().isEmpty());
    }

    @Test
    public void testWithArgumentOfEmpty() {
        CommandOptions commandOptions = CommandOptions.parseArgument(StringUtils.EMPTY).withArgument("test argument");
        // make sure the argument is the only thing present in arguments
        Assert.assertEquals(1, commandOptions.getArguments().size());
        Assert.assertEquals("test argument", commandOptions.getArguments().get(0));
        // make sure the argument is present
        Assert.assertEquals("test argument", commandOptions.getArgument().orElse(null));
        // make sure no options are present
        Assert.assertTrue(commandOptions.getOptions().isEmpty());
    }

    @Test
    public void testMentionWithoutOptionFlag() {
        CommandOptions commandOptions = CommandOptions.parseArgument("<@123456789123456789>");
        // make sure the mention is the only thing present in arguments
        Assert.assertEquals(1, commandOptions.getArguments().size());
        Assert.assertEquals("<@123456789123456789>", commandOptions.getArguments().get(0));
        // make sure no argument is present
        Assert.assertFalse(commandOptions.getArgument().isPresent());
        // make sure the mention is present in the options
        Assert.assertEquals(1, commandOptions.getOptions().size());
        Assert.assertEquals("<@123456789123456789>", commandOptions.getOptions().get(0));
    }

    @Test
    public void testMentionWithOptionFlag() {
        CommandOptions commandOptions = CommandOptions.parseArgument("--user <@123456789123456789>");
        // make sure the option is the only thing present in arguments
        Assert.assertEquals(1, commandOptions.getArguments().size());
        Assert.assertEquals("--user <@123456789123456789>", commandOptions.getArguments().get(0));
        // make sure no argument is present
        Assert.assertFalse(commandOptions.getArgument().isPresent());
        // make sure the option is present in the options
        Assert.assertEquals(1, commandOptions.getOptions().size());
        Assert.assertEquals("--user <@123456789123456789>", commandOptions.getOptions().get(0));
    }

}
