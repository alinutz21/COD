package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
@Config
public class BetterOuttake {
    public Slide slide;
    public Servo liftServo;
    public Servo specimenServo;
    ElapsedTime liftTimer = new ElapsedTime();
    public ValoriFunctii valori = new ValoriFunctii();
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
    final int LIFT_UP = valori.LIFT_BASKET1; // pozitia de sus
    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    final double DEPOSIT_IDLE = valori.DEPOSIT_IDLE; // pozitia lui normala
    final double DEPOSIT_SCORING = valori.DEPOSIT_SCORING; // pozitia lui cand arunca piesa

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    final double DUMP_TIME = valori.DUMP_BASKET_TIME;

    final double SPECIMEN_OPEN = valori.SPECIMEN_OPEN;
    final double SPECIMEN_CLOSED = valori.SPECIMEN_CLOSED;


    public void init(HardwareMap hardwareMap){
        slide = new Slide(hardwareMap,"LIFTMOTOR",true,false);
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
        liftServo.setPosition(DEPOSIT_IDLE);
        specimenServo.setPosition(SPECIMEN_CLOSED);
        liftTimer.reset();
        SetState(State.GROUND);
    }

    public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp, Telemetry telemetry) {

        switch (currentState){
            case GROUND:
                if(gp.x){
                    slide.setPosition(LIFT_UP,0.5);
                    currentState = State.EXTEND;
                }
                break;
            case EXTEND:
                if(slide.isOnTarget(5)) {

                   liftServo.setPosition(DEPOSIT_SCORING);
                    liftTimer.reset();
                    currentState = State.DUMP;
                }
                break;
            case DUMP:
                if(liftTimer.seconds() >= DUMP_TIME){
                    liftServo.setPosition(DEPOSIT_IDLE);
                    slide.setPosition(LIFT_DOWN,0.2);
                    currentState = State.RETRACT;
                }
                break;
            case RETRACT:
                if(slide.isOnTarget(5)){
                    currentState = State.GROUND;
                }
                break;
            default:
                currentState = State.GROUND;
        }
        if(gp.y && currentState != State.GROUND){
            currentState = State.GROUND;
        }
        if(gp.dpad_left) // deschide
            specimenServo.setPosition(SPECIMEN_OPEN);
        if(gp.dpad_right)
            specimenServo.setPosition(SPECIMEN_CLOSED);


        telemetry.addData("pozitie curenta",slide.getPosition());
        telemetry.addData("Target",LIFT_UP);
        telemetry.addData("Timp",liftTimer.seconds());
        telemetry.update();
        slide.slideUpdate();
    }



}
