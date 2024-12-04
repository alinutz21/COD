package org.firstinspires.ftc.teamcode.COD.Teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;

import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
@Disabled
@Deprecated
@TeleOp(name="Teleop fortat", group="Linear OpMode")
public class Teleop_Fortat extends LinearOpMode {
    ValoriFunctii valori;
    private ElapsedTime runtime = new ElapsedTime();
    Gamepad gp1,gp2;

///     DRIVETRAIN ____________________________________________

    MecanumDrive drive;
    Pose2d pose;

///     OUTAKE ________________________________________________
    DcMotorEx liftMotor;
    Servo liftServo;
    /// VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA

    final double DEPOSIT_IDLE = valori.DEPOSIT_IDLE; // pozitia lui normala
    final double DEPOSIT_SCORING = valori.DEPOSIT_SCORING; // pozitia lui cand scoreaza

///     INTAKE _________________________________________________
    Servo extensionServo;
    CRServo activeIntakeServo;
    Servo bendOverServo;
    final double EXT_HOME = valori.EXT_HOME; // pozitia lui normala
    final double EXT_EXTENDED = valori.EXT_EXTENDED; // pozitia lui cand arunca piesa

    final double DMP_INTAKE_SIDE = valori.DMP_INTAKE_SIDE;
    final double DMP_SCORING_SIDE = valori.DMP_SCORING_SIDE;


    ///     HANG _______________________________________________
    public DcMotorEx hang1Motor;
    public DcMotorEx hang2Motor;
    double powerHang = 0;

    int toInt(boolean bool){
        if(bool)
            return 1;
        else return 0;
    }

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        /// DRIVETRAIN
        pose = new Pose2d(0,0,Math.toRadians(0));
        drive = new MecanumDrive(hardwareMap,pose);

        /// OUTAKE
        liftMotor = hardwareMap.get(DcMotorEx.class,"LIFTMOTOR");
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftServo.setPosition(DEPOSIT_IDLE);
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        /// INTAKE

        extensionServo = hardwareMap.get(Servo.class,"EXTENSIONSERVO");
        extensionServo.setPosition(EXT_HOME);

        activeIntakeServo = hardwareMap.get(CRServo.class,"WHEELSERVO");

        bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
        bendOverServo.setPosition(DMP_INTAKE_SIDE);

        /// HANG
        hang1Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hang2Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gp1 = gamepad1;
        gp2 = gamepad1;

        waitForStart();
        runtime.reset();

        while (opModeIsActive() && !isStopRequested()) {
            /// DRIVETRAIN
            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -gp1.left_stick_y,
                            -(gp1.right_trigger - gp1.left_trigger)
                    ),
                    -gp1.right_stick_x
            ));

            /// OUTAKE
            liftMotor.setPower(gp2.left_trigger - gp2.right_trigger);

            if(gp2.dpad_left)
                liftServo.setPosition(DEPOSIT_SCORING);
            if(gp2.dpad_right)
                liftServo.setPosition(DEPOSIT_IDLE);


            /// INTKAE
            if(gp2.a){ // PRELUAREA
                extensionServo.setPosition(EXT_EXTENDED);
                bendOverServo.setPosition(DMP_SCORING_SIDE);
            }
            else if(gp2.b){ // DEPOZITARE PIESA IN TROACA
                extensionServo.setPosition(EXT_HOME);
                bendOverServo.setPosition(DMP_INTAKE_SIDE);
            }

            activeIntakeServo.setPower(toInt(gp2.left_bumper) - toInt(gp2.right_bumper));


            /// HANG
            if(gp1.dpad_up)
                powerHang = 1;
            else if (gp1.dpad_down)
                powerHang = -1;
            else powerHang = 0;
            hang1Motor.setPower(powerHang);
            hang2Motor.setPower(powerHang);


            telemetry.addData("Putere Lift Motor",liftMotor.getPower());
            telemetry.addData("Pozitie Lift Motor",liftMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
