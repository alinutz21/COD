package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class HangV2 {
    public SlideLift hang;
    public static double target  = -1;

    public void init(HardwareMap hardwareMap){
        target = -1;
        hang = new SlideLift(hardwareMap,"HANG1MOTOR","HANG2MOTOR",true,false,true);
    }
    public void Loop(Gamepad gp,Telemetry telemetry){
            if(gp.x) {
                if (hang.getPosition() > 5) {
                    target = -2.5;
                } else target = 10;
            }
        hang.setPosition(target,1);
        hang.slideUpdate();
    }


}
