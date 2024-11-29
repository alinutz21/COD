package org.firstinspires.ftc.teamcode.COD.TunningSubsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@Disabled
@Config
@TeleOp(name = "Outtake Tunning",group = "Tunning")
public class Outake_Tunning extends LinearOpMode {
    private PIDController controller;
    public static double p = 0,i = 0,d = 0;
    public static double f = 0;
    public static int target = 0;
    private final double ticks_in_degree = 537.7;
    public DcMotorEx liftMotor;

    @Override
    public void runOpMode(){
        controller = new PIDController(p,i,d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor = hardwareMap.get(DcMotorEx.class,"LIFTMOTOR");
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        while(opModeIsActive()){
            controller.setPID(p,i,d);
            int liftPos = liftMotor.getCurrentPosition();
            double pid = controller.calculate(liftPos,target);
            double ff = Math.cos(Math.toRadians(target/ticks_in_degree)) * f;
            double power = pid + ff;
            liftMotor.setPower(power);

            telemetry.addData("pos",liftPos);
            telemetry.addData("target",target);
            telemetry.addData("power",power);
            telemetry.update();

        }
    }
}
