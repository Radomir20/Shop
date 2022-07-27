package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ArtiklNarudzbe {
	
	float cijena;
	int kolicina;
	int proizvod_id;
	int narudzba_id;
	int id;
	
	static String url = "jdbc:mysql://localhost:3306/seminarski_ors1";
	static String username = "root";
	static String password = "ivana123";
	
	public ArtiklNarudzbe() {}
	
	public ArtiklNarudzbe(float cijena, int proizvod_id, int narudzba_id, int id) {
		this.cijena = cijena;
		this.proizvod_id = proizvod_id;
		this.narudzba_id = narudzba_id;
		this.id = id;
	}
	
	public ArtiklNarudzbe(int id, float cijena, int kolicina) {
		this.id = id;
		this.cijena = cijena;
		this.kolicina = kolicina;
	}
	
	
	static ArrayList<ArtiklNarudzbe> ucitajArtiklNarudzbe(){
		
		ArrayList<ArtiklNarudzbe> sviArtikli = new ArrayList<ArtiklNarudzbe>();
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM artikal_narudzbe");
			while (rs.next()) {
				ArtiklNarudzbe a = new ArtiklNarudzbe();
				a.cijena = rs.getFloat("cijena_po_komadu");
				a.id = rs.getInt("id");
				a.kolicina = rs.getInt("kolicina");
				a.proizvod_id = rs.getInt("proizvod_id");
				a.narudzba_id = rs.getInt("narudzba_id");
				sviArtikli.add(a);
	         }
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
		return sviArtikli;
		
	}
	
	static void dodajNoveArtikleNarudzbe(ArrayList<ArtiklNarudzbe> l) {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "INSERT INTO artikal_narudzbe (narudzba_id, proizvod_id, kolicina, cijena_po_komadu, id)"
			        + " values (?, ?, ?, ?, ?)";
			

			
			for(int i = 0 ; i < l.size() ; i++) {
				PreparedStatement preparedStmt = connection.prepareStatement(query);
				
				preparedStmt.setInt(1, l.get(i).narudzba_id);
				preparedStmt.setInt(2, l.get(i).proizvod_id);
				preparedStmt.setInt(3, l.get(i).kolicina);
				preparedStmt.setFloat(4, l.get(i).cijena);
				preparedStmt.setInt(5, l.get(i).id);
				
				preparedStmt.execute();
			}
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	static void obrisiArtikleNarudzbe(Narudzba n) {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			String query = "DELETE FROM artikal_narudzbe WHERE narudzba_id = ?";
				
			//stmt.executeUpdate(query);
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, n.id);
			preparedStmt.execute();
			
			connection.close();
			
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	
	
	static int zadnjiId() {
		ArrayList<ArtiklNarudzbe> l = ucitajArtiklNarudzbe();
		int zadnjiID = l.get(l.size() - 1).id + 1;
		return zadnjiID;
	}
	
	public float getCijena() {
		return cijena;
	}
	public int getId() {
		return id;
	}
	public int getKolicina() {
		return kolicina;
	}

	@Override
	public String toString() {
		return "ArtiklNarudzbe [kolicina=" + kolicina + ", id=" + id + "]";
	}
	
	


}
