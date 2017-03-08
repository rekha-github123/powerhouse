/**
 * 
 */
package powerhouse.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import powerhouse.utility.Months;

/**
 * @author rekha
 *
 */
public class MeterReading {

	private int meterId;
	private String profileName;
	private Map<String, Integer> monthlyReading = new LinkedHashMap<String, Integer>(
			12);
	private Map<String, Integer> monthlyConsumption = new HashMap<String, Integer>(
			12);

	public int getMeterId() {
		return meterId;
	}

	public String getProfileName() {
		return profileName;
	}

	public Map<String, Integer> getMonthlyReading() {
		return monthlyReading;
	}

	public Map<String, Integer> getMonthlyConsumption() {
		return monthlyConsumption;
	}

	public void setMeterId(int meterId) {
		this.meterId = meterId;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setMonthlyReading(Map<String, Integer> monthlyReading) {
		this.monthlyReading = monthlyReading;
	}

	public void setMonthlyConsumption(Map<String, Integer> monthlyConsumption) {
		this.monthlyConsumption = monthlyConsumption;
	}

	@Override
	public String toString() {
		return "MeterReading [meterId=" + meterId + ", profileName="
				+ profileName + ", MonthlyReading=" + monthlyReading
				+ ", MonthlyConsumption=" + monthlyConsumption + "]";
	}

}
