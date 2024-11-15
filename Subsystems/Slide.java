package org.firstinspires.ftc.teamcode.COD.Subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.util.Range;



public class Slide
{
    // Slide characterization constants.

    // KP specifies the Proportional PID coefficient in PIDF.
    private static final double KP = 0;

    // KI specifies the Integral PID coefficient in PIDF.
    private static final double KI = 0;

    // KD specifies the Derivative PID coefficient in PIDF.
    private static final double KD = 0;

    // KF specifies the slide holding power at any height.
    private static final double KF = 0;

    // ZERO_OFFSET specifies the slide extension length in inches at resting position (usually 0.0).
    private static final double ZERO_OFFSET = 0.0;

    // MIN_POS specifies the slide extension length in inches at its minimum position (usually the same as ZERO_OFFSET).
    private static final double MIN_POS = ZERO_OFFSET;

    // MAX_POS specifies the slide extension length in inches at its maximum position.
    private static final double MAX_POS = 39.37;

    // TICKS_PER_INCH specifies the scaling factor to translate slide extension length in inches to encoder ticks.
    private static final double TICKS_PER_INCH = 537.7;

    // Slide subsystem components.
    private final DcMotorEx leftMotor;
    private final PIDController pidController;

    // Slide states.
    private double targetPosInInches;
    private double revPowerLimit, fwdPowerLimit;

    /**
     * Constructor: create and initialize everything required by the slide subsystem including the slide motors and
     * PID controller.
     *
     * @param hardwareMap specifies the hardwareMap to get access to the motor object.
     * @param leftMotorName specifies the left motor's hardware name.
     * @param leftMotorInverted specifies true to invert the left motor direction, false otherwise.
     * @param brakeEnabled specifies true to set motor brake mode, false to set coast mode.
     */
    public Slide(
            HardwareMap hardwareMap, String leftMotorName, boolean leftMotorInverted, boolean brakeEnabled)
    {
        // Create slide motors and initialize it.
        leftMotor = hardwareMap.get(DcMotorEx.class, leftMotorName);
        leftMotor.setDirection(leftMotorInverted ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        leftMotor.setZeroPowerBehavior(
                brakeEnabled ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // You may want to use RUN_USING_ENCODER if you want to do velocity control instead of OpenLoop.
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        // Create PID controller for the slide and initialize it with proper PID coefficients.
        pidController = new PIDController(KP, KI, KD);

        this.targetPosInInches = ZERO_OFFSET;
        this.revPowerLimit = -1.0;
        this.fwdPowerLimit = 1.0;
    }

    /**
     * This method must be called periodically so that it will do PID control of the slide towards set target position
     * and hold it. Note: this is the equivalent of the .update() method in Command-Based (e.g. FTCLib).
     */
    public void slideUpdate()
    {
        // targetPosInInches, revPowerLimit and fwdPowerLimit are set by the setPosition method.
        double targetPosInTicks = (targetPosInInches - ZERO_OFFSET) * TICKS_PER_INCH;
        // Only need to read the position of one side of the slide assuming both sides are mechanically tied.
        double currPosInTicks = leftMotor.getCurrentPosition();
        double pidOutput = pidController.calculate(currPosInTicks, targetPosInTicks);
        // Gravity compensation should be a constant for a slide.
        double power = pidOutput + KF;
        // Clip power to the range of revPowerLimit and fwdPowerLimit.
        // For a slide/elevator, revPowerLimit and fwdPowerLimit can be asymmetric (i.e. revPowerLimit and
        // fwdPowerLimit have different magnitudes. For example, stronger power going up than down.
        power = Range.clip(power, revPowerLimit, fwdPowerLimit);
        leftMotor.setPower(power);
    }

    /**
     * This method is typically called by autonomous to determine when the slide has reached target so that it can
     * move on to perform the next operation.
     *
     * @param toleranceInInches specifies the tolerance in inches within which we will consider reaching target.
     * @return true if slide is on target within tolerance, false otherwise.
     */
    public boolean isOnTarget(double toleranceInInches)
    {
        double currPosInInches = getPosition();
        return Math.abs(targetPosInInches - currPosInInches) <= toleranceInInches;
    }

    /**
     * This method can be called by autonomous or teleop to set the slide target. Typically, in TeleOp, one can react
     * to a button press and call this method to move the slide to a preset position.
     *
     * @param targetPosInInches specifies the target position in inches.
     * @param powerLimit specifies the maximum power for the slide movement.
     */
    public void setPosition(double targetPosInInches, double powerLimit)
    {
        this.targetPosInInches = targetPosInInches;
        powerLimit = Math.abs(powerLimit);
        this.revPowerLimit = -powerLimit;
        this.fwdPowerLimit = powerLimit;
    }

    /**
     * This method is typically used by TeleOp to control the slide movement by the value of the joystick so that the
     * speed of the slide movement can be controlled by the joystick. Since this is PID controlled, the slide will
     * slow down when approaching the min or max limits even though the joystick is pushed to max position.
     *
     * @param power specifies the maximum power for the slide movement.
     */
    public void setPowers(double power)
    {
        if (power > 0.0)
        {
            // Move slide towards max position with specified power.
            setPosition(MAX_POS, power);
        }
        else if (power < 0.0)
        {
            // Move slide towards min position with specified power.
            setPosition(MIN_POS, power);
        }
        else
        {
            // Hold slide position without power limit.
            setPosition(getPosition(), 1.0);
        }
    }

    /**
     * This method translates encoder ticks to real world slide position in inches.
     *
     * @param encoderTicks specifies the motor encoder ticks.
     * @return translated slide position in inches.
     */
    public double ticksToRealWorldInches(double encoderTicks)
    {
        return encoderTicks / TICKS_PER_INCH + ZERO_OFFSET;
    }

    /**
     * This method returns the slide position in real world inches.
     *
     * @return slide position in inches.
     */
    public double getPosition()
    {
        return ticksToRealWorldInches(leftMotor.getCurrentPosition());
    }
}

