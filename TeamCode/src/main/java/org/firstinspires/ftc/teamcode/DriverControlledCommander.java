package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.bot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.bot.commands.teleop.TeleOpIntakeControl;
import org.firstinspires.ftc.teamcode.bot.commands.teleop.TeleOpScoringControl;
import org.firstinspires.ftc.teamcode.bot.commands.teleop.TeleOpLatchControl;
import org.firstinspires.ftc.teamcode.bot.subsystems.BrateAutonomie;
import org.firstinspires.ftc.teamcode.bot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.bot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.bot.subsystems.FourBar;
import org.firstinspires.ftc.teamcode.bot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.bot.subsystems.Gripper;
import org.firstinspires.ftc.teamcode.bot.subsystems.FoundationLatch;
import org.firstinspires.ftc.teamcode.bot.subsystems.BrateAutonomie;

@TeleOp(group = "DriverControlledCommander")
@Disabled
public class DriverControlledCommander extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() {
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        FourBar fourbar = new FourBar(hardwareMap);
        Lift lift = new Lift(hardwareMap);
        Gripper gripper = new Gripper(hardwareMap);
        FoundationLatch latch = new FoundationLatch(hardwareMap);
        BrateAutonomie brate = new BrateAutonomie(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(fourbar);
        commander.registerSubsystem(lift);
        commander.registerSubsystem(gripper);
        commander.registerSubsystem(latch);
        commander.registerSubsystem(brate);
        commander.init();

        waitForStart();

        commander.runCommandsParallel(
                new TeleOpDriveControl(drive, gamepad1),
                new TeleOpIntakeControl(intake, gamepad1),
                new TeleOpScoringControl(lift, fourbar, gripper, gamepad2),
                new TeleOpLatchControl(latch, gamepad1)
        );

        telemetry.addData("Position: ", lift.returnPosition());
        telemetry.update();

        commander.stop();
    }
}