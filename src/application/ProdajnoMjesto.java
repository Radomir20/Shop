package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProdajnoMjesto {
	
	int id;
	String grad, drzava, adresa, telefon;
	
	static String url = "jdbc:mysql://localhost:3306/seminarski_ors1";
	static String username = "root";
	static String password = "ivana123";
	
	public ProdajnoMjesto() {}
	public ProdajnoMjesto(String grad, String drzava, String adresa, String telefon) {
		this.grad = grad;
		this.drzava = drzava;
		this.adresa = adresa;
		this.telefon = telefon;
	}
	
	static ArrayList<ProdajnoMjesto> ucitajProdajnaMjesta() {
		
		ArrayList<ProdajnoMjesto> l = new ArrayList<ProdajnoMjesto>();
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM prodajno_mjesto");
			
			while (rs.next()) {
				ProdajnoMjesto pm = new ProdajnoMjesto();
	            pm.id = rs.getInt("id");
	            pm.grad = rs.getString("grad");
	            pm.drzava = rs.getString("drzava");
	            pm.adresa = rs.getString("adresa");
	            pm.telefon = rs.getString("telefon");
	            
	            l.add(pm);
	         }
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
		return l;
		
	}
	
	
	static String drzavaPMjesta(int prodajnoMjestoID) {
		ArrayList<ProdajnoMjesto> mjesta = ProdajnoMjesto.ucitajProdajnaMjesta();
		for(ProdajnoMjesto pm: mjesta) {
			if(prodajnoMjestoID == pm.id) return pm.drzava;
		}
		return null;
	}
	
	public int zadnjiId() {
		ArrayList<ProdajnoMjesto> l = ucitajProdajnaMjesta();
		int zadnji_id = l.get(l.size() - 1).id + 1;
		return zadnji_id;
	}
	
	public void dodajProdajnoMjesto() {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "INSERT INTO prodajno_mjesto (id, grad, drzava, adresa, telefon)"
			        + " values (?, ?, ?, ?, ?)";
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, zadnjiId());
			preparedStmt.setString(2, this.grad);
			preparedStmt.setString(3, this.drzava);
			preparedStmt.setString(4, this.adresa);
			preparedStmt.setString(5, this.telefon);
			
			preparedStmt.execute();
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	

}









