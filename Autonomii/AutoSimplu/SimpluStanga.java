
package org.firstinspires.ftc.teamcode.COD.Autonomii.AutoSimplu;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;
/*
    DUCE TOATE SAMPLE-URILE GALBENE IN NET ZONE: 12 P si apoi se parcheaza
 */
@Disabled
@Config
@Autonomous(name = "STANGA Simplu", group = "AutoSimplu")
public class SimpluStanga extends LinearOpMode {

    @Override
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Action trajectoryActionCloseOut = drive.actionBuilder(initialPose)
                .splineToConstantHeading(new Vector2d(-5,45), Math.toRadians(90))
                .turn(Math.toRadians(-20))
                .lineToY(7)
                .turn(Math.toRadians(25))
                .splineToConstantHeading(new Vector2d(-10,45), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-25,50), Math.toRadians(90))
                .lineToY(5)
                .turn(Math.toRadians(-3))
                .lineToY(50)
                .splineToConstantHeading(new Vector2d(-29,45), Math.toRadians(90))
                .lineToY(10)
                .lineToY(30)
                .build();

        // actions that need to happen on init; for instance, a claw tightening.
        //    Actions.runBlocking(claw.closeClaw());


        while (!isStopRequested() && !opModeIsActive()) {

            telemetry.addLine("Position during init");
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;


        Actions.runBlocking(
                new SequentialAction(
                        trajectoryActionCloseOut
                )
        );

    }
}
