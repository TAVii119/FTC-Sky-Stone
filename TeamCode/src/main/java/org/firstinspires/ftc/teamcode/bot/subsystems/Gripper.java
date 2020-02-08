package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Gripper implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo gripper;
    private State state = State.SPIT_OUT;

    public Gripper(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        INTAKE(1.0),
        SPIT_OUT(0);

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
        this.gripper = hardwareMap.get(Servo.class, "gripper");
    }

    @Override
    public void periodic() {
        gripper.setPosition(state.position);
    }
}

