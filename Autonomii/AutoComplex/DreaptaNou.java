package org.firstinspires.ftc.teamcode.COD.Autonomii.AutoComplex;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
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

// pune pe high chamber 3
@Disabled
@Config
@Autonomous(name = "DREAPTA Nou", group = "AutoSimplu")
public class DreaptaNou extends LinearOpMode {

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

//        public void init(){
//
//            specimenServo.setPosition(valori.SPECIMEN_CLOSED);
//            extensionServo.setPosition(valori.EXT_HOME);
//            liftServo.setPosition(valori.DEPOSIT_IDLE);
//            bendOverServo.setPosition(0.2);
//
//
//        }
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

        // Actiuni care au loc in init
        // preluare.init();

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();
        waitForStart();

        extensionServo.setPosition(valori.EXT_HOME);
        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
        bendOverServo.setPosition(0.2);

        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {lift.update(); return true;},
                        new SequentialAction(
                                drive.actionBuilder(initialPose)
                                        .waitSeconds(0.01)
                                        .build(),
                                new ParallelAction(
                                        //(p) -> {bendOverServo.setPosition(0.2); return false;},
                                        (p) -> {viteza_maxima = 0.65; return false;},
                                        (p) -> {target = 45; return false;},
                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},
                                        drive.actionBuilder(new Pose2d(0,0,Math.toRadians(90)))
                                                .splineToConstantHeading(new Vector2d(-5,34),Math.toRadians(90))
                                                .build()
                                ),
                                (p) -> {viteza_maxima = 0.75; return false;},
                                (p) -> {target = 33; return false;},
                                drive.actionBuilder(new Pose2d(-5,34,Math.toRadians(90)))
                                        .waitSeconds(0.15)
                                        .build(),
                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN);return false;},
                                new ParallelAction(
                                        (p) -> {viteza_maxima = 0.5; return false;},
                                        (p) -> {target = 4; return false;},
                                        drive.actionBuilder(new Pose2d(-5,34,Math.toRadians(90)))
                                                .lineToY(24)
                                                .waitSeconds(0.001)
                                                .splineToLinearHeading(new Pose2d(31,10,Math.toRadians(270)),Math.toRadians(90))
                                                .waitSeconds(0.001)
                                                .lineToY(-1)
                                                .waitSeconds(0.001)
                                                .build()
                                ),
                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},
                                drive.actionBuilder(new Pose2d(31,-1,Math.toRadians(270)))
                                        .waitSeconds(0.2)
                                        .build(),
                                (p) -> {viteza_maxima = 0.5; return false;},
                                (p) -> {target = 45; return false;},
                                drive.actionBuilder(new Pose2d(31,-1,Math.toRadians(270)))
                                        .lineToY(15)
                                        .waitSeconds(0.001)
                                        .setReversed(true)
                                        .setTangent(Math.toRadians(-90))
                                        .splineToSplineHeading(new Pose2d(-13,28,Math.toRadians(90)),Math.toRadians(270))
                                        .waitSeconds(0.001)
                                        .lineToY(35)
                                        .build(),
                                (p) -> {viteza_maxima = 0.75; return false;},
                                (p) -> {target = 33; return false;},
                                drive.actionBuilder(new Pose2d(-13,34,Math.toRadians(90)))
                                        .waitSeconds(0.15)
                                        .build(),
                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                drive.actionBuilder(new Pose2d(-13,34,Math.toRadians(90)))
                                        //.waitSeconds(0.8)
                                        .lineToY(15)
                                        .build(),
                                (p) -> {viteza_maxima = 0.3; return false;},
                                (p) -> {target = 4; return false;},

                                new ParallelAction(
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},
                                        drive.actionBuilder(new Pose2d(-13,15,Math.toRadians(90)))
                                        .splineToLinearHeading(new Pose2d(29,26,Math.toRadians(242)),Math.toRadians(90))
                                                .waitSeconds(0.1)

                                        .build()
                                ),

                                drive.actionBuilder(new Pose2d(29,26,Math.toRadians(242)))
                                        .lineToY(31)
                                        .waitSeconds(0.15)
                                        .build(),
                                (p) -> {bendOverServo.setPosition(0.75); return false;},
                                drive.actionBuilder(new Pose2d(29,31,Math.toRadians(242)))
                                        .waitSeconds(0.2)
                                        .splineToLinearHeading(new Pose2d(29,20,Math.toRadians(125)),Math.toRadians(242))
                                        .build(),
                                (p) -> {bendOverServo.setPosition(0.9); return false;},
                                (p) -> {activeIntakeServo.setPower(-1); return false;},
                                drive.actionBuilder(new Pose2d(29,20,Math.toRadians(125)))
                                        .waitSeconds(0.6)
                                        .build(),
                                (p) -> {activeIntakeServo.setPower(0); return false;},
                                (p) -> {bendOverServo.setPosition(0.35); return false;},
                                (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},
                                drive.actionBuilder(new Pose2d(29,20,Math.toRadians(125)))
                                        .waitSeconds(0.5)
                                        .splineToLinearHeading(new Pose2d(37,15,Math.toRadians(270)),Math.toRadians(125))
                                        .waitSeconds(0.1)
                                        .lineToY(3)
                                        .build(),
                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_CLOSED); return false;},
                                drive.actionBuilder(new Pose2d(37,3,Math.toRadians(270)))
                                        .waitSeconds(0.2)
                                        .build(),
                                (p) -> {viteza_maxima = 0.5; return false;},
                                (p) -> {target = 45; return false;},
                                drive.actionBuilder(new Pose2d(37,3,Math.toRadians(270)))
                                        .lineToY(20)
                                        //.waitSeconds(0.2)
                                        .setReversed(true)
                                        .setTangent(Math.toRadians(-90))
                                        .splineToSplineHeading(new Pose2d(-9,25,Math.toRadians(90)),Math.toRadians(270))
                                        .lineToY(34.5)
                                        .build(),
                                (p) -> {viteza_maxima = 0.75; return false;},
                                (p) -> {target = 33; return false;},
                                drive.actionBuilder(new Pose2d(-9,34.5,Math.toRadians(95)))
                                        .waitSeconds(0.2)
                                        .build(),
                                (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                (p) -> {bendOverServo.setPosition(0.35); return false;},
                                (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
                                new ParallelAction(
                                        drive.actionBuilder(new Pose2d(-11,34.5,Math.toRadians(95)))
                                                .waitSeconds(0.1)
                                                .lineToY(28)
                                                .build(),
                                        (p) -> {viteza_maxima = 0.9; return false;},
                                        (p) -> {target = 0.5; return false;}

                                )
//                                drive.actionBuilder(new Pose2d(-11,33,Math.toRadians(95)))
//                                        .waitSeconds(0.1)
//                                        .lineToY(28)
//                                        .build(),
//                                (p) -> {viteza_maxima = 0.8; return false;},
//                                (p) -> {target = 1; return false;},
//                                drive.actionBuilder(new Pose2d(-11,28,Math.toRadians(95)))
//                                        .waitSeconds(0.1)
//                                        .build()

                        )
                ));
    }
}