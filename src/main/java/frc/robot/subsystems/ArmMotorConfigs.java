package frc.robot.subsystems;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public final class ArmMotorConfigs {

    public static final SparkBaseConfig armConfig = new SparkMaxConfig();
    public static final SparkBaseConfig littleMotorConfig = new SparkMaxConfig();

    static {
      // Configure basic settings of the elevator motor
      armConfig
          .idleMode(IdleMode.kBrake)
          .voltageCompensation(12);

      littleMotorConfig
          .idleMode(IdleMode.kBrake)
          .voltageCompensation(12);
      /*
       * Configure the closed loop controller. We want to make sure we set the
       * feedback sensor as the primary encoder.
       */
      // armConfig
      //     .closedLoop
      //     .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      //     // Set PID values for position control
      //     .p(ElevatorConstants.kElevatorKp)
      //     .d(ElevatorConstants.kElevatorKd)
      //     .outputRange(-1, 1)
      //     .maxMotion
      //     // Set MAXMotion parameters for position control
      //     .maxVelocity(ElevatorConstants.kElevatorVel)
      //     .maxAcceleration(ElevatorConstants.kElevatorAcc)
      //     .allowedClosedLoopError(ArmConstants.kAllowableError);
    }
  }

