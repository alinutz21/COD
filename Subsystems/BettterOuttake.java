package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
@Config
public class BettterOuttake {
    public Slide slide;
    public Servo liftServo;
    public Servo specimenServo;
    ElapsedTime liftTimer = new ElapsedTime();
    ElapsedTime liftTimerSpecimen = new ElapsedTime();
    public ValoriFunctii valori = new ValoriFunctii();
    public enum State {
        GROUND,
        EXTEND_BASKET,
        EXTEND_SPECIMEN,
        DUMP_BASKET,
        DUMP_SPECIMEN,
        DUMPED_SPECIMEN,
        RETRACT
    }
    State currentState = State.GROUND;

    final double VITEZA_MAXIMA_URCARE = valori.VITEZA_MAXIMA_URCARE;
    final double VITEZA_MAXIMA_COBORARE = valori.VITEZA_MAXIMA_COBORARE;

    /*
     *  VALORILE PENTRU BRATUL DE RIDICARE
     */
    final int LIFT_HOME = valori.LIFT_HOME; // pozitia de jos
    final int LIFT_BASKET1 = valori.LIFT_BASKET1; // pozitia de sus
    final int LIFT_BASKET2 = valori.LIFT_BASKET2; // pozitia de sus
    final int LIFT_SPECIMEN1 = valori.LIFT_SPECIMEN1; // pozitia de sus
    final int LIFT_SPECIMEN2 = valori.LIFT_SPECIMEN2; // pozitia de sus

    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    final double DEPOSIT_IDLE = valori.DEPOSIT_IDLE; // pozitia lui normala
    final double DEPOSIT_SCORING = valori.DEPOSIT_SCORING; // pozitia lui cand arunca piesa

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    final double DUMP_BASKET_TIME = valori.DUMP_BASKET_TIME;
    final double DUMP_SPECIMEN_TIME = valori.DUMP_SPECIMEN_TIME;

    final double SPECIMEN_OPEN = valori.SPECIMEN_OPEN;
    final double SPECIMEN_CLOSED = valori.SPECIMEN_CLOSED;


    public void init(HardwareMap hardwareMap){
        slide = new Slide(hardwareMap,"LIFTMOTOR",true,false);
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
        liftServo.setPosition(DEPOSIT_IDLE);
        specimenServo.setPosition(SPECIMEN_CLOSED);
        liftTimer.reset();
        liftTimerSpecimen.reset();
        currentState = State.GROUND;

    }

 //   public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp1, Gamepad gp2, Telemetry telemetry) {

        switch (currentState){
            case GROUND:
                if(gp2.x){
                    slide.setPosition(LIFT_BASKET1,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_BASKET;
                }
                if(gp2.y){
                    slide.setPosition(LIFT_BASKET2,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_BASKET;
                }
                if(gp2.a){
                    slide.setPosition(LIFT_SPECIMEN1,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_SPECIMEN;
                }
                if(gp2.b){
                    slide.setPosition(LIFT_SPECIMEN2,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_SPECIMEN;
                }
                break;

            case EXTEND_BASKET:
                if(slide.isOnTarget(3)) {
                    liftServo.setPosition(DEPOSIT_SCORING);
                    liftTimer.reset();
                    currentState = State.DUMP_BASKET;
                }
                break;

            case EXTEND_SPECIMEN:
                if(slide.isOnTarget(2)){
                    liftTimer.reset();
                    currentState = State.DUMP_SPECIMEN;
                }
                break;
            case DUMP_BASKET:
                if(liftTimer.seconds() >= DUMP_BASKET_TIME){
                    liftServo.setPosition(DEPOSIT_IDLE);
                    slide.setPosition(LIFT_HOME,VITEZA_MAXIMA_COBORARE);
                    currentState = State.RETRACT;
                }
                break;
            case DUMP_SPECIMEN:
                if(gp2.dpad_left || liftTimer.seconds() >= DUMP_SPECIMEN_TIME){
                    slide.setPosition(slide.getPosition() - 5,0.3);
                    liftTimerSpecimen.reset();
                    currentState = State.DUMPED_SPECIMEN;
                }
                break;
            case DUMPED_SPECIMEN:
                if(slide.isOnTarget(1) || liftTimerSpecimen.seconds() >= 2){
                    specimenServo.setPosition(SPECIMEN_OPEN);
                    slide.setPosition(LIFT_HOME,VITEZA_MAXIMA_COBORARE);
                }
                break;
            case RETRACT:
                if(slide.isOnTarget(3)){
                    currentState = State.GROUND;
                }
                break;
            default:
                currentState = State.GROUND;
        }
        if(gp2.dpad_down && currentState != State.GROUND){
            slide.setPosition(LIFT_HOME,VITEZA_MAXIMA_COBORARE);
            currentState = State.RETRACT;
        }


        if(gp1.dpad_left) // deschide
            specimenServo.setPosition(SPECIMEN_OPEN);
        if(gp1.dpad_right)
            specimenServo.setPosition(SPECIMEN_CLOSED);
        slide.slideUpdate();
        telemetry.addData("pozitie curenta",slide.getPosition());
        telemetry.addData("Timp",liftTimer.seconds());
        telemetry.update();
    }
}
