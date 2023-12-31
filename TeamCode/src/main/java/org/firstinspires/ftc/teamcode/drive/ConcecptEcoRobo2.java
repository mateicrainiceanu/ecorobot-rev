
package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "Ardu_Swift_Concept")
public class ConcecptEcoRobo2 extends LinearOpMode {
    public DcMotor brat = null;
    public Servo sd,ss,cos;
    public DcMotorEx leftFront;
    public DcMotorEx rightRear;
    public DcMotorEx leftRear;
    public DcMotorEx rightFront;

    public AnalogInput anal1, anal2, anal3, anal4;
    public DistanceSensor dist;
    public Boolean vazut=false,vaz2=false,done=false;
    public double diste;
    public int mers=0;
    public int me=5;
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
        brat.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        if (opModeIsActive()) {
            sd.setPosition(0.25);
            ss.setPosition(0.79);
            while (opModeIsActive()) {
                diste=dist.getDistance(DistanceUnit.CM);
               while(diste>80 && opModeIsActive() && !done)
               {
                   Stop_Reset();
                   EncoderPower(2,0.4,'r');
                   diste=dist.getDistance(DistanceUnit.CM);
                   vazut=true;
                   telemetry.addLine(String.valueOf(diste));
                   telemetry.update();
               }
               if(vazut && !done)
               {
                   EncoderPower(10,0.4,'r');
                   detect();
                   sleep(200);
               }
               while(diste>10 && vazut && opModeIsActive() && !done)
               {
                   Stop_Reset();
                   EncoderPower(3,0.1,'f');
                   diste=dist.getDistance(DistanceUnit.CM);
                   vaz2=true;
                   telemetry.addLine(String.valueOf(diste));
                   telemetry.update();
                   mers+=3;
               }
               if(vaz2 && vazut && !done)
               {
                   Stop_Reset();
                   EncoderPower(me, 0.1, 'f');
                   telemetry.addLine(String.valueOf(diste));
                   telemetry.addLine(String.valueOf(mers));
                   telemetry.update();
                   sleep(2000);
                   sd.setPosition(0.45);
                   ss.setPosition(0.65);
                   sleep(200);
                   brat.setTargetPosition(1200);
                   brat.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                   brat.setPower(0.75);
                   while (brat.isBusy()) {
                       telemetry.addLine("L-am luat");
                       telemetry.update();
                   }
                   sd.setPosition(0.26);
                   ss.setPosition(0.79);
                   sleep(1000);
                   brat.setTargetPosition(0);
                   brat.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                   brat.setPower(0.75);
                   while (brat.isBusy()) {
                       telemetry.addLine("L-am luat");
                       telemetry.update();
                   }
                   EncoderPower(mers-5,0.2,'b');
                   done=true;
               }
               if(done)
               {
                   if(gamepad1.a)
                   {
                       vazut=false;
                       vaz2=false;
                       done=false;
                       mers=0;
                   }
               }
                telemetry.addLine(String.valueOf(diste));
                telemetry.update();
            }
        }
    }

    public void detect() {

        if (anal1.getVoltage() > 2) {
            objDetected = "doza";
            cos.setPosition(0.68);
            telemetry.addLine("Doza");
            telemetry.update();
            me=4;
        } else if (anal2.getVoltage() > 2) {
            objDetected = "sticla";
            cos.setPosition(1);
            telemetry.addLine("Sticla");
            telemetry.update();
            me=5;
        } else if (anal3.getVoltage() > 2) {
            objDetected = "plastic";
            cos.setPosition(0.32);
            telemetry.addLine("Plastic");
            telemetry.update();
            me=6;
        }

    }
    public void EncoderPower(double dist, double power, char heading)
    {
        // 1 thick = 1/537.5 * 1 Motor Rotation
        // Wheel Diameter = 9.6 cm
        // Pi = 3.1415
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
