package org.firstinspires.ftc.teamcode.COD.Subsystems.Depracated;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.COD.Subsystems.SlidePieseAutonomie;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

@Deprecated
@Config
public class Outake3 {
    public SlidePieseAutonomie slide;

    public Servo liftServo;
    public Servo specimenServo;
    ElapsedTime liftTimer = new ElapsedTime();
    ElapsedTime liftTimerSpecimen = new ElapsedTime();

    public ValoriFunctii valori = new ValoriFunctii();

    public double target = 0;
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
    final double LIFT_HOME = valori.LIFT_DOWN; // pozitia de jos
    final double LIFT_BASKET1 = valori.LIFT_BASKET1; // pozitia de sus
    final double LIFT_BASKET2 = valori.LIFT_BASKET2; // pozitia de sus
    final double LIFT_SPECIMEN1 = valori.LIFT_SPECIMEN1; // pozitia de sus
    final double LIFT_SPECIMEN2 = valori.LIFT_SPECIMEN2; // pozitia de sus

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

    boolean auto = false;


    public void init(HardwareMap hardwareMap){
        slide = new SlidePieseAutonomie(hardwareMap,"LIFTMOTOR",true,false);
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
        liftServo.setPosition(DEPOSIT_IDLE);
        // specimenServo.setPosition(0.6);
        liftTimer.reset();
        liftTimerSpecimen.reset();
        auto = false;
        currentState = State.GROUND;
    }

 //   public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp1, Gamepad gp,Telemetry telemetry) {
        if(gp1.x){
            slide.setPosition(30,VITEZA_MAXIMA_URCARE);
            /*
            liftServo.setPosition(DEPOSIT_IDLE);
            specimenServo.setPosition(SPECIMEN_CLOSED);
            slide.setPosition(LIFT_HOME,VITEZA_MAXIMA_COBORARE);
            currentState = State.GROUND;
             */
            currentState = State.GROUND;
        }

        switch (currentState){
            case GROUND:
                /// BASKETI
                if(gp.x){
                    slide.setPosition(LIFT_BASKET1,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_BASKET;
                    liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
                }
                if(gp.y){
                    slide.setPosition(LIFT_BASKET2,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_BASKET;
                    liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
                }

                /// SPECIMENE
                if(gp.a){
                    auto = true;
                    slide.setPosition(LIFT_SPECIMEN1,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_SPECIMEN;
                }
                if(gp.b){
                    auto = true;
                    slide.setPosition(LIFT_SPECIMEN2,VITEZA_MAXIMA_URCARE);
                    currentState = State.EXTEND_SPECIMEN;
                }
                break;

            case EXTEND_BASKET:
                if(gp.y){
                    liftServo.setPosition(DEPOSIT_SCORING);
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
                if(gp.y){
                    liftServo.setPosition(DEPOSIT_IDLE);
                    slide.setPosition(LIFT_HOME,VITEZA_MAXIMA_COBORARE);
                    currentState = State.RETRACT;
                }
                break;
            case DUMP_SPECIMEN:
                if(gp.dpad_left){
                    slide.setPosition(LIFT_HOME,0.3);
                    liftTimerSpecimen.reset();
                    currentState = State.DUMPED_SPECIMEN;
                }
                if(liftTimer.seconds() >= DUMP_SPECIMEN_TIME){
                    slide.setPosition(LIFT_HOME,VITEZA_MAXIMA_COBORARE);
                    currentState = State.GROUND;
                }
                break;
            case DUMPED_SPECIMEN:
                if(slide.isOnTarget(5) || liftTimerSpecimen.seconds() >= 0.1){
                    specimenServo.setPosition(SPECIMEN_OPEN);
                    auto = false;
                    currentState = State.RETRACT;
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
        if(gp.left_bumper && currentState != State.GROUND){
            liftServo.setPosition(DEPOSIT_IDLE);
            specimenServo.setPosition(SPECIMEN_CLOSED);
            slide.setPosition(LIFT_HOME,VITEZA_MAXIMA_COBORARE);
            currentState = State.GROUND;
        }
        if(gp.dpad_left || !auto) // deschid
            specimenServo.setPosition(SPECIMEN_OPEN);

        if(gp.dpad_right){
//            target = 5;
            specimenServo.setPosition(SPECIMEN_CLOSED);
        }

        slide.slideUpdate();
        telemetry.addData("pozitie curenta",slide.getPosition());
        telemetry.addData("Timp",liftTimer.seconds());
        telemetry.update();
    }
}
