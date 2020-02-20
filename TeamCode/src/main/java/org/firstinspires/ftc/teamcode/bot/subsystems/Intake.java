package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake implements Subsystem {
    private HardwareMap hardwareMap;

    private DcMotor motorIntakeL;
    private DcMotor motorIntakeR;
    private State state = State.STOP;

    public Intake(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        INTAKE(1.0),
        SPIT_OUT(-1.0),
        STOP(0.0);

        private final double power;

        State(double power) {
            this.power = power;
        }

    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void initHardware() {
        this.motorIntakeL= hardwareMap.get(DcMotor.class, "motorIntakeL");
        this.motorIntakeR = hardwareMap.get(DcMotor.class, "motorIntakeR");
        this.motorIntakeR.setDirection(DcMotorSimple.Direction.REVERSE);
        this.motorIntakeL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.motorIntakeR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void periodic() {
        this.motorIntakeL.setPower(state.power);
        this.motorIntakeR.setPower(state.power);
    }

    public double getPower() {
        return state.power;
    }
}
