package org.firstinspires.ftc.teamcode.drive;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
@TeleOp(name = "TeleOpSmart")
public class TeleOpSmart extends LinearOpMode {

    //private static final String TFOD_MODEL_ASSET = "demo.tflite";
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/ecorobo.tflite";
    private static final String[] LABELS = {
            "doza",
            "plastic",
            "sticla"
    };
    private static final String VUFORIA_KEY =
            "AVeXfHL/////AAABmX0uuijaJk/XhSJpiGV+reRCbhz8oPrfwiR1HNnHpDsZztPAoJl2crcU/T9Gj4s+Cux+YR6EVRwJzd4j8+Pyo/WdIiW8vdse3SlwJvrGa2mUDpQsmGAJH7rBr8bSTnuZbYDklS6QCUbKJ8qYwQ5oHUI1Z9UqINPGA19WK7hfXZmCKlcEZa0nh9b1zbP53TH9s/PmXpc1+QTI20ExOzBoTzW0YOyv6nWGqN+zPLb7RRfSklTGcz0D3pLevt4L7SG8tqNQswFKw+nsUkE46MCHl9GIpN7+KChz5tHhR9CVffHAY+lTVifTw90rc65w1/40ecvIjOzgq7Rv1P0ykwxwpclgNR2mI5mmMQQuYs0omSRh";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public DcMotor directie;
    public DcMotor stanga = null;
    public DcMotor dreapta = null;
    public DcMotor brat = null;
    public Servo sd, ss, cos;
    public int pos=0;
    public boolean slow=false;

    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0 / 9.0);
        }
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        FtcDashboard.getInstance().startCameraStream(vuforia, 0);
        brat = hardwareMap.get(DcMotor.class, "brat");
        sd = hardwareMap.get(Servo.class, "gd");
        ss = hardwareMap.get(Servo.class, "gs");
        cos = hardwareMap.get(Servo.class, "cos");
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brat.setDirection(DcMotorSimple.Direction.REVERSE);
        brat.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() != 0) {
                            Recognition recognition = updatedRecognitions.get(0);
                            FtcDashboard dashboard = FtcDashboard.getInstance();
                            telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
                            dashboard.startCameraStream(vuforia, 10);
                            if (recognition.getLabel() == "doza")
                                cos.setPosition(0.68);
                            else if (recognition.getLabel() == "sticla")
                                cos.setPosition(1);
                            else
                                cos.setPosition(0.32);
                        }
                    }
                }
                if (slow==false) {
                    drive.setWeightedDrivePower(
                            new Pose2d(
                                    gamepad1.left_stick_y,
                                    gamepad1.left_stick_x,
                                    gamepad1.right_stick_x
                            )
                    );
                }
                else {
                    drive.setWeightedDrivePower(
                            new Pose2d(
                                    gamepad1.left_stick_y/3,
                                    gamepad1.left_stick_x/3,
                                    gamepad1.right_stick_x/3
                            )
                    );
                }
                brat.setTargetPosition(pos);
                brat.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                brat.setPower(1);
                if (gamepad1.right_bumper)
                    slow=true;
                if(gamepad1.left_bumper)
                    slow=false;
                if(gamepad1.dpad_up && pos<=5000)
                {
                    pos+=25;
                    sleep(100);
                }
                if(gamepad1.dpad_down && pos>=25)
                {
                    pos-=25;
                    sleep(100);
                }
                if(gamepad1.a)
                {
                    sd.setPosition(0.5);
                    ss.setPosition(0.6);
                }
                if(gamepad1.b)
                {
                    sd.setPosition(0.3);
                    ss.setPosition(0.75);
                }
                if(gamepad1.x)
                {
                    cos.setPosition(cos.getPosition()+0.02);
                    sleep(200);
                }
                if(gamepad1.y)
                {
                    cos.setPosition(cos.getPosition()-0.02);
                    sleep(200);

                }
                telemetry.addData("Posss:", brat.getCurrentPosition());
                telemetry.addData("Pos", pos);
                telemetry.addData("cos", cos.getPosition());
                telemetry.update();
            }
        }
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
        private void initTfod () {
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