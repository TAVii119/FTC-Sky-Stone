package org.firstinspires.ftc.teamcode.bot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.bot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.bot.subsystems.FourBar;
import org.firstinspires.ftc.teamcode.bot.subsystems.Gripper;

import java.util.concurrent.TimeUnit;

import static android.os.SystemClock.sleep;

public class TeleOpScoringControl implements Command {
    private Lift lift;
    private FourBar fourbar;
    private Gripper gripper;
    private Gamepad gamepad;
    double liftPowerL = 0;
    double liftPowerR = 0;
    double level = 1;
    private final static int GAMEPAD_LOCKOUT = 200;
    Deadline gamepadRateLimit;

    public TeleOpScoringControl(Lift lift, FourBar fourbar, Gripper gripper, Gamepad gamepad) {
        this.lift = lift;
        this.fourbar = fourbar;
        this.gripper = gripper;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        lift.setPower(0, 0);
        fourbar.setState(FourBar.State.WAIT);
        gripper.setState(Gripper.State.SPIT_OUT);
        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void periodic() {
        handleGamepad();

        if (gamepad.a) {
            lift.moveToTarget(getTarget(), 1.0);
            if (level == 1)
                fourbar.setState(FourBar.State.SCORE2);
            else fourbar.setState(FourBar.State.SCORE1);

        } else if (gamepad.b) {
            fourbar.setState(FourBar.State.WAIT);
            sleep(250);
            lift.returnLift(1.0);

        }

        liftPowerL = -gamepad.left_stick_y * 0.1;
        liftPowerR = -gamepad.left_stick_y * 0.1;
        lift.setPower(liftPowerL, liftPowerR);
    }

    @Override
    public void stop() {
        lift.setPower(0, 0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    protected void handleGamepad() {
        if (!gamepadRateLimit.hasExpired()) {
            return;
        }

        if (gamepad.dpad_up && level <= 8) { // INCREASE THE CURRENT LEVEL OF THE SKYSCRAPER
            level++;
            gamepadRateLimit.reset();
        } else if (gamepad.dpad_down && level >= 2) { // DECREASE THE CURRENT LEVEL OF THE SKYSCRAPER
            level--;
            gamepadRateLimit.reset();
        }

        if (gamepad.x && gripper.getPosition() == 0.77) { // MOVE FROM INTAKE TO SPIT_OUT
            gripper.setState(Gripper.State.SPIT_OUT);
            gamepadRateLimit.reset();
        } else if (gamepad.x && gripper.getPosition() == 0.0) { // MOVE FROM SPIT_OUT TO INTAKE
            fourbar.setState(FourBar.State.INTAKE);
            sleep(250);
            gripper.setState(Gripper.State.INTAKE);
            gamepadRateLimit.reset();
        }

        if (gamepad.y && fourbar.getPosition() == 0.1) { // MOVE FROM WAIT TO INTAKE
            fourbar.setState(FourBar.State.INTAKE);
            gamepadRateLimit.reset();
        } else if (gamepad.y && fourbar.getPosition() == 0.0) { // MOVE FROM INTAKE TO SCORE1
            fourbar.setState(FourBar.State.SCORE1);
            gamepadRateLimit.reset();
        } else if (gamepad.y && fourbar.getPosition() == 0.55) { // MOVE FROM SCORE1 TO SCORE2
            fourbar.setState(FourBar.State.SCORE2);
            gamepadRateLimit.reset();
        } else if (gamepad.y && fourbar.getPosition() == 0.72) { // MOVE FROM SCORE 2 BACK TO WAIT
            fourbar.setState(FourBar.State.WAIT);
            gamepadRateLimit.reset();
        } else if (gamepad.dpad_left) {
            fourbar.setState(FourBar.State.SCORE1);
            gamepadRateLimit.reset();
        } else if (gamepad.dpad_right) {
            fourbar.setState(FourBar.State.SCORE2);
            gamepadRateLimit.reset();
        }
    }

    public int getTarget() {

        if (level == 1) {
            return 0;
        } else if (level == 2) {
            return 0;
        } else if (level == 3) {
            return 360;
        } else if (level == 4) {
            return 630;
        } else if (level == 5) {
            return 860;
        } else if (level == 6) {
            return 1250;
        } else if (level == 7) {
            return 1530;
        } else if (level == 8) {
            return 1790;
        } else if (level == 9) {
            return 2140;
        } else return 0;
    }
}
