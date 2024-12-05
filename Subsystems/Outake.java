package org.firstinspires.ftc.teamcode.COD.Subsystems;

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
@Config
public class Outake {
    //   public Slide slide;
    public Slide slide;
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
    boolean merge = false;

    public void init(HardwareMap hardwareMap){
        slide = new Slide(hardwareMap,"LIFTMOTOR",true,false);
        liftServo = hardwareMap.get(Servo.class,"LIFTSERVO");
        specimenServo = hardwareMap.get(Servo.class,"SPECIMENSERVO");
        liftServo.setPosition(valori.DEPOZIT_HORIZONTAL);
        dumpTimer.reset();


    }
    double prevSlidePower = 0.0;
    public void Loop(Gamepad gp2, Telemetry telemetry) {
        double slidePower = -(gp2.left_trigger * 0.4 - gp2.right_trigger * 0.65);
     //   slidePower = Math.abs(slidePower) >= 0.05 ? slidePower : 0.0; /// TODO: VERIRICA DACA ARE ROST LINIA ASTA
        if (slidePower != 0.0)
        {
            // Joystick action always interrupts and overrides button action.
            slide.setPowers(slidePower);
            prevSlidePower = slidePower;
        }
        else if (prevSlidePower != 0.0)
        {
            // Operator just let go the joystick. Stop the slide and hold its position.
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

        slide.slideUpdate();
        if(gp2.dpad_left) // deschide
            specimenServo.setPosition(SPECIMEN_OPEN);
        if(gp2.dpad_right) // inchide
            specimenServo.setPosition(SPECIMEN_CLOSED);

        telemetry.addData("Putere",slidePower);
        telemetry.addData("Motor",slide.getPosition());
        telemetry.update();
    }
}
