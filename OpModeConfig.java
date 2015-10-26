package com.qualcomm.ftcrobotcontroller.myPackageName;
// change myPackageName to wherever you put this file.  change it to opmodes if you put it in the generic opmodes folder.

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class OpModeConfig extends OpMode {

  private enum Config {
    TEST_GAMEPAD1,
    TEST_GAMEPAD2,
    COLOR,
    DELAY,
    AUTON_TYPE,
    READY;
    private static Config[] vals = values();
    public Config next() { return vals[(this.ordinal()+1) % vals.length];}
    public Config prev() { return vals[(this.ordinal()-1+vals.length) % vals.length];}
  }

  private enum AutonType {
    GO_FOR_BEACON,
    GO_FOR_MOUNTAIN;
    private static AutonType[] vals = values();
    public AutonType next() { return vals[(this.ordinal()+1) % vals.length];}
    public AutonType prev() { return vals[(this.ordinal()-1+vals.length) % vals.length];}
  }

  // the parameters that need to be setup during configuration
  boolean gamepad1IsOK, gamepad2IsOK;
  boolean colorIsRed;
  int delayInSec;
  AutonType autonType;

  // variables used during the configuration process
  Context context;
  Config configState, currConfigCheck;
  boolean back1, a1, b2, y1, start1;
  boolean lastBack1, lastA1, lastB2, lastY1, lastStart1;
  private String configFileName="FtcRobotConfig.txt";

  @Override
  public void init() {
    // setup initial configuration parameters here
    gamepad1IsOK=false;
    gamepad2IsOK=false;
    context=hardwareMap.appContext;

    // read configuration data from file
    try {
      InputStream inputStream = context.openFileInput(configFileName);

      if ( inputStream != null ) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        colorIsRed = Boolean.valueOf(bufferedReader.readLine());
        delayInSec = Integer.valueOf(bufferedReader.readLine());
        String autonTypeString = bufferedReader.readLine();
        for (AutonType a : AutonType.values()) {
          if (a.name().equals(autonTypeString)) {
            autonType = a;
          }
        }

        inputStream.close();
      }
    }
    catch (Exception e) {
      telemetry.addData("Exception", "Error reading config file: " + e.toString());
      // can't read from file, so initialize to reasonable values
      colorIsRed=true;
      delayInSec=0;
      autonType=AutonType.GO_FOR_BEACON;
    }

    // setup initial toggle memory states for buttons used
    lastBack1=false; lastA1=false; lastB2=false; lastY1=false; lastStart1=false;
    configState=Config.TEST_GAMEPAD1;
  }

  @Override
  public void init_loop() {
    // read the gamepad state
    back1 = gamepad1.back || gamepad1.left_bumper || gamepad1.right_bumper;
    a1 = gamepad1.a;
    b2 = gamepad2.b;
    y1 = gamepad1.y;
    start1 = gamepad1.start;

    telemetry.clearData();

    currConfigCheck = Config.TEST_GAMEPAD1;
    // message to driver about state of this config parameter
    if (configState.ordinal() >= currConfigCheck.ordinal()) {
      if (!gamepad1IsOK) {
        telemetry.addData("C" + currConfigCheck.ordinal(), "GAMEPAD1 NOT VERIFIED!!!");
      }
    }
    // configure this parameter
    if (configState == currConfigCheck) {
      if (!gamepad1IsOK) {
        telemetry.addData("C" + currConfigCheck.ordinal() + "A", "Push A on Gamepad 1");
      }
      if (a1) {
        gamepad1IsOK = true;
      }
    }

    currConfigCheck = Config.TEST_GAMEPAD2;
    // message to driver about state of this config parameter
    if (configState.ordinal() >= currConfigCheck.ordinal()) {
      if (!gamepad2IsOK) {
        telemetry.addData("C" + currConfigCheck.ordinal(), "GAMEPAD2 NOT VERIFIED!!!");
      }
    }
    // configure this parameter
    if (configState == currConfigCheck) {
      if (!gamepad2IsOK) {
        telemetry.addData("C" + currConfigCheck.ordinal() + "A", "Push B on Gamepad 2");
      }
      if (b2) {
        gamepad2IsOK = true;
      }
    }

    currConfigCheck = Config.COLOR;
    // message to driver about state of this config parameter
    if (configState.ordinal() >= currConfigCheck.ordinal()) {
      if (colorIsRed) {
        telemetry.addData("C" + currConfigCheck.ordinal(), "Color: Red");
      } else {
        telemetry.addData("C" + currConfigCheck.ordinal(), "Color: Blue");
      }
    }
    // configure this parameter
    if (configState == currConfigCheck) {
      telemetry.addData("C" + currConfigCheck.ordinal() + "A", "Push B for Red, X for Blue");
      if (gamepad1.x) {
        colorIsRed = false;
      }
      if (gamepad1.b) {
        colorIsRed = true;
      }
    }

    currConfigCheck = Config.DELAY;
    // message to driver about state of this config parameter
    if (configState.ordinal() >= currConfigCheck.ordinal()) {
      telemetry.addData("C" + currConfigCheck.ordinal(), "Delay: " + delayInSec + " sec");
    }
    // configure this parameter
    if (configState == currConfigCheck) {
      telemetry.addData("C" + configState.ordinal() + "A", "Push Y for +, A for -");
      if (y1 && !lastY1) {
        delayInSec++;
      }
      if (a1 && !lastA1) {
        delayInSec--;
        if (delayInSec < 0) {
          delayInSec = 0;
        }
      }
    }

    currConfigCheck = Config.AUTON_TYPE;
    // message to driver about state of this config parameter
    if (configState.ordinal() >= currConfigCheck.ordinal()) {
      telemetry.addData("C" + currConfigCheck.ordinal(), "Auton: " + autonType.name());
    }
    // configure this parameter
    if (configState == currConfigCheck) {
      telemetry.addData("C" + configState.ordinal() + "A", "Push Y for +, A for -");
      if (y1 && !lastY1) {
        autonType = autonType.next();
      }
      if (a1 && !lastA1) {
        autonType = autonType.prev();
      }
    }

    currConfigCheck = Config.READY;
    // message to driver about state of this config parameter
    if (configState.ordinal() >= currConfigCheck.ordinal() ) {
      telemetry.addData("C" + currConfigCheck.ordinal(), "READY TO GO!");

      // may want to write configuration parameters to a file here if they are needed for teleop too!
      try {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(configFileName, Context.MODE_PRIVATE));

        // write each configuration parameter as a string on its own line
        outputStreamWriter.write(Boolean.toString(colorIsRed)+"\n");
        outputStreamWriter.write(Integer.toString(delayInSec)+"\n");
        outputStreamWriter.write(autonType.name()+"\n");

        outputStreamWriter.close();
      }
      catch (IOException e) {
        telemetry.addData("Exception", "Configuration file write failed: " + e.toString());
      }

    }

    if (configState!=Config.READY) {
      telemetry.addData("D" + configState.ordinal(), "Push Start for next option");
    }
    telemetry.addData("E" + configState.ordinal(), "Push Back or a Bumper to go back");

    if (start1 && !lastStart1 && (configState.ordinal() < Config.READY.ordinal())) {
      configState = configState.next();
    }

    if (back1 && !lastBack1 && (configState.ordinal() >0) ) {
      configState = configState.prev();
    }

    // update toggle memory for next call
    lastBack1=back1;
    lastA1=a1;
    lastB2=b2;
    lastY1=y1;
    lastStart1=start1;
  }

  @Override
  public void loop() {
    telemetry.clearData();
    // can use configured variables here
  }

}
