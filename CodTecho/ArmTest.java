package org.firstinspires.ftc.teamcode.COD.CodTecho;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name ="ArmHoldPosition")
public class ArmTest extends OpMode{
    DcMotorEx armMotor;
    double power = 0;
    @Override
    public void init() {
        armMotor = hardwareMap.get(DcMotorEx.class,"CH_motor0");
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        power = gamepad1.left_stick_y;
        armMotor.setPower(power);
        telemetry.addData("Putere motor",armMotor.getPower());
        telemetry.update();
    }
}
