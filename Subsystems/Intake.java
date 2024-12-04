package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

@Config
public class Intake {
    public Servo extensionServo;
    public CRServo activeIntakeServo;
    public Servo bendOverServo;
    public Servo liftServo;
    ElapsedTime liftTimer = new ElapsedTime();
    ElapsedTime horizontalTime = new ElapsedTime();
    public ValoriFunctii valori = new ValoriFunctii();

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
      //  extensionServo.setPosition(EXT_HOME);

        activeIntakeServo = hardwareMap.get(CRServo.class,"WHEELSERVO");

        bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
        bendOverServo.setPosition(DMP_INTAKE_SIDE);

        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        SetState(State.HOME);
    }

    public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp){

        switch (currentState){
            case HOME:
                if(gp.x){
                    bendOverServo.setPosition(DMP_SCORING_SIDE);
                    extensionServo.setPosition(EXT_EXTENDED);
                    currentState = State.EXTEND;
                }
                break;
            case EXTEND:
                if(Math.abs(extensionServo.getPosition()-EXT_EXTENDED)<3) {
                    liftTimer.reset();
                    currentState = State.INTAKE;
                    activeIntakeServo.setPower(1);
                }
                break;
            case INTAKE:
                if(gp.b){
                    bendOverServo.setPosition(DMP_INTAKE_SIDE);
                    extensionServo.setPosition(EXT_HOME);
                    horizontalTime.reset();
                    currentState = State.RETRACT;
                }
                break;
            case RETRACT:
                if(horizontalTime.seconds() >= 1){
                    liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
                }
                if(Math.abs(extensionServo.getPosition()-EXT_HOME) < 0.05){
                    activeIntakeServo.setPower(0);

                    currentState = State.HOME;
                }
                break;
            default:
                currentState = State.HOME;
        }
        if(gp.a && currentState != State.HOME){
            currentState = State.HOME;
            // TODO: Scrie codul pentru intoarcerea bratului inapoi la pozitia initiala
        }
    }


}
