package monitoring.model;

import com.exponentus.common.model.SimpleAppEntity;
import com.exponentus.common.model.converter.UUIDConverter;
import monitoring.init.ModuleConst;
import monitoring.model.embedded.Event;
import monitoring.model.util.EventConverter;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
@Table(name = ModuleConst.CODE + "__doc_activities", uniqueConstraints = @UniqueConstraint(columnNames = {"act_entity_id"}))
public class DocumentActivity extends SimpleAppEntity {

    @Convert("uuidConverter")
    @Column(name = "act_entity_id", nullable = false, unique = true)
    private UUID actEntityId;

    @Column(name = "act_entity_kind", length = 32)
    private String actEntityKind;

    @javax.persistence.Convert(converter = EventConverter.class)
    @Column(name = "event", columnDefinition = "jsonb")
    @OrderBy("sort")
    private List<Event> events = new ArrayList<Event>();

    public UUID getActEntityId() {
        return actEntityId;
    }

    public void setActEntityId(UUID actEntityId) {
        this.actEntityId = actEntityId;
    }

    public String getActEntityKind() {
        return actEntityKind;
    }

    public void setActEntityKind(String actEntityKind) {
        this.actEntityKind = actEntityKind;
    }

    public List<Event> getDetails() {
        return events;
    }

    public void setDetails(List<Event> details) {
        this.events = details;
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    public String getURL() {
        return ModuleConst.BASE_URL + "document-activities/" + getId();
    }
}
