import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;

public class Demo {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    private static void showTables(Statement stmt) throws SQLException {
        System.out.println("*** Showing tables");
        String sql = "SHOW TABLES";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1) + ", " + res.getString(2));
        }
    }

    /**
     * For a given ResultSet, print the column metadata and then print the the data.
     */
    private static void describeAndPrint(ResultSet res) throws SQLException {
        ResultSetMetaData rsm = res.getMetaData();

        System.out.println("*** Column metadata:");
        int ncols = rsm.getColumnCount();
        System.out.println("total of " + ncols + " columns");
        for (int i = 1; i <= ncols; i++) {
            System.out.println("Column " + i + " : " + rsm.getColumnName(i));
            System.out.println("  Label : " + rsm.getColumnLabel(i));
            System.out.println("  Class Name : " + rsm.getColumnClassName(i));
            System.out.println("  Type : " + rsm.getColumnType(i));
            System.out.println("  Type Name : " + rsm.getColumnTypeName(i));
        }

        System.out.println("*** Data:");
        while (res.next()) {
            System.out.println("Row " + res.getRow());
            for (int i = 1; i <= ncols; i++) {
                System.out.println("  Column " + i + " : " + res.getObject(i));
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "hive", "");
        Statement stmt = con.createStatement();
        String tableName = "dataTable";

        // start with a clean slate
        // System.out.println("*** Cleaning up");
        stmt.execute("DROP TABLE IF EXISTS " + tableName);

        // register the temporary table
        System.out.println("*** Registering the table");
        stmt.execute("CREATE TEMPORARY TABLE " + tableName + " " +
                "USING nsmc.sql.MongoRelationProvider " +
                "OPTIONS (db 'test', collection 'scratch')");

        // show tables
        showTables(stmt);

        // describe table
        String sql = "describe " + tableName;
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1) + "\t" + res.getString(2));
        }

        // select * query
        sql = "select custid, billingAddress, orders from " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        describeAndPrint(res);

        // deeper query
        sql = "SELECT custid, billingAddress.zip FROM " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        describeAndPrint(res);

        // query usign Hive features
        sql = "SELECT custid, o.orderid, o.itemid, o.quantity FROM " + tableName + " LATERAL VIEW explode(orders) t AS o";
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        describeAndPrint(res);

        System.out.println("*** Dropping the table");
        stmt.execute("DROP TABLE " + tableName);

        // show tables
        showTables(stmt);

    }
}
