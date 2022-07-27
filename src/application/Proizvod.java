package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Proizvod {
	
	int id;
	String naziv, opis;
	float cijena;
	
	static String url = "jdbc:mysql://localhost:3306/seminarski_ors1";
	static String username = "root";
	static String password = "ivana123";
	
	public Proizvod() {}
	public Proizvod(String naziv, int id, float cijena) {
		this.naziv = naziv;
		this.id = id;
		this.cijena = cijena;
	}
	public Proizvod(String naziv, String opis, float cijena) {
		this.naziv = naziv;
		this.opis = opis;
		this.cijena = cijena;
	}
	public Proizvod(String naziv, int id, float cijena, String opis) {
		this.naziv = naziv;
		this.id = id;
		this.cijena = cijena;
		this.opis = opis;
	}
	
	public int getId() {return id;}
	public String getNaziv() {return naziv;}
	public float getCijena() {return cijena;}
	public String getOpis() {return opis;}
	
	static ArrayList<Proizvod> ucitajProizvode() {
		
		ArrayList<Proizvod> spisakProizvoda = new ArrayList<Proizvod>();
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM proizvod");
			while (rs.next()) {
				Proizvod p = new Proizvod();
				p.id = rs.getInt("id");
				p.naziv = rs.getString("naziv");
				p.opis = rs.getString("opis");
				p.cijena = rs.getFloat("cijena");
				spisakProizvoda.add(p);
	         }
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
		return spisakProizvoda;
	}

	
	public void azurirajProizvod(String naziv, String opis, float cijena) {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "update proizvod set naziv = ?, opis = ?, cijena = ? where id = ?";
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setString(1, naziv);
			preparedStmt.setString(2, opis);
			preparedStmt.setFloat(3, cijena);
			preparedStmt.setInt(4, this.id);
			
			
			preparedStmt.executeUpdate();
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	
	public void dodajProizvod() {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "INSERT INTO proizvod (id, naziv, opis, cijena)"
			        + " values (?, ?, ?, ?)";
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, zadnjiId());
			preparedStmt.setString(2, this.naziv);
			preparedStmt.setString(3, this.opis);
			preparedStmt.setFloat(4, this.cijena);
			
			
			preparedStmt.executeUpdate();
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	
	public int zadnjiId() {
		ArrayList<Proizvod> l = ucitajProizvode();
		int zadnjiID = l.get(l.size() - 1).id + 1;
		return zadnjiID;
	}
	
	
	
	
}

















