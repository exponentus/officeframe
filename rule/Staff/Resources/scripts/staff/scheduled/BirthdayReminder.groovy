package staff.scheduled

import com.exponentus.appenv.AppEnv
import com.exponentus.scripting._Session
import com.exponentus.scripting.event._DoScheduled



class BirthdayReminder extends _DoScheduled {
	@Override
	public void doEvery5Min(AppEnv env,_Session session) {
		//log("555")
	}

	@Override
	public void doEvery1Hour(AppEnv env,_Session session) {
	}

	@Override
	public void doEveryNight(AppEnv env,_Session session) {
	}
}
