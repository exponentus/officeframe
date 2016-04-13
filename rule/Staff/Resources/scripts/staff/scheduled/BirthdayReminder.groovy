package staff.scheduled

import kz.lof.scripting._Session
import kz.lof.scripting.event._DoScheduledTask

class BirthdayReminder extends _DoScheduledTask {
	@Override
	public void doEvery5Min(_Session session) {
		log("555")
	}

	@Override
	public void doEvery1Hour(_Session session) {
	}

	@Override
	public void doEveryNight(_Session session) {
	}
}
