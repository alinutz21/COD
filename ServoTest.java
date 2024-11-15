package org.firstinspires.ftc.teamcode.COD;

import com.acmerobotics.dashboard.FtcDashboard;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
@Config
@TeleOp(name = "Servo test",group = "Tests")
public class ServoTest extends LinearOpMode {

    public static double pozitie = 0.5;
    private Servo servo;
    @Override
    public void runOpMode(){

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        servo = hardwareMap.get(Servo.class,"servo");

        waitForStart();
        while(opModeIsActive()){
            servo.setPosition(pozitie);
            telemetry.addData("Pozitie",servo.getPosition());
            telemetry.update();
        }
    }
}
