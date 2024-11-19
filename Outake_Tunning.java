package org.firstinspires.ftc.teamcode.COD;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.arcrobotics.ftclib.controller.PIDController;

@TeleOp(name = "Outtake Tunning",group = "Tunning")
public class Outake_Tunning extends LinearOpMode {
    private PIDController controller;
    public static double p = 0,i = 0,d = 0;
    public static int target = 0;
    public DcMotorEx liftMotor;

    @Override
    public void runOpMode(){
        controller = new PIDController(p,i,d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor = hardwareMap.get(DcMotorEx.class,"LIFTMOTOR");

        waitForStart();
        while(opModeIsActive()){
            controller.setPID(p,i,d);
            int liftPos = liftMotor.getCurrentPosition();
            double pid = controller.calculate(liftPos,target);
            liftMotor.setPower(pid);
        }
    }
}
