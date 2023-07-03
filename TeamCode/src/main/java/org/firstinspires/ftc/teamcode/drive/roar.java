/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.drive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
@Disabled
@Autonomous(name = "RoarAuto_Simplu")
public class roar extends LinearOpMode {

    //private static final String TFOD_MODEL_ASSET = "demo.tflite";
    public static Pose2d startPose = new Pose2d(-72, -36, Math.toRadians(0));
    public static Pose2d currentPose =  startPose;
    private static final String TFOD_MODEL_FILE  = "/sdcard/FIRST/tflitemodels/demo.tflite";
    private static final String[] LABELS = {
            "1 Caz",
            "2 Caz",
            "3 Caz"
    };
    private static final String VUFORIA_KEY =
            "AVeXfHL/////AAABmX0uuijaJk/XhSJpiGV+reRCbhz8oPrfwiR1HNnHpDsZztPAoJl2crcU/T9Gj4s+Cux+YR6EVRwJzd4j8+Pyo/WdIiW8vdse3SlwJvrGa2mUDpQsmGAJH7rBr8bSTnuZbYDklS6QCUbKJ8qYwQ5oHUI1Z9UqINPGA19WK7hfXZmCKlcEZa0nh9b1zbP53TH9s/PmXpc1+QTI20ExOzBoTzW0YOyv6nWGqN+zPLb7RRfSklTGcz0D3pLevt4L7SG8tqNQswFKw+nsUkE46MCHl9GIpN7+KChz5tHhR9CVffHAY+lTVifTw90rc65w1/40ecvIjOzgq7Rv1P0ykwxwpclgNR2mI5mmMQQuYs0omSRh";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public String Caz;
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor brat1;
    private Servo gheara;
    private Servo sw;
    private Servo sw2;
    public int nr_conuri=5;
    public boolean con_1=false;

    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setPoseEstimate(startPose);
        brat1 = hardwareMap.get(DcMotor.class, "brat1");
        gheara = hardwareMap.get(Servo.class, "gheara");
        sw = hardwareMap.get(Servo.class, "sw");
        sw2 = hardwareMap.get(Servo.class, "sw2");
        brat1.setDirection(DcMotorSimple.Direction.REVERSE);
        brat1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brat1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            runtime = new ElapsedTime();
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
                        telemetry.addData("Timp", runtime.seconds());

                        // step through the list of recognitions and display image position/size information for each one
                        // Note: "Image number" refers to the randomized image orientation/number
                        for (Recognition recognition : updatedRecognitions) {
                            double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                            double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                            double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                            double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;
                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                            Caz=recognition.getLabel();
                        }
                        telemetry.update();
                    }
                }
                telemetry.addData("Caz: ", Caz );
                if(con_1==false)
                    con1();
                //requestOpModeStop();
                ciclu_high();
                //parcare_caz();
            }
        }
    }
     void con1(){
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gheara.setPosition(0.4);
        brat1.setTargetPosition(100);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(0.85);
        TrajectorySequence traj2 = drive.trajectorySequenceBuilder(startPose)
                .forward(47)
                .turn(Math.toRadians(100))
                .forward(12.7)
                .build();
        TrajectorySequence traj3 = drive.trajectorySequenceBuilder(traj2.end())
                .strafeRight(7)
                .build();
        drive.followTrajectorySequence(traj2);
        brat1.setTargetPosition(3000);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        sleep(1000);
        sw.setPosition(0.2);
        sw2.setPosition(0.5);
        sleep(1000);
        drive.followTrajectorySequence(traj3);
        gheara.setPosition(0.3);
        telemetry.addData("Pos x: ", currentPose.component1());
        telemetry.addData("Pos y: ", currentPose.component2());
        telemetry.update();
        con_1=true;
    }
    public void ciclu_high(){
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Trajectory traj2 = drive.trajectoryBuilder(currentPose)
                .strafeRight(3)
                .build();
        Trajectory traj3 = drive.trajectoryBuilder(traj2.end())
                .back(36.8)
                .build();
        Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                .forward(37.5)
                .build();
        Trajectory traj5 = drive.trajectoryBuilder(traj4.end())
                .strafeRight(5)
                .build();
        brat1.setTargetPosition(800);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        sw.setPosition(0.15);
        sw2.setPosition(0);
        drive.followTrajectory(traj3);
        brat1.setTargetPosition(nr_conuri*98);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        sleep(500);
        gheara.setPosition(0.4);
        sleep(300);
        brat1.setTargetPosition(800);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        drive.followTrajectory(traj4);
        brat1.setTargetPosition(3000);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        sleep(1000);
        sw.setPosition(0.2);
        sw2.setPosition(0.5);
        sleep(1000);
        drive.followTrajectory(traj5);
        gheara.setPosition(0.3);
        requestOpModeStop();
    }
    public void parcare_caz(){
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if(Caz=="3 Caz")
        {
            sleep(2000);
            requestOpModeStop();
        }
        else if (Caz=="1 Caz")
        {
            sleep(2000);
            requestOpModeStop();
        }
        else
        {
            sleep(2000);
            requestOpModeStop();
        }
        requestOpModeStop();
    }
    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        //tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
