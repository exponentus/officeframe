package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import reference.init.ModuleConst;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "ref__vehicles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "Vehicle.findAll", query = "SELECT m FROM Vehicle AS m ORDER BY m.regDate")
public class Vehicle extends SimpleReferenceEntity {

    public VehicleType vehicleType = VehicleType.CAR;
    public TRANSMISSION transmission = TRANSMISSION.AUTO;
    public int capacity = 5;

    @Override
    public String getURL() {
        return ModuleConst.BASE_URL + "vehicles/" + getIdentifier();
    }

    public enum VehicleType {CAR, SUV, PEOPLE_CARRIERS, VAN}

    public enum TRANSMISSION {AUTO, MANUAL}
}
