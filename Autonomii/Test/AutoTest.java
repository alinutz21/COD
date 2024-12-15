package org.firstinspires.ftc.teamcode.COD.Autonomii.Test;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Slide;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

@Autonomous(name="LeftAuto")
public class AutoTest extends LinearOpMode{
    double powerLimit = 0.5;
    double target = 0;
    ValoriFunctii valori = new ValoriFunctii();
    double clawOpen = valori.SPECIMEN_OPEN;
    double clawClose = valori.SPECIMEN_CLOSED;

    public class Lift {
        public Slide slide;
        public Lift(HardwareMap hardwareMap){
            slide = new Slide(hardwareMap,"LIFTMOTOR",true,false);
            slide.setPosition(0,0);
        }
        public void update() {
            slide.setPosition(target,powerLimit);
            slide.slideUpdate();
        }
    }

    public class Claw {
        private Servo servo;
        public Claw(HardwareMap hardwareMap){
            servo = hardwareMap.get(Servo.class,"LIFTSERVO");
            servo.setPosition(clawClose);
        }
        public void setServo(double pos){
            servo.setPosition(pos);
        }
    }


    @Override
    public void runOpMode() throws InterruptedException {
        Lift AutoLift = new Lift(hardwareMap);
        Claw AutoClaw = new Claw(hardwareMap);
        Pose2d initialPose = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        telemetry.addData("Mode", "waiting");
        telemetry.update();
        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();


        Actions.runBlocking(
                new ParallelAction(
                        (p) -> {AutoLift.update(); return true;},
                        new SequentialAction(
                                (p) -> {AutoClaw.setServo(clawOpen); return false;},

                                drive.actionBuilder(initialPose)
                                        .waitSeconds(.3)
                                        .strafeToLinearHeading(new Vector2d(0,17),Math.toRadians(0))
                                        .build(),
                                (p) -> {target = valori.LIFT_BASKET1; return false;},
                                /*
                                drive.actionBuilder(new Pose2d(10,17,Math.toRadians(-34)))
                                        .waitSeconds(1)
                                        .build(),
                                drive.actionBuilder(new Pose2d(10,17,Math.toRadians(-34)))
                                        .waitSeconds(1)
                                        .build(),
                                drive.actionBuilder(new Pose2d(10,17,Math.toRadians(-34)))
                                        .strafeToLinearHeading(new Vector2d(14.4,11),Math.toRadians(-10))
                                        .build(),

                                 */
                                drive.actionBuilder(drive.pose)
                                        .waitSeconds(0.55)
                                        .strafeToConstantHeading(new Vector2d(0,10))
                                        .build(),
                                new ParallelAction(
                                        drive.actionBuilder(drive.pose)
                                                .strafeToLinearHeading(new Vector2d(57,0),Math.toRadians(87))
                                                .strafeToLinearHeading(new Vector2d(57,-23.1),Math.toRadians(87))
                                                .build(),
                                        (p) -> {target=0; return false;}
                                ))
                ));
    }
}