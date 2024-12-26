package org.firstinspires.ftc.teamcode.COD.CodTecho;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp(name = "Tunning")
public class ArmTestTunning extends OpMode {
    ArmTestPID arm;
    private static double PUTERE_MAXIMA = 0.5;
    private static double target = 50;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        arm = new ArmTestPID(hardwareMap,"nume_motor",false,true);
    }

    @Override
    public void loop() {
        arm.setPosition(target,PUTERE_MAXIMA);
        arm.armUpdate();
        telemetry.addData("Poziite",arm.getPosition());
        telemetry.addData("Putere",arm.getPowers());
        telemetry.update();
    }
}
