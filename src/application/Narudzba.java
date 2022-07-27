package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Narudzba {
	
	int id;
	int kupac_id;
	int trgovac_id;
	String datum_narudzbe;
	String datum_isporuke;
	String napomena;
	float vrijednostNarudzbe;
	ArrayList<ArtiklNarudzbe> spisakArtikala = new ArrayList<ArtiklNarudzbe>();
	//ArrayList<ArtiklNarudzbe> novaNarudzba;
	
	static String url = "jdbc:mysql://localhost:3306/seminarski_ors1";
	static String username = "root";
	static String password = "ivana123";
	
	public Narudzba() {}
	
	public Narudzba(int id, float vrijednostNarudzbe, String datum_narudzbe, String datum_isporuke, String napomena) {
		this.id = id;
		this.vrijednostNarudzbe = vrijednostNarudzbe;
		this.datum_narudzbe = datum_narudzbe;
		this.datum_isporuke = datum_isporuke;
		this.napomena = napomena;
		izracunajVrijednostNarudzbe();
	}
	public Narudzba(int id, float vrijednostNarudzbe, String datum_narudzbe,  String napomena) {
		this.id = id;
		this.vrijednostNarudzbe = vrijednostNarudzbe;
		this.datum_narudzbe = datum_narudzbe;
		this.napomena = napomena;
		izracunajVrijednostNarudzbe();
	}
	public Narudzba(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public String getDatum_narudzbe() {
		return datum_narudzbe;
	}
	public String getDatum_isporuke() {
		return datum_isporuke;
	}
	public float getVrijednostNarudzbe() {
		return vrijednostNarudzbe;
	}
	
	static ArrayList<Narudzba> ucitajNarudzbe(){
		
		ArrayList<Narudzba> spisakNarudzbi = new ArrayList<Narudzba>();
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM narudzba");
			while (rs.next()) {
				Narudzba n = new Narudzba();
				n.id = rs.getInt("id");
				n.kupac_id = rs.getInt("kupac_id");
				n.trgovac_id = rs.getInt("trgovac_id");
				n.datum_isporuke = rs.getString("datum_isporuke");
				n.datum_narudzbe = rs.getString("datum_narudzbe");
				n.napomena = rs.getString("napomena");
				spisakNarudzbi.add(n);
	         }
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
		return spisakNarudzbi;
		
	}
	
	public int zadnjiId() {
		ArrayList<Narudzba> spisakNarudzbi = ucitajNarudzbe();
		int zadnji_id = spisakNarudzbi.get(spisakNarudzbi.size() - 1).id + 2;
		return zadnji_id;
	}

	
	public void ucitajArtikle(Narudzba n){
		
		//ArtiklNarudzbe an = new ArtiklNarudzbe();
		ArrayList<ArtiklNarudzbe> l = ArtiklNarudzbe.ucitajArtiklNarudzbe();
		spisakArtikala.clear();
		
		for(ArtiklNarudzbe a: l) {
			if(a.narudzba_id == n.id) n.spisakArtikala.add(a);
		}
		
	}
	
	public void izracunajVrijednostNarudzbe() {
		
		ucitajArtikle(this);
		vrijednostNarudzbe = 0;
		
		for(ArtiklNarudzbe a: spisakArtikala) {
			vrijednostNarudzbe += a.cijena * a.kolicina;
		}
		
	}
	
	
	public void otkaziNarudzbu() {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			String query = "DELETE FROM narudzba WHERE id = ?";
				
			//stmt.executeUpdate(query);
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, this.id);
			preparedStmt.execute();
			
			connection.close();
			
			ArtiklNarudzbe.obrisiArtikleNarudzbe(this);
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	
	static void dodajNovuNarudzbu(Narudzba n) {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "INSERT INTO narudzba (id, kupac_id, trgovac_id, datum_narudzbe, datum_isporuke, napomena)"
			        + " values (?, ?, ?, ?, ?, ?)";
			
			java.util.Date date= new java.util.Date();		
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, n.id);
			preparedStmt.setInt(2, n.kupac_id);
			preparedStmt.setInt(3, -1);
			preparedStmt.setDate(4, sqlDate);
			preparedStmt.setDate(5, null);
			preparedStmt.setString(6, null);
			
			
			preparedStmt.execute();
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	

	static ArrayList<Narudzba> ucitajObavjestenja(Trgovac t) {
		
		ArrayList<Narudzba> l = ucitajNarudzbe();
		ArrayList<Narudzba> rez = new ArrayList<Narudzba>();
		ArrayList<ProdajnoMjesto> pm = ProdajnoMjesto.ucitajProdajnaMjesta();
		
		for(Narudzba n: l) {
			boolean uslov = false;
			
			if(n.trgovac_id == -1) {
				String s = Kupac.drzavaKupca(n.kupac_id);
				
				if(s.equals(ProdajnoMjesto.drzavaPMjesta(t.prodajnoMjestoId))) {
					rez.add(n);
				} else {
					for(ProdajnoMjesto mjesto: pm) {
						if(s.equals(mjesto.drzava)) uslov = true;
					}
					if(!uslov) rez.add(n);
				}
			}
			
		}
		
		
		return rez;
	}
	
	
	static void prihvatiNarudzbu(int narudzbaID, int trgovac_id, String d) {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "update narudzba set trgovac_id = ?, datum_isporuke = ? where id = ?";
			
			//java.util.Date datum = Date.valueOf(d);
			Date datum = Date.valueOf(d);
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, trgovac_id);
			preparedStmt.setDate(2, datum);
			preparedStmt.setInt(3, narudzbaID);
			
			
			preparedStmt.executeUpdate();
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	
	
}
















