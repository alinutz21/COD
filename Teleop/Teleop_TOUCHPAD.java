package org.firstinspires.ftc.teamcode.COD.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.COD.Subsystems.Attention;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SmoothDrivetrain;
import org.firstinspires.ftc.teamcode.COD.Subsystems.HangV2;
import org.firstinspires.ftc.teamcode.COD.Subsystems.LazyIntake;
import org.firstinspires.ftc.teamcode.COD.Subsystems.Outake;

@TeleOp(name="TELE OP Regionala", group="Linear OpMode")
public class Teleop_TOUCHPAD extends LinearOpMode {
    Gamepad gp1,gp2;
    SmoothDrivetrain drivetrain;
    Outake outtake;
    LazyIntake intake;
    HangV2 hang;
    Attention attention;

    @Override
    public void runOpMode(){
        telemetry.addLine("RUBIX NR1");
        telemetry.update();
        double loopTime = 0;
        outtake = new Outake();
        intake = new LazyIntake();
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
            intake.Loop(gp2,telemetry);
            outtake.Loop(gp2,telemetry);
            hang.Loop(gp1,telemetry);
            attention.Loop(gp1,gp2,telemetry);
            double loop = System.nanoTime();
            telemetry.addData("hz ", 1000000000 / (loop - loopTime));
            loopTime = loop;
            telemetry.update();
        }
    }
}
