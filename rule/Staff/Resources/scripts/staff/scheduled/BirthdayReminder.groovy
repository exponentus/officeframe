package staff.scheduled

import com.exponentus.scripting._Session
import com.exponentus.scripting.event._DoScheduledTask



class BirthdayReminder extends _DoScheduledTask {
	@Override
	public void doEvery5Min(_Session session) {
		//log("555")
	}

	@Override
	public void doEvery1Hour(_Session session) {
	}

	@Override
	public void doEveryNight(_Session session) {
	}
}
