
package org.firstinspires.ftc.teamcode.COD.Autonomii.Rosu;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.COD.RR.MecanumDrive;
@Disabled
@Config
@Autonomous(name = "Rosu STANGA", group = "Autonomous")
public class AlbastruStanga extends LinearOpMode {
    /*  public class Lift {
          private Slide sli

          public class LiftUp implements Action {
              public boolean initialized = false;

              @Override
              public boolean run(@NonNull TelemetryPacket packet) {
                  if (!initialized) {
                      slide.
                      initialized = false;
                  }

                  double pos = lift.getCurrentPosition();
                  packet.put("liftPos", pos);
                  if (pos < 3000.0) {
                      return true;
                  } else {
                      lift.setPower(0);
                      return false;
                  }
              }
          }
          public Action liftUp() {
              return new LiftUp();
          }

          public class LiftDown implements Action {
              private boolean initialized = false;

              @Override
              public boolean run(@NonNull TelemetryPacket packet) {
                  if (!initialized) {
                      lift.setPower(-0.8);
                      initialized = true;
                  }

                  double pos = lift.getCurrentPosition();
                  packet.put("liftPos", pos);
                  if (pos > 100.0) {
                      return true;
                  } else {
                      lift.setPower(0);
                      return false;
                  }
              }
          }
          public Action liftDown(){
              return new LiftDown();
          }
      }
  */
    /*
    public class Claw {
        private Servo claw;

        public Claw(HardwareMap hardwareMap) {
            claw = hardwareMap.get(Servo.class, "claw");
        }

        public class CloseClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                claw.setPosition(0.55);
                return false;
            }
        }
        public Action closeClaw() {
            return new CloseClaw();
        }

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                claw.setPosition(1.0);
                return false;
            }
        }
        public Action openClaw() {
            return new OpenClaw();
        }
    }
*/
    @Override
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .waitSeconds(0.1);

        Action trajectoryActionCloseOut = tab1.endTrajectory().fresh()
                .splineToConstantHeading(new Vector2d(-5,45), Math.toRadians(90))
                .turn(Math.toRadians(-20))
                .lineToY(7)
                .turn(Math.toRadians(25))
                .splineToConstantHeading(new Vector2d(-10,45), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-25,50), Math.toRadians(90))
                .lineToY(5)
                .turn(Math.toRadians(-3))
                .lineToY(50)
                .splineToConstantHeading(new Vector2d(-29,50), Math.toRadians(90))
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

        Action trajectoryActionChosen;
        trajectoryActionChosen = tab1.build();
        Actions.runBlocking(
                new SequentialAction(
                        trajectoryActionChosen,
                        trajectoryActionCloseOut
                )
        );

    }
}
