package org.firstinspires.ftc.teamcode.COD.TeleopIndividuale;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.HangV2;
@Disabled
@TeleOp(name="Agatare Automata", group="Subsisteme")
public class AgatareAutomata extends LinearOpMode {
    Gamepad gp;
    HangV2 hang = null;

    @Override
    public void runOpMode(){
        hang = new HangV2();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        hang.init(hardwareMap);
        gp = gamepad1;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            hang.Loop(gp,telemetry);
        }
    }
}
