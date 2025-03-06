package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Attention {
    private double HALF_TIME = 90;
    boolean firstRun = true;
    boolean secondHalf = false;
    boolean last5 = true;
    public ElapsedTime timer = new ElapsedTime();
    Gamepad.RumbleEffect customRumbleEffect;
    public void init(HardwareMap hardwareMap){
        customRumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(0.0, 1.0, 100)  //  Rumble right motor 100% for 500 mSec
                .addStep(0.0, 0.0, 300)  //  Pause for 300 mSec
                .addStep(1.0, 0.0, 250)  //  Rumble left motor 100% for 250 mSec
                .addStep(0.0, 0.0, 250)  //  Pause for 250 mSec
                .addStep(1.0, 0.0, 250)  //  Rumble left motor 100% for 250 mSec
                .build();
    }
    public void Loop(Gamepad gp1,Gamepad gp2,Telemetry telemetry){
        if(firstRun){
            firstRun =false;timer.reset();
        }
        if ((timer.seconds() > HALF_TIME) && !secondHalf)  {
            gp1.setLedColor(1,0,0,5000);
            gp2.setLedColor(1,0,0,5000);
            gp1.runRumbleEffect(customRumbleEffect);
            gp2.runRumbleEffect(customRumbleEffect);
            secondHalf = true;
        }
        if((timer.seconds() >= 115 && timer.seconds() <= 120) && !last5){
            gp1.runRumbleEffect(customRumbleEffect);
            gp2.runRumbleEffect(customRumbleEffect);
            last5 = true;
        }
   //     telemetry.addData("Secunde",timer.seconds());
    }


}
