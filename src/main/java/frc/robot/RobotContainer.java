// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ArmMotorConfigs;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IndicatorLight;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

//Variables 
//Set ID's for each electronic device

//Arm motor variables
 public int ArmControllerID = 6;
 public final SparkMax armMotor;
 public final RelativeEncoder armEncoder;

//  Little motor variables
 private int LittleControllerID = 7;
 private final SparkMax littleMotor;
 private final RelativeEncoder littleMotorEncoder;

 //Swerve Turn variables
 private int swerveTurnControllerID = 3;
 private final TalonSRX swerveTurnMotor;

 //Swerve Drive variables
 private int swerveDriveControllerID = 4;
 private final TalonSRX swerveDriveMotor;

 //LED Variables
 private final IndicatorLight indicatorLight;
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

//Pnuematic Variables
  public final DoubleSolenoid doubleSolenoidTop;
  public final DoubleSolenoid doubleSolenoidBottom; 
  public final Solenoid singleSolenoidRelease;
      
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    doubleSolenoidTop = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 4, 3);
    doubleSolenoidBottom = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 1);
    singleSolenoidRelease = new Solenoid(PneumaticsModuleType.CTREPCM, 5);

    armMotor = new SparkMax(ArmControllerID, com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless);
    indicatorLight = new IndicatorLight();

    armEncoder = armMotor.getEncoder();
   
    armMotor.configure(
        ArmMotorConfigs.armConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    armEncoder.setPosition(0);

 

    littleMotor = new SparkMax(LittleControllerID, com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless);
    littleMotorEncoder = littleMotor.getEncoder();

    littleMotor.configure(
        ArmMotorConfigs.littleMotorConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    littleMotorEncoder.setPosition(0);

    littleMotor.set(0.02);

    TalonFXConfiguration config = new TalonFXConfiguration();
    swerveTurnMotor = new TalonSRX(swerveTurnControllerID); 
    // swerveTurnMotor.getConfigurator().apply(config); 
    swerveTurnMotor.set(TalonSRXControlMode.PercentOutput, 0.20);

    swerveDriveMotor = new TalonSRX(swerveDriveControllerID);
    // swerveDriveMotor.getConfigurator().apply(config);
    swerveDriveMotor.set(TalonSRXControlMode.PercentOutput, 0.20);

    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
