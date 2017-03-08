package powerhouse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import powerhouse.dao.IPowerhouseDao;
import powerhouse.model.MeterReading;
import powerhouse.model.Profile;

@Service
public class PowerhouseService {

	@Autowired
	private IPowerhouseDao powerhouseDao;
	
	public void createProfile(Profile profile){
		powerhouseDao.insertProfile(profile);
	}
	
	public Profile getProfile(String profileName){
		return powerhouseDao.getProfile(profileName);
	}
	
	public void createMeterReading(MeterReading meter){
		powerhouseDao.insertMeterReading(meter);
	}
	
	public MeterReading getMeterReading(int meterId){
		return powerhouseDao.getMeterReading(meterId);
	}
}
