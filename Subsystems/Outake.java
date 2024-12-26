package org.firstinspires.ftc.teamcode.COD.Subsystems;

import android.transition.Slide;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePiese;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
@Config
public class Outake {
    //   public Slide slide;
    public SlidePiese slide;
    public Servo liftServo;
    public Servo specimenServo;
    public enum State {
        HORIZONTAL,
        INCLINED,
    }
    State currentState = State.HORIZONTAL;
    ElapsedTime dumpTimer = new ElapsedTime();
    public ValoriFunctii valori = new ValoriFunctii();
    final double DEPOSIT_IDLE = valori.DEPOSIT_IDLE; // pozitia lui normala
    final double DEPOSIT_SCORING = valori.DEPOSIT_SCORING; // pozitia lui cand arunca piesa
    final double SPECIMEN_OPEN = valori.SPECIMEN_OPEN;
    final double SPECIMEN_CLOSED = valori.SPECIMEN_CLOSED;

    public void init(HardwareMap hardwareMap){
        slide = new SlidePiese(hardwareMap,"LIFTMOTOR",true,false);
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
    //    liftServo.setPosition(valori.DEPOSIT_IDLE);
        dumpTimer.reset();
    }
    double prevSlidePower = 0.0;
    public void Loop(Gamepad gp2, Telemetry telemetry) {
        double slidePower = -(gp2.left_trigger * 0.4 - gp2.right_trigger * 0.75);

        if (slidePower != 0.0) {
            slide.setPowers(slidePower);
            prevSlidePower = slidePower;
        }
        else if (prevSlidePower != 0.0) {
            slide.setPowers(0.0);
            prevSlidePower = 0.0;
        }

        if(slide.getPosition() > 1 && slidePower > 0){
            liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
        }
        if(slidePower < 0 && slide.getPosition() > 5){
            liftServo.setPosition(valori.DEPOSIT_IDLE);
        }
        if(gp2.y){
            liftServo.setPosition(DEPOSIT_SCORING);
        }

        if(gp2.dpad_left) // deschide
            specimenServo.setPosition(SPECIMEN_OPEN);
        if(gp2.dpad_right) // inchide
            specimenServo.setPosition(SPECIMEN_CLOSED);

        slide.slideUpdate();
    }
}
