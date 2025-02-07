package org.firstinspires.ftc.teamcode.COD.Subsystems.Depracated;

import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.COD.ValoriFunctii;
@Deprecated
@Config
public class Outake2 {
 //   public Slide slide;
    public DcMotorEx liftMotor;
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

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    final double DUMP_TIME = valori.DUMP_BASKET_TIME;

    final double SPECIMEN_OPEN = valori.SPECIMEN_OPEN;
    final double SPECIMEN_CLOSED = valori.SPECIMEN_CLOSED;


    public void init(HardwareMap hardwareMap){
       // slide = new Slide(hardwareMap,"LIFTMOTOR",true,false);
        liftMotor = hardwareMap.get(DcMotorEx.class,"LIFTMOTOR");
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
        liftServo.setPosition(DEPOSIT_IDLE);
        dumpTimer.reset();
    }

    public void Loop(Gamepad gp2, Telemetry telemetry) {
        liftMotor.setPower(gp2.left_trigger - gp2.right_trigger);
        switch (currentState){
            case HORIZONTAL:
                if(gp2.dpad_down) {
                    liftServo.setPosition(DEPOSIT_SCORING);
                    dumpTimer.reset();
                    currentState = State.INCLINED;
                }
                break;
            case INCLINED:
                if(dumpTimer.seconds()>= DUMP_TIME){
                    liftServo.setPosition(DEPOSIT_IDLE);
                    currentState = State.HORIZONTAL;
                }
                break;
            default:
                currentState = State.HORIZONTAL;
        }

        if(gp2.dpad_left) // deschide
            specimenServo.setPosition(SPECIMEN_OPEN);
        if(gp2.dpad_right)
            specimenServo.setPosition(SPECIMEN_CLOSED);
        telemetry.addData("MOTOR",liftMotor.getCurrentPosition());
        telemetry.update();
    }
}
