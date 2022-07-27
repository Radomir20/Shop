package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Kupac {
	
	int id;
	String korisnicko_ime, ime, prezime, lozinka, telefon, adresa, grad, drzava, pol, email;
	int postanski_broj;
	ArrayList<Narudzba> spisakSvihNarudzbi;
	
	static String url = "jdbc:mysql://localhost:3306/seminarski_ors1";
	static String username = "root";
	static String password = "ivana123";

	
	public Kupac() {}
	
	public Kupac(String ime, String prezime) {
		this.ime = ime;
		this.prezime = prezime;
	}
	public Kupac(String ime, String prezime, String korisnicko_ime, String lozinka, String email, String telefon, String drzava, 
			String grad, String adresa, String pol, int postanski) {
		
		this.ime = ime;
		this.prezime = prezime;
		this.korisnicko_ime = korisnicko_ime;
		this.lozinka = lozinka;
		this.email = email;
		this.telefon = telefon;
		this.drzava = drzava;
		this.adresa = adresa;
		this.grad = grad;
		this.pol = pol;
		this.postanski_broj = postanski;
		
	}
	
	public String getIme() {
		return ime;
	}
	public String getPrezime() {
		return prezime;
	}
	
	static ArrayList<Kupac> ucitajKupce() {
		
		ArrayList<Kupac> sviKupci = new ArrayList<Kupac>();
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM kupac");
			while (rs.next()) {
				Kupac k = new Kupac();
	            k.id = rs.getInt("id");
	            k.ime = rs.getString("ime");
	            k.prezime = rs.getString("prezime");
	            k.korisnicko_ime = rs.getString("korisnicko_ime");
	            k.lozinka = rs.getString("lozinka");
	            k.telefon = rs.getString("telefon");
	            k.adresa = rs.getString("adresa");
	            k.grad = rs.getString("grad");
	            k.drzava = rs.getString("drzava");
	            k.pol = rs.getString("pol");
	            k.email = rs.getString("email");
	            k.postanski_broj = rs.getInt("postanski_broj");
	            //System.out.println(id+"   "+ime+"    "+prezime);
	            sviKupci.add(k);
	         }
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
		return sviKupci;
		
	}
	
	public int zadnjiId() {
		ArrayList<Kupac> l = ucitajKupce();
		int zadnjiID = l.get(l.size() - 1).id + 1;
		return zadnjiID;
	}
	
	
	public void dodajKupca() {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "INSERT INTO kupac (id, korisnicko_ime, ime, prezime, lozinka, telefon, adresa, grad, drzava, postanski_broj, pol, email)"
			        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, zadnjiId());
			preparedStmt.setString(2, this.korisnicko_ime);
			preparedStmt.setString(3, this.ime);
			preparedStmt.setString(4, this.prezime);
			preparedStmt.setString(5, this.lozinka);
			preparedStmt.setString(6, this.telefon);
			preparedStmt.setString(7, this.adresa);
			preparedStmt.setString(8, this.grad);
			preparedStmt.setString(9, this.drzava);
			preparedStmt.setInt(10, this.postanski_broj);
			preparedStmt.setString(11, this.pol);
			preparedStmt.setString(12, this.email);
			
			preparedStmt.execute();
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
	
	public ArrayList<Narudzba> ucitajSveNarudzbe(){
		
		ArrayList<Narudzba> l = new ArrayList<Narudzba>();
		Narudzba narudzba = new Narudzba();
		spisakSvihNarudzbi = narudzba.ucitajNarudzbe();
		
		for(Narudzba n: spisakSvihNarudzbi) {
			if(n.kupac_id == this.id) {
				l.add(n);
			}
		}
		
		return l;
		
	}
	
	public int brojNarudzbi() {
		ArrayList<Narudzba> l = ucitajSveNarudzbe();
		return l.size();
	}
	
	static String drzavaKupca(int kupac_id) {
		ArrayList<Kupac> kupci = Kupac.ucitajKupce();
		for(Kupac k: kupci) {
			if(kupac_id == k.id) return k.drzava;
		}
		return null;
	}
	
	
	//-----------------------------------
	/*
	static void obrisiKupca() {
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			
			String query = "DELETE FROM kupac WHERE id = ?";
				
			//stmt.executeUpdate(query);
			
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, 426);
			preparedStmt.execute();
			
			connection.close();
			
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	*/
	//-----------------------------------
	
	
}

























