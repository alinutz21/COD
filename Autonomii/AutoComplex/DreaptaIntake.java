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
@Disabled
@Config
@Autonomous(name = "DREAPTA V7", group = "AutoSimplu")
public class DreaptaIntake extends LinearOpMode {

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
//            extensionServo.setPosition(valori.EXT_HOME);
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

        extensionServo.setPosition(0.65);
        specimenServo.setPosition(valori.SPECIMEN_CLOSED);
        bendOverServo.setPosition(0.2);
        //liftServo.setPosition(valori.DEPOSIT_IDLE);

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
                                                .strafeToSplineHeading(new Vector2d(0,34),Math.toRadians(90))
                                                .build()

                                ),


                                (p) -> {viteza_maxima = 0.8; return false;},
                                (p) -> {target = 33; return false;},

                                drive.actionBuilder(new Pose2d(0,33,Math.toRadians(90)))
                                        .waitSeconds(0.1)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {viteza_maxima = 0.5; return false;},
                                        (p) -> {target = 1; return false;},
                                        (p) -> {specimenServo.setPosition(valori.SPECIMEN_OPEN); return false;},
                                        (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},
                                        (p) -> {activeIntakeServo.setPower(1); return false;},

                                        drive.actionBuilder(new Pose2d(0,33,Math.toRadians(90)))

                                                .strafeToSplineHeading(new Vector2d(30,28),Math.toRadians(237))
//                                                .strafeTo(new Vector2d(32,28))
                                                .waitSeconds(0.8)

                                                .build()

                                ),

                                new ParallelAction(

                                        (p) -> {bendOverServo.setPosition(0.9); return false;},

                                        drive.actionBuilder(new Pose2d(29,26,Math.toRadians(237)))

                                                .strafeToSplineHeading(new Vector2d(29,20),Math.toRadians(140))
                                                .build()
                                ),

                                (p) -> {activeIntakeServo.setPower(-1); return false;},

                                drive.actionBuilder(new Pose2d(29,20,Math.toRadians(140)))
                                        .waitSeconds(0.5)
                                        .build(),

                                new ParallelAction(

                                        (p) -> {activeIntakeServo.setPower(1); return false;},
                                        (p) -> {bendOverServo.setPosition(valori.DMP_SCORING_SIDE); return false;},

                                        new SequentialAction(

                                            drive.actionBuilder(new Pose2d(29,20,Math.toRadians(140)))
                                                    .waitSeconds(0.5)
                                                    .strafeToSplineHeading(new Vector2d(41,26),Math.toRadians(240))

                                                    .waitSeconds(0.8)
                                                    .build(),

                                            (p) -> {extensionServo.setPosition(valori.EXT_HOME); return false;},
                                            (p) -> {bendOverServo.setPosition(0.9); return false;}

                                        )
                                ),



                                new ParallelAction(



                                        drive.actionBuilder(new Pose2d(39,28,Math.toRadians(242)))
                                                .strafeToSplineHeading(new Vector2d(39,22),Math.toRadians(140))
                                                .build()
                                ),

                                (p) -> {extensionServo.setPosition(valori.EXT_EXTENDED);return false;},
                                (p) -> {activeIntakeServo.setPower(-1); return false;},

                                drive.actionBuilder(new Pose2d(39,22,Math.toRadians(140)))
                                        .waitSeconds(1)
                                        .build()




                        )
                ));
    }
}