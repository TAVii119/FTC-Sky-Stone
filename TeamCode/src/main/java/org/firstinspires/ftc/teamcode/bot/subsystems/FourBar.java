package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FourBar implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo servoGbL;
    private Servo servoGbR;
    private State state = State.WAIT;

    public FourBar(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        WAIT(0.1),
        INTAKE(0.0),
        SCORE1(0.55),
        SCORE2(0.72);

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
        this.servoGbL = hardwareMap.get(Servo.class, "servoGbL");
        this.servoGbR = hardwareMap.get(Servo.class, "servoGbR");
        servoGbL.setDirection(Servo.Direction.REVERSE);
        servoGbR.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        servoGbL.setPosition(state.position);
        servoGbR.setPosition(state.position);
    }

    public double getPosition() {
        return state.position;
    }
}
