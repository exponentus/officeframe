package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import reference.dao.ProductTypeDAO;
import reference.dao.VehicleDAO;
import reference.init.ModuleConst;
import reference.model.ProductType;
import reference.model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = ModuleConst.CODE + "_fill_product_types")
public class FillProductTypes extends Do {

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<ProductType> entities = new ArrayList<>();

        String names[] = {"gold_in _the_alloy_dore", "gold-containing_products", "aluminum_radiators", "food_salt", "agricultural_raw materials_and_canning", "polyethylene_film_for_agriculture", "precious_metals", "fish", "ferro-concrete_products", "crafted_leather_and skins", "glyffosate, formulate_and_phosphorus_trichloride", "granite_blocks", "granite_slab", "flowers", "grain_and_feed", "meat", "canned meat_products", "furniture", "treated_wool", "sulfate-potassium", "sugar", "bricks", "lead-zinc_concentrate", "sulfuric_acid", "sand_and gravel", "dry_building_mixtures_and_gypsum_products", "manufacture_of_building_fittings", "building_sand", "construction_materials", "poultry_meat_and_eggs", "oilseeds", "pasta", "products_of_metallurgy", "processing_of_licorice_root", "oil", "sodium_hexamethophosphate", "sodium_cyanide", "welding_electrodes", "plastic", "polyethylene_pipes", "beef_meat", "sulfate-sodium", "milk_and_dairy_products", "technical_salt", "mineral_fertilizers", "pharmaceuticals", "ferrosilicomanganese", "cement", "sausages",  "extraction_phosphoric_acid", "electricity"};
        String namesEng[] = {"Gold in the alloy 'Dore'", "Gold-containing products", "Aluminum radiators", "Food salt", "Agricultural raw materials and canning", "Polyethylene film for agriculture", "Precious metals", "Fish", "Ferro-concrete products", "Crafted leather and skins", "Glyffosate, formulate and phosphorus trichloride", "Granite blocks", "Granite slab", "Flowers", "Grain and feed", "Meat", "Canned meat products", "Furniture", "Treated wool", "Sulfate-potassium", "Sugar", "Bricks", "Lead-zinc concentrate", "Sulfuric acid", "Sand and gravel", "Dry building mixtures and gypsum products", "Manufacture of building fittings", "Building sand", "Construction Materials", "Poultry meat and eggs", "Oilseeds", "Pasta", "Products of metallurgy", "Processing of licorice root", "Oil", "Sodium hexamethophosphate", "Sodium cyanide", "Welding electrodes", "Plastic", "Polyethylene pipes", "Beef meat", "Sulfate-sodium", "Milk and dairy products", "Technical salt", "Mineral Fertilizers", "Pharmaceuticals", "Ferrosilicomanganese", "Cement", "Sausages",  "Extraction Phosphoric Acid", "Electricity"};
        String namesRus[] = {"Золота в сплаве 'Доре'", "Золотосодержащая продукция", "Алюминиевые радиаторы", "Пищевая соль", "Сельхозсырье и консервирование", "Полиэтиленовая пленка для сельского хозяйства", "Драгоценные металлы", "Рыба", "Железо-бетонные изделия", "Выделанные кожа и шкуры", "Глиффосат, формулят и треххлористый фосфор", "Гранитные блоки", "Гранитная плита", "Цветы", "Зерно и корма", "Мясо", "Консервированные мясопродукты", "Мебель", "Обработанная шерсть", "Сульфат-Калия", "Сахар", "Кирпичи", "Свинцово-цинковый концентрат", "Серная кислота", "Песчано-гравийная смесь", "Сухие строительные смеси и гипсовые изделия", "Производство строительной арматуры", "Строительный песок", "Строительные материалы", "Мясо птицы и яйца", "Масличные культуры", "Макаронные изделия", "Продукты металлургии", "Переработка корня солодки", "Нефть", "Гексаметофосфат натрия", "Цианид натрия", "Сварочные электроды", "Пластмасса", "Полиэтиленовые трубы", "Мясо говядины", "Сульфат-Натрий", "Молоко и молочные продукты", "Техническая соль", "Минеральные удобрения", "Фармацевтика", "Ферросиликомарганец", "Цемент", "Колбасные изделия", "Экстракционная фосфорная кислота", "Электроэнергия"};
        String namesKaz[] = {"Алтын қорытпа 'Доре'", "Алтын өнімі", "Алюминий радиаторлар", "Ас тұзы", "Ауыл шаруашылығы және консервілеу", "Ауыл шаруашылығы үшін полиэтилен пленкасы", "Бағалы металдар", "Балық", "Темірбетон бұйымдары", "Жасанды былғары және терілер", "Глиффосат және үш хлорлық фосфор", "гранит блокты", "Гранит тақтасы", "Гүлдер", "Дән және жем", "Ет", "Ет өнімдерінің консервілеу", "Жиһаз", "қайта өңделген жүн", "Калий-сульфаты", "Қант", "кірпіштер", "қорғасын-мырыш концентраты", "Күкірт қышқылы", "Құм-шағал қоспалары", "Құрғық құрылыс қоспалары және гипс", "Құрылыс арматураның өндірісі", "Құрылыс құм, құм-шағал қоспалары", "Құрылыс материалдары", "құстын еті және жұмыртқа", "Майлы дақылдарды", "Макарон өнімдері", "металлургияның өнімі", "Мия тамырын өңдеу", "мұнай", "Натрий гексаметофосфатты", "Натрий цианидін", "Пісіру электродтар", "Пластмассалық", "Полиэтилендік құбырлар", "Сиыр еті", "Сульфат-Натрий", "Сүт және сүт өнімдері", "Техникалық тұз", "тынайтқыш", "Фармацевтикасы", "ферроқорытпа", "Цемент", "Шұжық өнімдері", "Экстракциондық фосфор қышқылық", "Электр энергиясы"  };
        for (int i = 0; i < names.length; i++) {
            ProductType entity = new ProductType();
            entity.setName(names[i]);
            Map<LanguageCode, String> name = new HashMap<>();
            name.put(LanguageCode.RUS, namesRus[i]);
            name.put(LanguageCode.ENG, namesEng[i]);
            name.put(LanguageCode.KAZ, namesKaz[i]);
            entity.setLocName(name);
            entities.add(entity);
        }

        try {
            ProductTypeDAO dao = new ProductTypeDAO(ses);
            for (ProductType entry : entities) {
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
