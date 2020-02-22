package org.firstinspires.ftc.teamcode.bot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.bot.subsystems.FoundationLatch;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

public class TeleOpLatchControl implements Command {
    private FoundationLatch latch;
    private Gamepad gamepad;
    private final static int GAMEPAD_LOCKOUT = 200;
    Deadline gamepadRateLimit;

    public TeleOpLatchControl(FoundationLatch latch, Gamepad gamepad) {
        this.latch = latch;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        latch.setState(FoundationLatch.State.UNLATCH);
        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void periodic() {
        handleGamepad();
    }

    @Override
    public void stop() {
        latch.setState(FoundationLatch.State.UNLATCH);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    protected void handleGamepad() {
        if (!gamepadRateLimit.hasExpired()) {
            return;
        }

        if (gamepad.y && latch.getPosition() == 0) {
            latch.setState(FoundationLatch.State.LATCH);
            gamepadRateLimit.reset();
        } else if (gamepad.y && latch.getPosition() == 0.67) {
            latch.setState(FoundationLatch.State.UNLATCH);
            gamepadRateLimit.reset();
        }
    }
}