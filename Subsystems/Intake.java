package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

@Config
public class Intake {
    public Servo extensionServo;
    public Servo activeIntakeServo;
    public Servo bendOverServo;
    ElapsedTime liftTimer = new ElapsedTime();
    public ValoriFunctii valori;

    public enum State {
        HOME,
        EXTEND,
        INTAKE,
        RETRACT
    }
    State currentState = State.HOME;

    /*
    *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    final double EXT_HOME = valori.EXT_HOME; // pozitia lui normala
    final double EXT_EXTENDED = valori.EXT_EXTENDED; // pozitia lui cand arunca piesa
    final double DMP_INTAKE_SIDE = valori.DMP_INTAKE_SIDE;
    final double DMP_SCORING_SIDE = valori.DMP_SCORING_SIDE;

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    final double INTAKE_TIME = valori.INTAKE_TIME;

    public void init(HardwareMap hardwareMap){
        liftTimer.reset();

        extensionServo = hardwareMap.get(Servo.class,"EXTENSIONSERVO");
        extensionServo.setPosition(EXT_HOME);

        activeIntakeServo = hardwareMap.get(Servo.class,"WHEELSERVO");

        bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
        bendOverServo.setPosition(DMP_INTAKE_SIDE);


        SetState(State.HOME);
    }

    public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp){
        switch (currentState){
            case HOME:
                if(gp.b){
                    bendOverServo.setPosition(DMP_SCORING_SIDE);
                    extensionServo.setPosition(EXT_EXTENDED);
                    currentState = State.EXTEND;
                }
                break;
            case EXTEND:
                if(Math.abs(extensionServo.getPosition()-EXT_EXTENDED)<3) {
                    liftTimer.reset();
                    currentState = State.INTAKE;
                   // activeIntakeServo.setPosition(1);
                }
                break;
            case INTAKE:
                if(liftTimer.seconds() >= INTAKE_TIME){
                    bendOverServo.setPosition(DMP_INTAKE_SIDE);
                    extensionServo.setPosition(EXT_HOME);
                    currentState = State.RETRACT;
                }
                break;
            case RETRACT:
                if(Math.abs(extensionServo.getPosition()-EXT_HOME) < 3){
                    currentState = State.HOME;
                }
                break;
            default:
                currentState = State.HOME;
        }
        if(gp.a && currentState != State.HOME){
            currentState = State.HOME;
        }
    }


}
