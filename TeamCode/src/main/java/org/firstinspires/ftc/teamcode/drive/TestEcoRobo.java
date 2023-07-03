package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="EcoRobo")

public class TestEcoRobo extends LinearOpMode {
    private DcMotor brat = null;
    private Servo sd;
    private Servo ss;
    private Servo cos;
    private AnalogInput anal1;
    private AnalogInput anal2;
    private AnalogInput anal3;
    private AnalogInput anal4;
    public int pos=0;
    boolean slow=false;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        brat = hardwareMap.get(DcMotor.class, "brat");
        sd = hardwareMap.get(Servo.class, "gd");
        ss = hardwareMap.get(Servo.class, "gs");
        cos = hardwareMap.get(Servo.class, "cos");
        anal1=hardwareMap.get(AnalogInput.class, "anal1");
        anal2=hardwareMap.get(AnalogInput.class, "anal2");
        anal3=hardwareMap.get(AnalogInput.class, "anal3");
        anal4=hardwareMap.get(AnalogInput.class, "anal4");
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brat.setDirection(DcMotorSimple.Direction.REVERSE);
        brat.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
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
                    sleep(20);
            }
            if(gamepad1.dpad_down && pos>=25)
            {
                pos-=25;
                sleep(20);
            }
            if(gamepad1.a)
            {
                sd.setPosition(0.4);
                ss.setPosition(0.65);
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
            telemetry.addData("Anal1:", anal1.getVoltage());
            telemetry.addData("Anal2:", anal2.getVoltage());
            telemetry.addData("Anal3:", anal3.getVoltage());
            telemetry.addData("Anal4:", anal4.getVoltage());
            telemetry.update();
        }
    }
}
