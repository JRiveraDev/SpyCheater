package org.runnerer.spycheater.violation;

public enum ViolationPriority
{
    TEST("Beta", 0),
    LOWEST("Lowest", 1),
    LOW("Low", 2),
    MEDIUM("Medium", 3),
    HIGH("High", 4),
    HIGHEST("High", 5);


    private ViolationPriority(String string2, int n2)
    {
    }
}

