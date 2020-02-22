package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Gripper implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo servoGrip;
    private State state = State.SPIT_OUT;

    public Gripper(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        INTAKE(0.77),
        SPIT_OUT(0.0);

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
        this.servoGrip = hardwareMap.get(Servo.class, "servoGrip");
        this.servoGrip.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        servoGrip.setPosition(state.position);
    }

    public double getPosition() {
        return state.position;
    }
}
