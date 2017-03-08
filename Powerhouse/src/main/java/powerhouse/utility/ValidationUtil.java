/**
 * 
 */
package powerhouse.utility;

import java.util.Map;

import powerhouse.model.MeterReading;
import powerhouse.model.Profile;

/**
 * @author rekha
 *
 */
public class ValidationUtil {

	private static int totalYearlyReading = 0;
	/**
	 * @param profile
	 * @return true if profile is valid else false 
	 */
	public static boolean validateProfile(Profile profile){
		boolean isValidProfile = false;
		if (profile == null || profile.getMonthFractionMap() == null || profile.getMonthFractionMap().size() == 0)
			return false;
		Map<String,Double>  monthFractionMap =  profile.getMonthFractionMap();
		Double totalFractions = 0.0;
		for(Double fraction: monthFractionMap.values()){
			totalFractions = totalFractions+fraction;
		}
		if(totalFractions == 1)
			isValidProfile= true;
		return isValidProfile;
	}
	
	
	public static boolean validateMeterReading(MeterReading meter){
		boolean isValidMeterReading = false;
		if(validateMonthlyReading(meter.getMonthlyReading())){
			return false;
		}
		
		return isValidMeterReading;
	}
	
	/**Assuming the list has months details in sequence from Jan to Dec*/
	public static boolean validateMonthlyReading(Map<String,Integer> monthlyReading){
		boolean isValid = true;
		Map.Entry<String,Integer> prev = null;
		for(Map.Entry<String,Integer> entry : monthlyReading.entrySet()){
			if(entry.getKey() == AppConstants.monJan){
				prev = entry;
			}
			if(prev != null && entry.getValue() < prev.getValue()){
				isValid = false;
			}
			totalYearlyReading = totalYearlyReading+entry.getValue();
			prev = entry;
		}
		return isValid;
	}
	
	public static boolean validateMonthlyConsumptionWithFraction(
			Map<String, Integer> monthlyReading,
			Map<String, Double> monthlyFraction) {
		boolean isValid = true;
		if(monthlyFraction.isEmpty()){
			System.out.println("the fractions are not available for month reading");
			return false;
		}
		for (Map.Entry<String, Integer> entry : monthlyReading.entrySet()) {
			System.out.println("validating for month: "+entry.getKey()+" with reading: "+entry.getValue());
			if(!isValidReadingAsPerMonthFraction(monthlyFraction.get(entry.getKey()),entry.getValue())){
				isValid = false;
				break;
			}
		}
		return isValid;
	}
	
	
	private static boolean isValidReadingAsPerMonthFraction(Double monthlyFraction,int monthReading){
		boolean isValid = false;
		double monthPercentForFraction = (monthlyFraction*totalYearlyReading)/100;
		System.out.println("monthPercentForFraction: "+monthPercentForFraction);
		double range = (monthPercentForFraction*25)/100;
		double lowerRange= monthPercentForFraction - range;
		double upperRange = monthPercentForFraction+range;
		System.out.println("Lower range: "+lowerRange+" upper range: "+upperRange);
		if(monthReading >= lowerRange && monthReading <= upperRange){
			isValid = true;
		}
		return isValid;
	}
	
	
}
