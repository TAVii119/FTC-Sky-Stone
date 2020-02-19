package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FoundationLatch implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo foundationL;
    private Servo foundationR;
    private State state = State.LATCH;

    public FoundationLatch(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        LATCH(1.0),
        UNLATCH(0.0);

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
        this.foundationL = hardwareMap.get(Servo.class, "foundationL");
        this.foundationR = hardwareMap.get(Servo.class, "foundationR");
    }

    @Override
    public void periodic() {
        foundationL.setPosition(state.position);
        foundationR.setPosition(state.position);
    }

    public double getPosition(){return state.position;}
}
