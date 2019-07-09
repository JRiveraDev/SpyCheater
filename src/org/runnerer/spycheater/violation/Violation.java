package org.runnerer.spycheater.violation;

import org.runnerer.spycheater.checks.Check;

public class Violation
{

    private Check check;
    private ViolationPriority level;
    private String message;
    private Long created;
    private boolean unused;

    public Violation(Check check, ViolationPriority violationPriority, String string)
    {
        this.check = check;
        this.level = violationPriority;
        this.message = string;
        this.created = System.currentTimeMillis();
    }

    public Check getCheck()
    {
        return this.check;
    }

    public ViolationPriority getPriority()
    {
        return this.level;
    }

    public String getMessage()
    {
        return this.message;
    }

    public boolean isUnused()
    {
        return this.unused;
    }

    public void setUnused(boolean bl)
    {
        this.unused = bl;
    }

    public long getWhenCreated()
    {
        return this.created;
    }

    public long getDiffWhenCreated()
    {
        return System.currentTimeMillis() - this.created;
    }
}

