package org.firstinspires.ftc.teamcode.COD.Subsystems;

import android.transition.Slide;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class HangV2 {
    public SlideLift hang;
    double power = 0;

    public void init(HardwareMap hardwareMap){
        hang = new SlideLift(hardwareMap,"HANG1MOTOR","HANG2MOTOR",true,false,true);
    }
    public void Loop(Gamepad gp,Telemetry telemetry){
        /*
        if(gp.dpad_up){
            power = 1;
        }
        else if(gp.dpad_down){
            power = -1;
        }else power = 0;
  */
    }


}
