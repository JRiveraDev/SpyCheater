package org.runnerer.spycheater.checks.movement.speed;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.NMSUtil;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class SpeedC extends Check
{

    private double previousDistance, moveSpeed;
    private double blockFriction = 0.91;
    private int fallTicks, waterTicks;
    private int threshold;

    public SpeedC(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.OTHER, "Speed", "Speed", 3, 50, 10, 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Location to = event.getTo();
        Location from = event.getFrom();
        Player player = event.getPlayer();

        PlayerStats playerStats = this.getCore().getPlayerStats(player);

        // THE LINE OF IMPORTANT THINGS. PVPING NOW IS REPAIRED.
        if (player.getFallDistance() > 0) return;
        if (!playerStats.isOnGround()) return;
        if (playerStats.getVelocityXZ() > 0) return;
        if ((double) player.getWalkSpeed() > 0.21) return;
        if (playerStats.getVelocityY() > 2) return;
        if (playerStats.getBlockPlacementSpam() > 0) return;

        double deltaX = to.getX() - from.getX();
        double deltaY = to.getY() - from.getY();
        double deltaZ = to.getZ() - from.getZ();

        double moveSpeed = 1;

        double blockFriction = this.blockFriction;

        EntityPlayer nmsPlayer = NMSUtil.getNMSPlayer(player);

        boolean onGround = nmsPlayer.onGround;


        if (deltaY < 0)
        {
            ++fallTicks;
        } else
        {
            fallTicks = 0;
        }

        if (onGround)
        {
            blockFriction *= 0.91;

            moveSpeed *= blockFriction > 0.708 ? 1.3 : 0.23315;

            moveSpeed *= 0.16277136 / Math.pow(blockFriction, 3);

            if (deltaY > 0)
            {
                moveSpeed += 0.2;

                if (deltaY < 0.41999998688697815)
                {
                    moveSpeed *= 0.76;
                }
            } else if (deltaY < 0.0)
            {
                moveSpeed *= 0.2;
            } else
            {
                moveSpeed -= 0.1;
            }
        } else
        {
            moveSpeed = 0.026;
            blockFriction = 0.91;

            if (nmsPlayer.inWater)
            {
                moveSpeed += 1;
            }

            if (fallTicks == 1)
            {
                double dy = Math.abs(deltaY);
                if (dy > 0.08 || dy < 0.07)
                {
                    moveSpeed /= (dy * 150);
                }
            }
        }

        double previousHorizontal = previousDistance;

        double horizontalDistance = UtilMath.getMagnitude(deltaX, deltaZ);

        boolean underBlock = isUnderBlock(nmsPlayer);

        if (underBlock)
        {
            moveSpeed += 0.05;
        }

        moveSpeed += horizontalDistance / 6.5;

        double horizontalMove = (horizontalDistance - previousHorizontal) / moveSpeed;

        //TODO: Implement a way better fix for this
        boolean fallFix = moveSpeed > 0.046 && moveSpeed < 0.047 &&
                deltaY == (-0.07840000152587923) ||
                deltaY == (-0.07840000152587834) ||
                deltaY == (-0.078400001525879);

        if (fallFix)
        {
            horizontalMove *= 0.34;
        }

        if (horizontalDistance > 0.2763 && horizontalMove > 1.0)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Speed"));
        }

        previousDistance = horizontalDistance * blockFriction;

        this.blockFriction = nmsPlayer.world.getType(new BlockPosition(nmsPlayer.locX, nmsPlayer.locY, nmsPlayer.locZ)).getBlock().frictionFactor;
    }

    private boolean isUnderBlock(EntityPlayer player)
    {
        return player.world.c(player.getBoundingBox().grow(0.5, 0, 0.5).a(0.0, 1, 0.0));
    }
}
