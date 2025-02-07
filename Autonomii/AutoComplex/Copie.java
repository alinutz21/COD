package org.firstinspires.ftc.teamcode.COD.Autonomii.AutoComplex;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePieseAutonomie;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

// PUNE IN BASKET 3
@Disabled
@Config
@Autonomous(name = "STANGA nou", group = "AutoSimplu")
public class Copie extends LinearOpMode {

    SlidePieseAutonomie slide;
    public Servo extensionServo;
    public CRServo activeIntakeServo;
    public Servo bendOverServo;
    public Servo liftServo;
    public Servo specimenServo;

    public ValoriFunctii valori = new ValoriFunctii();
    public double target = 0;
    public double viteza_maxima = 0.5;

    public class Lift {
        public Lift(HardwareMap hardwareMap){
            slide = new SlidePieseAutonomie(hardwareMap,"LIFTMOTOR",true,false);
            slide.setPosition(0,0);
            slide.slideUpdate();

        }
        public void update() {
            slide.setPosition(target,viteza_maxima);
            slide.slideUpdate();
        }
    }




    @Override
    public void runOpMode() throws InterruptedException {
        Lift lift = new Lift(hardwareMap);
        valori = new ValoriFunctii();
        extensionServo = hardwareMap.get(Servo.class,"EXTENSIONSERVO");
        activeIntakeServo = hardwareMap.get(CRServo.class,"WHEELSERVO");
        bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

//        // Actiuni care au loc in init
        /*
        extensionServo.setPosition(valori.EXT_HOME);
        liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
        bendOverServo.setPosition(0.2);
         */
        waitForStart();

        extensionServo.setPosition(valori.EXT_HOME);
        liftServo.setPosition(valori.DEPOSIT_IDLE);
        specimenServo.setPosition(valori.SPECIMEN_OPEN);
        bendOverServo.setPosition(0.35);



        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {lift.update(); return true;},
                        new SequentialAction(
                                drive.actionBuilder(initialPose)
//                                        .waitSeconds(0.01)
                                        .build(),



                                new ParallelAction(

                                        drive.actionBuilder(new Pose2d(0,0,Math.toRadians(180)))
                                                .splineToLinearHeading(new Pose2d(-21,3,Math.toRadians(225)),Math.toRadians(180))
                                                .build(),

                                        (p) -> {viteza_maxima = 0.8; return false;},
                                        (p) -> {target = valori.LIFT_BASKET2; return false;}

                                ),

                                drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))

                                        .waitSeconds(0.5)
                                        .build(),

                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},


                                drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))
                                        .waitSeconds(1)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(0.97); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},
                                        (p) -> {viteza_maxima = 0.2; return false;},
                                        (p) -> {target = 0.5; return false;},

                                        new SequentialAction(
                                                drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))
                                                        .strafeToSplineHeading(new Vector2d(10,21),Math.toRadians(320))
                                                        .waitSeconds(0.01)
                                                        .strafeTo(new Vector2d(-5,35))
                                                        .waitSeconds(0.8)
                                                        .build(),
                                                (p) -> {bendOverServo.setPosition(valori.DMP_INTAKE_SIDE); return false;},
                                                (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;}

                                        )


                                ),

                                new SequentialAction(

                                        drive.actionBuilder(new Pose2d(-5,35,Math.toRadians(320)))
                                                .waitSeconds(0.8)
                                                .build(),

                                        new ParallelAction(
                                                (p) ->{activeIntakeServo.setPower(-1); return false;},

                                                drive.actionBuilder(new Pose2d(-5,35,Math.toRadians(320)))
                                                        .strafeToSplineHeading(new Vector2d(-20,6),Math.toRadians(230))
                                                        .build()
                                        )


                                ),

                                        (p) -> {bendOverServo.setPosition(0.2); return false;},
                                        (p) -> {viteza_maxima = 0.8; return false;},
                                        (p) -> {target = valori.LIFT_BASKET2; return false;},

                                drive.actionBuilder(new Pose2d(-20,6,Math.toRadians(230)))
                                        .waitSeconds(1.5)
                                        .strafeTo(new Vector2d(-22.5,3),new TranslationalVelConstraint(5))
                                        .waitSeconds(1.1)
                                        .build(),

                                (p) -> {activeIntakeServo.setPower(0); return false;},
                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},

                                drive.actionBuilder(new Pose2d(-22.5,3,Math.toRadians(230)))
                                        .waitSeconds(1.5)
                                        .build(),

                                new ParallelAction(
                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                        (p) -> {viteza_maxima = 0.2; return false;},
                                        (p) -> {target = 0.5; return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},
                                        drive.actionBuilder(new Pose2d(-22.5,4,Math.toRadians(230)))

                                                .strafeToSplineHeading(new Vector2d(-15,15),Math.toRadians(290))
                                                .waitSeconds(10)
//                                                .lineToY(5)
//                                                .splineToLinearHeading(new Pose2d(-25,12,Math.toRadians(285)),Math.toRadians(10),new TranslationalVelConstraint(20), new ProfileAccelConstraint(-20,20))
//                                                .waitSeconds(0.2)
//                                                .lineToY(28,new TranslationalVelConstraint(60))
                                                .build()
                                ),

                                new SequentialAction(

                                        (p) -> {bendOverServo.setPosition(valori.DMP_INTAKE_SIDE); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},
                                        drive.actionBuilder(new Pose2d(-25,28,Math.toRadians(285)))
                                                .waitSeconds(0.5)
                                                .build(),

                                        new ParallelAction(

                                                (p) -> {activeIntakeServo.setPower(-1); return false;},

                                                drive.actionBuilder(new Pose2d(-22,20,Math.toRadians(280)))
                                                        .waitSeconds(0.5)
                                                        .splineToLinearHeading(new Pose2d(-22,4,Math.toRadians(225)),Math.toRadians(10),new TranslationalVelConstraint(20), new ProfileAccelConstraint(-20,20))
                                                        .build()

                                        )
                                ),

//                                new ParallelAction(
//
//                                        (p) -> {bendOverServo.setPosition(valori.DMP_INTAKE_SIDE); return false;},
//                                        (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},
//
//                                        drive.actionBuilder(new Pose2d(-28,28,Math.toRadians(270)))
//                                                .waitSeconds(0.2)
//                                                .splineToLinearHeading(new Pose2d(-21,3,Math.toRadians(225)),Math.toRadians(270),new TranslationalVelConstraint(25),new ProfileAccelConstraint(-10,25))
//                                                .build()
//
//
//                                ),
//                                (p) -> {activeIntakeServo.setPower(-1); return false;},
//                                drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))
//                                        .waitSeconds(1.5)
//                                        .build(),
//                                new ParallelAction(
//
//                                        drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))
//                                                .waitSeconds(2)
//                                                .build(),

                                        //(p) -> {activeIntakeServo.setPower(0); return false;},
                                        (p) -> {bendOverServo.setPosition(0.2); return false;},

                                drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))

                                        .waitSeconds(0.15)
                                        .build(),

                                        (p) -> {viteza_maxima = 0.8; return false;},
                                        (p) -> {target = valori.LIFT_BASKET2; return false;},

                                //),

                                drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))

                                        .waitSeconds(1.5)
                                        .build(),

                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},

                                drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))
                                        .waitSeconds(1.5)
                                        .build(),

                                new ParallelAction(
                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 0.5; return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},
                                        drive.actionBuilder(new Pose2d(-21,3,Math.toRadians(225)))
                                                .splineToLinearHeading(new Pose2d(-17,20,Math.toRadians(330)),Math.toRadians(225),new TranslationalVelConstraint(30),new ProfileAccelConstraint(-10,25))
                                                .waitSeconds(1.5)
                                                .build()
                                ),

                                new ParallelAction(

                                        (p) -> {bendOverServo.setPosition(valori.DMP_INTAKE_SIDE); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},

                                        drive.actionBuilder(new Pose2d(-17,20,Math.toRadians(330)))
                                                .waitSeconds(0.2)
                                                .splineToLinearHeading(new Pose2d(-20,4,Math.toRadians(225)),Math.toRadians(310),new TranslationalVelConstraint(25),new ProfileAccelConstraint(-10,25))
                                                .build()


                                ),

                                (p) -> {activeIntakeServo.setPower(-1); return false;},
                                drive.actionBuilder(new Pose2d(-20,4,Math.toRadians(225)))
                                        .waitSeconds(1.5)
                                        .build(),
                                new ParallelAction(

                                        drive.actionBuilder(new Pose2d(-20,4,Math.toRadians(225)))
                                                .waitSeconds(1)
                                                .build(),

                                        (p) -> {activeIntakeServo.setPower(0); return false;},
                                        (p) -> {bendOverServo.setPosition(0.35); return false;},
                                        (p) -> {viteza_maxima = 0.8; return false;},
                                        (p) -> {target = valori.LIFT_BASKET2; return false;}

                                ),

                                drive.actionBuilder(new Pose2d(-20,4,Math.toRadians(225)))
                                        .waitSeconds(2)
                                        .build(),

                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},

                                drive.actionBuilder(new Pose2d(-20,4,Math.toRadians(225)))
                                        .waitSeconds(1)
                                        .build(),

                                new ParallelAction(
                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                        (p) -> {viteza_maxima = 0.6; return false;},
                                        (p) -> {target = 0.5; return false;},

                                        drive.actionBuilder(new Pose2d(-20,4,Math.toRadians(225)))
                                                .splineToLinearHeading(new Pose2d(5,25,Math.toRadians(90)),Math.toRadians(225))
                                                .build()
                                )


                        )

                )
        );
    }
}