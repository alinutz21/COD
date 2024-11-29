package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;

@Config
public class Drivetrain {

    public MecanumDrive drive;
    public Pose2d pose;

//    int toInt(boolean bool){
//        if(bool)
//            return 1;
//        else return 0;
//    }

    public void init(HardwareMap hardwareMap){
        pose = new Pose2d(0,0, Math.toRadians(0));
        drive = new MecanumDrive(hardwareMap,pose);
    }


    public void Loop(Gamepad gp){


        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        (gp.right_trigger - gp.left_trigger * 0.65),
                        -gp.left_stick_x
                ),
                -gp.right_stick_x
        ));
// TODO: Verifica daca ai nevoie de functia asta, s-ar putea sa ingreuneze programul degeaba
      //  drive.updatePoseEstimate();
    }


}
