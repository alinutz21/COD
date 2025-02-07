package org.firstinspires.ftc.teamcode.COD.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;

import kotlin.contracts.ReturnsNotNull;

@Config
public class LazyIntake {
    public Servo extensionServo;
    public CRServo activeIntakeServo;
    public Servo bendOverServo;
    public Servo liftServo;
    ElapsedTime returnHomeTimer = new ElapsedTime();
    ElapsedTime turningBackHomeTimer = new ElapsedTime();
    ElapsedTime scoringTimer = new ElapsedTime();
    ElapsedTime backToHomeSideTimer = new ElapsedTime();

    public ValoriFunctii valori = new ValoriFunctii();

    public enum State {
        HOME,
        EXTENDING,
        BO_DOWN,
        INTAKE,
        INTAKE_SIDE,
        SAMPLE_STANDBY,
        SAMPLE_OUT,
        SCORING,
        RETRACT,
        RETURNING
    }
    State currentState = State.HOME;
    final double EXT_HOME = valori.EXT_HOME; // pozitia lui normala
    final double EXT_EXTENDED = valori.EXT_EXTENDED; // pozitia lui cand arunca piesa
    final double DMP_INTAKE_SIDE = valori.DMP_INTAKE_SIDE;
    final double DMP_SCORING_SIDE = valori.DMP_SCORING_SIDE;
    final double DMP_90DEGREES = valori.DMP_90DEGREES;
    final double DMP_HORIZONTAL = valori.DMP_HORIZONTAL_SIDE;

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    final double RETURN_TIME = valori.RETURN_TIME;
    final double SAMPLE_OUT_TIME = valori.SAMPLE_OUT_TIME;
    private boolean firstTime = true;

    public void init(HardwareMap hardwareMap){
        extensionServo = hardwareMap.get(Servo.class,"EXTENSIONSERVO");
        activeIntakeServo = hardwareMap.get(CRServo.class,"WHEELSERVO");
        bendOverServo = hardwareMap.get(Servo.class,"ROTATESERVO");
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        SetState(State.RETURNING);
    }

    public void SetState(State state) { currentState = state;}
    public void Loop(Gamepad gp){
        switch (currentState){
            case HOME:
                if(gp.x){
                    extensionServo.setPosition(EXT_EXTENDED);
                    bendOverServo.setPosition(DMP_SCORING_SIDE);
                    liftServo.setPosition(valori.DEPOSIT_IDLE);
                    currentState = State.EXTENDING;
                }
                if(gp.dpad_down){
                    bendOverServo.setPosition(DMP_HORIZONTAL);
                    extensionServo.setPosition(EXT_EXTENDED);
                    currentState = State.BO_DOWN;
                }
                if(gp.right_bumper){
                    bendOverServo.setPosition(valori.DMP_INTAKE_SIDE);
                }
                break;
            case BO_DOWN:
                if(gp.x){
                    activeIntakeServo.setPower(1);
                    bendOverServo.setPosition(DMP_SCORING_SIDE);
                    currentState = State.INTAKE_SIDE;
                }
                break;
            case EXTENDING:
                if(Math.abs(extensionServo.getPosition()-EXT_EXTENDED)<=0.2) {
                    currentState = State.INTAKE;
                    activeIntakeServo.setPower(1);
                }
                break;
            case INTAKE_SIDE:
                if(gp.b){
                    returnHomeTimer.reset();
                    bendOverServo.setPosition(DMP_HORIZONTAL);
                    extensionServo.setPosition(EXT_HOME);
                    currentState = State.SAMPLE_STANDBY;
                }
                break;
            case INTAKE:
                if(gp.touchpad){
                    bendOverServo.setPosition(DMP_90DEGREES);
                    extensionServo.setPosition(EXT_HOME);
                    returnHomeTimer.reset();
                    currentState = State.SAMPLE_STANDBY;
                }
                if(gp.b){
                    extensionServo.setPosition(EXT_HOME);
                //    activeIntakeServo.setPower(-1);
                    bendOverServo.setPosition(DMP_INTAKE_SIDE);
                    scoringTimer.reset();
                    currentState = State.SCORING;
                }
                break;
            case SAMPLE_STANDBY:
               if(returnHomeTimer.seconds() >= 2.5) {
                   bendOverServo.setPosition(DMP_90DEGREES);
                   activeIntakeServo.setPower(0);
                   if(gp.b){
                       activeIntakeServo.setPower(-1);
                       bendOverServo.setPosition(DMP_INTAKE_SIDE);
                       scoringTimer.reset();
                       currentState = State.SCORING;
                   }
                   if(gp.touchpad){
                       bendOverServo.setPosition(DMP_SCORING_SIDE-0.17);
                       activeIntakeServo.setPower(-1);
                       extensionServo.setPosition(EXT_EXTENDED);
                       turningBackHomeTimer.reset();
                       currentState = State.SAMPLE_OUT;
                   }
               }
                break;
            case SAMPLE_OUT:
                if(turningBackHomeTimer.seconds() >= SAMPLE_OUT_TIME){
                    bendOverServo.setPosition(DMP_90DEGREES);
                    extensionServo.setPosition(EXT_HOME);
                    activeIntakeServo.setPower(0);
                    returnHomeTimer.reset();
                    currentState = State.RETURNING;
                }
                break;
            case SCORING:
                if(scoringTimer.seconds() >= 2){
                    activeIntakeServo.setPower(-1);
                    returnHomeTimer.reset();
                    currentState = State.RETURNING;
                }
                break;
            case RETURNING:
                if(returnHomeTimer.seconds() >= 2 || firstTime || gp.b){
                    firstTime = false;
                    activeIntakeServo.setPower(0);
                    bendOverServo.setPosition(DMP_90DEGREES);
                    extensionServo.setPosition(EXT_HOME);
                    currentState = State.HOME;
                }
                break;
            default:
                currentState = State.HOME;
        }
        if(gp.right_bumper){
            bendOverServo.setPosition(valori.DMP_INTAKE_SIDE);
        }

        if(gp.a && currentState != State.HOME){
            activeIntakeServo.setPower(0);
            liftServo.setPosition(valori.DEPOSIT_IDLE);
            bendOverServo.setPosition(DMP_90DEGREES);
            extensionServo.setPosition(EXT_HOME);
            activeIntakeServo.setPower(0);
            currentState = State.HOME;
        }
    }


}
