package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FoundationLatch implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo servoFounL;
    private Servo servoFounR;
    private State state = State.UNLATCH;

    public FoundationLatch(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        UNLATCH(0.0),
        LATCH(0.67);

        private final double position;

        State(double position) {
            this.position = position;
        }

    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void initHardware() {
        this.servoFounL = hardwareMap.get(Servo.class, "servoFounL");
        this.servoFounR = hardwareMap.get(Servo.class, "servoFounR");
        servoFounL.setDirection(Servo.Direction.REVERSE);
        servoFounR.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        servoFounL.setPosition(state.position);
        servoFounR.setPosition(state.position);
    }

    public double getPosition(){return state.position;}
}
