package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeeptesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50.270057431256674, 50.270057431256674, Math.toRadians(184.02), Math.toRadians(184.02), 12)
                .followTrajectorySequence(drive ->
                                drive.trajectorySequenceBuilder(new Pose2d(-35, -62, Math.toRadians(90)))
                                        .lineToSplineHeading(new Pose2d(-35, -12, Math.toRadians(0)))
                                        .lineTo(new Vector2d(-23,-12))
                                        .lineTo(new Vector2d(-62,-12))
                                        .lineTo(new Vector2d(-23,-12))
                                        .build()
//                        drive.trajectorySequenceBuilder(new Pose2d(35, -62, Math.toRadians(90)))
//                                .lineToSplineHeading(new Pose2d(35, -12, Math.toRadians(180)))
//                                .lineTo(new Vector2d(23,-12))
//                                .lineTo(new Vector2d(62,-12))
//                                .lineTo(new Vector2d(23,-12))
//                                .build()
//                        drive.trajectorySequenceBuilder(new Pose2d(35, -62, Math.toRadians(90)))
//                                .lineToSplineHeading(new Pose2d(35, -16, Math.toRadians(90)))
//                                .lineToSplineHeading(new Pose2d(23, -12, Math.toRadians(180)))
//                                .lineToSplineHeading(new Pose2d(23, -10, Math.toRadians(180)))
//                                .lineToSplineHeading(new Pose2d(23, -12, Math.toRadians(180)))
//                                .lineToLinearHeading(new Pose2d(60, -12, Math.toRadians(180)))
//                                .lineToSplineHeading(new Pose2d(23, -12, Math.toRadians(180)))
//                                .build()
//                        drive.trajectorySequenceBuilder(new Pose2d(35, -62, Math.toRadians(90)))
//                                .forward(50)
//                                .turn(Math.toRadians(90))
//                                .forward(12)
//                                .strafeRight(3)
//                                .strafeLeft(3)
//                                .back(38)
//                                .forward(38)
//                                 .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}