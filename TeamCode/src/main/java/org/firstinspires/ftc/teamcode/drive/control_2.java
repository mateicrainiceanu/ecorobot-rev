package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(group = "drive")
public class control_2 extends LinearOpMode {
    private DcMotor brat1;
    private DcMotor intakes;
    private DcMotor intaked;
    private Servo gheara;
    private Servo sw;
    private Servo sw2;
    int pos=25,intake=0;
    boolean slow=false;
    @Override
    public void runOpMode() throws InterruptedException {
        brat1 = hardwareMap.get(DcMotor.class, "brat1");
        gheara = hardwareMap.get(Servo.class, "gheara");
        intaked= hardwareMap.get(DcMotor.class, "intaked");
        intakes= hardwareMap.get(DcMotor.class, "intakes");
        sw = hardwareMap.get(Servo.class, "sw");
        sw2 = hardwareMap.get(Servo.class, "sw2");
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brat1.setDirection(DcMotorSimple.Direction.REVERSE);
        brat1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brat1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (!isStopRequested()) {
            if (slow==false) {
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x,
                                -gamepad1.right_stick_x
                        )
                );
            }
            else {
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y/3,
                                -gamepad1.left_stick_x/3,
                                -gamepad1.right_stick_x/3
                        )
                );
            }
            if (gamepad1.right_bumper)
                slow=true;
            if(gamepad1.left_bumper)
                slow=false;
            brat1.setTargetPosition(pos);
            brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            brat1.setPower(0.85);
            if (gamepad2.x)
                gheara.setPosition(0.3);
            if (gamepad2.y)
                gheara.setPosition(0.4);
            if(gamepad1.a)
            {
                intaked.setPower(0.5);
                intakes.setPower(0.5);
            }
            if(gamepad1.b)
            {
                intaked.setPower(0);
                intakes.setPower(0);
            }
            if(gamepad1.dpad_down)
            {
                brat1.setTargetPosition(-50);
                brat1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                brat1.setPower(1);
                while(brat1.isBusy() & opModeIsActive())
                {
                    telemetry.addData("Reset", "Brat");
                    telemetry.update();
                }
                brat1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            if(gamepad2.dpad_left)
            {
                if(pos>500)
                {
                    sw.setPosition(0.7);
                    sw2.setPosition(0.7);
                }
            }
            if(gamepad2.dpad_right)
            {
                if(pos>500){
                    sw.setPosition(0.2);
                    sw2.setPosition(0.5);
                }
            }
            if(gamepad2.dpad_up)
            {
                if(pos>500)
                {
                    sw.setPosition(0.15);
                    sw2.setPosition(1);
                }
            }
            if(gamepad2.dpad_down)
            {
                if(pos>500){
                    sw.setPosition(0.15);
                    sw2.setPosition(0);
                }
            }
            if (gamepad2.right_trigger==1) {
                if(pos<3200)
                    pos+=10;
            }
            if (gamepad2.left_trigger==1) {
                if(pos>0)
                    pos-=10;
            }
            telemetry.addData("Posss:", brat1.getCurrentPosition());
            telemetry.update();
        }
    }
}
