package org.firstinspires.ftc.teamcode.COD.Autonomii.AutoComplex;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
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

@Config
@Autonomous(name = "DREAPTA V6", group = "AutoSimplu")
public class MasterClass extends LinearOpMode {

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
            extensionServo.setPosition(valori.EXT_HOME);
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

        extensionServo.setPosition(valori.EXT_HOME);
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
                                                .splineToConstantHeading(new Vector2d(-5,34),Math.toRadians(90))
                                                .build()

                                ),


                                (p) -> {viteza_maxima = 0.8; return false;},
                                (p) -> {target = 33; return false;},

                                drive.actionBuilder(new Pose2d(-5,34,Math.toRadians(90)))
                                        .waitSeconds(0.1)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {viteza_maxima = 0.5; return false;},
                                        (p) -> {target = 1; return false;},
                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},

                                        drive.actionBuilder(new Pose2d(-5,34,Math.toRadians(90)))
//
                                                .lineToY(33)
                                                .splineToConstantHeading(new Vector2d(27,31),Math.toRadians(90))
                                                .splineToSplineHeading(new Pose2d(33,50,Math.toRadians(270)),Math.toRadians(90), new TranslationalVelConstraint(20))
                                                .splineToSplineHeading(new Pose2d(33,15,Math.toRadians(270)),Math.toRadians(90), new TranslationalVelConstraint(20))
                                                .splineToSplineHeading(new Pose2d(40,50,Math.toRadians(270)),Math.toRadians(90), new TranslationalVelConstraint(20))
                                                .splineToSplineHeading(new Pose2d(40,15,Math.toRadians(270)),Math.toRadians(90), new TranslationalVelConstraint(20))
                                                .splineToSplineHeading(new Pose2d(48,50,Math.toRadians(270)),Math.toRadians(90), new TranslationalVelConstraint(20))
                                                .splineToSplineHeading(new Pose2d(48,15,Math.toRadians(270)),Math.toRadians(90), new TranslationalVelConstraint(20))
                                                .strafeTo(new Vector2d(29,1))
                                                .waitSeconds(0.1)
                                                .build()

                                ),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},

                                new ParallelAction(

                                        new SequentialAction(

                                                (p) -> {viteza_maxima = 0.5; return false;},
                                                (p) -> {target = 45; return false;},

                                                drive.actionBuilder(new Pose2d(29,1,Math.toRadians(273)))
                                                        .strafeToSplineHeading(new Vector2d(-3,34),Math.toRadians(90))
                                                        .build(),

                                                (p) -> {viteza_maxima = 0.7; return false;},
                                                (p) -> {target = 33; return false;}
                                        )
                                ),

                                drive.actionBuilder(new Pose2d(-3,34,Math.toRadians(90)))
                                        .waitSeconds(0.2)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 1; return false;},

                                        drive.actionBuilder(new Pose2d(-3,34,Math.toRadians(90)))
                                                .strafeToSplineHeading(new Vector2d(28,1),Math.toRadians(270))
                                                .build()
                                ),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},
                                (p) -> {viteza_maxima = 0.4; return false;},
                                (p) -> {target = 45; return false;},

                                drive.actionBuilder(new Pose2d(28,1,Math.toRadians(270)))
                                        .strafeToSplineHeading(new Vector2d(-7,34),Math.toRadians(90))
                                        .build(),

                                (p) -> {viteza_maxima = 0.7; return false;},
                                (p) -> {target = 33; return false;},

                                drive.actionBuilder(new Pose2d(-7,34,Math.toRadians(90)))
                                        .waitSeconds(0.15)
                                        .build(),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                (p) -> {viteza_maxima = 0.7; return false;},
                                (p) -> {target = 3; return false;},

                                drive.actionBuilder(new Pose2d(-7,34,Math.toRadians(85)))
                                        .strafeToSplineHeading(new Vector2d(29,1),Math.toRadians(90))
                                        .build(),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;}

                        )
                ));
    }
}