/**
 * 
 */
package powerhouse.model;

import java.util.HashMap;
import java.util.Map;

import powerhouse.utility.Months;

/**
 * @author rekha
 *
 */
public class Profile {

	private String profileName;
	private Map<String,Double>  MonthFractionMap =  new HashMap<String,Double>(12);
	
	public String getProfileName() {
		return profileName;
	}
	
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	
	
	public Map<String, Double> getMonthFractionMap() {
		return MonthFractionMap;
	}

	public void setMonthFractionMap(Map<String, Double> monthFractionMap) {
		MonthFractionMap = monthFractionMap;
	}

	@Override
	public String toString() {
		return "Profile [profileName=" + profileName + ", MonthsFractionMap="
				+ MonthFractionMap + "]";
	}
	
	
		
}
