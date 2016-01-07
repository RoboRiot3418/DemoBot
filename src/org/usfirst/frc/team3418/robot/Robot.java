// THis is the awesome comment made by josh
package org.usfirst.frc.team3418.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
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
	
	Joystick stick = new Joystick (0);
	double x = 0;
	double y = 0;
	double r = 0;
	double dzc=.2;//this is our deadzone constant
	public double deadzone(double original){
		if ((dzc*-1)<original && original<dzc){
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
    	
    	x=deadzone(stick.getRawAxis(4))*-1;
    	y=deadzone(stick.getRawAxis(5));
    	r=deadzone(stick.getRawAxis(0))*.6;
    	System.out.println("X="+x+" Y="+y+" R="+r);
    	
    	
    	/*
    	 * this is a comment made by josh
    	 * 
_____.___.________  .____    ________    ___________      __  _____    ________ 
\__  |   |\_____  \ |    |   \_____  \  /   _____/  \    /  \/  _  \  /  _____/ 
 /   |   | /   |   \|    |    /   |   \ \_____  \\   \/\/   /  /_\  \/   \  ___ 
 \____   |/    |    \    |___/    |    \/        \\        /    |    \    \_\  \
 / ______|\_______  /_______ \_______  /_______  / \__/\  /\____|__  /\______  /
 \/               \/        \/       \/        \/       \/         \/        \/ 
    	 */
    	/*
    	 * wjskadjflksjflkjldjfaslkjfalj
    	 * By josh
    	 */
    	motor1.set((-1/2*x) - (Math.sqrt(3)/2*y) + r);
    	motor2.set((-1/2*x) + (Math.sqrt(3)/2*y) + r);
    	motor3.set(x+r);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
