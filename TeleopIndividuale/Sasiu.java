package org.firstinspires.ftc.teamcode.COD.TeleopIndividuale;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.SmoothDrivetrain;

@Disabled
@TeleOp(name="Sasiu", group="Subsisteme")
public class Sasiu extends LinearOpMode {
    Gamepad gp1;
    SmoothDrivetrain drivetrain;
    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        double loopTime = 0;
        drivetrain = new SmoothDrivetrain() ;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drivetrain.init(hardwareMap);
        gp1 = gamepad1;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain.Loop(gp1);
            double loop = System.nanoTime();
            telemetry.addData("hz ", 1000000000 / (loop - loopTime));
            loopTime = loop;
            telemetry.update();
        }
    }
}
