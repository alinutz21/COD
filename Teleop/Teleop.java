package org.firstinspires.ftc.teamcode.COD.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.BetterOuttake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Outake;

@TeleOp(name="TeleOP", group="Linear OpMode")
public class Teleop extends LinearOpMode {
    private Gamepad gp1,gp2;
    private Drivetrain drivetrain;
    private Outake outake;
    private BetterOuttake outtake;
    private Intake intake;
    private Hang hang;

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        outtake = new BetterOuttake();
        outake = new Outake();
        intake = new Intake();
        drivetrain = new Drivetrain();
        hang = new Hang();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drivetrain.init(hardwareMap);
        outake.init(hardwareMap);
        intake.init(hardwareMap);
        hang.init(hardwareMap);

        gp1 = gamepad1;
        gp2 = gamepad2;

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain.Loop(gp1);
            intake.Loop(gp2); // TODO: TESTEAZA PENTRU DRIVER 1
            outake.Loop(gp2); // TODO: TESTEAZA PENTRU DRIVER 1
            //   outtake.Loop(gp2);
            hang.Loop(gp1);
        }
    }
}
