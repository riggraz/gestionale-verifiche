import java.sql.SQLException;

import utils.DBManager;
import views.MainFrame;

public class GestionaleVerifiche {
	
	private DBManager dbManager;
	
	public GestionaleVerifiche() {
		try {
			dbManager = new DBManager(DBManager.JDBCDriverSQLite, DBManager.JDBCURLSQLite);
			dbManager.executeUpdate("PRAGMA foreign_keys = ON;");
			
			// close db connection on program shutdown
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					dbManager.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}));
			
			new MainFrame(dbManager);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
