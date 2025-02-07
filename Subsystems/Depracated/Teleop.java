package org.firstinspires.ftc.teamcode.COD.Subsystems.Depracated;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.Attention;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SmoothDrivetrain;
import org.firstinspires.ftc.teamcode.COD.Subsystems.HangV2;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Outake;


@Disabled
@TeleOp(name="TELE OP", group="Linear OpMode")
public class Teleop extends LinearOpMode {
    Gamepad gp1,gp2;
    SmoothDrivetrain drivetrain;
    Outake outtake;
    Intake intake;
    HangV2 hang;
    Attention attention;
    @Override
    public void runOpMode(){
        telemetry.addLine("RUBIX NR1");
        telemetry.update();
        outtake = new Outake();
        intake = new Intake();
        drivetrain = new SmoothDrivetrain();
        hang = new HangV2();
        attention = new Attention();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drivetrain.init(hardwareMap);
        outtake.init(hardwareMap);
        intake.init(hardwareMap);
        hang.init(hardwareMap);
        attention.init(hardwareMap);
        gp1 = gamepad1;
        gp2 = gamepad2;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain.Loop(gp1);
            intake.Loop(gp2);
            outtake.Loop(gp2,telemetry);
            hang.Loop(gp1,telemetry);
            attention.Loop(gp1,gp2,telemetry);
            telemetry.update();
        }
    }
}
