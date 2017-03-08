package powerhouse.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import powerhouse.model.MeterReading;
import powerhouse.model.Profile;
import powerhouse.utility.CalculationHelper;
import powerhouse.utility.Months;
import powerhouse.utility.ValidationUtil;

@Repository
public class PowerhouseDaoImpl implements IPowerhouseDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	private Map<String, Map<String, Double>> profileMonthFractionMap = new HashMap<String, Map<String, Double>>();

	private static final String GET_METER_READING = "select * from METER_READING where METER_ID=:meterId";
	private static final String INSERT_PROFILE = "insert into profile (PROFILE_NM, MONTH, FRACTION) values (:profile,:month,:fraction)";
	private static final String GET_PROFILE_BY_NAME = "select * from profile where PROFILE_NM = :profile";;
	private static final String INSERT_METER_READING = "insert into METER_READING (METER_ID,PROFILE_NM, MONTH, READING,CONSUMPTION) values (:meterId,:profile,:month,:reading,:consumption)";

	@Override
	public void insertProfile(Profile profile) {
		if (ValidationUtil.validateProfile(profile)) {

			List<Map<String, Object>> batchValues = new ArrayList<>(profile
					.getMonthFractionMap().size());
			for (Entry<String, Double> entry : profile.getMonthFractionMap()
					.entrySet()) {
				batchValues.add(new MapSqlParameterSource("profile", profile
						.getProfileName()).addValue("month", entry.getKey())
						.addValue("fraction", entry.getValue()).getValues());
			}

			int[] updateCounts = namedParamJdbcTemplate.batchUpdate(
					INSERT_PROFILE, batchValues.toArray(new Map[profile
							.getMonthFractionMap().size()]));
			System.out.println("rows inserted: " + updateCounts.length);
			if (updateCounts.length > 0) {
				profileMonthFractionMap.put(profile.getProfileName(),
						profile.getMonthFractionMap());
			}
		} else {
			System.out
					.println("the input profile is not valid, since total of all fraction is not 1");
		}

	}

	@Override
	public Profile getProfile(String profileName) {

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("profile", profileName);
		Map<String, Double> monthFractionMap = namedParamJdbcTemplate.query(
				GET_PROFILE_BY_NAME, paramMap,
				new ResultSetExtractor<Map<String, Double>>() {
					@Override
					public Map<String, Double> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						Map<String, Double> map = new HashMap<String, Double>(
								12);
						while (rs.next()) {
							map.put(rs.getString("month"),
									rs.getDouble("fraction"));
						}
						return map;
					}
				});
		Profile p = new Profile();
		if (monthFractionMap.size() > 0) {
			p.setProfileName(profileName);
			p.setMonthFractionMap(monthFractionMap);
		}
		return p;
	}

	@Override
	public void insertMeterReading(MeterReading meter) {
		boolean isValidReading = ValidationUtil.validateMonthlyReading(meter
				.getMonthlyReading());
		boolean isValidConsumption = ValidationUtil
				.validateMonthlyConsumptionWithFraction(meter
						.getMonthlyReading(), this.profileMonthFractionMap
						.get(meter.getProfileName()));
		if (isValidReading && isValidConsumption) {
			/** calculating and setting monthly consumption */
			Map<String, Integer> monthlyConsumption = new CalculationHelper()
					.calculateMonthlyConsumption(meter.getMonthlyReading());
			List<Map<String, Object>> batchValues = new ArrayList<>(meter
					.getMonthlyReading().size());
			for (Entry<String, Integer> entry : meter.getMonthlyReading()
					.entrySet()) {
				batchValues.add(new MapSqlParameterSource("meterId", meter
						.getMeterId())
						.addValue("profile", meter.getProfileName())
						.addValue("month", entry.getKey())
						.addValue("consumption",
								monthlyConsumption.get(entry.getKey()))
						.addValue("reading", entry.getValue()).getValues());
			}

			int[] updateCounts = namedParamJdbcTemplate.batchUpdate(
					INSERT_METER_READING, batchValues.toArray(new Map[meter
							.getMonthlyReading().size()]));
			System.out.println("rows inserted: " + updateCounts.length);
		} else {
			System.out.println("the input meter data is not valid");
		}
	}

	@Override
	public MeterReading getMeterReading(int meterId) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("meterId", meterId);
		MeterReading meter = new MeterReading();
		Map<String, Integer> monthlyReading = new LinkedHashMap<String, Integer>();
		Map<String, Integer> monthlyConsumption = new HashMap<String, Integer>();
		namedParamJdbcTemplate.query(GET_METER_READING, paramMap,
				new ResultSetExtractor<Map<String, Integer>>() {

					@Override
					public Map<String, Integer> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						while (rs.next()) {
							monthlyReading.put(
									rs.getString("MONTH"),
									rs.getInt("READING"));
							monthlyConsumption.put(
									rs.getString("MONTH"),
									rs.getInt("CONSUMPTION"));
							meter.setProfileName(rs.getString("PROFILE_NM"));
						}
						return null;
					}
				});
		meter.setMeterId(meterId);
		meter.setMonthlyReading(monthlyReading);
		meter.setMonthlyConsumption(monthlyConsumption);

		return meter;
	}

}
