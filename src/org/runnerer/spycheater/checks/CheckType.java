package org.runnerer.spycheater.checks;

public enum CheckType
{
    AUTOCLICKER("AutoClicker", 0),
    COMBAT("Combat", 1),
    KILLAURA("KillAura", 2),
    MOVEMENT("Movement", 3),
    HEURISTIC("Heuristic", 4),
    OTHER("Other", 5);

    private String name;

    private CheckType(String string2, int n2)
    {
        this.name = string2;
    }

    public String getName()
    {
        return this.name;
    }
}

