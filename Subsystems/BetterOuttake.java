package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
@Config
public class BetterOuttake {
    public Slide slide;
    public Servo liftServo;
    ElapsedTime liftTimer = new ElapsedTime();
    public ValoriFunctii valori;
    public enum State {
        GROUND,
        EXTEND,
        DUMP,
        RETRACT
    }
    State currentState = State.GROUND;
    /*
     *  VALORILE PENTRU BRATUL DE RIDICARE
     */
    final int LIFT_DOWN = valori.LIFT_DOWN; // pozitia de jos
    final int LIFT_UP = valori.LIFT_UP; // pozitia de sus
    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    final double DEPOSIT_IDLE = valori.DEPOSIT_IDLE; // pozitia lui normala
    final double DEPOSIT_SCORING = valori.DEPOSIT_SCORING; // pozitia lui cand arunca piesa

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    final double DUMP_TIME = valori.DUMP_TIME;
    double cmToInch(double x){
        final double cmPerInch = 2.54000508;
        return x / cmPerInch;
    }



    public void init(HardwareMap hardwareMap){
        slide = new Slide(hardwareMap,"LIFTMOTOR",false,true);
        liftTimer.reset();

        SetState(State.GROUND);
    }

    public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp){
        switch (currentState){
            case GROUND:
                if(gp.x){
                    slide.setPosition(LIFT_UP,0.5);
                    currentState = State.EXTEND;
                }
                break;
            case EXTEND:
                if(slide.isOnTarget(cmToInch(10))) {

                    liftServo.setPosition(DEPOSIT_SCORING);
                    liftTimer.reset();
                    currentState = State.DUMP;
                }
                break;
            case DUMP:
                if(liftTimer.seconds() >= DUMP_TIME){
                    liftServo.setPosition(DEPOSIT_IDLE);
                    slide.setPosition(LIFT_DOWN,-0.5);
                    currentState = State.RETRACT;
                }
                break;
            case RETRACT:
                if(slide.isOnTarget(cmToInch(9))){
                    currentState = State.GROUND;
                }
                break;
            default:
                currentState = State.GROUND;
        }
        if(gp.y && currentState != State.GROUND){
            currentState = State.GROUND;
        }

        slide.slideUpdate();
    }



}
