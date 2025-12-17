// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class ArmMotorSubsystem extends SubsystemBase { 
public static Object spinArmMotor;
private final SparkMax armMotor;
private final RobotContainer m_robotContainer;

  /** Creates a new ExampleSubsystem. */
  public ArmMotorSubsystem(RobotContainer robotContainer) {
    m_robotContainer = robotContainer;
    armMotor = new SparkMax(m_robotContainer.ArmControllerID, com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless);

    armMotor.configure(
      ArmMotorConfigs.armConfig,
      ResetMode.kResetSafeParameters,
      PersistMode.kPersistParameters);    
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  // public Command spinArmMotor() {
  //   // Inline construction of command goes here.
  //   // Subsystem::RunOnce implicitly requires `this` subsystem.

  //   // return armMotor.set(0.02);     

  // }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
