package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake implements Subsystem {
    private HardwareMap hardwareMap;

    private DcMotor intakeL;
    private DcMotor intakeR;
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
        this.intakeL = hardwareMap.get(DcMotor.class, "intakeL");
        this.intakeR = hardwareMap.get(DcMotor.class, "intakeR");
        this.intakeR.setDirection(DcMotorSimple.Direction.REVERSE);
        this.intakeR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.intakeL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void periodic() {
        this.intakeL.setPower(state.power);
        this.intakeR.setPower(state.power);
    }

    public double getPower() {
        return state.power;
    }
}

