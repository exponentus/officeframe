package reference.init;

public class DataConst {

    public static String[][] NOT_NULL = {
            //   {CODE + "__countries","code"},
            {ModuleConst.CODE + "__city_districts", "locality_id"},
            {ModuleConst.CODE + "__regions", "country_id"},
            {ModuleConst.CODE + "__regions", "type_id"},
            {ModuleConst.CODE + "__localities", "region_id"},
            {ModuleConst.CODE + "__localities", "type_id"},
            {ModuleConst.CODE + "__streets", "locality_id"},
            //  {CODE + "__document_languages","code"},
            {ModuleConst.CODE + "__industry_types", "category_id"}
    };

}
