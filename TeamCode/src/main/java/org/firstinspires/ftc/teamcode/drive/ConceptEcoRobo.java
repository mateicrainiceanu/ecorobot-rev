
package org.firstinspires.ftc.teamcode.drive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import java.util.List;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorColor;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
@Autonomous(name = "ConceptEcoRobo")
public class ConceptEcoRobo extends LinearOpMode {

    //private static final String TFOD_MODEL_ASSET = "demo.tflite";
    private static final String TFOD_MODEL_FILE  = "/sdcard/FIRST/tflitemodels/ecorobo.tflite";
    private static final String[] LABELS = {
            "doza",
            "plastic",
            "sticla"
    };
    private static final String VUFORIA_KEY =
            "AVeXfHL/////AAABmX0uuijaJk/XhSJpiGV+reRCbhz8oPrfwiR1HNnHpDsZztPAoJl2crcU/T9Gj4s+Cux+YR6EVRwJzd4j8+Pyo/WdIiW8vdse3SlwJvrGa2mUDpQsmGAJH7rBr8bSTnuZbYDklS6QCUbKJ8qYwQ5oHUI1Z9UqINPGA19WK7hfXZmCKlcEZa0nh9b1zbP53TH9s/PmXpc1+QTI20ExOzBoTzW0YOyv6nWGqN+zPLb7RRfSklTGcz0D3pLevt4L7SG8tqNQswFKw+nsUkE46MCHl9GIpN7+KChz5tHhR9CVffHAY+lTVifTw90rc65w1/40ecvIjOzgq7Rv1P0ykwxwpclgNR2mI5mmMQQuYs0omSRh";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public DcMotor brat = null;
    public Servo sd,ss,cos;
    public DcMotorEx leftFront;
    public DcMotorEx rightRear;
    public DcMotorEx leftRear;
    public DcMotorEx rightFront;
    public boolean reglat=false,reglats=false;

    @Override
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();
        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0 / 9.0);
        }
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        leftFront = hardwareMap.get(DcMotorEx.class, "sf");
        leftRear = hardwareMap.get(DcMotorEx.class, "ss");
        rightRear = hardwareMap.get(DcMotorEx.class, "ds");
        rightFront = hardwareMap.get(DcMotorEx.class, "df");
        brat = hardwareMap.get(DcMotor.class, "brat");
        sd = hardwareMap.get(Servo.class, "gd");
        ss = hardwareMap.get(Servo.class, "gs");
        cos = hardwareMap.get(Servo.class, "cos");
        brat.setDirection(DcMotorSimple.Direction.REVERSE);
        brat.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        if (opModeIsActive()) {
            Stop_Reset();
            while (opModeIsActive()) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() != 0) {
                            Recognition recognition = updatedRecognitions.get(0);
                            telemetry.addData("vad", recognition.getLabel());
                            telemetry.addData("Stanga", recognition.getLeft());
                            telemetry.addData("Top", recognition.getTop());
                            Stop_Reset();
                            if(!reglat && !reglats)
                                if(Reglaj(recognition.getLeft()))
                                {
                                    telemetry.addLine("Reglat");
                                    telemetry.addData("vad", recognition.getLeft());
                                    sleep(500);
                                    reglats=true;
                                }
                            if(reglats && !reglat)
                            {
                                telemetry.addLine("Il iau");
                                telemetry.addData("vad", recognition.getTop());
                                if (recognition.getLabel() == "doza") {
                                    Stop_Reset();
                                    EncoderPower((int) (250 - recognition.getTop()), 0.2, 'f');
                                }
                                else if (recognition.getLabel() == "sticla") {
                                    Stop_Reset();
                                    EncoderPower((int) (3 - recognition.getTop()), 0.2, 'f');
                                }
                                else {
                                    Stop_Reset();
                                    EncoderPower((int) (195 - recognition.getTop()), 0.2, 'f');
                                }
                                reglat=true;
                            }
                            if (recognition.getLabel() == "doza")
                                cos.setPosition(0.68);
                            else if (recognition.getLabel() == "sticla")
                                cos.setPosition(1);
                            else
                                cos.setPosition(0.32);
                            telemetry.update();
                        }
                        else
                        {
                            if(!reglat && !reglats) {
                                EncoderPower(10, 0.2, 'r');
                                Stop_Reset();
                                sleep(1000);
                            }
                            if(reglat)
                            {
                                Stop_Reset();
                                EncoderPower(5,0.1,'f');
                                sd.setPosition(0.45);
                                ss.setPosition(0.65);
                                brat.setTargetPosition(3800);
                                brat.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                brat.setPower(1);
                                while(brat.isBusy())
                                {
                                    telemetry.addLine("L-am luat");
                                    telemetry.update();
                                }
                                sd.setPosition(0.3);
                                ss.setPosition(0.75);
                                requestOpModeStop();
                            }
                        }
                    }
                }
            }
        }
    }

    public void EncoderPower(double dist, double power, char heading)
    {
        int tick1 = (int) (dist*537.7/9.6/3.1415);
        int ss=0,ds=0,df=0,sf=0;
        if (heading=='b')
        {
            ss=sf=-1;
            ds=df=1;
        }
        if(heading=='f')
        {
            ss=sf=1;
            ds=df=-1;
        }
        if(heading=='l')
        {
            df=sf=-1;
            ss=ds=1;
        }
        if(heading=='r')
        {
            df=sf=1;
            ss=ds=-1;
        }
        if (heading=='a')
        {
            ss=sf=ds=df=-1;
        }
        if (heading=='t')
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
    public boolean Reglaj(float stanga)
    {
        if(stanga>230 || stanga<210)
        {
            Stop_Reset();
            EncoderPower((int)((stanga-220)*0.1),0.5, 'r');
            return false;
        }
        else
            return true;
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
        FtcDashboard.getInstance().startCameraStream(vuforia, 0);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
