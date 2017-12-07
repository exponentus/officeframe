package monitoring.dao;

import administrator.model.User;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.IAppEntity;
import com.exponentus.env.EnvConst;
import com.exponentus.env.Environment;
import com.exponentus.extconnect.IActivityRecorder;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import monitoring.model.DocumentActivity;
import monitoring.model.UserActivity;
import monitoring.model.constants.ActivityType;
import monitoring.model.embedded.Event;
import net.firefang.ip2c.Country;
import net.firefang.ip2c.IP2Country;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ActivityRecorder implements IActivityRecorder {
    private static IP2Country ip2c;
    private _Session ses;

    public ActivityRecorder() {
        try {
            ip2c = new IP2Country(Environment.getKernelDir() + EnvConst.RESOURCES_DIR + File.separator + "ip-to-country.bin", IP2Country.MEMORY_CACHE);
        } catch (IOException e) {
            Lg.exception(e);
        }
    }

    @Override
    public void postEmailSending(IAppEntity<UUID> entity, List<String> recipients, String info) {
        if (recipients.size() > 0) {
            DocumentActivity ua = new DocumentActivity();
            ua.setActEntityId(entity.getId());
            ua.setActEntityKind(entity.getEntityKind());
            Event event = new Event();
            event.setType(ActivityType.SENT_EMAIL);
            event.setTime(new Date());
            event.setLocInfo(info);
            event.setAddInfo(recipients.toString());
            ua.addEvent(event);
            try {
                DocumentActivityDAO dao = new DocumentActivityDAO(ses);
                dao.add(ua);
            } catch (DAOException e) {
                Lg.exception(e);
            }
        }
    }

    public void postSlackMsgSending(IAppEntity<UUID> entity, String addr, String info) {
        if (!addr.isEmpty()) {
            DocumentActivity ua = new DocumentActivity();
            ua.setActEntityId(entity.getId());
            ua.setActEntityKind(entity.getEntityKind());
            Event event = new Event();
            event.setType(ActivityType.SENT_SLACK_MESSAGE);
            event.setTime(new Date());
            event.setLocInfo(info);
            event.setAddInfo(addr);
            ua.addEvent(event);
            try {
                DocumentActivityDAO dao = new DocumentActivityDAO(ses);
                dao.add(ua);
            } catch (DAOException e) {
                Lg.exception(e);
            }
        }
    }


    @Override
    public void postLogin(IUser user, String ip) throws DAOException {

        UserActivity ua = new UserActivity();
        ua.setEventTime(new Date());
        ua.setType(ActivityType.LOGIN);
        ua.setActUser((User) user);
        UserActivityDAO dao = new UserActivityDAO(ses);

        if (!ip.equals("127.0.0.1") && !ip.equals("0:0:0:0:0:0:0:1")) {
            try {
                ua.setIp(ip);
                Country c = ip2c.getCountry(ip);
                ua.setCountry(c.getName());
                dao.add(ua);
            } catch (NumberFormatException e) {
                Lg.error("Incorrect address, IP=" + ip);
            } catch (Exception e) {
                Lg.error("IP=" + ip);
                Lg.exception(e);
            }
        } else {
            ua.setIp("localhost");
            dao.add(ua);
        }
    }

    @Override
    public void postLogout(IUser user) throws DAOException {
        UserActivity ua = new UserActivity();
        ua.setEventTime(new Date());
        ua.setType(ActivityType.LOGOUT);
        try {
            ua.setActUser((User) user);
        } catch (ClassCastException e) {

        }
        //	add(ua);
    }
}
