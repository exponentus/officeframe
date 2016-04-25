package reference.model.embedded;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.exponentus.scripting._Session;

import kz.flabs.util.Util;
import reference.dao.CountryDAO;
import reference.dao.LocalityDAO;
import reference.dao.RegionDAO;
import reference.model.CityDistrict;
import reference.model.Country;
import reference.model.Locality;
import reference.model.Region;
import reference.model.Street;
import reference.model.constants.CountryCode;

@Embeddable
public class Address {

	private Country country;

	private Region region;

	private CityDistrict cityDistrict;

	private Locality locality;

	private Street street;

	@Column(name = "house_number", length = 10)
	private String houseNumber;

	private String coordinates = "";

	@Column(name = "additional_info")
	private String additionalInfo = "";

	public Locality getLocality() {
		return locality;
	}

	public void setLocality(Locality l) {
		this.locality = l;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String c) {
		this.coordinates = c;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String toString() {
		return Util.toStringGettersVal(this);
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setRegion(String r) {
		region = new Region();

	}

	public CityDistrict getDistrict() {
		return cityDistrict;
	}

	public void setCityDistrict(CityDistrict district) {
		this.cityDistrict = district;
	}

	public static Address getStub(_Session session) {
		Address addr = new Address();
		CountryDAO cDao = new CountryDAO(session);
		Country country = cDao.findByCode(CountryCode.KZ);
		addr.setCountry(country);
		RegionDAO rDao = new RegionDAO(session);
		Region region = rDao.findByName("Алматы");
		addr.setRegion(region);
		CityDistrict cityDistrict = new CityDistrict();
		cityDistrict.setName("");
		addr.setCityDistrict(cityDistrict);
		LocalityDAO lDao = new LocalityDAO(session);
		Locality city = lDao.findByName("Алматы");
		addr.setLocality(city);
		Street street = new Street();
		street.setName("");
		addr.setStreet(street);
		addr.setHouseNumber("");
		addr.setAdditionalInfo("");
		return addr;

	}

}
