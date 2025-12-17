package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.IndicatorLightConstants.LED_EFFECTS;
import java.util.Random;
import java.util.function.Supplier;

public class IndicatorLight extends SubsystemBase {

  private LED_EFFECTS currentColor_GOAL = LED_EFFECTS.BLACK;
  private LED_EFFECTS LED_State = LED_EFFECTS.BLACK;

  // Define constants for the blink period (in seconds)
  private static final double MAX_BLINK_PERIOD = 0.5; // far from target, slow blink
  private static final double MIN_BLINK_PERIOD = 0.001; // very close, fast blink

  public static final double MIN_TOLERANCE_BLINK = 0.025; // below this, blink is at min period
  public static final double MAX_TOLERANCE_BLINK = 0.150; // above this, blink is at max period

  // Variable to hold the current blink period computed from the error
  private double currentBlinkPeriod = MAX_BLINK_PERIOD;

  private AddressableLEDBuffer wlLEDBuffer;
  private AddressableLED wlLED;
  private AddressableLEDBuffer wlGreenLEDBuffer;
  private AddressableLEDBuffer wlOrangeLEDBuffer;
  private AddressableLEDBuffer wlPurpleLEDBuffer;
  private AddressableLEDBuffer wlRedLEDBuffer;
  private AddressableLEDBuffer wlYellowLEDBuffer;
  private AddressableLEDBuffer wlBlueLEDBuffer;
  private AddressableLEDBuffer wlIndigoLEDBuffer;
  private AddressableLEDBuffer wlVioletLEDBuffer;
  private AddressableLEDBuffer wlWhiteLEDBuffer;
  private AddressableLEDBuffer wlBlackLEDBuffer;
  private AddressableLEDBuffer wlDark_BlueLEDBuffer;

  // Store what the last hue of the first pixel is
  private int rainbowFirstPixelHue = 0;
  private int currentSaturation = 100;
  private boolean forward = true;
  private int counter = 0;
  private double lastTime = 0.0;
  private double blinkTime = 0.0;
  private boolean on = false;
  private int skittleCount = 0;

  private Random random = new Random();

  private Timer effectTimer = new Timer();
  private final double restartInterval = 5.0; // Restart effect every 10 seconds
  private int effectPhase = 0;
  private final int maxBrightness = 255;
  private int center = 9;
  private final double updateInterval = 0.05; // Interval in seconds for updates
  private Supplier<Boolean> getHaveCoral;
  private boolean pickupBlinkTriggered = false;

  private boolean branchAlignmentOn = true; // start with branchAlignment lighting on

  private AddressableLEDBuffer currentActiveBuffer;

  public IndicatorLight() {

    wlLED = new AddressableLED(IndicatorLightConstants.ADDRESSABLE_LED_PORT);
    wlLEDBuffer = new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    wlLED.setLength(wlLEDBuffer.getLength());
    center = wlLEDBuffer.getLength() / 2;
    currentActiveBuffer = wlLEDBuffer; // Set the default active buffer
    wlLED.setData(wlLEDBuffer);
    wlLED.start();

    wlGreenLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlGreenLEDBuffer.getLength(); i++) {
      wlGreenLEDBuffer.setHSV(i, IndicatorLightConstants.GREEN_HUE, 255, 128);
    }

    wlOrangeLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlOrangeLEDBuffer.getLength(); i++) {
      wlOrangeLEDBuffer.setLED(i, new Color8Bit(255, 27, 0));
    }

    wlPurpleLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlPurpleLEDBuffer.getLength(); i++) {
      wlPurpleLEDBuffer.setHSV(i, IndicatorLightConstants.PURPLE_HUE, 63, 92);
    }

    wlRedLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlRedLEDBuffer.getLength(); i++) {
      wlRedLEDBuffer.setHSV(i, IndicatorLightConstants.RED_HUE, 255, 128);
    }

    wlYellowLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (int i = 0; i < wlYellowLEDBuffer.getLength(); i++) {
      wlYellowLEDBuffer.setLED(i, Color.kYellow);
    }

    wlBlueLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlBlueLEDBuffer.getLength(); i++) {
      wlBlueLEDBuffer.setLED(i, Color.kBlue);
    }

    wlIndigoLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlIndigoLEDBuffer.getLength(); i++) {
      wlIndigoLEDBuffer.setLED(i, Color.kIndigo);
    }

    wlVioletLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlVioletLEDBuffer.getLength(); i++) {
      wlVioletLEDBuffer.setLED(i, Color.kViolet);
    }

    wlWhiteLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlWhiteLEDBuffer.getLength(); i++) {
      wlWhiteLEDBuffer.setLED(i, Color.kWhite);
    }

    wlBlackLEDBuffer =
        new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlBlackLEDBuffer.getLength(); i++) {
      wlBlackLEDBuffer.setLED(i, Color.kBlack);
    }

    wlDark_BlueLEDBuffer =
    new AddressableLEDBuffer(IndicatorLightConstants.ADDRESSABLE_LED_BUFFER_LENGTH);
    for (var i = 0; i < wlRedLEDBuffer.getLength(); i++) {
    wlDark_BlueLEDBuffer.setLED(i, Color.kDarkBlue);
    }

    effectTimer.start();
  }

  @Override
  public void periodic() {
    currentColor_GOAL = updateLightingGoal();


    if (LED_State != LED_EFFECTS.BLINK) {
      LED_State = currentColor_GOAL;
    }
    switch (LED_State) {
      
      case DARK_BLUE -> setActiveBuffer(wlDark_BlueLEDBuffer);
      case RED -> setActiveBuffer(wlRedLEDBuffer);
      case YELLOW -> setActiveBuffer(wlYellowLEDBuffer);
      case GREEN -> setActiveBuffer(wlGreenLEDBuffer);
      case ORANGE -> setActiveBuffer(wlOrangeLEDBuffer);
      case PURPLE -> setActiveBuffer(wlPurpleLEDBuffer);
      case BLUE -> setActiveBuffer(wlBlueLEDBuffer);
      case BLACK -> setActiveBuffer(wlBlackLEDBuffer);
      case WHITE -> setActiveBuffer(wlWhiteLEDBuffer);
      case ARM_IN_ERROR -> doBlinkRed();
      case RAINBOW -> doRainbow();
      case BLUEOMBRE -> doBlueOmbre();
      case BLINK -> doBlink();
      case BLINK_RED -> doBlinkRed();
      case BLINK_PURPLE -> blinkPurple();
      case PARTY -> party();
      case RSL -> doRsl();
      case SEGMENTPARTY -> doSegmentParty();
      case EXPLOSION -> doExplosionEffect();
      case POLKADOT -> doPokadot();
      case DYNAMIC_BLINK -> dynamicBlink();
      case DRIVE_TO_REEF -> {}
      default -> {}
    }

    // Publish the first 10 LEDs of wlLEDBuffer under "StripA" and the second 10
    // under "StripB"
    publishLEDsToDashboardFlipped("StripA", currentActiveBuffer);
  }

    
  

  /**
   * Publishes the colors from an AddressableLEDBuffer of length 20 to the SmartDashboard as a
   * single strip, flipping its order so LED 0 appears last on the dashboard.
   *
   * @param baseKey NetworkTable key prefix (e.g., "StripA")
   * @param buffer The AddressableLEDBuffer containing the full LED data (20 LEDs)
   */
  public void publishLEDsToDashboardFlipped(String baseKey, AddressableLEDBuffer buffer) {
    int length = buffer.getLength(); // Should be 20

    // Publish all LEDs in reverse order so that LED 0 is "at the bottom"
    for (int i = 0; i < length; i++) {
      // Flip index: so LED 0 → last, LED 1 → second last, etc.
      int reversedIndex = length - 1 - i;

      // Retrieve color in 8-bit format
      var color8Bit = buffer.getLED8Bit(reversedIndex);

      // Convert to hex string (#RRGGBB)
      String hex = String.format("#%02X%02X%02X", color8Bit.red, color8Bit.green, color8Bit.blue);

      // Publish to SmartDashboard: e.g., "StripA/led0", "StripA/led1", ...
      SmartDashboard.putString(baseKey + "/led" + i, hex);
    }
  }

  public void blueOmbre() {
    currentColor_GOAL = LED_EFFECTS.BLUEOMBRE;
  }

  public void rainbow() {
    currentColor_GOAL = LED_EFFECTS.RAINBOW;
  }

  public void party() {
    currentColor_GOAL = LED_EFFECTS.PARTY;
  }

  public void segmentParty() {
    currentColor_GOAL = LED_EFFECTS.SEGMENTPARTY;
  }

  public void explosion() {
    currentColor_GOAL = LED_EFFECTS.EXPLOSION;
  }

  public void polkadot() {
    currentColor_GOAL = LED_EFFECTS.POLKADOT;
  }

  public void doExplosionEffect() {
    double elapsedTime = effectTimer.get();

    // Automatically restart the effect after a specific interval
    if (elapsedTime > restartInterval) {
      effectPhase = 1; // Reset to start phase
      effectTimer.reset();
    }

    // Determine the update step based on the elapsed time
    int step = (int) (elapsedTime / updateInterval);

    if (effectPhase == 1) {
      // Expansion phase
      if (step <= center) {
        for (int i = 0; i <= step; i++) {
          int brightness = Math.max(0, maxBrightness - ((maxBrightness / center) * i));
          wlLEDBuffer.setRGB(center + i, brightness, brightness, 0);
          wlLEDBuffer.setRGB(center - i, brightness, brightness, 0);
        }
        wlLED.setData(wlLEDBuffer);
      } else {
        effectPhase = 2; // Move to fading phase
      }
    } else if (effectPhase == 2) {
      // Fading phase
      int fadeStep = maxBrightness - (int) (step * 5.0 / updateInterval);
      if (fadeStep > 0) {
        for (int i = 0; i < wlLEDBuffer.getLength(); i++) {
          int distance = Math.abs(center - i);
          int brightness = Math.max(0, fadeStep - ((maxBrightness / center) * distance));
          wlLEDBuffer.setRGB(i, brightness, brightness, 0);
        }
        wlLED.setData(wlLEDBuffer);
      } else {
        effectPhase = 0; // End the effect and wait for the next restart
      }
    }
  }

  public void doParty() {
    // For every pixel
    for (var i = 0; i < wlLEDBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      // Set the value
      int red = random.nextInt(256);
      int green = random.nextInt(256);
      int blue = random.nextInt(256);
      wlLEDBuffer.setRGB(i, red, green, blue);
    }
    // Set the LEDs
    wlLED.setData(wlLEDBuffer);
  }

  private void doSegmentParty() {
    // First, turn off all LEDs
    if (counter > IndicatorLightConstants.UPDATE_FREQUENCY) {
      counter = 0;

      // Decide how many segments to create
      int numberOfSegments = 1 + random.nextInt(10); // For example, 1 to 3 segments

      for (int segment = 0; segment < numberOfSegments; segment++) {
        // Select a random color from the palette
        int[] color =
            IndicatorLightConstants.colorPalette[
                random.nextInt(IndicatorLightConstants.colorPalette.length)];

        // Choose random start point and length
        int start = random.nextInt(wlLEDBuffer.getLength());
        int length = 1 + random.nextInt(wlLEDBuffer.getLength() - start); // Ensure segment fits

        // Set the colors
        for (int i = start; i < start + length; i++) {
          wlLEDBuffer.setRGB(i, color[0], color[1], color[2]);
        }
      }
    } else counter++;

    // Update the LED strip
    wlLED.setData(wlLEDBuffer);
  }

  private void doPokadot() {
    // For every pixel
    for (var i = 0; i < wlLEDBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      // Set the value
      int red = random.nextInt(256);
      int green = random.nextInt(256);
      int blue = random.nextInt(256);
      wlLEDBuffer.setRGB(i, red, green, blue);
    }
    // Set the LEDs
    wlLED.setData(wlLEDBuffer);
  }

  public void doRainbow() {
    // For every pixel
    for (var i = 0; i < wlLEDBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (rainbowFirstPixelHue + (i * 180 / wlLEDBuffer.getLength())) % 180;
      // Set the value
      wlLEDBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    rainbowFirstPixelHue += 3;
    // Check bounds
    rainbowFirstPixelHue %= 180;
    // Set the LEDs
    setActiveBuffer(wlLEDBuffer);
  }

  public void doBlueOmbre() {
    // For every pixel
    for (var i = 0; i < wlLEDBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var saturation = (currentSaturation + (i * 255 / wlLEDBuffer.getLength())) % 255;
      // Set the value
      wlLEDBuffer.setHSV(i, 103, 255, saturation);
    }

    // Update currentSaturation and ensure it stays within the 0-255 range
    if (forward) {
      // Move the saturation up
      currentSaturation += 3;
      if (currentSaturation >= 255) {
        currentSaturation = 255;
        forward = false;
      }
    } else {
      // Move the saturation down
      currentSaturation -= 3;
      if (currentSaturation <= 0) {
        currentSaturation = 0;
        forward = true;
      }
    }

    // Set the LEDs
    setActiveBuffer(wlLEDBuffer);
  }

  public void blink() {
    currentColor_GOAL = LED_EFFECTS.BLINK;
  }

  public void doBlink() {
    LED_State = LED_EFFECTS.BLINK;
    double timeStamp = Timer.getFPGATimestamp();

    if (blinkTime == 0.0) {
      blinkTime = timeStamp;
    }
    if (timeStamp - lastTime > 0.05) {
      on = !on;
      lastTime = timeStamp;
    }
    if (timeStamp - blinkTime > 1.0) {
      blinkTime = 0.0;
      LED_State = currentColor_GOAL;
    }

    Color blinkColor = on ? Color.kWhite : Color.kBlack;
    for (int i = 0; i < wlLEDBuffer.getLength(); i++) {
      wlLEDBuffer.setLED(i, blinkColor);
    }

    setActiveBuffer(wlLEDBuffer);
  }

  public void blinkRed() {
    LED_State = LED_EFFECTS.BLINK_RED;
  }

  public void doBlinkRed() {
    LED_State = LED_EFFECTS.BLINK_RED;
    double timeStamp = Timer.getFPGATimestamp();

    if (timeStamp - lastTime > 0.1) {
      on = !on;
      lastTime = timeStamp;
    }
    if (on) {
      setActiveBuffer(wlRedLEDBuffer);
    } else {
      setActiveBuffer(wlBlackLEDBuffer);
    }
  }

  public void rainbowBlink() {
    LED_State = LED_EFFECTS.BLINK;
    double timeStamp = Timer.getFPGATimestamp();
    if (blinkTime == 0.0) {
      blinkTime = timeStamp;
    }
    if (timeStamp - lastTime > 0.1) {
      on = !on;
      lastTime = timeStamp;
      skittleCount++;
      skittleCount = skittleCount % 7;
      switch (skittleCount) {
        case 0:
          setActiveBuffer(wlRedLEDBuffer);
          break;
        case 1:
          setActiveBuffer(wlOrangeLEDBuffer);
          break;
        case 2:
          setActiveBuffer(wlYellowLEDBuffer);
          break;
        case 3:
          setActiveBuffer(wlGreenLEDBuffer);
          break;
        case 4:
          setActiveBuffer(wlBlueLEDBuffer);
          break;
        case 5:
          setActiveBuffer(wlIndigoLEDBuffer);
          break;
        case 6:
          setActiveBuffer(wlVioletLEDBuffer);
          break;
        default:
          break;
      }
    }
    if (timeStamp - blinkTime > 1.5) {
      blinkTime = 0.0;
      LED_State = currentColor_GOAL;
    }
  }

  public void blinkPurple() {
    LED_State = LED_EFFECTS.BLINK_PURPLE;
    double timeStamp = Timer.getFPGATimestamp();
    if (blinkTime == 0.0) {
      blinkTime = timeStamp;
    }
    if (timeStamp - lastTime > 0.05) {
      on = !on;
      lastTime = timeStamp;
    }
    if (timeStamp - blinkTime > 1.0) {
      blinkTime = 0.0;
      LED_State = currentColor_GOAL;
    }
    if (on) {
      setActiveBuffer(wlWhiteLEDBuffer);
    } else {
      setActiveBuffer(wlPurpleLEDBuffer);
    }
  }

  public void green() {
    currentColor_GOAL = LED_EFFECTS.GREEN;
  }

  public void orange() {
    currentColor_GOAL = LED_EFFECTS.ORANGE;
  }

  public void purple() {
    currentColor_GOAL = LED_EFFECTS.PURPLE;
  }

  public void red() {
    currentColor_GOAL = LED_EFFECTS.RED;
  }

  public void yellow() {
    currentColor_GOAL = LED_EFFECTS.YELLOW;
  }

  public void blue() {
    currentColor_GOAL = LED_EFFECTS.BLUE;
  }

  public void drivingToReefLighting() {
    currentColor_GOAL = LED_EFFECTS.DRIVE_TO_REEF;
  }

  private void setActiveBuffer(AddressableLEDBuffer buffer) {
    currentActiveBuffer = buffer;
    wlLED.setData(buffer);
  }

  public void updateBlinkPeriod(double lateralError) {
    // Clamp lateralError to be at least MIN_TOLERANCE_BLINK.
    if (lateralError <= MIN_TOLERANCE_BLINK) {
      currentBlinkPeriod = MIN_BLINK_PERIOD;
    } else if (lateralError >= MAX_TOLERANCE_BLINK) {
      currentBlinkPeriod = MAX_BLINK_PERIOD;
    } else {
      double fraction =
          (lateralError - MIN_TOLERANCE_BLINK) / (MAX_TOLERANCE_BLINK - MIN_TOLERANCE_BLINK);
      currentBlinkPeriod = MIN_BLINK_PERIOD + fraction * (MAX_BLINK_PERIOD - MIN_BLINK_PERIOD);
    }
  }

  public void dynamicBlink() {
    double timeStamp = Timer.getFPGATimestamp();

    // Initialize timing if necessary.
    if (blinkTime == 0.0) {
      blinkTime = timeStamp;
      lastTime = timeStamp;
    }

    // Check if it's time to toggle based on currentBlinkPeriod.
    if (timeStamp - lastTime >= currentBlinkPeriod) {
      on = !on;
      lastTime = timeStamp;

      // Show green when "on" and black when "off"
      if (on) {
        setActiveBuffer(wlGreenLEDBuffer);
      } else {
        setActiveBuffer(wlBlackLEDBuffer);
      }
    }
  }

  private LED_EFFECTS updateLightingGoal() {


    if (DriverStation.isDisabled()) {
      return LED_EFFECTS.DARK_BLUE;
    }

    return LED_EFFECTS.BLUEOMBRE;
  }

  private void doRsl() {
    setActiveBuffer(wlOrangeLEDBuffer);
  }
}
