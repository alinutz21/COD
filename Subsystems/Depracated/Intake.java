package org.firstinspires.ftc.teamcode.COD.Subsystems.Depracated;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

@Deprecated
@Config
public class Intake {
    public Servo extensionServo;
    public CRServo activeIntakeServo;
    public Servo bendOverServo;
    public Servo liftServo;
    ElapsedTime returnHomeTime = new ElapsedTime();
    ElapsedTime turningBackHome = new ElapsedTime();
    public ValoriFunctii valori = new ValoriFunctii();

    public enum State {
        HOME,
        EXTEND,
        INTAKE,
        SPECIMENIN,
        SPECIMENOUT,
        ENEMY,
        RETRACT,
        RETURNING
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
    final double RETURN_TIME = valori.RETURN_TIME;

    public void init(HardwareMap hardwareMap){

        extensionServo = hardwareMap.get(Servo.class,"EXTENSIONSERVO");
//        extensionServo.setPosition(EXT_HOME);

        activeIntakeServo = hardwareMap.get(CRServo.class,"WHEELSERVO");

        bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
  //      bendOverServo.setPosition(DMP_INTAKE_SIDE);

        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        SetState(State.HOME);
        //bendOverServo.setPosition(4);
        //liftServo.setPosition(0);

    }

    public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp){
        switch (currentState){
            case HOME:
                if(gp.x){
                    extensionServo.setPosition(EXT_EXTENDED);
                    bendOverServo.setPosition(DMP_SCORING_SIDE);
                    liftServo.setPosition(valori.DEPOSIT_IDLE);
                    currentState = State.EXTEND;
                }
                break;
            case EXTEND:
                if(Math.abs(extensionServo.getPosition()-EXT_EXTENDED)<=0.2) {
                    currentState = State.INTAKE;
                    activeIntakeServo.setPower(1);
                }
                break;
            case INTAKE:
                if(gp.b){
                    bendOverServo.setPosition(DMP_INTAKE_SIDE);
                    extensionServo.setPosition(EXT_HOME);
                    returnHomeTime.reset();
                    currentState = State.RETRACT;
                }
                break;
//            case SPECIMENIN:
//                if(gp.right_bumper){
//                    bendOverServo.setPosition(0.4);
//                    extensionServo.setPosition(EXT_HOME);
//                    activeIntakeServo.setPower(0);
//                }
//                break;
            case RETRACT:
                if(returnHomeTime.seconds() >= RETURN_TIME){
                    activeIntakeServo.setPower(-1);
                    returnHomeTime.reset();
                    currentState = State.RETURNING;
                }
                break;
            case RETURNING:
                if(returnHomeTime.seconds() >= 1){
                    activeIntakeServo.setPower(0);
                    bendOverServo.setPosition(0.35);
                    currentState = State.HOME;
                }
                break;
            default:
                currentState = State.HOME;
        }
        if(gp.a && currentState != State.HOME){
            activeIntakeServo.setPower(0);
            liftServo.setPosition(valori.DEPOSIT_IDLE);
            bendOverServo.setPosition(DMP_INTAKE_SIDE);
            extensionServo.setPosition(EXT_HOME);
            currentState = State.HOME;
        }
    }


}
