package staff.scheduled;

import kz.lof.scripting._Session;
import kz.lof.scripting.event._DoScheduledTask;

public class BirtdayReminder extends _DoScheduledTask {

	@Override
	public void doEvery5Min(_Session session) {
		System.out.println("test scheduled task, 5 min");
	}

	@Override
	public void doEvery1Hour(_Session session) {
		System.out.println("test test scheduled task, a hour");
	}

	@Override
	public void doEveryNight(_Session session) {
		System.out.println("test test scheduled task, a night");
	}

}
