package reference.model;

import com.exponentus.common.model.SimpleReferenceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@JsonRootName("vehicle")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Cacheable(true)
@Table(name = "vehicles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQuery(name = "Vehicle.findAll", query = "SELECT m FROM Vehicle AS m ORDER BY m.regDate")
public class Vehicle extends SimpleReferenceEntity {

    public enum VehicleType {CAR, SUV, PEOPLE_CARRIERS, VAN}

    public enum TRANSMISSION {AUTO, MANUAL}

    public VehicleType vehicleType = VehicleType.CAR;
    public TRANSMISSION transmission = TRANSMISSION.AUTO;
    public int capacity = 5;

}
