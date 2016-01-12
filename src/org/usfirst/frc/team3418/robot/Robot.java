
package org.usfirst.frc.team3418.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.Math;
import java.util.Comparator;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ParticleReport;
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
	double dzc =.2; // dead zone constant
	
	public double deadzone(double original){
		if (original>(dzc*-1) && original<dzc){
			return 0;
		}
		return original;
	}
	
	public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>{
		double PercentAreaToImageArea;
		double Area;
		double ConvexHullArea;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;
		
		public int compareTo(ParticleReport r)
		{
			return (int)(r.Area - this.Area);
		}
		
		public int compare(ParticleReport r1, ParticleReport r2)
		{
			return (int)(r1.Area - r2.Area);
		}
	};
	
	
	
	Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_HSL , 0);
	Image binary = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8 , 0);
	NIVision.Range TOTE_HUE_RANGE = new NIVision.Range(24, 49);	//Default hue range for yellow tote
	NIVision.Range TOTE_SAT_RANGE = new NIVision.Range(67, 255);	//Default saturation range for yellow tote
	NIVision.Range TOTE_VAL_RANGE = new NIVision.Range(49, 255);
	
	double AREA_MINIMUM = 0.5; //Default Area minimum for particle as a percentage of total image area
	double LONG_RATIO = 2.22; //Tote long side = 26.9 / Tote height = 12.1 = 2.22
	
	double SCORE_MIN = 75.0;  //Minimum score to be considered a tote
	double VIEW_ANGLE = 49.4;
	int imaqError;
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
	
	int session= NIVision.IMAQdxOpenCamera("cam0",NIVision.IMAQdxCameraControlMode.CameraControlModeController);
    
	double computeDistance (Image image, ParticleReport report, boolean isLong) {
		double normalizedWidth, targetWidth;
		NIVision.GetImageSizeResult size;

		size = NIVision.imaqGetImageSize(image);
		normalizedWidth = 2*(report.BoundingRectRight- report.BoundingRectLeft )/size.width;
		targetWidth = isLong ? 26.0 : 16.9;

		return  targetWidth/(normalizedWidth*12*Math.tan(VIEW_ANGLE*Math.PI/(180*2)));
	}
	
	
	
	
    public void robotInit(){
    	NIVision.IMAQdxConfigureGrab(session);
    	criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);
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
    	motor1.set((-.5*x) - ((Math.sqrt(3)/2)*y) + r);
    	motor2.set((-.5*x) + ((Math.sqrt(3)/2)*y) + r);
    	motor3.set(x+r);
    	
    	
    	NIVision.IMAQdxGrab(session, frame, 1);
    	TOTE_HUE_RANGE.minValue = (int)SmartDashboard.getNumber("Tote hue min", TOTE_HUE_RANGE.minValue);
		TOTE_HUE_RANGE.maxValue = (int)SmartDashboard.getNumber("Tote hue max", TOTE_HUE_RANGE.maxValue);
		TOTE_SAT_RANGE.minValue = (int)SmartDashboard.getNumber("Tote sat min", TOTE_SAT_RANGE.minValue);
		TOTE_SAT_RANGE.maxValue = (int)SmartDashboard.getNumber("Tote sat max", TOTE_SAT_RANGE.maxValue);
		TOTE_VAL_RANGE.minValue = (int)SmartDashboard.getNumber("Tote val min", TOTE_VAL_RANGE.minValue);
		TOTE_VAL_RANGE.maxValue = (int)SmartDashboard.getNumber("Tote val max", TOTE_VAL_RANGE.maxValue);
    	
    	
    	NIVision.imaqColorThreshold(binary, frame, 255,NIVision.ColorMode.HSV,TOTE_HUE_RANGE,TOTE_SAT_RANGE,TOTE_VAL_RANGE );
    	CameraServer.getInstance().setImage(frame);
    	CameraServer.getInstance().setImage(binary);
    	//particle junk
    	int numParticles = NIVision.imaqCountParticles(binary, 1);
		SmartDashboard.putNumber("Masked particles", numParticles);
		
		//filter out small particles
		float areaMin = (float)SmartDashboard.getNumber("Area min %", AREA_MINIMUM);
		criteria[0].lower = areaMin;
		imaqError = NIVision.imaqParticleFilter4(binary, binary, criteria, filterOptions, null);

		//Send particle count after filtering to dashboard
		numParticles = NIVision.imaqCountParticles(binary, 1);
		SmartDashboard.putNumber("Filtered particles", numParticles);
		
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
