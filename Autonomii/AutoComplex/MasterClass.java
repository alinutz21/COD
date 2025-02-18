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


@Config
@Autonomous(name = "DREAPTA V11", group = "AutoSimplu")
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

        extensionServo.setPosition(0.58);
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
                                                .strafeToSplineHeading(new Vector2d(-15,34),Math.toRadians(90))
                                                .build()

                                ),


                                (p) -> {viteza_maxima = 0.8; return false;},
                                (p) -> {target = 33; return false;},

                                drive.actionBuilder(new Pose2d(-15,34,Math.toRadians(90)))
                                        .waitSeconds(0.1)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 5; return false;},
                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},

                                        drive.actionBuilder(new Pose2d(-15,34,Math.toRadians(90)))
                                                .setReversed(true)
                                                .splineToConstantHeading(new Vector2d(26,30),Math.toRadians(90))
                                                .splineToConstantHeading(new Vector2d(26,50),Math.toRadians(90), null,new ProfileAccelConstraint(-10,10))
                                                .splineToConstantHeading(new Vector2d(40,50),Math.toRadians(-90),null,new ProfileAccelConstraint(-10,10))

                                                .splineToConstantHeading(new Vector2d(40,15),Math.toRadians(-90),new TranslationalVelConstraint(30),new ProfileAccelConstraint(-15,30))
                                                .splineToConstantHeading(new Vector2d(40,50),Math.toRadians(-40),new TranslationalVelConstraint(30),new ProfileAccelConstraint(-15,30))
                                                .splineToConstantHeading(new Vector2d(48,50),Math.toRadians(90),new TranslationalVelConstraint(30), new ProfileAccelConstraint(-15,30))
                                                //.splineToConstantHeading(new Vector2d(55,50),Math.toRadians(90),new TranslationalVelConstraint(25), new ProfileAccelConstraint(-15,30))
                                                .splineToConstantHeading(new Vector2d(48,15),Math.toRadians(90),new TranslationalVelConstraint(30), new ProfileAccelConstraint(-15,30))

                                                //.splineToSplineHeading(new Pose2d(59,50,Math.toRadians(85)),Math.toRadians(60),new TranslationalVelConstraint(25),new ProfileAccelConstraint(-15,30))
                                                //.splineToConstantHeading(new Vector2d(63,50),Math.toRadians(-90),new TranslationalVelConstraint(25),new ProfileAccelConstraint(-15,30))
                                                //.splineToConstantHeading(new Vector2d(63,5),Math.toRadians(-90),new TranslationalVelConstraint(25),new ProfileAccelConstraint(-15,30))
                                                //.splineToSplineHeading(new Pose2d(30,0,Math.toRadians(270)), Math.toRadians(200))
                                                .strafeToSplineHeading(new Vector2d(35,0),Math.toRadians(280))
                                                .waitSeconds(0.1)
                                                .build()

                                ),
                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},

                                drive.actionBuilder(new Pose2d(30,0,Math.toRadians(270)))
                                        .waitSeconds(0.2)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {viteza_maxima = 0.5; return false;},
                                        (p) -> {target = 45; return false;},

                                        drive.actionBuilder(new Pose2d(30,0,Math.toRadians(270)))
                                                .setReversed(true)
                                                .splineToSplineHeading(new Pose2d(30,10,Math.toRadians(90)),Math.toRadians(90),new TranslationalVelConstraint(20))
                                                .splineToConstantHeading(new Vector2d(-10,34),Math.toRadians(120),new TranslationalVelConstraint(20))


                                                //.strafeToSplineHeading(new Vector2d(-2,15),Math.toRadians(90))
                                                //.strafeToSplineHeading(new Vector2d(-3,32),Math.toRadians(90))
                                                .build()

                                ),

                                (p) -> {viteza_maxima = 0.7; return false;},
                                (p) -> {target = 33; return false;},

                                drive.actionBuilder(new Pose2d(-12,34,Math.toRadians(100)))
                                        .waitSeconds(0.2)
                                        .build(),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},

                                drive.actionBuilder(new Pose2d(-12,34,Math.toRadians(100)))
                                        .waitSeconds(0.2)
                                        .build(),

                                new ParallelAction(


                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 5; return false;},

                                        drive.actionBuilder(new Pose2d(-12,34,Math.toRadians(100)))
                                                //.waitSeconds(0.2)
                                                .setReversed(true)
                                                .splineToSplineHeading(new Pose2d(-12,20,Math.toRadians(270)),Math.toRadians(-90),new TranslationalVelConstraint(20),new ProfileAccelConstraint(-40,40))
                                                .splineToConstantHeading(new Vector2d(30,0),Math.toRadians(-90),new TranslationalVelConstraint(20),new ProfileAccelConstraint(-40,40))
                                                //.strafeToSplineHeading(new Vector2d(36,-2),Math.toRadians(270))

                                                .build()
                                ),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},

                                drive.actionBuilder(new Pose2d(36,-6,Math.toRadians(255)))
                                        .waitSeconds(0.2)
                                        .build(),

                                (p) -> {viteza_maxima = 0.4; return false;},
                                (p) -> {target = 45; return false;},

                                drive.actionBuilder(new Pose2d(36,-6,Math.toRadians(270)))
                                        .setReversed(true)
                                        .splineToConstantHeading(new Vector2d(-6,10),Math.toRadians(120),new TranslationalVelConstraint(20))
                                        .strafeToSplineHeading(new Vector2d(-6,30),Math.toRadians(90))
                                        //.splineToSplineHeading(new Pose2d(-4,32,Math.toRadians(90)),Math.toRadians(90),new TranslationalVelConstraint(20))

                                        .build(),

                                (p) -> {viteza_maxima = 0.7; return false;},
                                (p) -> {target = 33; return false;},

                                drive.actionBuilder(new Pose2d(-6,30,Math.toRadians(90)))
                                        .waitSeconds(0.2)
                                        .build(),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},

                                drive.actionBuilder(new Pose2d(-6,30,Math.toRadians(90)))
                                        .waitSeconds(0.1)
                                        .build(),

                                new ParallelAction(
                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 5; return false;},

                                        drive.actionBuilder(new Pose2d(-6,30,Math.toRadians(90)))
                                                .setReversed(true)
                                                .splineToConstantHeading(new Vector2d(10,20),Math.toRadians(-90),new TranslationalVelConstraint(20))
                                                .strafeToSplineHeading(new Vector2d(36,-2),Math.toRadians(270))
                                                //.splineToSplineHeading(new Pose2d(36,-2,Math.toRadians(270)),Math.toRadians(-90),new TranslationalVelConstraint(20))

                                                .waitSeconds(0.2)
                                                .build()
                                ),


                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},

                                drive.actionBuilder(new Pose2d(36,-2,Math.toRadians(270)))
                                        .waitSeconds(0.15)
                                        .build(),

                                (p) -> {viteza_maxima = 0.7; return false;},
                                (p) -> {target = 45; return false;},

                                drive.actionBuilder(new Pose2d(36,-2,Math.toRadians(270)))
                                        .waitSeconds(0.1)
                                        .setReversed(true)
                                        .splineToConstantHeading(new Vector2d(-6,10),Math.toRadians(120),new TranslationalVelConstraint(20))
                                        .strafeToSplineHeading(new Vector2d(-6,30),Math.toRadians(90))
                                        //.splineToSplineHeading(new Pose2d(-5,32,Math.toRadians(90)),Math.toRadians(90),new TranslationalVelConstraint(20))
                                        .build(),

                                (p) -> {viteza_maxima = 0.7; return false;},
                                (p) -> {target = 33; return false;},

                                drive.actionBuilder(new Pose2d(-6,30,Math.toRadians(90)))
                                        .waitSeconds(0.2)
                                        .build(),

                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},

                                new ParallelAction(

                                        (p) -> {viteza_maxima = 0.4; return false;},
                                        (p) -> {target = 1; return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(0.85); return false;},

                                        drive.actionBuilder(new Pose2d(-6,30,Math.toRadians(90)))
                                                .strafeToSplineHeading(new Vector2d(25,20),Math.toRadians(140))
                                                .build()
                                )



                        )
                ));
    }
}