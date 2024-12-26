package org.firstinspires.ftc.teamcode.COD.CodTecho;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.util.Range;
@Config
public class ArmTestPID
{
    // Arm characterization constants.

    // KP specifies the Proportional PID coefficient in PIDF.
    private static double KP = 0;

    // KI specifies the Integral PID coefficient in PIDF.
    private static double KI = 0;

    // KD specifies the Derivative PID coefficient in PIDF.
    private static double KD = 0;

    // KF specifies the arm holding power when the arm is at 90-degree (horizontal).
    private static double KF =  0;

    // ZERO_OFFSET specifies the arm angle in degrees from vertical when it is at resting position.
    private static double ZERO_OFFSET = 0;

    // MIN_POS specifies the arm angle in degrees at its minimum position (usually the same as ZERO_OFFSET).
    private static final double MIN_POS = ZERO_OFFSET;

    // MAX_POS specifies the arm angle in degrees at its maximum position.
    private static final double MAX_POS = 0;

    // TICKS_PER_DEGREE specifies the scaling factor to translate arm angle in degrees to encoder ticks.
    private static final double ENCODER_CPR = 0;
    private static final double GEAR_RATIO = 0;	// don't include motor internal gear ratio
    private static final double TICKS_PER_DEGREE = ENCODER_CPR * GEAR_RATIO / 360.0;

    // Arm subsystem components.
    private final DcMotorEx armMotor;
    private final PIDController pidController;

    // Arm states.
    private double targetPosInDegrees;
    private double revPowerLimit, fwdPowerLimit;

    /**
     * Constructor: create and initialize everything required by the arm subsystem including the arm motor and
     * PID controller.
     *
     * @param hardwareMap specifies the hardwareMap to get access to the motor object.
     * @param motorName specifies the motor's hardware name.
     * @param motorInverted specifies true to invert the motor direction, false otherwise.
     * @param brakeEnabled specifies true to set motor brake mode, false to set coast mode.
     */
    public ArmTestPID(HardwareMap hardwareMap, String motorName, boolean motorInverted, boolean brakeEnabled)
    {
        // Create arm motor and initialize it.
        armMotor = hardwareMap.get(DcMotorEx.class, motorName);
        armMotor.setDirection(motorInverted ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        armMotor.setZeroPowerBehavior(
                brakeEnabled ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // You may want to use RUN_USING_ENCODER if you want to do velocity control instead of OpenLoop.
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Create PID controller for the arm and initialize it with proper PID coefficients.
        pidController = new PIDController(KP, KI, KD);

        this.targetPosInDegrees = ZERO_OFFSET;
        this.revPowerLimit = -1.0;
        this.fwdPowerLimit = 1.0;
    }

    /**
     * This method must be called periodically so that it will do PID control of the arm towards set target position
     * and hold it. Note: this is the equivalent of the .update() method in Command-Based (e.g. FTCLib).
     */
    public void armUpdate()
    {
        // targetPosInDegrees, revPowerLimit and fwdPowerLimit are set by the setPosition method.
        double targetPosInTicks = (targetPosInDegrees - ZERO_OFFSET) * TICKS_PER_DEGREE;
        double currPosInTicks = armMotor.getCurrentPosition();
        double pidOutput = pidController.calculate(currPosInTicks, targetPosInTicks);
        // Calculate gravity compensation (assuming arm at horizontal position is 90-degree).
        double gravityComp = KF * Math.sin(Math.toRadians(ticksToRealWorldDegrees(currPosInTicks)));
        double power = pidOutput + gravityComp;
        // Clip power to the range of revPowerLimit and fwdPowerLimit.
        // For an arm, revPowerLimit and fwdPowerLimit should be symmetrical (in the range of -powerLimit and +powerLimit).
        power = Range.clip(power, revPowerLimit, fwdPowerLimit);
        armMotor.setPower(power);
    }

    /**
     * This method is typically called by autonomous to determine when the arm has reached target so that it can move
     * on to perform the next operation.
     *
     * @param toleranceInDegrees specifies the tolerance in degrees within which we will consider reaching target.
     * @return true if arm is on target within tolerance, false otherwise.
     */
    public boolean isOnTarget(double toleranceInDegrees)
    {
        double currPosInDegrees = getPosition();
        return Math.abs(targetPosInDegrees - currPosInDegrees) <= toleranceInDegrees;
    }

    /**
     * This method can be called by autonomous or teleop to set the arm target. Typically, in TeleOp, one can react to
     * a button press and call this method to move the arm to a preset position.
     *
     * @param targetPosInDegrees specifies the target position in degrees.
     * @param powerLimit specifies the maximum power for the arm movement.
     */
    public void setPosition(double targetPosInDegrees, double powerLimit)
    {
        this.targetPosInDegrees = targetPosInDegrees;
        powerLimit = Math.abs(powerLimit);
        this.revPowerLimit = -powerLimit;
        this.fwdPowerLimit = powerLimit;
    }

    /**
     * This method is typically used by TeleOp to control the arm movement by the value of the joystick so that the
     * speed of the arm movement can be controlled by the joystick. Since this is PID controlled, the arm will slow
     * down when approaching the min or max limits even though the joystick is pushed to max position.
     *
     * @param power specifies the maximum power for the arm movement.
     */
    public void setPower(double power)
    {
        if (power > 0.0)
        {
            // Move arm towards max position with specified power.
            setPosition(MAX_POS, power);
        }
        else if (power < 0.0)
        {
            // Move arm towards min position with specified power.
            setPosition(MIN_POS, power);
        }
        else
        {
            // Hold arm position without power limit.
            setPosition(getPosition(), 1.0);
        }
    }

    /**
     * This method translates encoder ticks to real world arm position in degrees.
     *
     * @param encoderTicks specifies the motor encoder ticks.
     * @return translated arm position in degrees.
     */
    public double ticksToRealWorldDegrees(double encoderTicks)
    {
        return encoderTicks / TICKS_PER_DEGREE + ZERO_OFFSET;
    }

    /**
     * This method returns the arm position in real world degrees.
     *
     * @return arm position in degrees.
     */
    public double getPosition()
    {
        return ticksToRealWorldDegrees(armMotor.getCurrentPosition());
    }

    public double getPowers()
    {
        return armMotor.getPower();
    }
}