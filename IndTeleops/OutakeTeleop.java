package org.firstinspires.ftc.teamcode.COD.IndTeleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.Outake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Outake2;
@Disabled
@TeleOp(name="Outake Subsystem", group="Subsisteme")
public class OutakeTeleop extends LinearOpMode {
    Gamepad gp1,gp2;
    Outake outake;



    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        outake = new Outake();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        outake.init(hardwareMap);
        gp1 = gamepad1;
        gp2 = gamepad2;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            outake.Loop(gp2,telemetry);
            telemetry.update();
        }
    }
}
