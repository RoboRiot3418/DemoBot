//Don't read this
package org.usfirst.frc.team3418.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import java.lang.Math;
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
	Victor motor1 = new Victor (0);
	Victor motor2 = new Victor (1);
	Victor motor3 = new Victor (2);
	double m1=0;
	double m2=0;
	double m3=0;
	Joystick stick = new Joystick (0);
	double x = 0;
	double y = 0;
	double r = 0;
	double dzc =.2; // dead zone constant
	
	public double deadzone(double original){
		if (original>(dzc*-1) && original<dzc){
			return 0;
		}
		return original;
	}
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
    	
    	x=deadzone(stick.getRawAxis(4))*-1;//this reverses the input
    	y=deadzone(stick.getRawAxis(5));
    	r=deadzone(stick.getRawAxis(0))*.6;//this slows rotation
    	
    	// these formulas mostly work
    	m1=((-.5)*x) - ((.866)*y) + r;
    	m2=((-.5)*x) + ((.866)*y) + r;
    	m3=x+r;
    	System.out.println(m1+"     "+m2+"    "+m3+"    "+x+"   "+y);
    	motor1.set(m1);
    	motor2.set(m2);
    	motor3.set(m3);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
