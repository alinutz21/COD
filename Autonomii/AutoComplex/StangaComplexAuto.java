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
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePiese2;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
import org.opencv.core.Mat;

@Config
@Autonomous(name = "STANGA Complex", group = "AutoSimplu")
public class StangaComplexAuto extends LinearOpMode {

    SlidePiese2 slide;
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
            slide = new SlidePiese2(hardwareMap,"LIFTMOTOR",true,false);
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
        Pose2d initialPose = new Pose2d(-32, -61, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

//        // Actiuni care au loc in init
        extensionServo.setPosition(valori.EXT_HOME);
        liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
        bendOverServo.setPosition(0.2);

        waitForStart();



        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {lift.update(); return true;},
                        new SequentialAction(
                                drive.actionBuilder(initialPose)
                                        .splineToSplineHeading(new Pose2d(-40,-40,Math.toRadians(225)),Math.toRadians(225))
                                        .splineToConstantHeading(new Vector2d(-54,-54),Math.toRadians(225)) // <-- aici ajunge in fata la cos
                                        .build(),
                                (p) -> {viteza_maxima = 0.5;return false;},
                                (p) -> {target = valori.LIFT_BASKET2; return false;},
                                drive.actionBuilder(new Pose2d(-54,-54,Math.toRadians(225)))
                                        .waitSeconds(2)
                                        .lineToY(-59).waitSeconds(1)
                                        .build(),
                                (p) -> {liftServo.setPosition(valori.DEPOSIT_SCORING);return false;},
                                drive.actionBuilder(new Pose2d(-54,-59,Math.toRadians(225)))
                                       .waitSeconds(1)
                                        .lineToY(-53)
                                        .build(),
                                (p) ->{viteza_maxima = 0.3;return false;},
                                (p) -> {target = -0.1;return false;},
                                (p) -> {liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);return false;},
                                drive.actionBuilder(new Pose2d(-54,-53,Math.toRadians(225)))
                                        .waitSeconds(2)
//                                        .setReversed(true)
                                        .lineToY(-45)
                                        .splineToLinearHeading(new Pose2d(-44,-15,Math.toRadians(270)),Math.toRadians(270))

                                       // .splineToConstantHeading(new Vector2d(-44,-15),Math.toRadians(270))
                                        .turn(Math.toRadians(-10))
                                        .lineToY(-55)
                                        .turn(Math.toRadians(20))
                                        .splineToConstantHeading(new Vector2d(-45,-35), Math.toRadians(270))
                                        .splineToConstantHeading(new Vector2d(-60,-15), Math.toRadians(270))
                                        .turn(Math.toRadians(-5))
                                        .lineToY(-55)
                                        .lineToY(-15)
                                        //.turn(Math.toRadians(5))
                                        .splineToConstantHeading(new Vector2d(-63,-13), Math.toRadians(270))
                                        .turn(Math.toRadians(2))
                                        .waitSeconds(0.5)
                                        .lineToY(-55)


                                        .build()

                        )


                ));

    }
}