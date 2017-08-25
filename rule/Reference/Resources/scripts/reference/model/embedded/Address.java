package reference.model.embedded;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.dao.CountryDAO;
import reference.dao.LocalityDAO;
import reference.dao.RegionDAO;
import reference.model.*;
import reference.model.constants.CountryCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private Country country;

    private Region region;

    private CityDistrict cityDistrict;

    private Locality locality;

    private Street street;

    @Column(name = "house_number", length = 10)
    private String houseNumber;

    @Column(name = "flat", length = 10)
    private String flat;

    private String coordinates;

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

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
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
        return country + " " + locality + " " + street;
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

//    public void setRegion(String r) {
//        region = new Region();
//
//    }

    public CityDistrict getDistrict() {
        return cityDistrict;
    }

    public void setCityDistrict(CityDistrict district) {
        this.cityDistrict = district;
    }

    public static Address getStub(_Session session) {
        Address addr = new Address();
        try {
            CountryDAO cDao = new CountryDAO(session);
            Country country = cDao.findByCode(CountryCode.KZ);
            addr.setCountry(country);
            RegionDAO rDao = new RegionDAO(session);
            Region region = rDao.findByName("Алматы");
            addr.setRegion(region);
        } catch (DAOException e) {

        }

        CityDistrict cityDistrict = new CityDistrict();
        cityDistrict.setName("");
        addr.setCityDistrict(cityDistrict);
        try {
            LocalityDAO lDao = new LocalityDAO(session);
            Locality city = lDao.findByName("Алматы");
            addr.setLocality(city);
        } catch (DAOException e) {

        }
        Street street = new Street();
        street.setName("");
        addr.setStreet(street);
        addr.setHouseNumber("");
        addr.setAdditionalInfo("");
        return addr;

    }
}
