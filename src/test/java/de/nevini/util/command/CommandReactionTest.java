package de.nevini.util.command;

import org.junit.Assert;
import org.junit.Test;

public class CommandReactionTest {

    @Test
    public void testEnumConstantsUnicode() {
        Assert.assertEquals("\u2705", CommandReaction.OK.getUnicode());
        Assert.assertEquals("\u2611", CommandReaction.DEFAULT_OK.getUnicode());
        Assert.assertEquals("\u2716", CommandReaction.DEFAULT_NOK.getUnicode());
        Assert.assertEquals("\u2754", CommandReaction.UNKNOWN.getUnicode());
        Assert.assertEquals("\u26A0", CommandReaction.WARNING.getUnicode());
        Assert.assertEquals("\u274C", CommandReaction.ERROR.getUnicode());
        Assert.assertEquals("\u26D4", CommandReaction.DISABLED.getUnicode());
        Assert.assertEquals("\uD83D\uDEAB", CommandReaction.PROHIBITED.getUnicode());
        Assert.assertEquals("\u2709", CommandReaction.DM.getUnicode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("CommandReaction.OK(unicode=\u2705)", CommandReaction.OK.toString());
    }

}
