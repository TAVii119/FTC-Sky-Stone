package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift implements Subsystem {
    private HardwareMap hardwareMap;

    private DcMotor liftMotorL;
    private DcMotor liftMotorR;

    private double leftPower = 0;
    private double rightPower = 0;

    public Lift(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void setPower(double leftPower, double rightPower) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
    }

    @Override
    public void initHardware() {
        this.liftMotorL = hardwareMap.get(DcMotor.class, "liftMotorL");
        this.liftMotorR = hardwareMap.get(DcMotor.class, "liftMotorR");

        this.liftMotorR.setDirection(DcMotor.Direction.REVERSE);

        this.liftMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.liftMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.liftMotorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.liftMotorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void periodic() {
        liftMotorL.setPower(leftPower);
        liftMotorR.setPower(rightPower);
    }

    public int getLiftPosition() {
        int liftPosition = liftMotorL.getCurrentPosition();

        return liftPosition;
    }

    public void moveToTarget(int target, double power) {

        liftMotorL.setTargetPosition(target);
        liftMotorR.setTargetPosition(target);

        liftMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        liftMotorL.setPower(power);
        liftMotorR.setPower(power);

        while ((target - liftMotorL.getCurrentPosition() < target - 25) && (target - liftMotorR.getCurrentPosition() < target -25)) {

        }

        liftMotorL.setPower(0);
        liftMotorR.setPower(0);

        liftMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void returnLift(double power) {
        liftMotorL.setTargetPosition(0);
        liftMotorR.setTargetPosition(0);

        liftMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        liftMotorL.setPower(power);
        liftMotorR.setPower(power);

        while ((liftMotorL.getCurrentPosition() > 25) && (liftMotorR.getCurrentPosition() > 25)) {

        }

        liftMotorL.setPower(0);
        liftMotorR.setPower(0);

        liftMotorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
