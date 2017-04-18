package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.MeetingRoom;

import java.util.UUID;

public class MeetingRoomDAO extends ReferenceDAO<MeetingRoom, UUID> {

    public MeetingRoomDAO(_Session session) throws DAOException {
        super(MeetingRoom.class, session);
    }
}
