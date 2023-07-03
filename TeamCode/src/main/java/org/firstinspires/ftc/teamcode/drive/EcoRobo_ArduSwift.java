
package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.List;

@Autonomous(name = "Ardu_Swift")
public class EcoRobo_ArduSwift extends LinearOpMode {
    public DcMotor brat = null;
    public Servo sd,ss,cos;
    public DcMotorEx leftFront;
    public DcMotorEx rightRear;
    public DcMotorEx leftRear;
    public DcMotorEx rightFront;

    public AnalogInput anal1, anal2, anal3, anal4;
    public double pizde=0;
    public DistanceSensor dist;
    public Boolean vazut=false;
    public String objDetected = "_";

    @Override
    public void runOpMode() {

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
        dist = hardwareMap.get(DistanceSensor.class, "dist");
        anal1 = hardwareMap.get(AnalogInput.class, "anal1");
        anal2 = hardwareMap.get(AnalogInput.class, "anal2");
        anal3 = hardwareMap.get(AnalogInput.class, "anal3");
        anal4 = hardwareMap.get(AnalogInput.class, "anal4");
        brat.setDirection(DcMotorSimple.Direction.REVERSE);
        brat.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                Stop_Reset();
                pizde= dist.getDistance(DistanceUnit.CM);
                if(pizde<80)
                {
                    Stop_Reset();
                    vazut=true;
                }
                if (vazut) {
                    detect();
                    Stop_Reset();
                    telemetry.addLine(String.valueOf(dist.getDistance(DistanceUnit.CM)));
                    telemetry.addLine(String.valueOf(pizde));
                    telemetry.update();
                    sleep(200);
                    Stop_Reset();
                    EncoderPower(6, 0.2, 'r');
                    sleep(2000);
                    Stop_Reset();
                    EncoderPower( pizde , 0.3, 'f');
                    sleep(200);
                    Stop_Reset();
                    EncoderPower(5, 0.2, 'f');
                    sd.setPosition(0.45);
                    ss.setPosition(0.65);
                    brat.setTargetPosition(3800);
                    brat.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    brat.setPower(1);
                    while (brat.isBusy()) {
                        telemetry.addLine("L-am luat");
                        telemetry.update();
                    }
                    sd.setPosition(0.3);
                    ss.setPosition(0.75);
                    requestOpModeStop();
                }
                else {
                    EncoderPower(2,0.5,'r');
                }
                telemetry.addLine(String.valueOf(dist.getDistance(DistanceUnit.CM)));
                telemetry.update();
            }
        }
    }

    public void detect() {

        if (anal1.getVoltage() > 2) {
            objDetected = "doza";
            cos.setPosition(0.68);
        } else if (anal2.getVoltage() > 2) {
            objDetected = "sticla";
            cos.setPosition(1);
        } else if (anal3.getVoltage() > 2) {
            objDetected = "plastic";
            cos.setPosition(0.32);
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
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(50);
    }

}
