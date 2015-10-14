# OpModeConfig
Shows how pre-match OpMode configuration can be done using the gamepads with telemetry messages.

The general idea is that you want to step through different configuration options. You can always go to the next option by pressing Start or go back one step by pressing Back or a bumper.
In the example code, the configuration steps are listed in the Config enum. They are:

TEST_GAMEPAD1, checks that gamepad 1 is working by requesting driver 1 to press the A button. This will prevent matches from starting without active gamepads (e.g., if Start A hasn't been pressed).

TEST_GAMEPAD2. The same for gamepad 2.

COLOR. Lets the driver select the color of the alliance: red or blue. For example, you might need that if you're going to drive on the mountain in autonomous as for red you need to turn left and blue you need to turn right. Or you might use this when trying to push the rescue beacon button corresponding to your alliance color.

DELAY. Let's you enter a number of seconds to delay the start of your code, in case your alliance partner needs to run something first to get out of your way before you start your routine.

AUTON_TYPE. As an example, the code pretends that there might be 2 things you might try to do: go for the beacon or go for the mountain. This lets you select which one you might try to do. You can add as many different options as you might want to have in one OpMode.

READY. All configuration is done.

As you step through these options, the current configuration is shown via telemetry messages back on the driver station. The best way to understand how it works and what it's doing is just to run it on a RobotController/ DriverStation pair and see how it works. Then you can look through the code to understand the functionality.

You can add additional configuration inputs by copying and editing the example parameters shown in the code: color is a boolean, delay is an integer, and autonomousType is an enumerated type. The code blocks that follow the line currConfigCheck=XXX (like currConfigCheck=Config.TEST_GAMEPAD1) show what messages are sent using telemetry and how the input is used to configure the parameter what that step of configuration is active.
