package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Artikal_narudzbe {
	private int id, narudzba_id, proizvod_id, kolicina;
	private double cijena_po_komadu;
	private Proizvod proizvod;
	private static ArrayList<Artikal_narudzbe> spisak_artikala_naruzbi = new ArrayList<>();

	public Artikal_narudzbe(int id, int narudzba_id, int proizvod_id, int kolicina, double cijena_po_komadu) {
		this.id = id;
		this.narudzba_id = narudzba_id;
		this.proizvod_id = proizvod_id;
		this.kolicina = kolicina;
		this.cijena_po_komadu = cijena_po_komadu;
		if (spisak_artikala_naruzbi.isEmpty())
			spisak_artikala_naruzbi.add(this);
		else {
			boolean uslov = true;
			for (Artikal_narudzbe an : spisak_artikala_naruzbi)
				if (an.getId() == this.getId())
					uslov = false;
			if (uslov)
				spisak_artikala_naruzbi.add(this);
		}
		ArrayList<Proizvod> spisak = Proizvod.getSpisak_proizvoda();
		for (Proizvod p : spisak)
			if (p.getId() == this.proizvod_id)
				try {
					proizvod = (Proizvod) p.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
	}

	public static void unosIzBaze() {
		spisak_artikala_naruzbi.clear();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_ors1", "root",
					"sifra123");
			Statement stat = conn.createStatement();
			ResultSet res = stat.executeQuery("select * from artikal_narudzbe");
			while (res.next()) {
				Artikal_narudzbe an = new Artikal_narudzbe(res.getInt("id"), res.getInt("narudzba_id"),
						res.getInt("proizvod_id"), res.getInt("kolicina"), res.getDouble("cijena_po_komadu"));
			}
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public int getNarudzba_id() {
		return narudzba_id;
	}

	public int getProizvod_id() {
		return proizvod_id;
	}

	public int getKolicina() {
		return kolicina;
	}

	public double getCijena_po_komadu() {
		return cijena_po_komadu;
	}

	public Proizvod getProizvod() {
		return proizvod;
	}

	public static ArrayList<Artikal_narudzbe> getSpisak_artikala_naruzbi() {
		return spisak_artikala_naruzbi;
	}

	public static int zadnjiId() {
		Artikal_narudzbe.unosIzBaze();
		ArrayList<Artikal_narudzbe> l = getSpisak_artikala_naruzbi();
		int zadnjiID = l.get(l.size() - 1).id + 1;
		return zadnjiID;
	}
	public static void dodajNoveArtikleNarudzbe(ArrayList<Artikal_narudzbe> l) {
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_ors1", "root",
				"sifra123");
			//System.out.println("CONNECTED TO THE DATABASE\n");
			
			Statement stmt = connection.createStatement();
			
			String query = "INSERT INTO artikal_narudzbe (narudzba_id, proizvod_id, kolicina, cijena_po_komadu, id)"
			        + " values (?, ?, ?, ?, ?)";
			

			
			for(int i = 0 ; i < l.size() ; i++) {
				PreparedStatement preparedStmt = connection.prepareStatement(query);
				
				preparedStmt.setInt(1, l.get(i).narudzba_id);
				preparedStmt.setInt(2, l.get(i).proizvod_id);
				preparedStmt.setInt(3, l.get(i).kolicina);
				preparedStmt.setDouble(4, l.get(i).cijena_po_komadu);
				preparedStmt.setInt(5, l.get(i).id);
				
				preparedStmt.execute();
			}
			
			
			connection.close();
			
		} catch(Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		spisak_artikala_naruzbi.clear();
		unosIzBaze();
		
	}
	

	static void obrisiArtikleNarudzbe(application.Narudzba narudzba) {

		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_ors1", "root",
					"sifra123");

			String query = "DELETE FROM artikal_narudzbe WHERE narudzba_id = ?";


			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, narudzba.getId());
			preparedStmt.execute();

			connection.close();

		} catch (Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		spisak_artikala_naruzbi.clear();
		unosIzBaze();

	}
}
