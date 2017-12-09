package monitoring.model.util;

import com.exponentus.server.Server;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import monitoring.model.embedded.Event;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class EventConverter implements AttributeConverter<List<Event>, PGobject> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PGobject convertToDatabaseColumn(List<Event> val) {
        try {
            PGobject po = new PGobject();
            po.setType("jsonb");
            po.setValue(mapper.writeValueAsString(val));
            return po;
        } catch (SQLException e) {
            Server.logger.exception(e);
            return null;
        } catch (JsonGenerationException e) {
            Server.logger.exception(e);
            return null;
        } catch (JsonMappingException e) {
            Server.logger.exception(e);
            return null;
        } catch (IOException e) {
            Server.logger.exception(e);
            return null;
        }
    }

    @Override
    public List<Event> convertToEntityAttribute(PGobject po) {
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(ArrayList.class, Event.class);
            return mapper.readValue(po.getValue(), collectionType);
        } catch (Exception e) {
            Server.logger.error(e.toString());
            return new ArrayList<Event>();
        }
    }
}