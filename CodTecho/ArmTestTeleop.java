package org.firstinspires.ftc.teamcode.COD.CodTecho;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp(name = "Test")
public class ArmTestTeleop extends OpMode {
    ArmTestPID arm;
    double prevArmPower = 0;
    @Override
    public void init() {
        arm = new ArmTestPID(hardwareMap,"nume_motor",false,true);
    }

    @Override
    public void loop() {
        double armPower = gamepad1.left_stick_y;
        armPower = Math.abs(armPower) >= 0.05 ? armPower : 0.0;
        if (armPower != 0.0)
        {
            arm.setPower(armPower);
            prevArmPower = armPower;
        }
        else if (prevArmPower != 0.0)
        {
            arm.setPower(0.0);
            prevArmPower = 0.0;
        }
        arm.armUpdate();
    }
}
