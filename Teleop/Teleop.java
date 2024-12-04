package org.firstinspires.ftc.teamcode.COD.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.BetterOuttake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.BettterOuttake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Outake2;

@TeleOp(name="TeleOP", group="Linear OpMode")
public class Teleop extends LinearOpMode {
    Gamepad gp1,gp2;
    Drivetrain drivetrain;
    Outake2 outtake;
    Intake intake;
    Hang hang;

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        outtake = new Outake2();
        intake = new Intake();
        drivetrain = new Drivetrain();
        hang = new Hang();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drivetrain.init(hardwareMap);
        outtake.init(hardwareMap);
        intake.init(hardwareMap);
        hang.init(hardwareMap);

        gp1 = gamepad1;
        gp2 = gamepad2;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain.Loop(gp1);
            intake.Loop(gp2); // TODO: TESTEAZA PENTRU DRIVER 1
            outtake.Loop(gp2,telemetry); // TODO: TESTEAZA PENTRU DRIVER 1
            hang.Loop(gp1,telemetry);
        }
    }
}
