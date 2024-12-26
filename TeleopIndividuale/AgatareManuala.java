package org.firstinspires.ftc.teamcode.COD.TeleopIndividuale;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
@Disabled
@TeleOp(name="Hang Help", group="Subsisteme")
public class AgatareManuala extends LinearOpMode {
    Gamepad gp;
    DcMotorEx hang1,hang2;

    @Override
    public void runOpMode(){
        hang1 = hardwareMap.get(DcMotorEx.class,"HANG1MOTOR");
        hang2 = hardwareMap.get(DcMotorEx.class,"HANG2MOTOR");
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        hang1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hang2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hang1.setDirection(DcMotorSimple.Direction.REVERSE);
        gp = gamepad1;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            hang1.setPower(gp.left_stick_x);
            hang2.setPower(gp.right_stick_x);
            telemetry.addData("HANG1",hang1.getPower());
            telemetry.addData("HANG1",hang1.getCurrentPosition());
            telemetry.addData("HANG2",hang2.getPower());
            telemetry.addData("HANG2",hang2.getCurrentPosition());
            telemetry.update();
        }
    }
}
