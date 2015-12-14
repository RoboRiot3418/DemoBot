
package org.usfirst.frc.team3418.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import java.lang.Math
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	Talon motor1 = new TAlon (0);
	Talon motor2 = new Talon (1);
	Talon motor3 = new Talon (2);
	
	Joystick stick = new Joystick (3);
	double x = 0;
	double y = 0;
	double r = 0;
    public void robotInit(){

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	x=stick.getRawAxis(0);
    	y=stick.getRawAxis(1);
    	r=stick.getRawAxis(2);
    	motor1.set((-1/2*x) - (Math.sqrt(3)/2*y) + r);
    	motor2.set((-1/2*x) + (Math.sqrt(3)/2*y) + r);
    	motor3.set((-1/2*x) + (Math.sqrt(3)/2*y) + r);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
