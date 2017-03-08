package powerhouse.dao;

import powerhouse.model.MeterReading;
import powerhouse.model.Profile;

public interface IPowerhouseDao {

	public void insertProfile(Profile profile);
	public Profile getProfile(String profileName);
	public void insertMeterReading(MeterReading meter);
	public MeterReading getMeterReading(int meterId);
		
}
