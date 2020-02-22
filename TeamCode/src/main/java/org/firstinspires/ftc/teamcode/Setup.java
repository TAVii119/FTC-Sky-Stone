package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.bot.subsystems.FourBar;
import org.firstinspires.ftc.teamcode.bot.subsystems.Gripper;

import static android.os.SystemClock.sleep;


@TeleOp(name="Setup", group="SETUP")

public class Setup extends LinearOpMode {

    // DEFINE ROBOT'S HARDWARE
    Hardware map = new Hardware();

    // INSTANTIATE AND DEFINE VARIABLES
    double LF; double RF; double LB; double RB;

    @Override
    public void runOpMode() {
        map.init(hardwareMap); // Initialize hardware map

        // Wait for the game to start
        waitForStart();

        // Run until driver presses STOP
        while(opModeIsActive()){


            //-//-----------\\-\\
            //-//   DRIVE   \\-\\
            //-//-----------\\-\\

            LF = -gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x + gamepad1.right_trigger;
            RF = -gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x + gamepad1.left_trigger;
            LB = -gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x + gamepad1.left_trigger;
            RB = -gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x + gamepad1.right_trigger;

            map.motorLF.setPower(LF);
            map.motorRF.setPower(RF);
            map.motorLB.setPower(LB);
            map.motorRB.setPower(RB);

            map.motorLiftL.setPower(-gamepad2.left_stick_y * 0.1);
            map.motorLiftR.setPower(-gamepad2.left_stick_y * 0.1);

            if (gamepad1.a) {
                map.motorIntakeL.setPower(1.0);
                map.motorIntakeR.setPower(1.0);
            } else if (gamepad1.b) {
                map.motorIntakeL.setPower(-1.0);
                map.motorIntakeR.setPower(-1.0);
            } else {
                map.motorIntakeL.setPower(0);
                map.motorIntakeR.setPower(0);
            }

            //-//-------------------\\-\\
            //-// TELEMETRY & OTHER \\-\\
            //-//-------------------\\-\\

            telemetry.addData("motorLiftL position: ", map.motorLiftL.getCurrentPosition());
            telemetry.addData("motorLiftR position: ", map.motorLiftR.getCurrentPosition());
            telemetry.addData("Servo position: ", -gamepad2.right_stick_x);
            telemetry.update();
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
        map.motorIntakeL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorIntakeR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // SET MOTOR MODE
        map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorIntakeL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.motorIntakeR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        map.motorLiftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        map.motorLiftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
}