import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;

public class Demo {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        //replace "hive" here with the name of the user the queries should run as
        Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "hive", "");
        Statement stmt = con.createStatement();
        String tableName = "dataTable";
        stmt.execute("drop table if exists " + tableName);
        stmt.execute("CREATE TEMPORARY TABLE dataTable USING nsmc.sql.MongoRelationProvider OPTIONS (db 'test', collection 'scratch')");
        // show tables
        String sql = "show tables";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        if (res.next()) {
            System.out.println(res.getString(1));
        }
        // describe table
        sql = "describe " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1) + "\t" + res.getString(2));
        }

        // select * query
        sql = "select custid, billingAddress, orders from " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);

	ResultSetMetaData rsm = res.getMetaData();

	for (int i = 1; i <= rsm.getColumnCount(); i++) {
	    System.out.println("[" + i + "] " + rsm.getColumnName(i) + " : " + rsm.getColumnClassName(i));
	}

        while (res.next()) {
            System.out.println(String.valueOf(res.getString(1)));
	    Object o = res.getObject(2);
	    if (o != null) System.out.println(o.getClass() + ", " + (String)o);
	    Object o3 = res.getObject(3);
	    if (o3 != null) System.out.println(o3.getClass() + ", " + (String)o3);
	    /*
	    Struct s = (Struct)o;
	    Object[] attrs = s.getAttributes();
	    for (Object a: attrs) {
		System.out.println(a);
	    }
	    */
        }

	// show tables
        sql = "show tables";
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1) + ", " + res.getString(2));
        }

        // regular hive query
        sql = "select count(1) from " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }
    }
}
