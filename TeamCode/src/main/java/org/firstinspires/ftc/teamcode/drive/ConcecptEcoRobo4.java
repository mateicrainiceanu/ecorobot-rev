
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

@Autonomous(name = "Ardu_Swift_Bun_Mih")
public class ConcecptEcoRobo4 extends LinearOpMode {
    public DcMotor brat = null;
    public Servo sd,ss,cos;
    public DcMotorEx leftFront;
    public DcMotorEx rightRear;
    public DcMotorEx leftRear;
    public DcMotorEx rightFront;

    public AnalogInput anal1, anal2, anal3, anal4;
    public DistanceSensor dist;
    public int me=5;
    public boolean det=false,done=false;
    public int dist_to_deseu=0;
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
               if(dist.getDistance(DistanceUnit.CM)<80 && !done)
               {
                   Stop_Reset();
                   sleep(1000);
                   det=true;
                   detect();
                   Deseu();
                   done=true;
               }
               else if(!det)
               {
                   Power_Heading(0.4, 'r');
               }
               if (gamepad1.a && done)
               {
                   done=false;
                   det=false;
               }
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
    public void Power_Heading(double power, char heading)
    {
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
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setPower(power*ss);
        rightRear.setPower(power*ds);
        rightFront.setPower(power*df);
        leftFront.setPower(power*sf);
    }
    public void Deseu ()
    {
        while(dist.getDistance(DistanceUnit.CM)>15)
        {
            Power_Heading(0.4,'f');
        }
        telemetry.addData("parcurs",dist_to_deseu);
        telemetry.addData("Dist", dist.getDistance(DistanceUnit.CM));
        telemetry.update();
        dist_to_deseu=leftFront.getCurrentPosition();
        Stop_Reset();
        EncoderPower(4,0.2,'f');
        sd.setPosition(0.45);
        ss.setPosition(0.65);
        brat.setTargetPosition(1250);
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
            telemetry.addData("Mers", dist_to_deseu);
            telemetry.update();
        }
        sleep(500);
        EncoderPower(dist_to_deseu/537.7*9.6*3.1415,0.4, 'b');

    }
}
