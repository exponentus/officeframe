package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.CashInflowDAO;
import reference.init.AppConst;
import reference.model.CashInflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = AppConst.CODE + "_fill_cash_inflows")
public class FillCashInflow extends Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<CashInflow> entities = new ArrayList<CashInflow>();
		String[] data = {"income_from_loans", "non-tax_revenue", "proceeds_from_the_sale_of_fixed_capital",
				"receipts_of_transfers","redemption_of_budget_loans","proceeds_from_the_sale_of_financial_assets_of_the_state",
				"remaining_budget_funds_are_used", "tax_revenue" };
		String[] dataEng = {"Income from loans", "Non-tax revenues", "Proceeds from the sale of fixed capital",
				"Receipts of transfers","Redemption of budget loans","Proceeds from the sale of financial assets of the state",
				"Remaining budget funds are used", "Tax revenues" };
		String[] dataRus = { "Поступления займов", "Неналоговые поступления", "Поступления от продажи основного капитала",
				"Поступления трансферов", "Погашение бюджетных кредитов", "Поступления от распродажи финансовых активов государства",
				"Используются остатки бюджетных средств", "Налоговые поступления" };
		String[] dataKaz = {"Қарыздар бойынша кірістер", "Салыққа жатпайтын түсім", "Негізгі капиталды сатудан түсетін түсімдер",
				"Аударымдардан түсетін түсімдер", "Бюджеттік қарыздарды өтеу", "Мемлекеттің қаржы активтерін сатудан түсетін түсімдер",
				"Қалған бюджет қаражаттары пайдаланылады", "Салық кірістері"};


		for (int i = 0; i < data.length; i++) {
			CashInflow entity = new CashInflow();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, dataEng[i]);
			name.put(LanguageCode.RUS, dataRus[i]);
			name.put(LanguageCode.KAZ, dataKaz[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			CashInflowDAO dao = new CashInflowDAO(ses);
			for (CashInflow entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.info(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warning("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.exception(e);
					}
				} catch (SecureException e) {
					logger.exception(e);
				}
			}
		} catch (DAOException e) {
			logger.exception(e);
		}
		logger.info("done...");
	}

}
