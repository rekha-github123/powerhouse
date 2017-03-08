/**
 * 
 */
package powerhouse.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rekha
 *
 */
public class CalculationHelper {

	public Map<String,Integer> calculateMonthlyConsumption(Map<String,Integer> monthlyReading){
		Map<String,Integer> monthlyConsumption = new HashMap<String,Integer>();
		Map.Entry<String,Integer> prev = null;
		for(Map.Entry<String,Integer> entry : monthlyReading.entrySet()){
			if(entry.getKey() == AppConstants.monJan){
				prev = entry;
			}
			if(prev != null && entry.getValue() < prev.getValue()){
				int consumption = entry.getValue()-prev.getValue();
				monthlyConsumption.put(entry.getKey(), consumption);
			}
			prev = entry;
		}
		return monthlyConsumption;
		
	}
}
