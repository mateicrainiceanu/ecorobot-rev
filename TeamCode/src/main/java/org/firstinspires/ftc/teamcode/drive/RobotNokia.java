package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;



@TeleOp(name = "1. Robot Nokia")
public class RobotNokia extends LinearOpMode {
    private DcMotor right, left;
    boolean spate=false,slow=false;
    @Override
    public void runOpMode() throws InterruptedException {
        right= hardwareMap.get(DcMotor.class, "dr");
        left= hardwareMap.get(DcMotor.class, "st");
        waitForStart();
        while (!isStopRequested()) {
            if (slow == false) {
                if (spate == false) {
                    right.setPower(-gamepad1.right_trigger);
                    left.setPower(gamepad1.left_trigger);
                } else {
                    right.setPower(gamepad1.right_trigger);
                    left.setPower(-gamepad1.left_trigger);
                }
            } else {
                if (spate == false) {
                    right.setPower(-gamepad1.right_trigger / 2);
                    left.setPower(gamepad1.left_trigger / 2);
                } else {
                    right.setPower(gamepad1.right_trigger / 2);
                    left.setPower(-gamepad1.left_trigger / 2);
                }
            }
            if (gamepad1.a) {
                spate ^= true;
                sleep(200);
            }
            if(gamepad1.b){
                slow^=true;
                sleep(200);
            }
            }
        }
    }
