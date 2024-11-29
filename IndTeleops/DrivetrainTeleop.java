package org.firstinspires.ftc.teamcode.COD.IndTeleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.Drivetrain;


@TeleOp(name="Drivetrain Subsystem", group="Subsisteme")
public class DrivetrainTeleop extends LinearOpMode {
    Gamepad gp1;
    Drivetrain drivetrain;

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        drivetrain = new Drivetrain();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drivetrain.init(hardwareMap);
        gp1 = gamepad1;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain.Loop(gp1);
        }
    }
}
