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
import org.firstinspires.ftc.teamcode.COD.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePiese;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePiese2;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
import org.opencv.core.Mat;

@Config
@Autonomous(name = "DREAPTA Complex", group = "AutoSimplu")
public class DreaptaComplexAuto extends LinearOpMode {

    SlidePiese2 slide;
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
            slide = new SlidePiese2(hardwareMap,"LIFTMOTOR",true,false);
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
            //      bendOverServo.setPosition(DMP_INTAKE_SIDE);
            liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        }

        public void init(){
            specimenServo.setPosition(valori.SPECIMEN_CLOSED);
            extensionServo.setPosition(valori.EXT_HOME);
            liftServo.setPosition(valori.DEPOSIT_IDLE);
            bendOverServo.setPosition(valori.DMP_SCORING_SIDE);
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
        Pose2d initialPose = new Pose2d(-32, -61, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // Actiuni care au loc in init
        preluare.init();

        waitForStart();
        telemetry.addData("Mode", "running");
        telemetry.update();

        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {lift.update(); return true;},
                        new SequentialAction(
                                drive.actionBuilder(initialPose)
                                        .splineToSplineHeading(new Pose2d(-40,-40,Math.toRadians(225)),Math.toRadians(225))
                                        .splineToConstantHeading(new Vector2d(-54,-54),Math.toRadians(225)) // <-- aici ajunge in fata la cos
                                        .waitSeconds(2)
                                        .build()
                        )



                                /*
                                new ParallelAction(
                                        drive.actionBuilder(drive.pose)
                                                .strafeToLinearHeading(new Vector2d(57,0),Math.toRadians(87))
                                                .strafeToLinearHeading(new Vector2d(57,-23.1),Math.toRadians(87))
                                                .build(),
                                        (p) -> {target=0; return false;}
                                ))

                                 */
                ));
    }
}