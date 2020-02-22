package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name= "FullBlue", group="DF")

public class AutoFullBlue extends LinearOpMode {
    Hardware map = new Hardware();
    ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_MOTOR_REV = 537.6;    // Motor ticks
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 3.9370;    // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    private double found_latch = 0.67;
    private double bar_intake = 0.0, bar_wait = 0.1, bar_score1 = 0.60, bar_score2 = 0.70;
    private double gripper_intake = 0.77;
    private double arm_intake = 0.32;
    private double claw_intake = 0.30;
    private double driveSpeed = 0.5;
    private double strafeSpeed = 0.5;
    private double rotateSpeed = 0.3;

    //0 means skystone, 1 means yellow stone
    //-1 for debug, but we can keep it like this because if it works, it should change to either 0 or 255
    private static int valMid = -1;
    private static int valLeft = -1;
    private static int valRight = -1;

    private static float rectHeight = .6f/8f;
    private static float rectWidth = 1.5f/8f;

    private static float offsetX = 0f/8f;//changing this moves the three rects and the three circles left or right, range : (-2, 2) not inclusive
    private static float offsetY = 2.5f/8f;//changing this moves the three rects and circles up or down, range: (-4, 4) not inclusive

    private static float[] midPos = {4f/8f+offsetX, 4f/8f+offsetY};//0 = col, 1 = row
    private static float[] leftPos = {2f/8f+offsetX, 4f/8f+offsetY};
    private static float[] rightPos = {6f/8f+offsetX, 4f/8f+offsetY};
    //moves all rectangles right or left by amount. units are in ratio to monitor

    private final int rows = 640;
    private final int cols = 480;

    OpenCvCamera phoneCam;

    @Override
    public void runOpMode() throws InterruptedException {
        map.init(hardwareMap);
        map.servoArmL.setPosition(0);
        map.servoArmR.setPosition(0);
        map.servoClawL.setPosition(0);
        map.servoClawR.setPosition(0);
        map.servoGbL.setPosition(bar_wait);
        map.servoGbR.setPosition(bar_wait);
        map.servoClawR.setPosition(claw_intake);
        resetEncoders();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();//open camera
        phoneCam.setPipeline(new StageSwitchingPipeline());//different stages
        phoneCam.startStreaming(rows, cols, OpenCvCameraRotation.SIDEWAYS_RIGHT);//display on RC
        //width, height
        //width = height in this case, because camera is in portrait mode.

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            if (valLeft == 0) {
                moveArm(arm_intake);
                drive(15, driveSpeed);
                rotate(22.5, rotateSpeed, 'r'); // rotate 90 degrees
                drive(9.5, driveSpeed);
                strafe(11,strafeSpeed, 'l');
                strafe(2, 0.3, 'l');
                moveClaw(claw_intake);
                sleep(270);
                moveArm(0.0);
                strafe(8, strafeSpeed, 'r');
                drive(-90, 0.5);
                strafe(10, strafeSpeed, 'l');
                moveArm(0.28);
                sleep(250);
                moveClaw(0.0);
                sleep(150);
                strafe(5, 0.3, 'l');
                strafe(5, strafeSpeed, 'r');
                moveArm(0.0);
                moveClaw(claw_intake);
                strafe(10, strafeSpeed, 'r');
                rotate(22.5, rotateSpeed, 'r');
                strafe(10, 0.5, 'r');
                drive(-10, driveSpeed);
                drive(-5, 0.3);
                moveLatch(found_latch);
                sleep(750);
                drive(34, driveSpeed);
                strafe(5, strafeSpeed, 'r');
                drive(10, driveSpeed);
                sleep(30000);
            }

        }
    }

    public void drive(double inches, double power) {
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()) {

            // Determine new target position
            newLFtarget = map.motorLF.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newRFtarget = map.motorRF.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newLBtarget = map.motorLB.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newRBtarget = map.motorRB.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            map.motorLF.setTargetPosition(newLFtarget);
            map.motorRF.setTargetPosition(newRFtarget);
            map.motorLB.setTargetPosition(newLBtarget);
            map.motorRB.setTargetPosition(newRBtarget);

            // Turn on RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Start motion
            map.motorLF.setPower(power);
            map.motorRF.setPower(power);
            map.motorLB.setPower(power);
            map.motorRB.setPower(power);

            while (opModeIsActive() && (map.motorLF.isBusy() || map.motorRF.isBusy() || map.motorLB.isBusy() || map.motorRB.isBusy())) {
                // Display it for the driver.
                telemetry.addData(">","Driving to:" + inches);
                telemetry.update();
            }

            // Stop all motion;
            map.motorLF.setPower(0);
            map.motorRF.setPower(0);
            map.motorLB.setPower(0);
            map.motorRB.setPower(0);

            // Turn off RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    private void strafe(double inches, double power, char direction){
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()){

            if(direction == 'l'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }else if(direction == 'r'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }

            // Turn on RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Start motion
            map.motorLF.setPower(power);
            map.motorRF.setPower(power);
            map.motorLB.setPower(power);
            map.motorRB.setPower(power);

            while (opModeIsActive() && (map.motorLF.isBusy() || map.motorRF.isBusy() || map.motorLB.isBusy() || map.motorRB.isBusy())) {
                // Display it for the driver.
                telemetry.addData(">","Strafing to: " + inches);
                telemetry.update();
            }

            // Stop all motion;
            map.motorLF.setPower(0);
            map.motorRF.setPower(0);
            map.motorLB.setPower(0);
            map.motorRB.setPower(0);

            // Turn off RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    private void rotate(double inches, double power, char direction){
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()){

            if(direction == 'r'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }else if(direction == 'l'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }

            // Turn on RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Start motion
            map.motorLF.setPower(power);
            map.motorRF.setPower(power);
            map.motorLB.setPower(power);
            map.motorRB.setPower(power);

            while (opModeIsActive() && (map.motorLF.isBusy() || map.motorRF.isBusy() || map.motorLB.isBusy() || map.motorRB.isBusy())) {
                // Display it for the driver.
                telemetry.addData(">","Rotating to: " + inches);
                telemetry.update();
            }

            // Stop all motion;
            map.motorLF.setPower(0);
            map.motorRF.setPower(0);
            map.motorLB.setPower(0);
            map.motorRB.setPower(0);

            // Turn off RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    private void diagonal(double inches, double power, char direction){
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()){

            if(direction == 'r'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }else if(direction == 'l'){
                // Determine new target position
                newRFtarget = map.motorLF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorRB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);

            }

            // Turn on RUN_TO_POSITION
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Start motion
            map.motorRF.setPower(power);
            map.motorLB.setPower(power);

            while (opModeIsActive() && (map.motorRF.isBusy() || map.motorLB.isBusy())) {
                // Display it for the driver.
                telemetry.addData(">","Rotating to: " + inches);
                telemetry.update();
            }

            // Stop all motion;
            map.motorLF.setPower(0);
            map.motorRF.setPower(0);
            map.motorLB.setPower(0);
            map.motorRB.setPower(0);

            // Turn off RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void moveLatch(double position) {

        if (opModeIsActive()) {
            map.servoFounL.setPosition(position);
            map.servoFounR.setPosition(position);
        }
    }

    public void moveArm(double position) {

        if (opModeIsActive()) {
            map.servoArmL.setPosition(position);
        }
    }

    public void moveClaw(double position) {

        if (opModeIsActive()) {
            map.servoClawL.setPosition(position);
        }
    }

    public void resetEncoders() {
        // RESET MOTOR ENCODERS
        map.motorLF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorRF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorLB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorRB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // SET RUN MODE
        map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //detection pipeline
    static class StageSwitchingPipeline extends OpenCvPipeline
    {
        Mat yCbCrChan2Mat = new Mat();
        Mat thresholdMat = new Mat();
        Mat all = new Mat();
        List<MatOfPoint> contoursList = new ArrayList<>();

        enum Stage
        {//color difference. greyscale
            detection,//includes outlines
            THRESHOLD,//b&w
            RAW_IMAGE,//displays raw view
        }

        private Stage stageToRenderToViewport = Stage.detection;
        private Stage[] stages = Stage.values();

        @Override
        public void onViewportTapped()
        {
            /*
             * Note that this method is invoked from the UI thread
             * so whatever we do here, we must do quickly.
             */

            int currentStageNum = stageToRenderToViewport.ordinal();

            int nextStageNum = currentStageNum + 1;

            if(nextStageNum >= stages.length)
            {
                nextStageNum = 0;
            }

            stageToRenderToViewport = stages[nextStageNum];
        }

        @Override
        public Mat processFrame(Mat input)
        {
            contoursList.clear();
            /*
             * This pipeline finds the contours of yellow blobs such as the Gold Mineral
             * from the Rover Ruckus game.
             */

            //color diff cb.
            //lower cb = more blue = skystone = white
            //higher cb = less blue = yellow stone = grey
            Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);//converts rgb to ycrcb
            Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);//takes cb difference and stores

            //b&w
            Imgproc.threshold(yCbCrChan2Mat, thresholdMat, 102, 255, Imgproc.THRESH_BINARY_INV);

            //outline/contour
            Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            yCbCrChan2Mat.copyTo(all);//copies mat object
            //Imgproc.drawContours(all, contoursList, -1, new Scalar(255, 0, 0), 3, 8);//draws blue contours


            //get values from frame
            double[] pixMid = thresholdMat.get((int)(input.rows()* midPos[1]), (int)(input.cols()* midPos[0]));//gets value at circle
            valMid = (int)pixMid[0];

            double[] pixLeft = thresholdMat.get((int)(input.rows()* leftPos[1]), (int)(input.cols()* leftPos[0]));//gets value at circle
            valLeft = (int)pixLeft[0];

            double[] pixRight = thresholdMat.get((int)(input.rows()* rightPos[1]), (int)(input.cols()* rightPos[0]));//gets value at circle
            valRight = (int)pixRight[0];

            //create three points
            Point pointMid = new Point((int)(input.cols()* midPos[0]), (int)(input.rows()* midPos[1]));
            Point pointLeft = new Point((int)(input.cols()* leftPos[0]), (int)(input.rows()* leftPos[1]));
            Point pointRight = new Point((int)(input.cols()* rightPos[0]), (int)(input.rows()* rightPos[1]));

            //draw circles on those points
            Imgproc.circle(all, pointMid,5, new Scalar( 255, 0, 0 ),1 );//draws circle
            Imgproc.circle(all, pointLeft,5, new Scalar( 255, 0, 0 ),1 );//draws circle
            Imgproc.circle(all, pointRight,5, new Scalar( 255, 0, 0 ),1 );//draws circle

            //draw 3 rectangles
            Imgproc.rectangle(//1-3
                    all,
                    new Point(
                            input.cols()*(leftPos[0]-rectWidth/2),
                            input.rows()*(leftPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(leftPos[0]+rectWidth/2),
                            input.rows()*(leftPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);
            Imgproc.rectangle(//3-5
                    all,
                    new Point(
                            input.cols()*(midPos[0]-rectWidth/2),
                            input.rows()*(midPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(midPos[0]+rectWidth/2),
                            input.rows()*(midPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);
            Imgproc.rectangle(//5-7
                    all,
                    new Point(
                            input.cols()*(rightPos[0]-rectWidth/2),
                            input.rows()*(rightPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(rightPos[0]+rectWidth/2),
                            input.rows()*(rightPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);

            switch (stageToRenderToViewport)
            {
                case THRESHOLD:
                {
                    return thresholdMat;
                }

                case detection:
                {
                    return all;
                }

                case RAW_IMAGE:
                {
                    return input;
                }

                default:
                {
                    return input;
                }
            }
        }

    }
}
