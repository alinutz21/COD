package org.firstinspires.ftc.teamcode.COD.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.COD.Subsystems.HangV2;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Outake3;

@TeleOp(name="TELE OP AUTOMATIZAT ** IN TESTE", group="Linear OpMode")
public class TeleopAutomatizat extends LinearOpMode {
    Gamepad gp1,gp2;
    Drivetrain drivetrain;
    Outake3 outtake;
    Intake intake;
    HangV2 hang;

    @Override
    public void runOpMode(){
        telemetry.addLine("RUBIX NR1");
        telemetry.update();
        outtake = new Outake3();
        intake = new Intake();
        drivetrain = new Drivetrain();
        hang = new HangV2();
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
            intake.Loop(gp2);
            outtake.Loop(gp2,telemetry);
            hang.Loop(gp1,telemetry);
        }
    }
}
