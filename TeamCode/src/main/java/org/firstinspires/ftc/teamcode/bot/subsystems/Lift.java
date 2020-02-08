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
        liftMotorL = hardwareMap.get(DcMotor.class, "liftMotorL");
        liftMotorR = hardwareMap.get(DcMotor.class, "liftMotorR");

        liftMotorR.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        liftMotorL.setPower(leftPower);
        liftMotorR.setPower(rightPower);
    }
}
