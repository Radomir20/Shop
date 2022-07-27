package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Prodajno_mjesto implements Cloneable {
	private int id;
	private String grad, drzava, adresa, telefon;
	private static ArrayList<Prodajno_mjesto> spisak_prodajnih_mjesta = new ArrayList<>();

	public Prodajno_mjesto(int id, String grad, String drzava, String adresa, String telefon) {
		this.id = id;
		this.grad = grad;
		this.drzava = drzava;
		this.adresa = adresa;
		this.telefon = telefon;
		if (spisak_prodajnih_mjesta.isEmpty())
			spisak_prodajnih_mjesta.add(this);
		else {
			boolean uslov = true;
			for (Prodajno_mjesto pm : spisak_prodajnih_mjesta)
				if (pm.getId() == this.getId())
					uslov = false;
			if (uslov)
				spisak_prodajnih_mjesta.add(this);
		}
	}

	public static void unosIzBaze() {
		spisak_prodajnih_mjesta.clear();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_ors1", "root",
					"sifra123");
			Statement stat = conn.createStatement();
			ResultSet res = stat.executeQuery("select * from prodajno_mjesto");
			while (res.next()) {
				Prodajno_mjesto pm = new Prodajno_mjesto(res.getInt("id"), res.getString("grad"),
						res.getString("drzava"), res.getString("adresa"), res.getString("telefon"));
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getId() {
		return id;
	}

	public String getGrad() {
		return grad;
	}

	public String getDrzava() {
		return drzava;
	}

	public String getAdresa() {
		return adresa;
	}

	public String getTelefon() {
		return telefon;
	}

	public static ArrayList<Prodajno_mjesto> getSpisak_prodajnih_mjesta() {
		return spisak_prodajnih_mjesta;
	}

	static String drzavaPMjesta(int prodajnoMjestoID) {
		ArrayList<Prodajno_mjesto> mjesta = Prodajno_mjesto.getSpisak_prodajnih_mjesta();
		for (Prodajno_mjesto pm : mjesta) {
			if (prodajnoMjestoID == pm.getId())
				return pm.getDrzava();
		}
		return null;
	}

	public static int zadnjiId() {
		ArrayList<Prodajno_mjesto> l = getSpisak_prodajnih_mjesta();
		int zadnji_id = l.get(l.size() - 1).getId() + 1;
		return zadnji_id;
	}

	public void dodajProdajnoMjesto() {

		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_ors1", "root",
					"sifra123");
			// System.out.println("CONNECTED TO THE DATABASE\n");

			Statement stmt = connection.createStatement();

			String query = "INSERT INTO prodajno_mjesto (id, grad, drzava, adresa, telefon)"
					+ " values (?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, zadnjiId());
			preparedStmt.setString(2, this.getGrad());
			preparedStmt.setString(3, this.getDrzava());
			preparedStmt.setString(4, this.getAdresa());
			preparedStmt.setString(5, this.getTelefon());

			preparedStmt.execute();

			connection.close();

		} catch (Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		spisak_prodajnih_mjesta.clear();
		unosIzBaze();
	}

}
