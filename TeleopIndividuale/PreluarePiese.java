package org.firstinspires.ftc.teamcode.COD.TeleopIndividuale;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Depracated.Intake;

@Disabled
@TeleOp(name="Preluare piese", group="Subsisteme")
public class PreluarePiese extends LinearOpMode {
    Gamepad gp2;
    Intake intake = null;

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        intake = new Intake();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        intake.init(hardwareMap);
        gp2 = gamepad2;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            intake.Loop(gp2);
        }
    }
}
