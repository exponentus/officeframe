package reference.page;

import com.exponentus.scriptprocessor.page.IOutcomeObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by medin on 07.10.16.
 */
public class ListColumnOptionsForTest implements IOutcomeObject {

    // { name: 'name', value: 'name', type: 'localizedName', sort: 'desc', className: 'vw-name' }

    List<_ColumnOption> columns = new LinkedList<>();

    public void addOption(String name,
                          String value,
                          String type,
                          String sort,
                          String className) {
        columns.add(new _ColumnOption(name, value, type, sort, className));
    }

    class _ColumnOption {
        String name;
        String value;
        String type;
        String sort;
        String className;

        public _ColumnOption(String name,
                             String value,
                             String type,
                             String sort,
                             String className) {
            this.name = name;
            this.value = value;
            this.type = type;
            this.sort = sort;
            this.className = className;
        }
    }
}
