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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class KoalaTestTeleop extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor sucking_l, // Left and right block-pushing motors respectivly
                    sucking_r;
    private DcMotor moving_lf, // Moving motors
                    moving_lr,
                    moving_rf,
                    moving_rr;
    private DcMotor panning; // Center - panning motor
    private DcMotor lift; // Lift motor
    
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

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();
            
            // Move the robot using the left stick 
            moving_lf.setPower(-gamepad1.left_stick_y);
            moving_lr.setPower(-gamepad1.left_stick_y);
            moving_rf.setPower(gamepad1.left_stick_y);
            moving_rr.setPower(gamepad1.left_stick_y);
            panning.setPower(-gamepad1.left_stick_x);
            
            /*
            if (gamepad1.a) {
                sucking_l.setPower(1.f);
                sucking_r.setPower(-1.f);
            } else { 
                sucking_l.setPower(-gamepad1.right_trigger);
                sucking_r.setPower(gamepad1.right_trigger);
            }*/

            idle();
        }
    }
}
