package org.firstinspires.ftc.teamcode.COD.IndTeleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Intake;
@Disabled
@TeleOp(name="Hang Subsytem", group="Subsisteme")
public class HangTeleop extends LinearOpMode {
    Gamepad gp;
    Hang hang = null;

    @Override
    public void runOpMode(){
        hang = new Hang();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        hang.init(hardwareMap);
        gp = gamepad1;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            hang.Loop(gp,telemetry);
        }
    }
}
