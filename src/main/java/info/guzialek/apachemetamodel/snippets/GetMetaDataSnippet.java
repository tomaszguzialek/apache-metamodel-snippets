package info.guzialek.apachemetamodel.snippets;

import java.util.ArrayList;
import java.util.List;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.pojo.ArrayTableDataProvider;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.pojo.TableDataProvider;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;
import org.apache.metamodel.util.SimpleTableDef;

/**
 * A code snippet exploring metadata of the underlying {@link DataContext}.
 * 
 * @author Tomasz Guzialek
 *
 */
public class GetMetaDataSnippet {
    
    public static void main(String[] args) {
        // Prepare a data context based on plain old Java objects
        List<TableDataProvider<?>> tableDataProviders = new ArrayList<TableDataProvider<?>>();
        SimpleTableDef tableDef1 = new SimpleTableDef("snippetTableName1", new String[] {"id", "name"});
        tableDataProviders.add(new ArrayTableDataProvider(tableDef1,
                new ArrayList<Object[]>()));
        PojoDataContext dataContext = new PojoDataContext("snippetSchemaName", tableDataProviders);
        
        // Prints a schema tree
        for (Schema schema : dataContext.getSchemas()) {
            System.out.println("Schema: " + schema.getName());
            for (Table table : schema.getTables()) {
                System.out.println("\t Table: " + table.getName());
                for (Column column : table.getColumns()) {
                    System.out.println("\t\t Column: " + column.getName() + " of type: " + column.getType());
                }
            }
        }
    }
}
