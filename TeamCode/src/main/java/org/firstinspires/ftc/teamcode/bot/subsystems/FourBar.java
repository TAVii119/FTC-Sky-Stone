package org.firstinspires.ftc.teamcode.bot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FourBar implements Subsystem {
    private HardwareMap hardwareMap;

    private Servo barLeft;
    private Servo barRight;
    private State state = State.MIDDLE;

    public FourBar(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public enum State {
        INTAKE(1.0),
        MIDDLE(0.5),
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
        this.barLeft = hardwareMap.get(Servo.class, "barLeft");
        this.barRight = hardwareMap.get(Servo.class, "barRight");
        this.barRight.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        barLeft.setPosition(state.position);
        barRight.setPosition(state.position);
    }
}
