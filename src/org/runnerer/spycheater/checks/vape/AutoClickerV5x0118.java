package org.runnerer.spycheater.checks.vape;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.WeakHashMap;

public class AutoClickerV5x0118
        extends Check
{

    private WeakHashMap<Player, ClickProfile> profiles = new WeakHashMap();

    public AutoClickerV5x0118(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.AUTOCLICKER, "VClicker", "AutoClicker", 110, 7, 3, 0);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent playerInteractEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ClickProfile clickProfile = null;
        if (!this.profiles.containsKey((Object) player))
        {
            clickProfile = new ClickProfile();
            this.profiles.put(player, clickProfile);
        } else
        {
            clickProfile = this.profiles.get((Object) player);
        }
        clickProfile.issueClick(player, this);
    }

    public class ClickProfile
    {

        public double clicks = 0.0;
        private long clickSprint = 0L;
        private double lastCPS = 0.0;
        private double twoSecondsAgoCPS = 0.0;
        private double threeSecondsAgoCPS = 0.0;
        private int violations = 0;

        public void issueClick(Player player, Check check)
        {
            long l = System.currentTimeMillis();
            if (l - this.clickSprint >= 1000L)
            {
                this.shuffleDown();
                this.clickSprint = l;
                this.clicks = 0.0;
                double d = this.lastCPS;
                double d2 = this.twoSecondsAgoCPS;
                double d3 = this.threeSecondsAgoCPS;
                if (d == 9.0 && d2 == 11.0 && d3 == 10.0 || d == 9.0 && d2 == 8.0 && d3 == 10.0)
                {
                    System.out.println("[0x01]: " + player.getName() + "logged for a Pattern of VIDJDEI");
                    ++this.violations;
                    if (this.violations >= 10)
                    {
                        AutoClickerV5x0118.this.getCore().addViolation(player, check, new Violation(check, ViolationPriority.HIGH, String.format("CPS[1s ago]: %.2f, CPS[2s ago]: %.2f, CPS[3s ago]: %.2f", this.lastCPS, this.twoSecondsAgoCPS, this.threeSecondsAgoCPS)));
                    }
                }
            }
            this.clicks += 1.0;
        }

        private void shuffleDown()
        {
            this.threeSecondsAgoCPS = this.twoSecondsAgoCPS;
            this.twoSecondsAgoCPS = this.lastCPS;
            this.lastCPS = this.clicks;
        }
    }

}

