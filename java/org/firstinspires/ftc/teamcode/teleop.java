/*
Copyright 2020 Tech Challenge Team #11452 - Koala Robotics

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp
public class KoalaTeleop extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor sucking_l, // Left and right block-pushing motors respectivly
                    sucking_r;
    private DcMotor moving_lf, // Moving motors
                    moving_lr,
                    moving_rf,
                    moving_rr;
    private DcMotor panning; // Center - panning motor
    private DcMotor lift; // Lift motor
    private Servo liftRotate;
    private Servo liftGrabber;
    private TouchSensor touchSensor;  
    private static double suckingPower = 0.5;
    private boolean servoToggleX = false;
    private boolean GamepadOneXPressed  = false;
    private boolean servoToggleB = false;
    private boolean GamepadOneBPressed  = false;
    int pressBcount = 0;
    int pressXcount = 0;

    
    //lift collection position
    int liftCposition = 1000;
    
    @Override
    public void runOpMode() {
        // Initialize connections to the hardware
        imu = hardwareMap.get(Gyroscope.class, "imu");
        sucking_l = hardwareMap.get(DcMotor.class, "sucking_l");
        sucking_r = hardwareMap.get(DcMotor.class, "sucking_r");
        moving_lf = hardwareMap.get(DcMotor.class, "moving_lf");
        moving_lr = hardwareMap.get(DcMotor.class, "moving_lr");
        moving_rf = hardwareMap.get(DcMotor.class, "moving_rf");
        moving_rr = hardwareMap.get(DcMotor.class, "moving_rr");
        panning = hardwareMap.get(DcMotor.class, "panning");
        lift = hardwareMap.get(DcMotor.class, "lift");
        liftRotate = hardwareMap.get(Servo.class, "liftRotate");
        liftGrabber = hardwareMap.get(Servo.class, "liftGrabber");
        touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");
        
        // Set digital channel to input mode.
        liftGrabber = hardwareMap.get(Servo.class, "liftGrabber");

        moving_lf.setDirection(DcMotor.Direction.REVERSE);
        moving_lr.setDirection(DcMotor.Direction.REVERSE);
        panning.setDirection(DcMotor.Direction.REVERSE);
        sucking_r.setDirection(DcMotor.Direction.REVERSE);

        // Reset lift encoders
        lift.setMode(DcMotor.RunMode.RESET_ENCODERS);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        liftGrabber.setPosition(0.6);
        liftRotate.setPosition(0.675);
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.addData("pressBcount", "pressBcount");
        // lift.setTargetPosition(500);
        // lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.addData("liftPos", lift.getCurrentPosition());
            //telemetry.addData("liftExPos", liftExPos);
            telemetry.addData("liftRotate", liftRotate.getPosition());
            telemetry.addData("liftGrabber", liftGrabber.getPosition());
            telemetry.addData("touchSensor", touchSensor.isPressed());
            telemetry.update();
            
            
            // Calculate the power needed for each motor
            float moving_lf_power = gamepad1.left_stick_y - gamepad1.right_trigger + gamepad1.left_trigger;
            float moving_lr_power = gamepad1.left_stick_y - gamepad1.right_trigger + gamepad1.left_trigger;
            float moving_rf_power = gamepad1.left_stick_y + gamepad1.right_trigger - gamepad1.left_trigger;
            float moving_rr_power = gamepad1.left_stick_y + gamepad1.right_trigger - gamepad1.left_trigger;
            float panning_power = gamepad1.left_stick_x;
            
            // Move the robot using the left stick 
            moving_lf.setPower(moving_lf_power);
            moving_rr.setPower(moving_rr_power);
            moving_rf.setPower(moving_rf_power);
            moving_lr.setPower(moving_lr_power);
            panning.setPower(panning_power);
            
            // Control the sucking motors
            if (gamepad1.y) {
                sucking_l.setPower(suckingPower);
                sucking_r.setPower(suckingPower);
            } else if (gamepad1.a) { 
                sucking_l.setPower(-suckingPower);
                sucking_r.setPower(-suckingPower);
            } else { 
                sucking_l.setPower(0.f);
                sucking_r.setPower(0.f);
            }
            
            
            // Control the lift
            if (gamepad1.dpad_up && lift.getCurrentPosition() < 5000) {
                lift.setPower(1);
            } else if (gamepad1.dpad_down && !touchSensor.isPressed()) { 
                lift.setPower(-1);
            } else {
                lift.setPower(0);
            }
            
            if (gamepad1.x && !GamepadOneXPressed){
                GamepadOneXPressed = true;
                if (servoToggleX) {
                    servoToggleX = false;
                    liftRotate.setPosition(0.675);
                } else {
                    servoToggleX = true;
                    liftRotate.setPosition(0.55);
                }
            } else if (!gamepad1.x && GamepadOneXPressed) {
                GamepadOneXPressed = false;
            }
            
            if (gamepad1.b && !GamepadOneBPressed)  {
                GamepadOneBPressed = true;
                if (servoToggleB) {
                    servoToggleB = false;
                    liftGrabber.setPosition(0.5);
                } else {
                    servoToggleB = true;
                    liftGrabber.setPosition(0.6);
                }
            } else if (!gamepad1.b && GamepadOneBPressed) {
                GamepadOneBPressed = false;
            }
            
            if (touchSensor.isPressed()) {
                lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                lift.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
            }
            
            if (gamepad1.left_bumper) {
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift.setTargetPosition(1250);
                while(lift.getCurrentPosition() > 1250){
                    lift.setPower(0.5);
                }while(lift.getCurrentPosition() < 1250){
                    lift.setPower(-0.5);
                }
                lift.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

            }
            idle();
        }
    }
    
    private void runLiftToPos(int pos) {
        int dist = lift.getCurrentPosition() - pos;
        float pow = 0;
        pow = (Math.abs(dist) > 75)?0.3f:pow;
        pow = (Math.abs(dist) > 400)?1.f:pow;
        
        telemetry.addData("dist", dist);
        if (dist > 0)
            lift.setPower(-pow);
        else
            lift.setPower(pow);
    }
}
    
