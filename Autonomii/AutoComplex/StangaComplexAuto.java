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
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePieseAutonomie;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

// pune imn bascket impinge douas i incearca sa o ieie pe cealalta
@Disabled
@Config
@Autonomous(name = "TestStrafe", group = "AutoSimplu")
public class StangaComplexAuto extends LinearOpMode {

    SlidePieseAutonomie slide;
    public Servo extensionServo;
   // public CRServo activeIntakeServo;
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

      //  activeIntakeServo = hardwareMap.get(CRServo.class,"WHEELSERVO");
        bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

//        // Actiuni care au loc in init
//        extensionServo.setPosition(valori.EXT_HOME);
//        liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
//        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
//        bendOverServo.setPosition(0.2);

        waitForStart();
        extensionServo.setPosition(valori.EXT_HOME);
        bendOverServo.setPosition(0.3);
        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
        liftServo.setPosition(1);



        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {lift.update(); return true;},
                        new SequentialAction(
                                drive.actionBuilder(initialPose)
                                        .waitSeconds(0.01)
                                        .build(),
                                drive.actionBuilder(new Pose2d(0,0,Math.toRadians(90)))
                                        .strafeToSplineHeading(new Vector2d(10,35),Math.toRadians(180))
                                        .build()




//                                (p) -> {bendOverServo.setPosition(0.35); return false;},
//                                (p) -> {liftServo.setPosition(valori.DEPOSIT_IDLE); return false;},
//                                drive.actionBuilder(new Pose2d(0,0,Math.toRadians(90)))
//                                        .splineToLinearHeading(new Pose2d(-25,21,Math.toRadians(225)),Math.toRadians(90))
//                                        .build(),
//                                (p) -> {viteza_maxima = 0.5; return false;},
//                                (p) -> {target = valori.LIFT_BASKET2; return false;},
//                                (p) -> {liftServo.setPosition(valori.DEPOZIT_HORIZONTAL); return false;},
//                                drive.actionBuilder(new Pose2d(-25,21,225))
//                                        .lineToY(15)
//                                        .build(),
//                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING); return false;},
//                                drive.actionBuilder(new Pose2d(-25,15,Math.toRadians(225)))
//                                        .waitSeconds(1)
//                                        .lineToY(21)
//                                        .build(),
//                                (p) -> {viteza_maxima = 0.3; return false;},
//                                (p) -> {target = 1; return false;},
//                                drive.actionBuilder(new Pose2d(-25,21,Math.toRadians(225)))
//                                        .waitSeconds(1)
//                                        .build()


                        )


                ));

    }
}