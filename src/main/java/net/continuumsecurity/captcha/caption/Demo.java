// Copyright (C) 2009

package net.continuumsecurity.captcha.caption;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.imageio.ImageIO;


/**
 * CAPTCHA bypass Demo
 * @author 
 *
 */
public class Demo {
	/**
	 * The entry point 
	 * @param args arguments
	 */
	public static void main(String[] args) {				
		System.out.println("Loading CAPTCHA (Captcha.jpg)...");
		Image captcha;
		try {
			captcha = ImageIO.read(new File("Captcha.jpg"));
		} catch (IOException ex) {
			System.err.println(String.format("Cannot load CAPTCHA: %s", ex.getMessage()));
			return;
		}
		
		System.out.println("Initializing CAPTCHA solver...");
		CaptchaSolver solver;
		try {
            solver = new CaptchaSolver();
            solver.initialize();
            System.out.println("CAPTCHA solver initialized OK");
        }
        catch (Exception ex) {
            System.err.println(String.format("Cannot initialize CAPTCHA solver: %s", ex.getMessage()));
            return;
        }
		
        System.out.println("Getting balance...");
        try {
        	System.out.println(String.format("Your balance: %d CAPTCHAs", solver.getBalance()));
        } catch (Exception ex) {
        	ex.printStackTrace();
        	solver.dispose();
            System.err.println(String.format("Error while getting balance: %s", ex.getMessage()));
            return;
        }
        
        System.out.println("Solving CAPTCHA...");
        SolveResult result = null;
        try {
        	long dateStart = Calendar.getInstance().getTimeInMillis();
        	DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        	df.setTimeZone(TimeZone.getTimeZone("GMT"));

        	
        	result = solver.solveCaptcha(new FileInputStream("Captcha.jpg"));
        	long difference = Calendar.getInstance().getTimeInMillis() - dateStart;
        	
        	System.out.println("CAPTCHA solved OK");
        	System.out.println(String.format("CAPTCHA solve time: %s", df.format(difference)));
            System.out.println(String.format("Result: %s", result.getResult()));
            System.out.println(String.format("Captcha ID generated by the server: %s", result.getCaptchaId()));
            System.out.println(String.format("Server software version: %s", result.getServerSoftwareVersion()));
        } catch (Exception ex) {
        	solver.dispose();
            System.err.println(String.format("Error while solving CAPTCHA: %s", ex.getMessage()));
            return;
        }
        
        //// Please call only in CAPTCHA is recognized incorrectly to avoid ban
        //System.out.println("Marking as ERROR...");
        //try {
        //	solver.markAsError(result.getCaptchaId());
        //	System.out.println("CAPTCHA marked as ERROR OK");
        //} catch (Exception ex) {
        //	solver.dispose();
        //    System.err.println(String.format("Error make CAPTCHA as error: %s", ex.getMessage()));
        //    return;
        //}
        
        System.out.println("Getting balance...");
        try {
        	System.out.println(String.format("Your balance: %d CAPTCHAs", solver.getBalance()));
        } catch (Exception ex) {
        	solver.dispose();
            System.err.println(String.format("Error while getting balance: %s", ex.getMessage()));
            return;
        }

        solver.dispose();
		System.out.println("Done.");
	}
}