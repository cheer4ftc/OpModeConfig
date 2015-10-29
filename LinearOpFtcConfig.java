package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// DOES NOT WORK AS OF 10/26/15 DUE TO A BUG IN THE SDK REPORTED HERE
// http://ftcforum.usfirst.org/showthread.php?4576-Game-controller-input-during-init_loop()&p=18496&viewfull=1#post18496
// SHOULD WORK(?) WHEN THE BUG IS FIXED IN THE NEXT RELEASE!!!!!

public class LinearOpFtcConfig extends LinearOpMode {

  @Override
  public void runOpMode() throws InterruptedException {

    FtcConfig ftcConfig=new FtcConfig();

    ftcConfig.init(hardwareMap.appContext, this);
    // waitOneFullHardwareCycle();
    sleep(50); // temporary fix to waitXXXHardwareCycle bug?

    // init_loop type functionality here
    while (!opModeIsActive() ) {
      ftcConfig.init_loop(hardwareMap.appContext, this);
      // waitOneFullHardwareCycle();
      sleep(50); // temporary fix to waitXXXHardwareCycle bug?
    }
    // done with configuration

    // OpMode is active so get going!!
    // waitForStart not needed since we already checked that OpMode is active!
    // waitForStart();


    // run the opmode, using the configured variables here
    // can do state machine style or linear style
    while (opModeIsActive()) {
      telemetry.clearData();
      telemetry.addData("A","Running the LinearOpMode now");
      telemetry.addData("ColorIsRed", Boolean.toString(ftcConfig.param.colorIsRed));
      telemetry.addData("DelayInSec", Integer.toString(ftcConfig.param.delayInSec));
      telemetry.addData("AutonType", ftcConfig.param.autonType);
      waitOneFullHardwareCycle();
    }

    telemetry.clearData();
    telemetry.addData("A","LinearOpMode no longer running");

  }
}
