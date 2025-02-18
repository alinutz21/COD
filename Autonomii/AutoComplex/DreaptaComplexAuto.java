package org.firstinspires.ftc.teamcode.COD.Autonomii.AutoComplex;

import android.animation.ValueAnimator;
import android.media.MediaFeature;

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

import java.util.Objects;


@Config
@Autonomous(name = "DREAPTA", group = "AutoSimplu")

public class DreaptaComplexAuto extends LinearOpMode {

    SlidePieseAutonomie slide;
    public Servo extensionServo;
    public CRServo activeIntakeServo;
    public Servo bendOverServo;
    public Servo liftServo;
    public Servo specimenServo;

    ValoriFunctii valori = new ValoriFunctii();
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

    public class Preluare {
        public Preluare(HardwareMap hardwareMap){
            extensionServo = hardwareMap.get(Servo.class,"EXTENSIONSERVO");
            activeIntakeServo = hardwareMap.get(CRServo.class,"WHEELSERVO");
            bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
            specimenServo = hardwareMap.get(Servo.class, "SPECIMENSERVO");
            liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        }

        public void init(){

        }
        public void arunca(){
            liftServo.setPosition(valori.DEPOSIT_SCORING);
        }
        public void extindere(){

        }
    }



    @Override
    public void runOpMode() throws InterruptedException {
        Lift lift = new Lift(hardwareMap);
        Preluare preluare = new Preluare(hardwareMap);
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addData("Mode", "waiting");
        telemetry.update();



        waitForStart();

        extensionServo.setPosition(0.6);
        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
        bendOverServo.setPosition(0.2);
        liftServo.setPosition(valori.DEPOSIT_IDLE);

        telemetry.addData("Mode", "running");
        telemetry.update();
        preluare.init();
        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {lift.update(); return true;},
                        new SequentialAction(

                                new ParallelAction(

                                        (p) -> {viteza_maxima = 0.65; return false;},
                                        (p) -> {target = 45; return false;},

                                        drive.actionBuilder(new Pose2d(0,0,Math.toRadians(90)))
                                                .splineToConstantHeading(new Vector2d(-6,34),Math.toRadians(90))
                                                .build()

                                ),


                                (p) -> {viteza_maxima = 0.8; return false;},
                                (p) -> {target = 33; return false;},


                                drive.actionBuilder(new Pose2d(-6,34,Math.toRadians(90)))
                                        .waitSeconds(0.1)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 5; return false;},
                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},

                                        drive.actionBuilder(new Pose2d(-6 ,34,Math.toRadians(90)))
                                                .setReversed(true)
                                                .splineToLinearHeading(new Pose2d(31,15,Math.toRadians(237)),Math.toRadians(90))
                                                .strafeToSplineHeading(new Vector2d(31,26),Math.toRadians(237))
                                                .waitSeconds(1)
                                                .build()

                                ),

                                (p) -> {bendOverServo.setPosition(0.6); return false;},

                                new SequentialAction(

                                        drive.actionBuilder(new Pose2d(31,26,Math.toRadians(237)))
                                                .strafeToSplineHeading(new Vector2d(30,16),Math.toRadians(125))
                                                .build(),

                                        (p) -> {bendOverServo.setPosition(0.9); return false;},
                                        (p) -> {activeIntakeServo.setPower(-1); return false;},

                                        drive.actionBuilder(new Pose2d(31,16,Math.toRadians(125)))
                                                .waitSeconds(0.3)
                                                .build(),

                                        (p) -> {bendOverServo.setPosition(0.6); return false;},


                                        new ParallelAction(

                                                (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                                (p) -> {activeIntakeServo.setPower(1); return false;},

                                                drive.actionBuilder(new Pose2d(31,16,Math.toRadians(125)))
                                                        .strafeToSplineHeading(new Vector2d(44,15),Math.toRadians(247))
                                                        .strafeToSplineHeading(new Vector2d(44,30),Math.toRadians(247))
                                                        .waitSeconds(1)
                                                        .build()
                                        ),

                                        (p) -> {bendOverServo.setPosition(0.6); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},

                                        drive.actionBuilder(new Pose2d(44,30,Math.toRadians(247)))
                                                .strafeToSplineHeading(new Vector2d(30,14),Math.toRadians(125))
                                                .build(),

                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(0.9); return false;},
                                        (p) -> {activeIntakeServo.setPower(-1); return false;},

                                        drive.actionBuilder(new Pose2d(30,14,Math.toRadians(125)))
                                                .waitSeconds(0.6)
                                                .build(),

                                        (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},
                                        (p) -> {bendOverServo.setPosition(0.25); return false;},
                                        (p) -> {activeIntakeServo.setPower(0); return false;},


                                       drive.actionBuilder(new Pose2d(30,16,Math.toRadians(125)))
                                               .strafeToSplineHeading(new Vector2d(30,-0.3),Math.toRadians(265))

                                               .build(),

                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},

                                        drive.actionBuilder(new Pose2d(30,-0.3,Math.toRadians(265)))
                                                .waitSeconds(0.2)
                                                .build(),

                                        new ParallelAction(

                                                (p) -> {viteza_maxima = 0.4; return false;},
                                                (p) -> {target = 45; return false;},

                                                drive.actionBuilder(new Pose2d(30,-0.3,Math.toRadians(265)))
                                                        .strafeToSplineHeading(new Vector2d(-3,35),Math.toRadians(95))
                                                        .waitSeconds(0.15)
                                                        .build()
                                        ),

                                        (p) -> {viteza_maxima = 0.8; return false;},
                                        (p) -> {target = 33; return false;},

                                        drive.actionBuilder(new Pose2d(-3,35,Math.toRadians(95)))
                                                .waitSeconds(0.15)
                                                .build(),

                                        new ParallelAction(
                                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                                (p) -> {viteza_maxima = 0.4; return false;},
                                                (p) -> {target = 4.2; return false;},

                                                drive.actionBuilder(new Pose2d(-3,35,Math.toRadians(95)))
                                                        .strafeToSplineHeading(new Vector2d(30,-0.3),Math.toRadians(270))
                                                        .waitSeconds(0.15)
                                                        .build()
                                        ),

                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},

                                        drive.actionBuilder(new Pose2d(30,-0.3,Math.toRadians(270)))
                                                .waitSeconds(0.15)
                                                .build(),

                                        new ParallelAction(

                                                (p) -> {viteza_maxima = 0.4; return false;},
                                                (p) -> {target = 45; return false;},

                                                drive.actionBuilder(new Pose2d(30,-0.3,Math.toRadians(270)))
                                                        .strafeToSplineHeading(new Vector2d(0,35),Math.toRadians(90))
                                                        .waitSeconds(0.15)
                                                        .build()

                                        ),

                                        (p) -> {viteza_maxima = 0.8; return false;},
                                        (p) -> {target = 33; return false;},

                                        drive.actionBuilder(new Pose2d(0,35,Math.toRadians(90)))
                                                .waitSeconds(0.15)
                                                .build(),

                                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                                (p) -> {viteza_maxima = 0.4; return false;},
                                                (p) -> {target = 4.2; return false;},

                                        new ParallelAction(

                                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                                (p) -> {viteza_maxima = 0.4; return false;},
                                                (p) -> {target = 1; return false;},

                                                drive.actionBuilder(new Pose2d(0,35,Math.toRadians(90)))
                                                        .strafeToSplineHeading(new Vector2d(35,10),Math.toRadians(90))
                                                        .build()
                                        )



                        ))
                ));
    }
}