package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Trgovac {
	
	String korisnicko_ime, ime, prezime, lozinka, pol, telefon, email;
	int id, prodajnoMjestoId;
	static ArrayList<Narudzba> obavjestenja = new ArrayList<Narudzba>();
	
	static String url = "jdbc:mysql://localhost:3306/seminarski_ors1";
	static String username = "root";
	static String password = "ivana123";
	
	public Trgovac() {}
	
	public Trgovac(String korisnicko_ime, String ime, String prezime, String lozinka, String pol, String telefon, String email, int prodajnoMjestoId) {
		this.korisnicko_ime = korisnicko_ime;
		this.ime = ime;
		this.prezime = prezime;
		this.lozinka = lozinka;
		this.pol = pol;
		this.telefon = telefon;
		this.email = email;
		this.prodajnoMjestoId = prodajnoMjestoId;
	}

	
	static ArrayList<Trgovac> ucitajTrgovce() {
		
		ArrayList<Trgovac> sviTrgovci = new ArrayList<Trgovac>();
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM trgovac");
			
			while (rs.next()) {
				Trgovac t = new Trgovac();
	            t.id = rs.getInt("id");
	            t.korisnicko_ime = rs.getString("korisnicko_ime");
	            t.ime = rs.getString("ime");
	            t.prezime = rs.getString("prezime");
	            t.lozinka = rs.getString("lozinka");
	            t.telefon = rs.getString("telefon");
	            t.pol = rs.getString("pol");
	            t.email = rs.getString("email");
	            t.prodajnoMjestoId = rs.getInt("prodajno_mjesto_id");
	            //System.out.println(id+"   "+ime+"    "+prezime);
	            sviTrgovci.add(t);
	         }
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
		return sviTrgovci;
		
	}
	
	
 	public void dodajTrgovca() {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "INSERT INTO trgovac (id, korisnicko_ime, ime, prezime, lozinka, pol, telefon, email, prodajno_mjesto_id)"
			        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, zadnjiId());
			preparedStmt.setString(2, this.korisnicko_ime);
			preparedStmt.setString(3, this.ime);
			preparedStmt.setString(4, this.prezime);
			preparedStmt.setString(5, this.lozinka);
			preparedStmt.setString(6, this.pol);
			preparedStmt.setString(7, this.telefon);
			preparedStmt.setString(8, this.email);
			preparedStmt.setInt(9, this.prodajnoMjestoId);
			
			preparedStmt.execute();
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}

 	public int zadnjiId() {
		ArrayList<Trgovac> l = ucitajTrgovce();
		int zadnji_id = l.get(l.size() - 1).id + 1;
		return zadnji_id;
	}
 	

	static ArrayList<Narudzba> ucitajNarudzbeTrgovca(Trgovac t){
		
		ArrayList<Narudzba> sveNarudzbe = Narudzba.ucitajNarudzbe();
		ArrayList<Narudzba> rez = new ArrayList<Narudzba>();
		
		for(Narudzba n: sveNarudzbe) 
			if(n.trgovac_id == t.id) rez.add(n);
		
		return rez;
	}







}
















