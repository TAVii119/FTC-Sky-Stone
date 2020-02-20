package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static com.qualcomm.robotcore.hardware.Servo.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.Servo.Direction.REVERSE;

/**
 * Hardware map for team Delta Force
 * Created on 21.11.2018 by Tavi
 */

public class Hardware
{
    // INSTANTIATE MOTORS
    public DcMotor motorLF = null; // FRONT LEFT MOTOR
    public DcMotor motorRF = null; // FRONT RIGHT MOTOR
    public DcMotor motorLB = null; // BACK LEFT MOTOR
    public DcMotor motorRB = null; // BACK RIGHT MOTOR
    public DcMotor motorLiftL = null;
    public DcMotor motorLiftR = null;
    public DcMotor motorIntakeL = null;
    public DcMotor motorIntakeR = null;

    // INSTANTIATE SERVOS
    public Servo servoGbL = null;
    public Servo servoGbR = null;
    public Servo servoFounL = null;
    public Servo servoFounR = null;
    public Servo servoGrip = null;
    public Servo servoArmL = null;
    public Servo servoArmR = null;
    public Servo servoClawL = null;
    public Servo servoClawR = null;


    // CREATE NEW HardwareMap
    HardwareMap robotMap;

    // DEFINE NEW HardwareMap
    public void init(HardwareMap robotMap) {

        // DEFINE MOTORS
        motorLF = robotMap.get(DcMotor.class,"motorLF");
        motorRF = robotMap.get(DcMotor.class,"motorRF");
        motorLB = robotMap.get(DcMotor.class,"motorLB");
        motorRB = robotMap.get(DcMotor.class,"motorRB");
        motorLiftL = robotMap.get(DcMotor.class, "motorLiftL");
        motorLiftR = robotMap.get(DcMotor.class, "motorLiftR");
        motorIntakeL = robotMap.get(DcMotor.class, "motorIntakeL");
        motorIntakeR = robotMap.get(DcMotor.class, "motorIntakeR");

        // SET MOTOR DIRECTION
        motorLF.setDirection(DcMotor.Direction.REVERSE);
        motorRF.setDirection(DcMotor.Direction.FORWARD);
        motorLB.setDirection(DcMotor.Direction.REVERSE);
        motorRB.setDirection(DcMotor.Direction.FORWARD);
        motorLiftL.setDirection(DcMotor.Direction.REVERSE);
        motorLiftR.setDirection(DcMotor.Direction.REVERSE);
        motorIntakeL.setDirection(DcMotor.Direction.FORWARD);
        motorIntakeR.setDirection(DcMotor.Direction.REVERSE);

        // SET MOTOR POWER
        motorLF.setPower(0);
        motorRF.setPower(0);
        motorLB.setPower(0);
        motorRB.setPower(0);
        motorLiftL.setPower(0);
        motorLiftR.setPower(0);
        motorIntakeL.setPower(0);
        motorIntakeR.setPower(0);

        // SET MOTOR MODE
        motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLiftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorIntakeL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorIntakeR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // SET MOTOR ZeroPowerBehavior
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLiftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorLiftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorIntakeL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorIntakeR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // DEFINE SERVOS
        servoGbL = robotMap.get(Servo.class, "servoGbL");
        servoGbR = robotMap.get(Servo.class, "servoGbR");
        servoFounL = robotMap.get(Servo.class, "servoFounL");
        servoFounR = robotMap.get(Servo.class, "servoFounR");
        servoGrip = robotMap.get(Servo.class, "servoGrip");
        servoArmL = robotMap.get(Servo.class, "servoArmL");
        servoArmR = robotMap.get(Servo.class, "servoArmR");
        servoClawL = robotMap.get(Servo.class, "servoClawL");
        servoClawR = robotMap.get(Servo.class, "servoClawR");


        // SET SERVO DIRECTION
        servoGbL.setDirection(Servo.Direction.REVERSE);
        servoGbR.setDirection(Servo.Direction.FORWARD);
        servoFounL.setDirection(Servo.Direction.REVERSE);
        servoFounR.setDirection(Servo.Direction.FORWARD);
        servoGrip.setDirection(Servo.Direction.REVERSE);
        servoArmL.setDirection(Servo.Direction.FORWARD);
        servoArmR.setDirection(Servo.Direction.REVERSE);
        servoClawL.setDirection(Servo.Direction.FORWARD);
        servoClawR.setDirection(Servo.Direction.REVERSE);

        // SET SERVO POSITION
        servoGbL.setPosition(0.0);
        servoGbR.setPosition(0.0);
        servoFounL.setPosition(0.0);
        servoFounR.setPosition(0.0);
        servoGrip.setPosition(0.0);
        servoArmL.setPosition(0.0);
        servoArmR.setPosition(0.0);
        servoClawL.setPosition(0.0);
        servoClawR.setPosition(0.0);
    }
}