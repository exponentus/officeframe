package reference.model.embedded;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;
import reference.model.*;
import reference.model.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Embeddable
@JsonPropertyOrder({ "country", "region", "district", "locality","cityDistrict",
        "street", "houseNumber", "flat", "additionalInfo", "coordiantes" })
@Converters({ @org.eclipse.persistence.annotations.Converter(name = "country_conv", converterClass = CountryConverter.class),
        @Converter(name = "region_conv", converterClass = RegionConverter.class),
        @Converter(name = "district_conv", converterClass = DistrictConverter.class),
        @Converter(name = "citydistrict_conv", converterClass = CityDistrictConverter.class),
        @Converter(name = "locality_conv", converterClass = LocalityConverter.class),
        @Converter(name = "street_conv", converterClass = StreetConverter.class)})
public class Address {

    @NotNull
    @Convert("country_conv")
    @Basic(fetch = FetchType.LAZY)
    private Country country;


    @NotNull
    @Convert("region_conv")
    @Basic(fetch = FetchType.LAZY)
    private Region region;

    @NotNull
    @Convert("district_conv")
    @Basic(fetch = FetchType.LAZY)
    private District district;

    @NotNull
    @Convert("locality_conv")
    @Basic(fetch = FetchType.LAZY)
    private Locality locality;


    @NotNull
    @Convert("citydistrict_conv")
    @Basic(fetch = FetchType.LAZY)
    private CityDistrict cityDistrict;



    @NotNull
    @Convert("street_conv")
    @Basic(fetch = FetchType.LAZY)
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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public CityDistrict getCityDistrict() {
        return cityDistrict;
    }

    public void setCityDistrict(CityDistrict district) {
        this.cityDistrict = district;
    }


}
