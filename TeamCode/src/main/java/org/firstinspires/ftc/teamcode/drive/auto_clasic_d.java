package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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

@Autonomous(name = "Auto RoarD")
@Disabled
public class auto_clasic_d extends LinearOpMode{
    private static final String TFOD_MODEL_FILE  = "/sdcard/FIRST/tflitemodels/regionala_2.tflite";
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
    private DcMotorEx brat1;
    private Servo gheara;
    private Servo sw;
    private Servo sw2;
    public DcMotorEx leftFront;
    public DcMotorEx rightRear;
    public DcMotorEx leftRear;
    public DcMotorEx rightFront;
    private DcMotor intakes;
    private DcMotor intaked;
    public ElapsedTime runtime = new ElapsedTime();
    public int nr_conuri=5;


    @Override
    public void runOpMode()  {
        initVuforia();
        initTfod();
        brat1 = hardwareMap.get(DcMotorEx.class, "brat1");
        gheara = hardwareMap.get(Servo.class, "gheara");
        sw = hardwareMap.get(Servo.class, "sw");
        sw2 = hardwareMap.get(Servo.class, "sw2");
        leftFront = hardwareMap.get(DcMotorEx.class, "sf");
        leftRear = hardwareMap.get(DcMotorEx.class, "ss");
        rightRear = hardwareMap.get(DcMotorEx.class, "ds");
        rightFront = hardwareMap.get(DcMotorEx.class, "df");
        intaked= hardwareMap.get(DcMotor.class, "intaked");
        intakes= hardwareMap.get(DcMotor.class, "intakes");
        brat1.setDirection(DcMotorSimple.Direction.REVERSE);
        brat1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brat1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }
        gheara.setPosition(0.4);
        sleep(500);
        brat1.setTargetPosition(100);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(0.85);
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
                con_1();
                while(runtime.seconds()<15)
                {
                    ciclu_high();
                    nr_conuri--;
                }
                Parcare_caz();
            }
        }
    }
    public void con_1()
    {
        Stop_Reset();
        EncoderPower(15,0.3,'f');
        Stop_Reset();
        EncoderPower(48,0.5,'a');
        Stop_Reset();
        EncoderPower(130,0.5,'r');
        intaked.setPower(0);
        intakes.setPower(0);
        Stop_Reset();
        EncoderPower(23,0.5,'f');
        EncoderPower(35,0.2,'f');
        brat1.setTargetPosition(2900);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(0.85);
        Stop_Reset();
        EncoderPower(12,0.4, 'r');
        while(brat1.isBusy() && opModeIsActive())
        {
            telemetry.addData("Merg in", "Sus");
            telemetry.update();
        }
        sw.setPosition(0.2);
        sw2.setPosition(0.5);
        sleep(3000);
        gheara.setPosition(0.3);
        sleep(650);
    }
    public void ciclu_high()
    {
        sw.setPosition(0.14);
        sw2.setPosition(0);
        sleep(500);
        brat1.setTargetPosition(800);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        Stop_Reset();
        EncoderPower(95,0.5,'b');
        Stop_Reset();
        EncoderPower(40,0.2,'b');
        Stop_Reset();
        EncoderPower(3,0.2,'f');
        brat1.setTargetPosition(nr_conuri*98);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        sleep(500);
        gheara.setPosition(0.4);
        sleep(300);
        brat1.setTargetPosition(1000);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        Stop_Reset();
        EncoderPower(25,0.2,'f');
        EncoderPower(100,0.5,'f');
        brat1.setTargetPosition(2950);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(0.85);
        while(brat1.isBusy() && opModeIsActive())
        {
            telemetry.addData("Merg in", "Sus");
            telemetry.update();
        }
        sw.setPosition(0.2);
        sw2.setPosition(0.5);
        Stop_Reset();
        EncoderPower(4,0.4, 'r');
        sleep(600);
        gheara.setPosition(0.3);
        sleep(500);

    }
    public void Parcare_caz()
    {
        sw.setPosition(0.15);
        sw2.setPosition(0);
        sleep(1000);
        brat1.setTargetPosition(0);
        brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat1.setPower(1);
        if(Caz=="2 Caz")
        { //check
            Stop_Reset();
            EncoderPower(30, 0.5, 'b');
            requestOpModeStop();
        }
        else if(Caz=="1 Caz")
        { //check
            Stop_Reset();
            EncoderPower(30, 0.5, 'f');
            requestOpModeStop();
        }
        else
        {
            //check
            Stop_Reset();
            EncoderPower(90, 0.5, 'b');
            requestOpModeStop();
        }
    }
    public void EncoderPower(double dist, double power, char heading)
    {
        int tick1 = (int) (dist*537.7/9.6/3.1415);
        int ss=0,ds=0,df=0,sf=0;
        if (heading=='f')
        {
            ss=sf=-1;
            ds=df=1;
        }
        if(heading=='b')
        {
            ss=sf=1;
            ds=df=-1;
        }
        if(heading=='r')
        {
            df=sf=-1;
            ss=ds=1;
        }
        if(heading=='l')
        {
            df=sf=1;
            ss=ds=-1;
        }
        if (heading=='a')
        {
            ss=sf=ds=df=-1;
        }
        if (heading=='a')
        {
            ss=sf=ds=df=1;
        }
        leftRear.setTargetPosition(ss*tick1);
        rightRear.setTargetPosition(ds*tick1);
        rightFront.setTargetPosition(df*tick1);
        leftFront.setTargetPosition(sf*tick1);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setPower(power);
        rightRear.setPower(power);
        rightFront.setPower(power);
        leftFront.setPower(power);
        while(leftFront.isBusy() && rightFront.isBusy() && rightRear.isBusy() && leftRear.isBusy())
        {
            telemetry.addData("Merg in", heading);
            telemetry.update();
        }
    }
    public void Stop_Reset()
    {
        sleep(100);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

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
