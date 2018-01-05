package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.ActivityTypeCategoryDAO;
import reference.init.ModuleConst;
import reference.model.ActivityTypeCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_activity_type_categories")
public class FillActivityTypeCategories extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<ActivityTypeCategory> entities = new ArrayList<ActivityTypeCategory>();
        String[] data = {"agriculture_forestry_fisheries", "industry", "construction",
                "wholesale_and_retail_trade, repair_of_cars_and_motorcycles", "transport_and_warehousing",
                "accommodation_and_food_services", "information_and_communication", "financial_and_insurance_activities",
                "operations_with_real_estate", "professional_scientific_and_technical_activities",
                "activities_in_the_field_of_administrative_and_support_services",
                "public_administration_and_defense+compulsory_social_security", "education",
                "health_and_social_services", "art_entertainment_and_recreation", "provision_of_other_types_of_services",
                "taxes_on_products"};

        String[] dataEng = {"Agriculture, Forestry and Fisheries", "Industry", "Construction",
                "Wholesale and retail trade, repair of cars and motorcycles", "Transport and warehousing",
                "Accommodation and food services", "Information and communication", "Financial and insurance activities",
                "Operations with real estate", "Professional, scientific and technical activities",
                "Activities in the field of administrative and support services",
                "Public administration and defense, compulsory social security", "Education",
                "Health and social services", "Art, entertainment and recreation", "Provision of other types of services",
                "Taxes on products"};

        String[] dataRus = {"Сельское, лесное и рыбное хозяйство", "Промышленность", "Строительство",
                "Оптовая и розничная торговля; ремонт автомобилей и мотоциклов", "Транспорт и складирование",
                "Услуги по проживанию и питанию", "Информация и связь", "Финансовая и страховая деятельность",
                "Операции с недвижимым имуществом", "Профессиональная, научная и техническая деятельность",
                "Деятельность в области административного и вспомогательного обслуживания",
                "Государственное управление и оборона; обязательное социальное обеспечение", "Образование",
                "Здравоохранение и социальные услуги", "Искусство, развлечения и отдых", "Предоставление прочих видов услуг",
                "Налоги на продукты"};

        String[] dataKaz = {"Ауыл шаруашылығы, орман және балық шаруашылығы", "Өнеркәсіп", "Құрылыс",
                "Көтерме және бөлшек сауда, автомобильдер мен мотоциклдерді жөндеу", "Көлік және қоймалау",
                "Тұру және тамақтану", "Ақпарат және байланыс", "Қаржы және сақтандыру қызметі",
                "Жылжымайтын мүлікпен операциялар", "Кәсіби, ғылыми және техникалық қызмет",
                "Әкімшілік және қолдау қызметі саласындағы қызмет",
                "Мемлекеттік басқару және қорғаныс, міндетті әлеуметтік қамтамасыз ету", "Білім беру",
                "Денсаулық және әлеуметтік қызметтер", "Өнер, ойын-сауық және демалыс", "Қызметтің басқа түрлерін ұсыну",
                "Өнімге салынатын салықтар"};


        String names[] = {"non_ferrous_metals_and_black_metals", "pesticides", "medical_activities",
                "pharmaceutical_activities", "activities_in_the_field_of_veterinary_medicine"};
        String namesEng[] = {"Non-ferrous metals and black metals", "Pesticides (pesticides)", "Medical activities",
                "Pharmaceutical activities", "Activities in the field of veterinary medicine"};
        String namesRus[] = {"Түсті және қара металдар", "Пестицидтер", "Медициналық қызмет",
                "Фармацевтикалық қызмет", "Ветеринариялық медицина саласындағы қызмет"};

        for (int i = 0; i < data.length; i++) {
            ActivityTypeCategory entity = new ActivityTypeCategory();
            entity.setName(data[i]);
            Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
            name.put(LanguageCode.ENG, data[i]);
            name.put(LanguageCode.ENG, dataEng[i]);
            name.put(LanguageCode.RUS, dataRus[i]);
            name.put(LanguageCode.KAZ, dataKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            ActivityTypeCategoryDAO dao = new ActivityTypeCategoryDAO(ses);
            for (ActivityTypeCategory entry : entities) {
                try {
                    if (dao.add(entry) != null) {
                        logger.info(entry.getName() + " added");
                    }
                } catch (DAOException e) {
                    if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                        logger.warning("a data is already exists (" + entry.getTitle() + "), record was skipped");
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
