package edu.rit.csci759.rspi.exercise;

public interface RpiGetStatusInterface {
	static final int AMBIENT_DARK=35;
	static final int AMBIENT_BRIGHT=80;
	
	/*
	 * Constant thresholds for temperature in F
	 */
	static final int TEMPERATURE_COLD=60;
	static final int TEMPERATURE_HOT=76;
	
	/*
	 * function to turn off all LEDs
	 */
	void led_all_off();
	
	/*
	 * function to turn on all LEDs
	 */
	void led_all_on();
	
	/*
	 * Turn on a LED to indicate the value is low 
	 */
	/*
	 * function to indicate error; normally blnking red LED
	 */
	void led_error(int blink_count) throws InterruptedException;
	
	void led_when_low();
	
	/*
	 * Turn on a LED to indicate the value is mid 
	 */
	void led_when_mid();
	
	/*
	 * Turn on a LED to indicate the value is high 
	 */
	void led_when_high();
	/*
	 * Read the light intensity
	 */
	int read_ambient_light_intensity();
	
	/*
	 * Read the temperature
	 */
	float read_temperature();
	
	/*
	 * Get blind status of LEDs
	 */
	
	String get_Status();
	
	
	
}
