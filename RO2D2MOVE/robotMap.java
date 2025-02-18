package org.firstinspires.ftc.teamcode.COD.RO2D2MOVE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class robotMap {

    /**
     * Chassis
     */

    public DcMotorEx leftFront = null;
    public DcMotorEx leftBack = null;
    public DcMotorEx rightFront = null;
    public DcMotorEx rightBack = null;



    //   public MecanumDrivetrain drivetrain;



    /**
     * Transfer
     */


    /**
     * Sensors
     */
    /**
     * ENDGAME
     */

    public Servo pto = null;


    /**
     * INIT
     */


    public robotMap(HardwareMap Init) {
        /**
         * Chassis
         */

        rightFront = Init.get(DcMotorEx.class, "rightFront");
        leftFront = Init.get(DcMotorEx.class, "leftFront");
        rightBack = Init.get(DcMotorEx.class, "rightBack");
        leftBack = Init.get(DcMotorEx.class, "leftBack");


        leftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftBack.setDirection(DcMotorEx.Direction.REVERSE);

        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // collect.setDirection(DcMotorSimple.Direction.REVERSE);

//        extendoRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        extendoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // right.setModeRange(urm09_customdriver.DistanceMode.MODE_300CM.bVal, urm09_customdriver.MeasurementMode.AUTOMATIC.bVal);


//        DcMotor.RunMode prevMode = lift.getMode();
//        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lift.setMode(prevMode);

        //  lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //  lift.setMode(DcMotor.RunMode.RUN_AND_RESET_ENCODER);






//        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        extendoLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        extendoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        extendoLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        extendoRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        extendoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        extendoRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

}