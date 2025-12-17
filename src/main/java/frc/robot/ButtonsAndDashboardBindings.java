package frc.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.ArmMotorSubsystem;

import java.util.function.Function;


public class ButtonsAndDashboardBindings {

  private static OperatorInterface oi;

  public ButtonsAndDashboardBindings() {

  }

  // Updated method signature to receive CoralSubsystem instead of Elevator.
  public static void configureBindings(){
    
    configureOperatorButtonBindings();
    
  }

  /****************************** */
  /*** BUTTON BOX BINDINGS ****** */
  /****************************** */

  private static void configureOperatorButtonBindings() {

    // oi.getButtonBox1XAxisPositive().onTrue(createPresetCommand(ArmMotorSubsystem.spinArmMotor)); // Spins ArmMotor

  }
}




