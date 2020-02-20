package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drive implements Subsystem{
    private HardwareMap hardwareMap;

    private DcMotor motorLF;
    private DcMotor motorRF;
    private DcMotor motorLB;
    private DcMotor motorRB;


    private double LFpower = 0;
    private double RFpower = 0;
    private double LBpower = 0;
    private double RBpower = 0;

    public Drive(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void setPower(double LFpower, double RFpower, double LBpower, double RBpower) {
        this.LFpower = LFpower;
        this.RFpower = RFpower;
        this.LBpower = LBpower;
        this.RBpower = RBpower;
    }

    @Override
    public void initHardware() {
        motorLF = hardwareMap.get(DcMotor.class, "LF");
        motorRF = hardwareMap.get(DcMotor.class, "RF");
        motorLB = hardwareMap.get(DcMotor.class, "LB");
        motorRB = hardwareMap.get(DcMotor.class, "RB");

        motorRF.setDirection(DcMotor.Direction.REVERSE);
        motorRB.setDirection(DcMotorSimple.Direction.REVERSE);

        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void periodic() {
        motorLF.setPower(LFpower);
        motorRF.setPower(RFpower);
        motorLB.setPower(LBpower);
        motorRB.setPower(RBpower);
    }
}
