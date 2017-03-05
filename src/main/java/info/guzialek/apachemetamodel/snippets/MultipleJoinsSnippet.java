package info.guzialek.apachemetamodel.snippets;

import org.apache.metamodel.UpdateCallback;
import org.apache.metamodel.UpdateScript;
import org.apache.metamodel.UpdateableDataContext;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.ColumnType;
import org.apache.metamodel.schema.Table;

public class MultipleJoinsSnippet {

    public static void main(String[] args) {
        UpdateableDataContext dataContext = new PojoDataContext();

        dataContext.executeUpdate(new UpdateScript() {

            public void run(UpdateCallback callback) {
                Table table1 = callback.createTable(callback.getDataContext().getDefaultSchema(), "table1").withColumn("id")
                        .ofType(ColumnType.INTEGER).asPrimaryKey().execute();

                Table table2 = callback.createTable(callback.getDataContext().getDefaultSchema(), "table2").withColumn("id")
                        .ofType(ColumnType.INTEGER).asPrimaryKey().withColumn("table1_id").ofType(ColumnType.INTEGER)
                        .execute();
                
                Table table3 = callback.createTable(callback.getDataContext().getDefaultSchema(), "table3").withColumn("id")
                        .ofType(ColumnType.INTEGER).asPrimaryKey().withColumn("table2_id").ofType(ColumnType.INTEGER)
                        .execute();
                
                callback.insertInto(table1).value("id", 1).execute();
                callback.insertInto(table2).value("id", 1).value("table1_id", 1).execute();
                callback.insertInto(table3).value("id", 1).value("table2_id", 1).execute();
                callback.insertInto(table3).value("id", 2).value("table2_id", 0).execute();
            }
        });
        
        Table table1 = dataContext.getTableByQualifiedLabel("table1");
        Table table2 = dataContext.getTableByQualifiedLabel("table2");
        Table table3 = dataContext.getTableByQualifiedLabel("table3");
        
        Column table1id = table1.getColumnByName("id");
        Column table2table1_id = table2.getColumnByName("table1_id");
        Column table3table2_id = table3.getColumnByName("table2_id");
        
        try (DataSet dataSet = dataContext.query().from(table1).and(table2).and(table3).selectAll().where(table1id).eq(table2table1_id).and(table1id).eq(table3table2_id).execute()) {
            while (dataSet.next()) {
                Row row = dataSet.getRow();
                System.out.println(row);
            }
        }
    }
}
