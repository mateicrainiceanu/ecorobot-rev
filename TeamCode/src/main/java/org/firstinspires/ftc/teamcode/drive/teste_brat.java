package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name="TeleOp")
@Disabled
public class teste_brat extends LinearOpMode {
    private DcMotor brat1;
    private CRServo brat2;
    private DcMotor ss;
    private DcMotor sf;
    private DcMotor ds;
    private DcMotor df;
    private Servo gheara;
    private TouchSensor load;
    @Override   
    public void runOpMode() {
        brat1 = hardwareMap.get(DcMotor.class, "brat1");
        brat2 = hardwareMap.get(CRServo.class, "brat2");
        ss = hardwareMap.get(DcMotor.class, "ss");
        ds = hardwareMap.get(DcMotor.class, "ds");
        df = hardwareMap.get(DcMotor.class, "df");
        sf = hardwareMap.get(DcMotor.class, "sf");
        gheara = hardwareMap.get(Servo.class, "gheara");
        load= hardwareMap.get(TouchSensor.class, "load");
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        double fs, sd, st,press=0;
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                fs = 0;
                sd = 0;
                st = 0;
                brat1.setPower(-gamepad2.left_stick_y / 2);
                brat2.setPower(-gamepad2.left_stick_x / 2);
                    //fata
                    df.setPower(-gamepad1.right_trigger/2);
                    ds.setPower(gamepad1.right_trigger/2);
                    sf.setPower(gamepad1.right_trigger/2);
                    ss.setPower(-gamepad1.right_trigger/2);
                    //spate
                    df.setPower(gamepad1.left_trigger/2);
                    ds.setPower(-gamepad1.left_trigger/2);
                    sf.setPower(-gamepad1.left_trigger/2);
                    ss.setPower(gamepad1.left_trigger/2);
                    //stanga dreapta
                    df.setPower(gamepad1.right_stick_x/2);
                    ds.setPower(-gamepad1.right_stick_x/2);
                    sf.setPower(gamepad1.right_stick_x/2);
                    ss.setPower(-gamepad1.right_stick_x/2);
                    //strafe
                    df.setPower(gamepad1.left_stick_x/2);
                    ds.setPower(gamepad1.left_stick_x/2);
                    sf.setPower(gamepad1.left_stick_x/2);
                    ss.setPower(gamepad1.left_stick_x/2);
                if(gamepad2.dpad_right)
                    gheara.setPosition(0.3);
                if(gamepad2.dpad_left)
                    gheara.setPosition(0.4);
                if (gamepad1.a) {
                    gamepad1.rumble(1000);
                    if(press==0) {
                        df.setPower(-1);
                        ds.setPower(1);
                        sf.setPower(1);
                        ss.setPower(-1);
                        press++;
                    }

                }
                if(press==1)
                {
                    df.setPower(0);
                    ds.setPower(0);
                    sf.setPower(0);
                    ss.setPower(0);
                    press=0;
                }


            }
        }
    }
}
