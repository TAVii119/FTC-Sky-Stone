package org.firstinspires.ftc.teamcode.bot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.bot.subsystems.Drive;

public class TeleOpDriveControl implements Command {
    private Drive drive;
    private Gamepad gamepad;
    double powerLimit = 1;

    public TeleOpDriveControl(Drive drive, Gamepad gamepad) {
        this.drive = drive;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        drive.setPower(0, 0, 0, 0);
    }

    @Override
    public void periodic() {
         if (gamepad.left_bumper)
             powerLimit = 1;
         else if (gamepad.right_bumper)
             powerLimit = 0.1;

        drive.setPower(
                (-gamepad.left_stick_y + gamepad.right_stick_x + gamepad.left_stick_x + gamepad.right_trigger)*powerLimit,
                (-gamepad.left_stick_y - gamepad.right_stick_x - gamepad.left_stick_x + gamepad.left_trigger)*powerLimit,
                (-gamepad.left_stick_y + gamepad.right_stick_x - gamepad.left_stick_x + gamepad.left_trigger)*powerLimit,
                (-gamepad.left_stick_y - gamepad.right_stick_x + gamepad.left_stick_x + gamepad.right_trigger)*powerLimit
        );
    }

    @Override
    public void stop() {
        drive.setPower(0, 0, 0, 0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}