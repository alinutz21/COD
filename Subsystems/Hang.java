package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Hang {
    public DcMotorEx hang1Motor;
    public DcMotorEx hang2Motor;
    double power = 0;

    public void init(HardwareMap hardwareMap){
        hang1Motor = hardwareMap.get(DcMotorEx.class,"HANG1MOTOR");
        hang2Motor = hardwareMap.get(DcMotorEx.class,"HANG2MOTOR");
        hang1Motor.setDirection(DcMotorSimple.Direction.REVERSE);
        hang1Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hang2Motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void Loop(Gamepad gp,Telemetry telemetry){
        if(gp.dpad_up){
            
        }
        else if(gp.dpad_down){
            power = -1;
        }else power = 0;
        hang1Motor.setPower(power);
        hang2Motor.setPower(power);
    }


}
