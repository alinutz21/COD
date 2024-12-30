package org.firstinspires.ftc.teamcode.COD.TunningSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@Disabled
@TeleOp(name = "Hang Tunning",group = "Tunning")
public class Hang_Tunning extends LinearOpMode {
    public DcMotorEx hang1Motor;
    public DcMotorEx hang2Motor;
    public static boolean invers1 = false;
    public static boolean invers2 = false;

    @Override
    public void runOpMode(){
        hang1Motor = hardwareMap.get(DcMotorEx.class,"HANG1MOTOR");
        hang2Motor = hardwareMap.get(DcMotorEx.class,"HANG2MOTOR");
        hang1Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hang2Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hang1Motor.setDirection(DcMotorSimple.Direction.REVERSE);
        if(invers1)
            hang1Motor.setDirection(DcMotorSimple.Direction.REVERSE);
        if(invers2)
            hang2Motor.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        while(opModeIsActive()){

            hang1Motor.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            hang2Motor.setPower(gamepad2.left_trigger - gamepad2.right_trigger);

            telemetry.addData("MOTOR 1",hang1Motor.getPower());
            telemetry.addData("MOTOR 2",hang2Motor.getPower());
            telemetry.update();

        }
    }
}
