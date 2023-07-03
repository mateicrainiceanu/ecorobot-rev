package org.firstinspires.ftc.teamcode.drive;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class ResetServos extends LinearOpMode {
    private Servo sd;
    private Servo ss;
    private Servo cos;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        sd = hardwareMap.get(Servo.class, "sd");
        ss = hardwareMap.get(Servo.class, "ss");
        cos = hardwareMap.get(Servo.class, "cos");
        waitForStart();
        while (opModeIsActive()) {
            sd.setPosition(0.5);
            ss.setPosition(0.6);
            if (gamepad1.a)
                cos.setPosition(0.32);
            else if (gamepad1.b)
                cos.setPosition(1);
            else
                cos.setPosition(0.68);
            telemetry.addData("ServoD:", sd.getPosition());
            telemetry.addData("ServoS:", ss.getPosition());
            telemetry.update();
        }
    }
}