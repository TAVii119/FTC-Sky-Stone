package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import java.util.concurrent.TimeUnit;

/**
 * DriverControlled for team Delta Force
 * Created on 21.11.2019 by Tavi
 */

@TeleOp(name="DriverControlledAutomated", group="DF")

public class DriverControlledAutomated extends LinearOpMode {

    // DEFINE ROBOT'S HARDWARE
    Hardware map = new Hardware();

    // INSTANTIATE AND DEFINE VARIABLES
    ElapsedTime runtime = new ElapsedTime();
    double LF; double RF; double LB; double RB;
    private final static int GAMEPAD_LOCKOUT = 200; // PRESS DELAY IN MS
    Deadline gamepadRateLimit;
    private int level = 1;
    private int target;
    private int liftPower;
    private double found_latch = 0.67;
    private double bar_intake = 0.0, bar_wait = 0.1, bar_score1 = 0.60, bar_score2 = 0.70;
    private double gripper_intake = 0.77;
    private double claw_retract = 0.5;
    boolean grip = false;
    boolean intake_active = false;
    boolean latch = false;

    @Override
    public void runOpMode() {
        map.init(hardwareMap); // Initialize hardware map
        map.servoArmL.setPosition(0);
        map.servoArmR.setPosition(0);
        map.servoGbL.setPosition(bar_wait);
        map.servoGbR.setPosition(bar_wait);
        resetEncoders();
        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);

        // Wait for the game to start
        waitForStart();
        runtime.reset();

        // Run until driver presses STOP
        while(opModeIsActive() && !isStopRequested()) {
            handleGamepad();

            //-//-----------\\-\\
            //-// GAMEPAD 1 \\-\\
            //-//-----------\\-\\

            // Drive
            LF = -gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x + gamepad1.right_trigger;
            RF = -gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x + gamepad1.left_trigger;
            LB = -gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x + gamepad1.left_trigger;
            RB = -gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x + gamepad1.right_trigger;

            map.motorLF.setPower(LF);
            map.motorRF.setPower(RF);
            map.motorLB.setPower(LB);
            map.motorRB.setPower(RB);

            //-//-----------\\-\\
            //-// GAMEPAD 2 \\-\\
            //-//-----------\\-\\

            // Handle lift
            if (gamepad2.a) { // Extend Lift
                moveToTarget(getTarget(), 1.0);
                if (level >= 2) {
                    map.servoGbL.setPosition(bar_score1);
                    map.servoGbR.setPosition(bar_score1);
                } else {
                    map.servoGbL.setPosition(bar_score2);
                    map.servoGbR.setPosition(bar_score2);
                }
            } else if (gamepad2.b) { // Retract Lift
                returnLift(-1.0);
            }

            if (-gamepad2.left_stick_y > 0 && map.motorLiftR.getCurrentPosition() < 2150) {
                map.motorLiftL.setPower(-gamepad2.left_stick_y * 0.1);
                map.motorLiftR.setPower(-gamepad2.left_stick_y * 0.1);
            } else if (-gamepad2.left_stick_y < 0) {
                map.motorLiftR.setPower(-gamepad2.left_stick_y * 0.1);
            } else {
                map.motorLiftL.setPower(0);
                map.motorLiftR.setPower(0);
            }

            //-//-------------------\\-\\
            //-// TELEMETRY & OTHER \\-\\
            //-//-------------------\\-\\

            telemetry.addData("level: ", level);
            telemetry.addData("target:", getTarget());
            telemetry.addData("lift position: ", map.motorLiftR.getCurrentPosition());
            telemetry.update();
        }
    }

    private int getTarget()
    {
        if (level == 1)
            target = 370;
        else if (level == 2)
            target = 370;
        else if (level == 3)
            target = 420;
        else if (level == 4)
            target = 720;
        else if (level == 5)
            target = 920;
        else if (level == 6)
            target = 1320;
        else if (level == 7)
            target = 1620;
        else if (level == 8)
            target = 2070;
        else if (level == 9)
            target = 2135;
        return target;
    }

    public void moveToTarget(int target, double power) {
        map.motorLiftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        map.motorLiftL.setTargetPosition(target);
        map.motorLiftR.setTargetPosition(target);

        map.motorLiftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        map.motorLiftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        map.motorLiftL.setPower(power);
        map.motorLiftR.setPower(power);
        map.motorLF.setPower(0);
        map.motorRF.setPower(0);
        map.motorLB.setPower(0);
        map.motorRB.setPower(0);


        while (opModeIsActive() && map.motorLiftR.isBusy()) {
            telemetry.addData("Moving to: ", getTarget());
            telemetry.addData("Current position: ", map.motorLiftR.getCurrentPosition());
            telemetry.update();
        }

        map.motorLiftL.setPower(0);
        map.motorLiftR.setPower(0);

        map.motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void returnLift(double power) {
        map.motorLiftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        map.motorLiftR.setTargetPosition(0);

        map.motorLiftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        map.motorLiftR.setPower(power);
        map.motorLF.setPower(0);
        map.motorRF.setPower(0);
        map.motorLB.setPower(0);
        map.motorRB.setPower(0);

        while (opModeIsActive() && map.motorLiftR.isBusy()) {
            telemetry.addData("Moving to: ", 0);
            telemetry.addData("Current position: ", map.motorLiftR.getCurrentPosition());
            telemetry.update();
        }

        map.motorLiftL.setPower(0);
        map.motorLiftR.setPower(0);

        map.motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /*
     * handleGamepad
     *
     * Responds to a gamepad button press.  Demonstrates rate limiting for
     * button presses.  If loop() is called every 10ms and and you don't rate
     * limit, then any given button press may register as multiple button presses,
     * which in this application is problematic.
     */

    public void handleGamepad()
    {
        if (!gamepadRateLimit.hasExpired()) { // Check if gamepadRate has expired
            return;
        }

        if (gamepad1.a && !intake_active) {
            map.motorIntakeL.setPower(1);
            map.motorIntakeR.setPower(1);
            intake_active = true;
            gamepadRateLimit.reset();
        } else if (gamepad1.a && (intake_active = true)) {
            map.motorIntakeL.setPower(0);
            map.motorIntakeR.setPower(0);
            intake_active = false;
            gamepadRateLimit.reset();
        } else if (gamepad1.b && !intake_active) {
            map.motorIntakeL.setPower(-1);
            map.motorIntakeR.setPower(-1);
            intake_active = true;
            gamepadRateLimit.reset();
        } else if (gamepad1.b && (intake_active = true)) {
            map.motorIntakeL.setPower(0);
            map.motorIntakeR.setPower(0);
            intake_active = false;
            gamepadRateLimit.reset();
        }

        if (gamepad1.y && !latch) {
            map.servoFounL.setPosition(found_latch);
            map.servoFounR.setPosition(found_latch);
            latch = true;
            gamepadRateLimit.reset();
        } else if (gamepad1.y && (latch = true)) {
            map.servoFounL.setPosition(0);
            map.servoFounR.setPosition(0);
            latch = false;
            gamepadRateLimit.reset();
        }

        if (gamepad2.dpad_up && level <= 8) { // INCREASE THE CURRENT LEVEL OF THE SKYSCRAPER
            level++;
            gamepadRateLimit.reset();
        } else if (gamepad2.dpad_down && level >= 2) { // DECREASE THE CURRENT LEVEL OF THE SKYSCRAPER
            level--;
            gamepadRateLimit.reset();
        }

        if (gamepad2.dpad_left) {
            map.servoGbL.setPosition(bar_score1);
            map.servoGbR.setPosition(bar_score1);
            gamepadRateLimit.reset();
        } else if (gamepad2.dpad_right) {
            map.servoGbL.setPosition(bar_score2);
            map.servoGbR.setPosition(bar_score2);
            gamepadRateLimit.reset();
        } else if (gamepad2.y) {
            map.servoGbL.setPosition(bar_wait);
            map.servoGbR.setPosition(bar_wait);
            gamepadRateLimit.reset();
        } else if (gamepad2.x && !grip) {
            map.servoGbL.setPosition(bar_intake);
            map.servoGbR.setPosition(bar_intake);
            sleep(150);
            map.servoGrip.setPosition(gripper_intake);
            grip = true;
            gamepadRateLimit.reset();
        } else if (gamepad2.x && (grip = true)) {
            map.servoGrip.setPosition(0);
            grip = false;
            gamepadRateLimit.reset();
        }
    }

    public void resetEncoders(){
        // RESET MOTOR ENCODERS
        map.motorLF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorRF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorLB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorRB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorLiftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorLiftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // SET RUN MODE
        map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorIntakeL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.motorIntakeR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}