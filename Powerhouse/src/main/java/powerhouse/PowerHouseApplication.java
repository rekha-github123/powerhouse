package powerhouse;

import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import org.





import powerhouse.model.MeterReading;
import powerhouse.model.Profile;
import powerhouse.services.PowerhouseService;
import powerhouse.utility.AppConstants;
import powerhouse.utility.Months;

/**
 * @author rekha
 * GET- http://localhost:9292/profile/A
 * PUT- http://localhost:9292/profile/A
 * 
 * PUT- http://localhost:9292/meter/001/A
 * GET- http://localhost:9292/meter/001
 */
@RestController
@SpringBootApplication
public class PowerHouseApplication {

	@Autowired
	private PowerhouseService powerService;

	
	@Bean
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public DataSource dataSource() {
		// no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.H2) 
			.addScript("schema.sql")
			//.addScript("data.sql")
			.build();
		return db;
	}

	/**
	 * Invoking Rest end point without Circuit Breaker configuration. If
	 * external server(provider) is down then it will throw 500 error.
	 * 
	 * @return String
	 */
	@RequestMapping(value="/profile/{profileName}",method=RequestMethod.GET)
	public Profile  getProfile(@PathVariable("profileName") String profileName) {
		return powerService.getProfile(profileName);
	}

	@RequestMapping(value="/profile/{profileName}",method=RequestMethod.PUT)
	public void  createProfile(@PathVariable("profileName") String profileName) {
		Profile profile = populateProfile(profileName);
		powerService.createProfile(profile);
	}
	
	@RequestMapping(value="/meter/{meterId}/{profileName}",method=RequestMethod.PUT)
	public void createMeterReading(@PathVariable("meterId") int meterId,@PathVariable("profileName") String profileName){
		MeterReading meterReading = populateMeterReading(meterId,profileName); 
		powerService.createMeterReading(meterReading);
	}
	
	@RequestMapping(value="/meter/{meterId}",method=RequestMethod.GET)
	public MeterReading getMeterReading(@PathVariable("meterId") int meterId){
		return powerService.getMeterReading(meterId);
	}
	
	private Profile populateProfile(String profileName) {
		Map<String,Double>  monthFractionMap =  new HashMap<String,Double>(12);
		monthFractionMap.put(AppConstants.monJan, 0.15);
		monthFractionMap.put(AppConstants.monFeb, 0.17);
		monthFractionMap.put(AppConstants.monMar, 0.13);
		monthFractionMap.put(AppConstants.monApr, 0.08);
		monthFractionMap.put(AppConstants.monMay, 0.08);
		monthFractionMap.put(AppConstants.monJun, 0.00);
		monthFractionMap.put(AppConstants.monJul, 0.00);
		monthFractionMap.put(AppConstants.monAug, 0.01);
		monthFractionMap.put(AppConstants.monSep, 0.04);
		monthFractionMap.put(AppConstants.monOct, 0.09);
		monthFractionMap.put(AppConstants.monNov, 0.10);
		monthFractionMap.put(AppConstants.monDec, 0.15);
		//monthFractionMap.put("DEC", 0.25);
		
		Profile profile = new Profile();
		profile.setProfileName(profileName); 
		profile.setMonthFractionMap(monthFractionMap);
		return profile;
	}
	
	private MeterReading populateMeterReading(int meterId, String profileName) {
		Map<String,Integer> monthlyReading = new LinkedHashMap<String,Integer>(12);
		monthlyReading.put(AppConstants.monJan, 36);
		monthlyReading.put(AppConstants.monFeb, 77);
		monthlyReading.put(AppConstants.monMar, 108);
		monthlyReading.put(AppConstants.monApr, 127);
		monthlyReading.put(AppConstants.monMay, 146);
		monthlyReading.put(AppConstants.monJun, 146);
		monthlyReading.put(AppConstants.monJul, 146);
		monthlyReading.put(AppConstants.monAug, 148);
		monthlyReading.put(AppConstants.monSep, 158);
		monthlyReading.put(AppConstants.monOct, 180);
		monthlyReading.put(AppConstants.monNov, 204);
		monthlyReading.put(AppConstants.monDec, 240);
		
		MeterReading meter = new MeterReading();
		meter.setMeterId(meterId);
		meter.setProfileName(profileName);
		meter.setMonthlyReading(monthlyReading);
		return meter;
	}

	/**
	 * Test method to check is application available.
	 * 
	 * @return String
	 */
	@RequestMapping("/test")
	public String test() {
		return "Application is up and running.";
	}

	
	public static void main(String[] args) {
		SpringApplication.run(PowerHouseApplication.class, args);
	}

}
