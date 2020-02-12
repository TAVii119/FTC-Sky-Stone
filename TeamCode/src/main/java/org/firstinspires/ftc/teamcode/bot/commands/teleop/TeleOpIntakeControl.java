package org.firstinspires.ftc.teamcode.bot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcontroller.external.samples.SampleRevBlinkinLedDriver;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.bot.subsystems.Intake;

import java.util.concurrent.TimeUnit;

public class TeleOpIntakeControl implements Command {
    private Intake intake;
    private Gamepad gamepad;
    private final static int GAMEPAD_LOCKOUT = 200;
    Deadline gamepadRateLimit;

    public TeleOpIntakeControl(Intake intake, Gamepad gamepad) {
        this.intake = intake;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        intake.setState(Intake.State.STOP);
        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void periodic() {
        handleGamepad();
    }

    @Override
    public void stop() {
        intake.setState(Intake.State.STOP);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    protected void handleGamepad()
    {
        if (!gamepadRateLimit.hasExpired()) {
            return;
        }

        if (gamepad.a && intake.getPower() != 1) {
            intake.setState(Intake.State.INTAKE);
            gamepadRateLimit.reset();
        } else if (gamepad.a && (intake.getPower() == 1 || intake.getPower() == -1)) {
            intake.setState(Intake.State.STOP);
            gamepadRateLimit.reset();
        } else if (gamepad.b && intake.getPower() != -1) {
            intake.setState(Intake.State.SPIT_OUT);
            gamepadRateLimit.reset();
        } else if (gamepad.b && (intake.getPower() == 1 || intake.getPower() == -1)) {
            intake.setState(Intake.State.STOP);
            gamepadRateLimit.reset();
        }
    }
}
