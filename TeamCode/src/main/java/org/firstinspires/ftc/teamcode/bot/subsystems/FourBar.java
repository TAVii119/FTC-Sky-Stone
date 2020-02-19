package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FourBar implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo barLeft;
    private Servo barRight;
    private State state = State.INTAKE;

    public FourBar(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        WAIT(0.2),
        INTAKE(0.0),
        SCORE1(0.5),
        SCORE2(1.0);

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
        this.barLeft = hardwareMap.get(Servo.class, "barLeft");
        this.barRight = hardwareMap.get(Servo.class, "barRight");
        this.barRight.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        barLeft.setPosition(state.position);
        barRight.setPosition(state.position);
    }

    public double getPosition() {
        return state.position;
    }
}
