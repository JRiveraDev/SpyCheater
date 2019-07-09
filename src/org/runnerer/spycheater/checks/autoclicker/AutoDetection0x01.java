package org.runnerer.spycheater.checks.autoclicker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.WeakHashMap;

public class AutoDetection0x01
        extends Check
{

    private WeakHashMap<Player, ClickProfile> profiles = new WeakHashMap();

    public AutoDetection0x01(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.AUTOCLICKER, "AutoClicker", "AutoClicker", 110, 7, 7, 0);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent playerInteractEvent)
    {
        if (playerInteractEvent.getAction() != Action.LEFT_CLICK_AIR)
        {
            return;
        }
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        if (player.getAllowFlight())
        {
            return;
        }
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
                if (this.isConstant() || this.hasInvalidClickSpeed() || this.hasVapePattern())
                {
                    ++this.violations;
                    if (this.violations >= 4)
                    {
                        AutoDetection0x01.this.getCore().addViolation(player, check, new Violation(check, ViolationPriority.LOW, String.format("CPS[1s ago]: %.2f, CPS[2s ago]: %.2f, CPS[3s ago]: %.2f", this.lastCPS, this.twoSecondsAgoCPS, this.threeSecondsAgoCPS)));
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

        private boolean hasInvalidClickSpeed()
        {
            if (!(this.lastCPS >= 19.0)) return false;
            return true;
        }

        private boolean hasVapePattern()
        {
            if (this.lastCPS != 9.0) return false;
            if (this.twoSecondsAgoCPS != 11.0) return false;
            if (this.threeSecondsAgoCPS != 10.0) return false;
            return true;
        }

        private boolean isConstant()
        {
            if (!(this.threeSecondsAgoCPS >= 14.0)) return false;
            if (this.lastCPS != this.twoSecondsAgoCPS) return false;
            if (this.twoSecondsAgoCPS != this.threeSecondsAgoCPS) return false;
            return true;
        }
    }

}

