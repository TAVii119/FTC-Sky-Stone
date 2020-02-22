package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BrateAutonomie implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo servoArmL;
    private Servo servoArmR;
    private Servo servoClawL;
    private Servo servoClawR;
    private State state = State.HOLD;

    public BrateAutonomie(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        HOLD(0.0);

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
        this.servoArmL = hardwareMap.get(Servo.class, "servoArmL");
        this.servoArmR = hardwareMap.get(Servo.class, "servoArmR");
        this.servoClawL = hardwareMap.get(Servo.class, "servoClawL");
        this.servoClawR = hardwareMap.get(Servo.class, "servoClawR");

        servoArmL.setDirection(Servo.Direction.FORWARD);
        servoArmR.setDirection(Servo.Direction.REVERSE);
        servoClawL.setDirection(Servo.Direction.FORWARD);
        servoClawR.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        servoArmL.setPosition(state.position);
        servoArmR.setPosition(state.position);
        servoClawL.setPosition(state.position);
        servoClawR.setPosition(state.position);
    }

}
