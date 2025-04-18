package org.firstinspires.ftc.teamcode.COD.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePieseAutonomie;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

// PUNE IN BASKET 3
@Config
@Autonomous(name = "STANGA NATIONALA", group = "AutoSimplu")
public class StangaNationala extends LinearOpMode {

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
        Pose2d initialPose = new Pose2d(-1, 0, Math.toRadians(180)); // 0,0,180
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        waitForStart();

        extensionServo.setPosition(valori.EXT_HOME);
        liftServo.setPosition(valori.DEPOSIT_IDLE);
        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
        bendOverServo.setPosition(0.35);

        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {lift.update(); return true;},
                        new SequentialAction(

                                new ParallelAction(

                                        //VARIANTA DE MAI DIN SPATE
                                        drive.actionBuilder(new Pose2d(-1,0,Math.toRadians(180)))
                                                .strafeToSplineHeading(new Vector2d(-23,5.2),Math.toRadians(213)) // -23,.25,208
                                                .build(),

                                        //VARIANTA ORIGINALA
                                        //drive.actionBuilder(new Pose2d(0,0,Math.toRadians(180)))
                                                //.strafeToSplineHeading(new Vector2d(-22,5.8),Math.toRadians(208)) // -22.5,4.5,208
                                                 //.build(),

                                        (p) -> {viteza_maxima = 1; return false;},
                                        (p) -> {target = valori.LIFT_BASKET2; return false;},
                                        (p) -> {bendOverServo.setPosition(0.6); return false;}

                                ),

                                drive.actionBuilder(new Pose2d(-23,5.2,Math.toRadians(213)))
                                        .waitSeconds(0.1)
                                        .build(),

                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},


                                drive.actionBuilder(new Pose2d(-23,5.2,Math.toRadians(213)))
                                        .waitSeconds(1.1)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},
                                        (p) -> {viteza_maxima = 0.2; return false;},
                                        (p) -> {target = 0.5; return false;},

                                        new SequentialAction(

                                                drive.actionBuilder(new Pose2d(-23,5.2,Math.toRadians(213)))

                                                        //VARIANTA NOUA
                                                        .strafeToSplineHeading(new Vector2d(-12.5,24),Math.toRadians(275))
                                                        .splineToConstantHeading(new Vector2d(-12.5,33),Math.toRadians(90))



                                                        //VARIANTA ORIGINALA
                                                        //.strafeToSplineHeading(new Vector2d(-9.5,24),Math.toRadians(275))
                                                        //.splineToConstantHeading(new Vector2d(-9.5,33),Math.toRadians(90))
                                                        //.waitSeconds(0.45)
                                                        .build(),

                                                (p) -> {bendOverServo.setPosition(valori.DMP_INTAKE_SIDE); return false;},
                                                (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;}

                                        )

                                ),

                                new SequentialAction(

                                        drive.actionBuilder(new Pose2d(-12.5,33,Math.toRadians(275)))
                                                .waitSeconds(1.1)
                                                .build(),

                                        new ParallelAction(

                                                (p) -> {activeIntakeServo.setPower(-0.4); return false;},
                                                (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},

                                                drive.actionBuilder(new Pose2d(-12.5,33,Math.toRadians(275)))
                                                        .strafeToSplineHeading(new Vector2d(-20,6),Math.toRadians(225))
                                                        .build()

                                        )

                                ),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},
                                (p) -> {bendOverServo.setPosition(0.6); return false;},
                                (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                (p) -> {activeIntakeServo.setPower(0); return false;},
                                (p) -> {liftServo.setPosition(valori.DEPOZIT_HORIZONTAL); return false;},
                                (p) -> {viteza_maxima = 0.99; return false;},
                                (p) -> {target = valori.LIFT_BASKET2; return false;},

                                drive.actionBuilder(new Pose2d(-20,6,Math.toRadians(225)))
                                        .waitSeconds(1.3)
                                        .strafeToSplineHeading(new Vector2d(-26,3.5),Math.toRadians(200))
                                        .build(),

                                (p) -> {activeIntakeServo.setPower(0); return false;},
                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},

                                drive.actionBuilder(new Pose2d(-26,3.5,Math.toRadians(200)))
                                        .waitSeconds(1.2)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                        (p) -> {viteza_maxima = 0.2; return false;},
                                        (p) -> {target = 0.5; return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},

                                        drive.actionBuilder(new Pose2d(-26,3.5,Math.toRadians(200)))
                                                .strafeToSplineHeading(new Vector2d(-24.5,15),Math.toRadians(272))
                                                .splineToConstantHeading(new Vector2d(-24.5,25),Math.toRadians(90))

                                                .waitSeconds(0.4)
                                                .build()

                                ),

                                new SequentialAction(

                                        (p) -> {bendOverServo.setPosition(valori.DMP_INTAKE_SIDE); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},
                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},

                                        drive.actionBuilder(new Pose2d(-24.5,25,Math.toRadians(272)))
                                                .waitSeconds(0.9)
                                                .build(),

                                        new ParallelAction(

                                                (p) -> {activeIntakeServo.setPower(-0.4); return false;},

                                                drive.actionBuilder(new Pose2d(-24.5,25,Math.toRadians(272)))
                                                        .waitSeconds(0.45)
                                                        .strafeToSplineHeading(new Vector2d(-18,6),Math.toRadians(225))
                                                        .build()

                                        ),

                                        (p) -> {bendOverServo.setPosition(0.6);return false;},
                                        (p) -> {activeIntakeServo.setPower(0); return false;}

                                ),

                                (p) -> {liftServo.setPosition(valori.DEPOZIT_HORIZONTAL); return false;},
                                (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                (p) -> {viteza_maxima = 0.99; return false;},
                                (p) -> {target = valori.LIFT_BASKET2; return false;},

                                drive.actionBuilder(new Pose2d(-18,6,Math.toRadians(225)))
                                        .waitSeconds(1.2)
                                        .strafeToSplineHeading(new Vector2d(-26,3.5),Math.toRadians(200), new TranslationalVelConstraint(15),new ProfileAccelConstraint(-30,30))
                                        .build(),

                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},

                                drive.actionBuilder(new Pose2d(-26,3.5,Math.toRadians(200)))
                                        .waitSeconds(1)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 0.5; return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},

                                        drive.actionBuilder(new Pose2d(-26,3.5,Math.toRadians(200)))
                                                .strafeToSplineHeading(new Vector2d(-17,23.5),Math.toRadians(320))
                                                //.strafeToSplineHeading(new Vector2d(-7,36),Math.toRadians(0))
                                                //.strafeTo(new Vector2d(-12,36),new TranslationalVelConstraint(20))
                                                .waitSeconds(0.4)
                                                .build()

                                ),

                                new ParallelAction(

                                        (p) -> {bendOverServo.setPosition(valori.DMP_INTAKE_SIDE); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},
                                        (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},

                                        new SequentialAction(

                                                drive.actionBuilder(new Pose2d(-16.5,22.5,Math.toRadians(320)))
                                                        .waitSeconds(0.8)
                                                        .build(),

                                                (p) -> {activeIntakeServo.setPower(-0.4); return false;},

                                                drive.actionBuilder(new Pose2d(-16.5,22.5,Math.toRadians(320)))
                                                        .waitSeconds(0.3)
                                                        //.strafeTo(new Vector2d(-20,15))
                                                        .strafeToSplineHeading(new Vector2d(-19,4),Math.toRadians(216))
                                                        //.waitSeconds(3)
                                                        .build()

                                        )


//
                                ),

                                new ParallelAction(

                                        (p) -> {activeIntakeServo.setPower(0); return false;},
                                        (p) -> {bendOverServo.setPosition(0.6); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED-0.05); return false;},
                                        (p) -> {activeIntakeServo.setPower(0); return false;},
                                        (p) -> {liftServo.setPosition(valori.DEPOZIT_HORIZONTAL); return false;},
                                        (p) -> {viteza_maxima = 0.99; return false;},
                                        (p) -> {target = valori.LIFT_BASKET2; return false;},

                                        new SequentialAction(

                                                drive.actionBuilder(new Pose2d(-19,4,Math.toRadians(216)))
                                                        .waitSeconds(1.3)
                                                        .strafeToSplineHeading(new Vector2d(-25,4),Math.toRadians(195))
                                                        .build()

                                        )

                                ),

                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},

                                drive.actionBuilder(new Pose2d(-25,4,Math.toRadians(195)))
                                        .waitSeconds(0.55)
                                        .strafeTo(new Vector2d(-22,10),new TranslationalVelConstraint(30),new ProfileAccelConstraint(-10,30))
                                        .build(),

                                (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                (p) -> {viteza_maxima = 0.5; return false;},
                                (p) -> {target = 0.5; return false;}


                        )

                )

        );

    }

}