package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;



@TeleOp(group = "1. Robot Nokia")
public class RobotNokia extends LinearOpMode {
    private DcMotor right, left;
    @Override
    public void runOpMode() throws InterruptedException {
        right= hardwareMap.get(DcMotor.class, "dr");
        left= hardwareMap.get(DcMotor.class, "st");
        waitForStart();
        while (!isStopRequested()) {
            right.setPower(gamepad1.right_trigger/2);
            left.setPower(-gamepad1.left_trigger/2);
        }
    }
}
