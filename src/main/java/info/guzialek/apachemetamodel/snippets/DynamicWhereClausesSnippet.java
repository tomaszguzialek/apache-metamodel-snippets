package info.guzialek.apachemetamodel.snippets;

import java.util.ArrayList;
import java.util.List;

import org.apache.metamodel.UpdateCallback;
import org.apache.metamodel.UpdateScript;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.pojo.ArrayTableDataProvider;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.pojo.TableDataProvider;
import org.apache.metamodel.util.SimpleTableDef;

/**
 * A code snippet demonstrating querying with dynamic values.
 * 
 * @author Tomasz Guzialek
 *
 */
public class DynamicWhereClausesSnippet {
    
    public static void main(String[] args) {
        // Prepare a data context based on plain old Java objects
        List<TableDataProvider<?>> tableDataProviders = new ArrayList<TableDataProvider<?>>();
        SimpleTableDef tableDef1 = new SimpleTableDef("snippetTableName1", new String[] {"id", "name"});
        tableDataProviders.add(new ArrayTableDataProvider(tableDef1,
                new ArrayList<Object[]>()));
        PojoDataContext dataContext = new PojoDataContext("snippetSchemaName", tableDataProviders);
        
        dataContext.executeUpdate(new UpdateScript() {
            
            public void run(UpdateCallback callback) {
                callback.insertInto("snippetTableName1").value("id", "id1").value("name", "name1").execute();
                callback.insertInto("snippetTableName1").value("id", "id2").value("name", "name2").execute();
            }
        });
        
        String[] selectArray = new String[2];
        selectArray[0] = "id";
        selectArray[1] = "name";
        
        DataSet dataSet = dataContext.query().from("snippetTableName1").select(selectArray).where("id").eq("id1").and("name").eq("name1").execute();
        
        while (dataSet.next()) {
            Row row = dataSet.getRow();
            System.out.println("Row: " + row);
        }
    }
}
