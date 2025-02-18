package org.firstinspires.ftc.teamcode.COD.RO2D2MOVE;



import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.teamcode.system_controllers.SensorPublisher;

@Disabled
@TeleOp(name="miscareRO2D2", group="OpMode")
public class  MovemenRO2D2 extends LinearOpMode {

    /**
     * DRIVE
     */
    public static double  PrecisionDenominatorTranslational = 1, PrecisionDenominatorAngle = 1;

    public void robotCentricDrive(DcMotor leftFront, DcMotor leftBack, DcMotor rightFront, DcMotor rightBack, double  SpeedLimit, boolean StrafesOn , double LeftTrigger, double RightTrigger, boolean pto_ON)
    {
        double y = -gamepad1.right_stick_y; // Remember, this is reversed!
        double x = gamepad1.right_stick_x;
        if (StrafesOn == false)
        {
            x=0;
        }

        double rx = gamepad1.left_stick_x*1 - LeftTrigger + RightTrigger;

        rx*=PrecisionDenominatorAngle;
        x/=PrecisionDenominatorTranslational;
        y/=PrecisionDenominatorTranslational;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeftPower = Clip(frontLeftPower,SpeedLimit);
        backLeftPower = Clip(backLeftPower,SpeedLimit);
        frontRightPower = Clip(frontRightPower,SpeedLimit);
        backRightPower = Clip(backRightPower,SpeedLimit);

        if(pto_ON == true)
        {
            if(gamepad1.right_trigger > 0)
            {
                leftBack.setPower(-1);
                rightBack.setPower(-1);
            }
            else
            {
                leftBack.setPower(backLeftPower);
                rightBack.setPower(backRightPower);
            }
            leftFront.setPower(0);
            rightFront.setPower(0);

        } else
        {
            leftFront.setPower(frontLeftPower);
            leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);
        }


    }

    double Clip(double Speed, double lim)
    {
        return Math.max(Math.min(Speed,lim), -lim);
    }


    @Override
    public void runOpMode() {
        robotMap r = new robotMap(hardwareMap);

        double loopTime = 0;


        boolean StrafesOn = true;
        boolean pto = false;


        double SpeedLimit = 1;

        ElapsedTime timer = new ElapsedTime();

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        timer.reset();


        waitForStart();
        telemetry.speak("RUBIX");

        while (opModeIsActive() && !isStopRequested()) {


            MotorConfigurationType motorConfigurationType = r.leftBack.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            r.leftBack.setMotorType(motorConfigurationType);

            motorConfigurationType = r.rightBack.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            r.rightBack.setMotorType(motorConfigurationType);

            motorConfigurationType = r.rightFront.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            r.rightFront.setMotorType(motorConfigurationType);

            motorConfigurationType = r.leftFront.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            r.leftFront.setMotorType(motorConfigurationType);

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);


            robotCentricDrive(r.leftFront, r.leftBack, r.rightFront, r.rightBack, SpeedLimit , StrafesOn , 0,0, pto);
            if(gamepad1.dpad_left)
                pto = !pto;
            if(gamepad1.dpad_right)
                StrafesOn = !StrafesOn;

            double loop = System.nanoTime();

            telemetry.addData("hz ", 1000000000 / (loop - loopTime));
            telemetry.addData("Fata Dreapta",r.rightFront.getPower());
            telemetry.addData("Fata Stanga",r.leftFront.getPower());
            telemetry.addData("Spate Dreapta",r.rightBack.getPower());
            telemetry.addData("Spate Stanga",r.leftBack.getPower());
            telemetry.addData("Strafe",StrafesOn);
            telemetry.addData("Pto",pto);
            loopTime = loop;
            telemetry.update();
        }
        //sensorPublisher.stopPublishing();
    }
}