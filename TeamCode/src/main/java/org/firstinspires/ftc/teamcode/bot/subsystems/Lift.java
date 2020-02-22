package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift implements Subsystem {
    private HardwareMap hardwareMap;

    private DcMotor motorLiftL;
    private DcMotor motorLiftR;

    private double leftPower = 0;
    private double rightPower = 0;

    public Lift(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void setPower(double leftPower, double rightPower) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
    }

    public int returnPosition() {
        return motorLiftR.getCurrentPosition();
    }

    public void moveToTarget(int target, double power) {
        motorLiftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorLiftL.setTargetPosition(target);
        motorLiftR.setTargetPosition(target);

        motorLiftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorLiftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorLiftL.setPower(power);
        motorLiftR.setPower(power);

        while (motorLiftR.isBusy()) {
            // wait
        }

        motorLiftL.setPower(0);
        motorLiftR.setPower(0);

        motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void returnLift(double power) {
        motorLiftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        motorLiftR.setTargetPosition(0);

        motorLiftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorLiftR.setPower(power);

        while (motorLiftR.isBusy()) {

        }

        motorLiftL.setPower(0);
        motorLiftR.setPower(0);

        motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void initHardware() {
        this.motorLiftL = hardwareMap.get(DcMotor.class, "motorLiftL");
        this.motorLiftR = hardwareMap.get(DcMotor.class, "motorLiftR");

        this.motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.motorLiftL.setDirection(DcMotor.Direction.REVERSE);

        this.motorLiftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.motorLiftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // BRAKE
    }

    @Override
    public void periodic() {
        motorLiftL.setPower(leftPower);
        motorLiftR.setPower(rightPower);
    }
}
