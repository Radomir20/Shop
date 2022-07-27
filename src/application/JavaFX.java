package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class JavaFX extends Application {
	Kupac kupac;
	Trgovac trgovac;
	Color siva_boja = Color.web("#606060FF", 1.0);
	Color tamna_boja = Color.web("#A8CB10", 1.0);
	String SIVA_BOJA = "#606060FF;";
	String TAMNA_BOJA = "#A8CB10;";
	String SVIJETLA_BOJA = "#D6ED17FF;";

	Stage window;
	Scene scene;

	// main
	public static void main(String[] args) throws CloneNotSupportedException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		unosiIzBaze();

		// pocetna
		VBox pocetna = new VBox(30);
		pocetna.setStyle("-fx-background-color: #606060FF");
		pocetna.setAlignment(Pos.TOP_CENTER);
		pocetna.setPrefSize(900, 600);

		VBox naslovO = new VBox(10);
		naslovO.setAlignment(Pos.TOP_CENTER);
		naslovO.setStyle("-fx-background-color: #D6ED17FF");
		naslovO.setMaxSize(700, 150);
		naslovO.setPadding(new Insets(20));
		Text naslov = new Text();
		naslov.setText("SEMINARSKI RAD");
		naslov.setFont(Font.font("verdana", FontWeight.BOLD, 26));
		naslov.setFill(Color.GRAY);
		Text podnaslov = new Text();
		podnaslov.setText("OBJEKTNO ORIJENTISANO PROGRAMIRANJE");
		podnaslov.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		podnaslov.setFill(Color.GRAY);
		naslovO.getChildren().addAll(naslov, podnaslov);

		Text prijava = new Text();
		prijava.setText("PRIJAVA");
		prijava.setFont(Font.font("verdana", FontWeight.BOLD, 26));
		Color cP = Color.web("#D6ED17FF", 1.0);
		prijava.setFill(cP);

		HBox izbor = new HBox(100);
		izbor.setAlignment(Pos.TOP_CENTER);
		Button kupac = new Button();
		kupac.setStyle(
				"-fx-background-image: url('/application/slike/Kupac.jpg'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-border-width: 5px; -fx-border-style: solid; -fx-border-color: #A8CB10;");
		kupac.setMinSize(300, 300);
		kupac.setOnMouseEntered(e -> {
			kupac.setStyle(
					"-fx-background-color: #D6ED17FF; -fx-border-width: 5px; -fx-border-style: solid; -fx-border-color: #A8CB10;");
			kupac.setText("Kupac");
			kupac.setFont(Font.font("verdana", FontWeight.BOLD, 26));
			Color k = Color.web("#606060FF", 1.0);
			kupac.setTextFill(k);
		});
		kupac.setOnMouseExited(e -> {
			kupac.setStyle(
					"-fx-background-image: url('/application/slike/Kupac.jpg'); -fx-background-repeat: no-repeat; -fx-background-size: cover;  -fx-border-width: 5px; -fx-border-style: solid; -fx-border-color: #A8CB10;;");
			kupac.setText("");
		});
		kupac.setOnAction(e -> {
			setPrijavaKupca();
		});
		Button trgovac = new Button();
		trgovac.setStyle(
				"-fx-background-image: url('/application/slike/Trgovac.jpg'); -fx-background-repeat: no-repeat; -fx-background-size: cover;  -fx-border-width: 5px; -fx-border-style: solid; -fx-border-color: #A8CB10;");
		trgovac.setMinSize(300, 300);
		trgovac.setOnMouseEntered(e -> {
			trgovac.setStyle(
					"-fx-background-color: #D6ED17FF;  -fx-border-width: 5px; -fx-border-style: solid; -fx-border-color: #A8CB10;");
			trgovac.setText("Trgovac");
			trgovac.setFont(Font.font("verdana", FontWeight.BOLD, 26));
			Color t = Color.web("#606060FF", 1.0);
			trgovac.setTextFill(t);
		});
		trgovac.setOnMouseExited(e -> {
			trgovac.setStyle(
					"-fx-background-image: url('/application/slike/Trgovac.jpg'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-border-width: 5px; -fx-border-style: solid; -fx-border-color: #A8CB10;");
			trgovac.setText("");
		});
		trgovac.setOnAction(e -> {
			setPrijavaTrgovca();
		});
		izbor.getChildren().addAll(kupac, trgovac);
		pocetna.getChildren().addAll(naslovO, prijava, izbor);
		scene = new Scene(pocetna);

		// window
		window.setScene(scene);
		window.show();
	}

	public void setPrijavaKupca() {
		// prijava_kupca
		unosiIzBaze();
		VBox prijavaKupca = new VBox(40);
		prijavaKupca.setStyle("-fx-background-color: #606060FF");
		prijavaKupca.setAlignment(Pos.TOP_CENTER);
		prijavaKupca.setPadding(new Insets(40));

		Text prijavaK = new Text();
		prijavaK.setText("PRIJAVA KUPCA");
		prijavaK.setFont(Font.font("verdana", FontWeight.BOLD, 26));
		Color prijavaKB = Color.web("#D6ED17FF", 1.0);
		prijavaK.setFill(prijavaKB);

		VBox unos = new VBox(10);
		unos.setStyle("-fx-background-color: #D6ED17FF");
		unos.setPadding(new Insets(20));
		Text korisnicko = new Text();
		korisnicko.setText("Korisnicko ime");
		korisnicko.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color kor = Color.web("#606060FF", 1.0);
		korisnicko.setFill(kor);
		TextField korisnicko_ime = new TextField("");
		korisnicko_ime.setMinSize(400, 30);
		korisnicko_ime.setAlignment(Pos.CENTER_LEFT);
		Text pass = new Text();
		pass.setText("Lozinka");
		pass.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color p = Color.web("#606060FF", 1.0);
		pass.setFill(p);
		PasswordField lozinka = new PasswordField();
		lozinka.setMinSize(400, 30);
		Text greska = new Text();
		greska.setFont(Font.font("verdana", FontWeight.BOLD, 12));
		Color g = Color.web("#ff0000FF", 1.0);
		greska.setFill(g);
		HBox dugme = new HBox(50);
		dugme.setPadding(new Insets(20, 0, 0, 0));
		Button registruj_se = new Button("Registruj se");
		registruj_se.setMinSize(200, 50);
		registruj_se
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		registruj_se.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color r = Color.web("#606060FF", 1.0);
		registruj_se.setTextFill(r);
		registruj_se.setOnAction(e -> {
			setRegistracijaKupca();
		});
		Button prijavi_se = new Button("Prijavi se");
		prijavi_se.setMinSize(200, 50);
		prijavi_se
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		prijavi_se.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color pri = Color.web("#606060FF", 1.0);
		prijavi_se.setTextFill(pri);
		prijavi_se.setOnAction(e -> {
			boolean uslov = false;
			ArrayList<Kupac> lista = Kupac.getListaKupaca();
			String loz = mdConverter(lozinka.getText());
			for (Kupac k : lista)
				if (korisnicko_ime.getText().equals(k.getKorisnicko_ime()) && loz.equals(k.getLozinka())) {
					kupac = k;
					uslov = true;
				}
			if (!uslov) {
				greska.setText("GRESKA!!!");
				prijavi_se.setDisable(true);
			}
			if (uslov)
				setKupac(kupac);
		});
		korisnicko_ime.textProperty().addListener((observable) -> {
			greska.setText("");
			prijavi_se.setDisable(false);
		});
		lozinka.textProperty().addListener((observable) -> {
			greska.setText("");
			prijavi_se.setDisable(false);
		});

		dugme.getChildren().addAll(prijavi_se, registruj_se);
		unos.getChildren().addAll(korisnicko, korisnicko_ime, pass, lozinka, greska, dugme);

		prijavaKupca.getChildren().addAll(prijavaK, unos);
		scene = new Scene(prijavaKupca);
		window.setScene(scene);

	}

	public void setPrijavaTrgovca() {
		// prijava_trgovca
		unosiIzBaze();
		VBox prijavaTrgovca = new VBox(40);
		prijavaTrgovca.setStyle("-fx-background-color: #606060FF");
		prijavaTrgovca.setAlignment(Pos.TOP_CENTER);
		prijavaTrgovca.setPadding(new Insets(40));

		Text prijavaT = new Text();
		prijavaT.setText("PRIJAVA TRGOVCA");
		prijavaT.setFont(Font.font("verdana", FontWeight.BOLD, 26));
		Color prijavaTB = Color.web("#D6ED17FF", 1.0);
		prijavaT.setFill(prijavaTB);

		VBox unosT = new VBox(10);
		unosT.setStyle("-fx-background-color: #D6ED17FF");
		unosT.setPadding(new Insets(10));
		Text korisnickoT = new Text();
		korisnickoT.setText("Korisnicko ime");
		korisnickoT.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color kT = Color.web("#606060FF", 1.0);
		korisnickoT.setFill(kT);
		TextField korisnicko_imeT = new TextField("");
		korisnicko_imeT.setMinSize(400, 30);
		korisnicko_imeT.setAlignment(Pos.CENTER_LEFT);
		Text passT = new Text();
		passT.setText("Lozinka");
		passT.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color pT = Color.web("#606060FF", 1.0);
		passT.setFill(pT);
		PasswordField lozinkaT = new PasswordField();
		lozinkaT.setMinSize(400, 30);
		Text greska = new Text();
		greska.setFont(Font.font("verdana", FontWeight.BOLD, 12));
		Color g = Color.web("#ff0000FF", 1.0);
		greska.setFill(g);
		HBox dugmeT = new HBox(50);
		dugmeT.setAlignment(Pos.CENTER);
		Button prijavi_seT = new Button("Prijavi se");
		prijavi_seT.setMinSize(200, 50);
		prijavi_seT
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		prijavi_seT.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color priT = Color.web("#606060FF", 1.0);
		prijavi_seT.setTextFill(priT);
		prijavi_seT.setOnAction(e -> {
			boolean uslov = false;
			ArrayList<Trgovac> lista = Trgovac.getSpisak_trgovaca();
			String loz = mdConverter(lozinkaT.getText());
			for (Trgovac t : lista)
				if (korisnicko_imeT.getText().equals(t.getKorisnicko_ime()) && loz.equals(t.getLozinka())) {
					trgovac = t;
					uslov = true;
				}
			if (!uslov) {
				greska.setText("GRESKA!!!");
				prijavi_seT.setDisable(true);
			}
			if (uslov)
				setTrgovacScene(trgovac);
		});
		korisnicko_imeT.textProperty().addListener((observable) -> {
			greska.setText("");
			prijavi_seT.setDisable(false);
		});
		lozinkaT.textProperty().addListener((observable) -> {
			greska.setText("");
			prijavi_seT.setDisable(false);
		});
		dugmeT.getChildren().add(prijavi_seT);
		unosT.getChildren().addAll(korisnickoT, korisnicko_imeT, passT, lozinkaT, greska, dugmeT);
		prijavaTrgovca.getChildren().addAll(prijavaT, unosT);
		scene = new Scene(prijavaTrgovca);
		window.setScene(scene);

	}

	public void setKupac(Kupac k) {
		unosiIzBaze();
		VBox kupacFX = new VBox();
		HBox kupacInfo = new HBox(20);
		kupacInfo.setPadding(new Insets(20));
		kupacInfo.setStyle("-fx-background-color: #606060FF");
		Image kupacProfilna = null;
		try {
			kupacProfilna = new Image(
					new FileInputStream("D:\\Seminarski\\Seminarski\\src\\application\\slike\\KupacProfilna.png"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImageView kupacSlika = new ImageView(kupacProfilna);
		kupacSlika.setFitHeight(150);
		kupacSlika.setFitWidth(150);
		Color cbB = Color.web("#D6ED17FF", 1.0);
		VBox kupacL = new VBox(20);
		Text kupacIme = new Text("Ime: " + k.getIme());
		kupacIme.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		kupacIme.setFill(cbB);
		Text kupacPrezime = new Text("Prezime: " + k.getPrezime());
		kupacPrezime.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		kupacPrezime.setFill(cbB);
		Text kupacGrad = new Text("Grad: " + k.getGrad());
		kupacGrad.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		kupacGrad.setFill(cbB);
		kupacL.getChildren().addAll(kupacIme, kupacPrezime, kupacGrad);
		VBox kupacR = new VBox(20);
		Text kupacAdresa = new Text("Postanski broj: " + k.getPostanski_broj());
		kupacAdresa.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		kupacAdresa.setFill(cbB);
		Text kupacDrzava = new Text("Drzava: " + k.getDrzava());
		kupacDrzava.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		kupacDrzava.setFill(cbB);
		HBox kupacDugme = new HBox(20);
		kupacDugme.setAlignment(Pos.CENTER);
		Button nazad = new Button("Nazad");
		nazad.setMinSize(130, 30);
		nazad.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		nazad.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color narD = Color.web("#606060FF", 1.0);
		nazad.setTextFill(narD);
		nazad.setOnAction(e -> {
			setPrijavaKupca();
		});
		Button naruci = new Button("Naruci");
		naruci.setMinSize(130, 30);
		naruci.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		naruci.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		naruci.setTextFill(narD);
		naruci.setOnAction(e -> {
			naruci(k);
		});
		kupacDugme.getChildren().addAll(nazad, naruci);
		kupacR.getChildren().addAll(kupacAdresa, kupacDrzava, kupacDugme);
		kupacInfo.getChildren().addAll(kupacSlika, kupacL, kupacR);

		HBox kupacNarudzbe = new HBox(10);
		kupacNarudzbe.setPadding(new Insets(20));
		kupacNarudzbe.setStyle("-fx-background-color: #606060FF");
		TableView tabela = new TableView();
		tabela.setPrefSize(350, 200);
		tabela.setStyle("-fx-base: " + TAMNA_BOJA + "-fx-control-inner-background: " + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");

		TableColumn<Narudzba, Integer> kol1 = new TableColumn<>("Cijena narudžbe");
		kol1.setCellValueFactory(new PropertyValueFactory<>("vrijednost_narudzbe"));

		TableColumn<Narudzba, String> kol2 = new TableColumn<>("Datum Narudžbe");
		kol2.setCellValueFactory(new PropertyValueFactory<>("datum_isporuke"));

		TableColumn<Narudzba, String> kol3 = new TableColumn<>("Datum Isporuke");
		kol3.setCellValueFactory(new PropertyValueFactory<>("datum_narudzbe"));

		tabela.getColumns().add(kol1);
		tabela.getColumns().add(kol2);
		tabela.getColumns().add(kol3);

		ArrayList<Narudzba> l1 = k.getSpisak_narudzbi();

		for (int i = 0; i < l1.size(); i++) {
			if (l1.get(i).getDatum_isporuke() != null)
				try {
					tabela.getItems()
							.add(new Narudzba(l1.get(i).getId(), l1.get(i).getKupac_id(), l1.get(i).getTrgovac_id(),
									l1.get(i).getDatum_isporuke(), l1.get(i).getDatum_narudzbe(),
									l1.get(i).getNapomena()));
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}

		tabela.setRowFactory(tv -> {
			TableRow<Narudzba> kolona = new TableRow<>();
			kolona.setOnMouseClicked(event -> {
				if (!kolona.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					Narudzba odabranaKol = kolona.getItem();
					popup1(odabranaKol);
				}
			});
			return kolona;
		});
		// ------//
		TableView tabela2 = new TableView();
		tabela2.setStyle("-fx-base: " + TAMNA_BOJA + "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");

		TableColumn<Narudzba, Double> kolona1 = new TableColumn<>("Vrijednost narudžbe");
		kolona1.setCellValueFactory(new PropertyValueFactory<>("vrijednost_narudzbe"));
		kolona1.setMinWidth(150);

		TableColumn<Narudzba, String> kolona2 = new TableColumn<>("Datum Narudžbe");
		kolona2.setCellValueFactory(new PropertyValueFactory<>("datum_isporuke"));
		kolona2.setMinWidth(150);

		tabela2.getColumns().add(kolona1);
		tabela2.getColumns().add(kolona2);

		ArrayList<Narudzba> l2 = k.getSpisak_narudzbi();

		for (int i = 0; i < l2.size(); i++) {
			if (l2.get(i).getDatum_isporuke() == null)
				try {
					tabela2.getItems()
							.addAll(new Narudzba(l1.get(i).getId(), l1.get(i).getKupac_id(), l1.get(i).getTrgovac_id(),
									l1.get(i).getDatum_isporuke(), l1.get(i).getDatum_narudzbe(),
									l1.get(i).getNapomena()));
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}

		tabela2.setRowFactory(tv -> {
			TableRow<Narudzba> kolona = new TableRow<>();
			kolona.setOnMouseClicked(event -> {
				if (!kolona.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					Narudzba odabranaKol = kolona.getItem();
					popup2(odabranaKol);
				}
			});
			return kolona;
		});

		kupacNarudzbe.getChildren().addAll(tabela, tabela2);
		kupacFX.getChildren().addAll(kupacInfo, kupacNarudzbe);

		scene = new Scene(kupacFX);
		window.setScene(scene);
	}

	public void popup1(Narudzba n) {

		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(window);
		dialog.setTitle("Detaljno o narudzbi");

		// Rajko, ce55e837ba3d1d3bf3b01d0ef41dc537

		VBox vb = new VBox(10);
		vb.setPadding(new Insets(10, 10, 10, 10));
		Label napomenaL = new Label("Napomena: " + n.getNapomena());
		napomenaL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);

		// ---------------------------------------------------------

		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA + "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");

		TableColumn<Proizvod, String> kol1 = new TableColumn<>("Proizvod");
		kol1.setCellValueFactory(new PropertyValueFactory<>("proizvod"));
		kol1.setMinWidth(120);

		TableColumn<Artikal_narudzbe, Double> kol2 = new TableColumn<>("Cijena");
		kol2.setCellValueFactory(new PropertyValueFactory<>("cijena_po_komadu"));
		kol2.setMinWidth(120);

		TableColumn<Artikal_narudzbe, Integer> kol3 = new TableColumn<>("Kolicina");
		kol3.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
		kol3.setMinWidth(120);

		tabela.getColumns().add(kol1);
		tabela.getColumns().add(kol2);
		tabela.getColumns().add(kol3);

		for (int i = 0; i < n.getSpisak_artikala().size(); i++)
			tabela.getItems().add(new Artikal_narudzbe(n.getSpisak_artikala().get(i).getId(),
					n.getSpisak_artikala().get(i).getNarudzba_id(), n.getSpisak_artikala().get(i).getProizvod_id(),
					n.getSpisak_artikala().get(i).getKolicina(), n.getSpisak_artikala().get(i).getCijena_po_komadu()));

		vb.getChildren().addAll(napomenaL, tabela);
		vb.setStyle("-fx-background-color: " + TAMNA_BOJA);

		Scene dialogScene = new Scene(vb, 400, 300);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void popup2(Narudzba n) {

		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(window);
		dialog.setTitle("Detaljno o narudzbi");

		// Svetlana, c3d636bce8c725b5f2f9a20db7821c12

		VBox vb = new VBox(10);
		vb.setPadding(new Insets(10, 10, 10, 10));

		Label napomenaL = new Label("Napomena: " + n.getNapomena());
		napomenaL.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);

		// ---------------------------------------------------------

		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA + "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");

		TableColumn<Artikal_narudzbe, String> kol1 = new TableColumn<>("Proizvod");
		kol1.setCellValueFactory(new PropertyValueFactory<>("proizvod"));
		kol1.setMinWidth(80);

		TableColumn<Artikal_narudzbe, Float> kol2 = new TableColumn<>("Cijena");
		kol2.setCellValueFactory(new PropertyValueFactory<>("cijena_po_komadu"));
		kol2.setMinWidth(80);

		TableColumn<Artikal_narudzbe, Integer> kol3 = new TableColumn<>("Kolièina");
		kol3.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
		kol3.setMinWidth(80);

		tabela.getColumns().add(kol1);
		tabela.getColumns().add(kol2);
		tabela.getColumns().add(kol3);

		for (int i = 0; i < n.getSpisak_artikala().size(); i++) {
			tabela.getItems().add(new Artikal_narudzbe(n.getSpisak_artikala().get(i).getId(),
					n.getSpisak_artikala().get(i).getNarudzba_id(), n.getSpisak_artikala().get(i).getProizvod_id(),
					n.getSpisak_artikala().get(i).getKolicina(), n.getSpisak_artikala().get(i).getCijena_po_komadu()));
		}

		// -----------------------------------------------------------------------------

		Button otkazi = new Button("Otkaži narudžbu");
		otkazi.setOnMouseClicked(event -> {
			n.otkaziNarudzbu();
			setKupac(kupac);
			dialog.close();
		});
		otkazi.setStyle("-fx-font-weight: bold; -fx-background-color: " + SIVA_BOJA + "-fx-text-fill: " + TAMNA_BOJA);

		vb.getChildren().addAll(otkazi, napomenaL, tabela);
		vb.setStyle("-fx-background-color:" + TAMNA_BOJA);

		Scene dialogScene = new Scene(vb, 400, 300);
		dialog.setScene(dialogScene);
		dialog.show();

	}

	public void naruci(Kupac kupac) {
		TableView tabela = new TableView();
		tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tabela.setStyle("-fx-base: " + TAMNA_BOJA + "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");
		TableColumn<Map, String> kolonaNaziv = new TableColumn<>("Naziv");
		TableColumn<Map, String> kolonaCijena = new TableColumn<>("Cijena");
		kolonaNaziv.setCellValueFactory(new MapValueFactory<>("naziv"));
		kolonaCijena.setCellValueFactory(new MapValueFactory<>("cijena"));
		tabela.getColumns().addAll(kolonaNaziv, kolonaCijena);

		tabela.setMaxHeight(300);
		tabela.setPrefWidth(400);

		ScrollPane sp2 = new ScrollPane();
		sp2.setContent(tabela);
		sp2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		sp2.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		TreeMap<Integer, Proizvod> proizvodi = Proizvod.getProizvodi();
		for (Proizvod p : proizvodi.values()) {

			Map<String, Object> m1 = new HashMap<>();
			m1.put("naziv", p.getNaziv());
			DecimalFormat df = new DecimalFormat("#.00");
			m1.put("cijena", df.format(p.getCijena()));
			m1.put("id", p.getId());

			tabela.getItems().add(m1);
		}

		VBox desniB = new VBox(10);
		desniB.setAlignment(Pos.CENTER);
		Label kolicina = new Label("Kolicina:");
		kolicina.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		TextField kolicinaTF = new TextField();
		kolicinaTF.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);
		kolicinaTF.setBackground(new Background((new BackgroundFill(tamna_boja, CornerRadii.EMPTY, Insets.EMPTY))));
		kolicinaTF.setMaxWidth(100);
		Button dodaj = new Button("Dodaj u korpu");
		dodaj.setStyle("-fx-font-weight: bold; -fx-background-color: " + TAMNA_BOJA + "-fx-text-fill: " + SIVA_BOJA);
		Label greska = new Label("");
		greska.setTextFill(Color.RED);
		greska.setManaged(false);
		greska.setVisible(false);
		VBox desniDjeca = new VBox(10);
		desniDjeca.getChildren().addAll(kolicina, kolicinaTF, greska, dodaj);
		desniB.getChildren().add(desniDjeca);
		VBox tabela5VB = new VBox(tabela);
		tabela5VB.setPadding(new Insets(20, 10, 10, 20));
		HBox tabela5HB = new HBox(10);
		tabela5HB.getChildren().addAll(tabela5VB, desniB);

		Label korpa = new Label("Korpa:");
		korpa.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		TableView tabela2 = new TableView();
		tabela2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tabela2.setStyle("-fx-base: " + TAMNA_BOJA + "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");
		TableColumn<Map, String> kolonaNaziv1 = new TableColumn<>("Naziv");
		TableColumn<Map, String> kolonaCijena1 = new TableColumn<>("Cijena");
		TableColumn<Map, String> kolonaKolicina1 = new TableColumn<>("Kolicina");
		kolonaNaziv1.setCellValueFactory(new MapValueFactory<>("naziv"));
		kolonaCijena1.setCellValueFactory(new MapValueFactory<>("cijena"));
		kolonaKolicina1.setCellValueFactory(new MapValueFactory<>("kolicina"));
		tabela2.getColumns().addAll(kolonaNaziv1, kolonaCijena1, kolonaKolicina1);

		tabela2.setMaxHeight(150);
		tabela2.setMinWidth(400);

		ScrollPane sp3 = new ScrollPane();
		sp3.setContent(tabela2);
		sp3.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		sp3.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		Button izbaci = new Button("Izbaci iz korpe");
		izbaci.setStyle("-fx-font-weight: bold; -fx-background-color: " + TAMNA_BOJA + "-fx-text-fill: " + SIVA_BOJA);
		Button isprazni = new Button("Isprazni korpu");
		isprazni.setStyle("-fx-font-weight: bold; -fx-background-color: " + TAMNA_BOJA + "-fx-text-fill: " + SIVA_BOJA);
		VBox izbVB = new VBox(10);
		izbVB.setAlignment(Pos.CENTER);
		izbVB.getChildren().addAll(izbaci, isprazni);
		Label ukupanIzn = new Label("Ukupan iznos:");
		ukupanIzn.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		Label ukupanIznTF = new Label();
		ukupanIznTF.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		ukupanIznTF.setAlignment(Pos.TOP_LEFT);
		ukupanIznTF.setMinWidth(100);
		ukupanIznTF.setMinHeight(20);
		ukupanIznTF.setBackground(new Background((new BackgroundFill(siva_boja, CornerRadii.EMPTY, Insets.EMPTY))));
		Label greskaUk = new Label();
		greskaUk.setTextFill(Color.RED);
		HBox ukupanIznHB = new HBox(10);
		ukupanIznHB.getChildren().addAll(ukupanIzn, ukupanIznTF, greskaUk);
		VBox kosarica = new VBox(5);
		kosarica.getChildren().addAll(korpa, tabela2, ukupanIznHB);

		Button naruci = new Button("Narucite");
		naruci.setStyle("-fx-font-weight: bold; -fx-background-color: " + TAMNA_BOJA + "-fx-text-fill: " + SIVA_BOJA);
		Button nazad = new Button("Nazad");
		nazad.setStyle("-fx-font-weight: bold; -fx-background-color: " + TAMNA_BOJA + "-fx-text-fill: " + SIVA_BOJA);
		HBox dugNN = new HBox(20);
		dugNN.getChildren().addAll(naruci, nazad);
		dugNN.setPadding(new Insets(10, 10, 10, 20));
		HBox kosIzb = new HBox(10);
		kosIzb.getChildren().addAll(kosarica, izbVB);
		kosIzb.setPadding(new Insets(10, 10, 10, 20));
		VBox narudzba = new VBox(10);
		narudzba.setBackground(
				new Background((new BackgroundFill(Color.rgb(244, 244, 244), CornerRadii.EMPTY, Insets.EMPTY))));
		narudzba.getChildren().addAll(tabela5HB, kosIzb, dugNN);
		narudzba.setStyle("-fx-background-color: " + SIVA_BOJA);
		Scene scena = new Scene(narudzba, 600, 650);
		window.setScene(scena);

		dodaj.setOnAction(exc -> {
			;
			greskaUk.setText("");
			Map<String, Object> selected = (HashMap<String, Object>) tabela.getSelectionModel().getSelectedItem();
			if (selected == null)
				return;
			if (kolicinaTF.getText().equals("") || kolicinaTF.getText().equals("0")) {
				greska.setVisible(true);
				greska.setManaged(true);
				greska.setText("Nije validan unos!");
				return;
			}

			int kolicinaBroj = 0;
			try {
				kolicinaBroj = Integer.parseInt(kolicinaTF.getText());
			} catch (Exception excep) {
				greska.setText("Nije validan unos!");
				greska.setVisible(true);
				greska.setManaged(true);
				return;
			}
			if (greska.isManaged() && greska.isVisible()) {
				greska.setVisible(false);
				greska.setManaged(false);
			}

			int ID1 = (int) selected.get("id");
			Proizvod proizvod = proizvodi.get(ID1);

			double ukupno = 0;
			for (Object mapa : tabela2.getItems()) {
				Map<String, Object> m = (HashMap<String, Object>) mapa;
				ukupno += (double) m.get("cijena") * (int) m.get("kolicina");
			}

			for (Object mapa : tabela2.getItems()) {
				Map<String, Object> m = (HashMap<String, Object>) mapa;
				if ((int) m.get("id") == ID1) {
					m.put("kolicina", (int) m.get("kolicina") + kolicinaBroj);
					ukupno += (double) m.get("cijena") * kolicinaBroj;
					DecimalFormat df = new DecimalFormat("#.00");
					BigDecimal bd = BigDecimal.valueOf(ukupno);
					bd = bd.setScale(3, RoundingMode.HALF_UP);
					ukupanIznTF.setText(bd.doubleValue() + "");
					tabela2.refresh();
					return;
				}
			}
			DecimalFormat df = new DecimalFormat("#.00");
			Map<String, Object> m1 = new HashMap<>();
			m1.put("naziv", proizvod.getNaziv());
			BigDecimal bd = BigDecimal.valueOf(proizvod.getCijena());
			bd = bd.setScale(3, RoundingMode.HALF_UP);
			m1.put("cijena", bd.doubleValue());
			m1.put("kolicina", kolicinaBroj);
			m1.put("id", proizvod.getId());

			tabela2.getItems().add(m1);
			ukupno += proizvod.getCijena() * kolicinaBroj;
			BigDecimal bd1 = BigDecimal.valueOf(ukupno);
			bd1 = bd1.setScale(3, RoundingMode.HALF_UP);
			ukupanIznTF.setText(bd1.doubleValue() + "");
			kolicinaTF.setText("");
		});

		izbaci.setOnAction(exc -> {
			Map<String, Object> selected = (HashMap<String, Object>) tabela2.getSelectionModel().getSelectedItem();
			if (selected == null)
				return;
			int ID1 = (int) selected.get("id");
			if (tabela2.getItems().size() == 1)
				ukupanIznTF.setText("");
			else {
				double ukupno = Double.parseDouble(ukupanIznTF.getText())
						- (double) selected.get("cijena") * (int) selected.get("kolicina");
				BigDecimal bd1 = BigDecimal.valueOf(ukupno);
				bd1 = bd1.setScale(3, RoundingMode.HALF_UP);
				ukupanIznTF.setText(bd1.doubleValue() + "");
			}
			tabela2.getItems().remove(selected);
		});
		isprazni.setOnAction(exc -> {
			ukupanIznTF.setText("");
			tabela2.getItems().clear();
			tabela2.refresh();
		});
		naruci.setOnAction(exc -> {
			if (ukupanIznTF.getText().isEmpty()) {
				greskaUk.setText("Korpa je prazna!");
				return;
			}

			int narID = Narudzba.zadnjiId();
			int kupID = kupac.getId();
			LocalDate datum = java.time.LocalDate.now();
			String datumNar = datum.toString();
			String datumIsp = null;
			String napomena = null;
			try {
				Narudzba nar = new Narudzba(narID, kupID, -1, datumNar, datumIsp, napomena);
				Narudzba.dodajNovuNarudzbu(nar);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			ArrayList<Artikal_narudzbe> anLista = new ArrayList<>();
			for (Object mapa : tabela2.getItems()) {
				Map<String, Object> m = (HashMap<String, Object>) mapa;
				int proID = (int) m.get("id");
				int kolPr = (int) m.get("kolicina");
				double cijP = (double) m.get("cijena");
				int anID = Artikal_narudzbe.zadnjiId();
				Artikal_narudzbe an = new Artikal_narudzbe(anID, narID, proID, kolPr, cijP);
				anLista.add(an);
			}
			Artikal_narudzbe.dodajNoveArtikleNarudzbe(anLista);
			setKupac(kupac);

		});
		nazad.setOnAction(exc -> {
			setKupac(kupac);
		});

	}

	public void setRegistracijaKupca() {
		unosiIzBaze();
		// registracija_kupca
		VBox registracijaKupca = new VBox(40);
		registracijaKupca.setStyle("-fx-background-color: #606060FF");
		registracijaKupca.setAlignment(Pos.TOP_CENTER);
		registracijaKupca.setPadding(new Insets(40));

		Text registracija = new Text();
		registracija.setText("REGISTRACIJA KUPCA");
		registracija.setFont(Font.font("verdana", FontWeight.BOLD, 26));
		Color c = Color.web("#D6ED17FF", 1.0);
		registracija.setFill(c);
		Button nazad = new Button("< Nazad");
		nazad.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		nazad.setOnAction(e ->{
			setPrijavaKupca();
		});
		HBox top = new HBox(200);
		top.getChildren().addAll(nazad, registracija);

		HBox unosK = new HBox();
		VBox lijevi = new VBox(10);
		lijevi.setStyle("-fx-background-color: #D6ED17FF");
		lijevi.setPadding(new Insets(20));
		Text txt_ime = new Text();
		txt_ime.setText("Ime");
		txt_ime.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color imeB = Color.web("#606060FF", 1.0);
		txt_ime.setFill(imeB);
		TextField ime = new TextField("");
		ime.setMinSize(400, 30);
		ime.setAlignment(Pos.CENTER_LEFT);
		Text txt_prezime = new Text();
		txt_prezime.setText("Prezime");
		txt_prezime.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color prezimeB = Color.web("#606060FF", 1.0);
		txt_prezime.setFill(prezimeB);
		TextField prezime = new TextField("");
		prezime.setMinSize(400, 30);
		prezime.setAlignment(Pos.CENTER_LEFT);
		Text korisnickoK = new Text();
		korisnickoK.setText("Korisnicko ime");
		korisnickoK.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color korisnickoKB = Color.web("#606060FF", 1.0);
		korisnickoK.setFill(korisnickoKB);
		TextField korisnicko_imeK = new TextField("");
		korisnicko_imeK.setMinSize(400, 30);
		korisnicko_imeK.setAlignment(Pos.CENTER_LEFT);
		Text passK = new Text();
		passK.setText("Lozinka");
		passK.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color pK = Color.web("#606060FF", 1.0);
		passK.setFill(pK);
		PasswordField lozinkaK = new PasswordField();
		lozinkaK.setMinSize(400, 30);
		Text txt_pp = new Text();
		txt_pp.setText("Ponovi lozinku");
		txt_pp.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color pp = Color.web("#606060FF", 1.0);
		txt_pp.setFill(pp);
		PasswordField lozinkaP = new PasswordField();
		lozinkaP.setMinSize(400, 30);
		Text txt_telefon = new Text();
		txt_telefon.setText("Broj telefona");
		txt_telefon.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color telefonB = Color.web("#606060FF", 1.0);
		txt_telefon.setFill(telefonB);
		TextField telefon = new TextField("");
		telefon.setMinSize(400, 30);
		telefon.setAlignment(Pos.CENTER_LEFT);
		lijevi.getChildren().addAll(txt_ime, ime, txt_prezime, prezime, korisnickoK, korisnicko_imeK, passK, lozinkaK,
				txt_pp, lozinkaP, txt_telefon, telefon);
		VBox desni = new VBox(10);
		desni.setStyle("-fx-background-color: #D6ED17FF");
		desni.setPadding(new Insets(20));
		Text txt_grad = new Text();
		txt_grad.setText("Grad");
		txt_grad.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color gradB = Color.web("#606060FF", 1.0);
		txt_grad.setFill(gradB);
		TextField grad = new TextField("");
		grad.setMinSize(400, 30);
		grad.setAlignment(Pos.CENTER_LEFT);
		Text txt_drzava = new Text();
		txt_drzava.setText("Drzava");
		txt_drzava.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color drzavaB = Color.web("#606060FF", 1.0);
		txt_drzava.setFill(drzavaB);
		TextField drzava = new TextField("");
		drzava.setMinSize(400, 30);
		drzava.setAlignment(Pos.CENTER_LEFT);
		Text txt_email = new Text();
		txt_email.setText("Email");
		txt_email.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color emailB = Color.web("#606060FF", 1.0);
		txt_email.setFill(emailB);
		TextField email = new TextField("");
		email.setMinSize(400, 30);
		email.setAlignment(Pos.CENTER_LEFT);
		Text txt_pb = new Text();
		txt_pb.setText("Postanski broj");
		txt_pb.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color pbB = Color.web("#606060FF", 1.0);
		txt_pb.setFill(pbB);
		TextField postanski_broj = new TextField("");
		postanski_broj.setMinSize(400, 30);
		postanski_broj.setAlignment(Pos.CENTER_LEFT);
		Text greskaK = new Text();
		greskaK.setFont(Font.font("verdana", FontWeight.BOLD, 12));
		Color gK = Color.web("#ff0000FF", 1.0);
		greskaK.setFill(gK);
		HBox dugmeK = new HBox(20);
		dugmeK.setPadding(new Insets(20, 0, 0, 0));
		Text txt_cb = new Text();
		txt_cb.setText("Pol");
		txt_cb.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color cbB = Color.web("#606060FF", 1.0);
		txt_cb.setFill(cbB);
		ChoiceBox<String> choiceBox = new ChoiceBox<String>();
		choiceBox.getItems().add("Musko");
		choiceBox.getItems().add("Zensko");
		choiceBox.setMinWidth(190);
		Button prijavi_seK = new Button("Prijavi se");
		prijavi_seK.setMinSize(190, 50);
		prijavi_seK
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		prijavi_seK.setFont(Font.font("verdana", FontWeight.BOLD, 18));
		Color priK = Color.web("#606060FF", 1.0);
		prijavi_seK.setTextFill(priK);
		prijavi_seK.setOnAction(ev -> {
			boolean uslov = true;
			if (ime.getText().equals("") || prezime.getText().equals("") || korisnicko_imeK.getText().equals("")
					|| lozinkaK.getText().equals("") || lozinkaP.getText().equals("") || telefon.getText().equals("")
					|| grad.getText().equals("") || drzava.getText().equals("") || email.getText().equals("")
					|| !lozinkaP.getText().equals(lozinkaK.getText()) || postanski_broj.getText().equals("")
					|| choiceBox.getValue().equals("") || !isNumeric(postanski_broj.getText()))
				uslov = false;
			ArrayList<Kupac> listaKupaca = Kupac.getListaKupaca();
			for (Kupac k : listaKupaca)
				if (korisnicko_imeK.getText().equals(k.getKorisnicko_ime()))
					uslov = false;
			int id = Kupac.zadnjiId();
			if (!uslov) {
				greskaK.setText("GRESKA!!!");
			} else
				greskaK.setText("");
			if (uslov) {
				Kupac k = new Kupac(id, Integer.parseInt(postanski_broj.getText()), korisnicko_imeK.getText(),
						ime.getText(), prezime.getText(), mdConverter(lozinkaK.getText()), telefon.getText(),
						grad.getText(), drzava.getText(), choiceBox.getValue(), email.getText());
				Kupac.dodajKupca(k);
				setPrijavaKupca();
			}
		});
		dugmeK.getChildren().addAll(txt_cb, choiceBox, prijavi_seK);
		desni.getChildren().addAll(txt_grad, grad, txt_drzava, drzava, txt_email, email, txt_pb, postanski_broj, dugmeK,
				greskaK);
		unosK.getChildren().addAll(lijevi, desni);

		registracijaKupca.getChildren().addAll(top, unosK);
		scene = new Scene(registracijaKupca);

		window.setScene(scene);
	}

	// TRGOVAC
	public void setTrgovacScene(Trgovac t) {
		unosiIzBaze();
		BorderPane root = new BorderPane();

		Label naslov = new Label("PRIJAVLJENI STE KAO TRGOVAC");
		naslov.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-background-color: " + TAMNA_BOJA
				+ "-fx-text-fill: " + SIVA_BOJA);
		naslov.setPadding(new Insets(10, 10, 10, 10));
		Button nazad = new Button("< Nazad");
		nazad.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		nazad.setOnAction(e ->{
			setPrijavaTrgovca();
		});
		HBox top = new HBox(150);
		top.getChildren().addAll(nazad, naslov);


		// LIJEVI MENI
		Label imeL = new Label("Ime: " + t.getIme() + " " + t.getPrezime());
		imeL.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		Label korImeL = new Label("Korisnicko ime: " + t.getKorisnicko_ime());
		korImeL.setStyle("-fx-font-size: 16; -fx-text-fill:" + TAMNA_BOJA);
		Label emailL = new Label("Email: " + t.getEmail());
		emailL.setStyle("-fx-font-size: 16; -fx-text-fill:" + TAMNA_BOJA);
		Label telL = new Label("Tel: " + t.getTelefon());
		telL.setStyle("-fx-font-size: 16; -fx-text-fill: " + TAMNA_BOJA);
		Label polL = new Label("Pol: " + t.getPol());
		polL.setStyle("-fx-font-size: 16; -fx-text-fill:" + TAMNA_BOJA);

		HBox hb1 = new HBox(30);
		HBox hb2 = new HBox(30);
		Button sveNarudzbeB = new Button();
		sveNarudzbeB.setText("Sve narudžbe");
		Button prodMjestoB = new Button();
		prodMjestoB.setText("Dodaj prodajno mjesto");
		hb1.getChildren().addAll(sveNarudzbeB, prodMjestoB);
		hb1.setPadding(new Insets(60, 0, 0, 0));
		Button noviTrgovacB = new Button();
		noviTrgovacB.setText("Dodaj trgovca");
		Button azurirajProizvodB = new Button();
		azurirajProizvodB.setText("Azuriraj Proizvod");
		hb2.getChildren().addAll(noviTrgovacB, azurirajProizvodB);

		sveNarudzbeB
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		prodMjestoB
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		noviTrgovacB
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);
		azurirajProizvodB
				.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);

		VBox lijevo = new VBox();
		lijevo.getChildren().addAll(imeL, korImeL, emailL, telL, polL, hb1, hb2);
		lijevo.setSpacing(15);
		lijevo.setPadding(new Insets(50, 20, 40, 30));

		// DESNI MENI

		VBox desno = new VBox(10);

		Label obavjestenjaL = new Label("Obavještenja o narudžbama");
		obavjestenjaL.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);

		ArrayList<Narudzba> obavjestenja = Narudzba.ucitajObavjestenja(t);

		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA + "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");

		TableColumn<Narudzba, Integer> kol1 = new TableColumn<>("ID");
		kol1.setCellValueFactory(new PropertyValueFactory<>("id"));
		kol1.setMinWidth(100);
		kol1.setStyle("-fx-text-fill:" + SIVA_BOJA);

		TableColumn<Narudzba, Void> kol2 = new TableColumn(" ");
		kol2.setMinWidth(100);
		kol2.setStyle("-fx-text-fill:" + SIVA_BOJA);

		Callback<TableColumn<Narudzba, Void>, TableCell<Narudzba, Void>> cellFactory = new Callback<TableColumn<Narudzba, Void>, TableCell<Narudzba, Void>>() {
			@Override
			public TableCell<Narudzba, Void> call(final TableColumn<Narudzba, Void> param) {
				final TableCell<Narudzba, Void> cell = new TableCell<Narudzba, Void>() {

					private final Button btn = new Button("Prihvati");
					{
						btn.setOnAction(event -> {
							Narudzba n = getTableView().getItems().get(getIndex());
							LocalDate datum = java.time.LocalDate.now();
							String datumIporuke = datum.toString();
							Narudzba.prihvatiNarudzbu(n.getId(), trgovac.getId(), datumIporuke);
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};

		kol2.setCellFactory(cellFactory);

		tabela.getColumns().add(kol1);
		tabela.getColumns().add(kol2);

		for (int i = 0; i < obavjestenja.size(); i++) {
			tabela.getItems().add(new Narudzba(obavjestenja.get(i).getId()));
		}

		// PRIKAZI SVE NARUDZBE - TRGOVAC
		sveNarudzbeB.setOnAction(event -> {
			setNarudzbeTrgovacScene();
		});
		// DODAVANJE NOVOG PRODAJNOG MJESTA - TRGOVAC
		prodMjestoB.setOnAction(event -> {
			dodajProdajnoMjestoScene();
		});
		// DODAVANJE NOVOG TRGOVCA
		noviTrgovacB.setOnAction(action -> {
			dodajTrgovcaScene();
		});
		// AZURIRANJE PROIZVODA - TRGOVAC
		azurirajProizvodB.setOnAction(event -> {
			azurirajProizvodScene();
		});

		desno.getChildren().addAll(obavjestenjaL, tabela);
		desno.setSpacing(30);
		desno.setPadding(new Insets(50, 40, 60, 0));

		root.setTop(top);
		root.setLeft(lijevo);
		root.setRight(desno);
		root.setStyle("-fx-background-color: " + SIVA_BOJA);
		BorderPane.setAlignment(naslov, Pos.CENTER);

		Scene scene = new Scene(root, 700, 500);
		window.setScene(scene);
		window.show();

	}
	
	public void setNarudzbeTrgovacScene() {

		Button nazad = new Button("< Nazad");
		nazad.setPrefSize(80, 35);
		nazad.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);

		nazad.setOnMouseClicked(event -> {
			setTrgovacScene(trgovac);
		});

		// ---------------------------------------------

		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + SIVA_BOJA + "-fx-control-inner-background:" + SIVA_BOJA
				+ "-fx-background-color: " + SIVA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");

		TableColumn<Narudzba, Integer> kol1 = new TableColumn<>("Cijena narudžbe");
		kol1.setCellValueFactory(new PropertyValueFactory<>("vrijednost_narudzbe"));
		kol1.setStyle("-fx-text-fill:" + TAMNA_BOJA);

		TableColumn<Narudzba, String> kol2 = new TableColumn<>("Datum Narudžbe");
		kol2.setCellValueFactory(new PropertyValueFactory<>("datum_narudzbe"));
		kol2.setStyle("-fx-text-fill:" + TAMNA_BOJA);

		TableColumn<Narudzba, String> kol3 = new TableColumn<>("Datum Isporuke");
		kol3.setCellValueFactory(new PropertyValueFactory<>("datum_isporuke"));
		kol3.setStyle("-fx-text-fill:" + TAMNA_BOJA);

		tabela.getColumns().add(kol1);
		tabela.getColumns().add(kol2);
		tabela.getColumns().add(kol3);

		ArrayList<Narudzba> l = Trgovac.ucitajNarudzbeTrgovca(trgovac);

		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getDatum_isporuke() != null)
				try {
					tabela.getItems()
							.add(new Narudzba(l.get(i).getId(), l.get(i).getKupac_id(), l.get(i).getTrgovac_id(),
									l.get(i).getDatum_narudzbe(), l.get(i).getDatum_isporuke(),
									l.get(i).getNapomena()));
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(nazad, tabela);
		vbox.setStyle("-fx-background-color: " + SIVA_BOJA);
		vbox.setPadding(new Insets(10, 10, 10, 10));

		Scene scene = new Scene(vbox, 600, 400);
		window.setScene(scene);

	}

	public void dodajProdajnoMjestoScene() {

		VBox vb = new VBox(10);

		Label naslov = new Label("Dodaj prodajno mjesto");
		naslov.setStyle("-fx-font-size: 25; -fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		Label s1 = new Label("");

		Label gradLabel = new Label("Grad:");
		TextField gradTF = new TextField();
		Label drzavaLabel = new Label("Država:");
		TextField drzavaTF = new TextField();
		Label adresaLabel = new Label("Adresa:");
		TextField adresaTF = new TextField();
		Label telefonLabel = new Label("Telefon:");
		TextField telefonTF = new TextField();

		gradLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		drzavaLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		adresaLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		telefonLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		gradTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color: " + TAMNA_BOJA);
		drzavaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color: " + TAMNA_BOJA);
		adresaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color: " + TAMNA_BOJA);
		telefonTF
				.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color: " + TAMNA_BOJA);

		Label greska = new Label("");
		Button dodaj = new Button("Dodaj");
		Button otkazi = new Button("Otkaži");
		HBox hb = new HBox(30);
		hb.getChildren().addAll(dodaj, otkazi);

		vb.getChildren().addAll(naslov, s1, gradLabel, gradTF, drzavaLabel, drzavaTF, adresaLabel, adresaTF,
				telefonLabel, telefonTF, greska, hb);
		vb.setPadding(new Insets(30, 50, 0, 50));

		dodaj.setOnAction(event -> {
			if (!gradTF.getText().equals("") && !drzavaTF.getText().equals("") && !adresaTF.getText().equals("")
					&& !telefonTF.getText().equals("")) {
				Prodajno_mjesto mjesto = new Prodajno_mjesto(Prodajno_mjesto.zadnjiId(), gradTF.getText(),
						drzavaTF.getText(), adresaTF.getText(), telefonTF.getText());
				mjesto.dodajProdajnoMjesto();
				setTrgovacScene(trgovac);
			} else {
				greska.setText("POPUNITE SVA POLJA!");
				greska.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #de2e21");
			}
		});
		dodaj.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);

		otkazi.setOnAction(event -> {
			setTrgovacScene(trgovac);
		});
		otkazi.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);

		Scene scene = new Scene(vb, 400, 500);
		vb.setStyle("-fx-background-color: " + SIVA_BOJA);
		window.setScene(scene);

	}

	public void dodajTrgovcaScene() {

		BorderPane root = new BorderPane();
		VBox lijevo = new VBox(15);
		VBox desno = new VBox(15);

		Label naslov = new Label("Dodaj novog trgovca");
		naslov.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		Button nazad = new Button("< Nazad");
		nazad.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		HBox top = new HBox(150);
		top.getChildren().addAll(nazad, naslov);

		nazad.setOnAction(event -> {
			setTrgovacScene(trgovac);
		});

		Label imeL = new Label("Ime:");
		TextField imeTF = new TextField();
		Label prezimeL = new Label("Prezime:");
		TextField prezimeTF = new TextField();
		Label korImeL = new Label("Korisnièko ime:");
		TextField korImeTF = new TextField();
		Label lozinkaL = new Label("Lozinka:");
		TextField lozinkaTF = new TextField();

		lijevo.getChildren().addAll(imeL, imeTF, prezimeL, prezimeTF, korImeL, korImeTF, lozinkaL, lozinkaTF);

		Label emailL = new Label("Email:");
		TextField emailTF = new TextField();
		Label telefonL = new Label("Telefon:");
		TextField telefonTF = new TextField();
		Label prodMjL = new Label("Prodajno mjesto:");
		TextField prodMjTF = new TextField();
		Label polL = new Label("Pol:");
		CheckBox mCB = new CheckBox("Muško");
		CheckBox zCB = new CheckBox("Žensko");
		HBox hb = new HBox(20);
		hb.getChildren().addAll(mCB, zCB);

		desno.getChildren().addAll(emailL, emailTF, telefonL, telefonTF, prodMjL, prodMjTF, polL, hb);

		// css
		imeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		prezimeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		korImeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		lozinkaL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		emailL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		telefonL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		prodMjL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		polL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		imeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		prezimeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		korImeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		lozinkaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		emailTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		telefonTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		prodMjTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		mCB.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		zCB.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);

		Button potvrdi = new Button("Potvrdi");
		potvrdi.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA + "-fx-background-color:" + TAMNA_BOJA);
		potvrdi.setOnAction(event -> {
			String pol;
			if (mCB.isSelected())
				pol = "M";
			else
				pol = "Ž";

			Trgovac t = null;
			try {
				t = new Trgovac(Trgovac.zadnjiId(), Integer.parseInt(prodMjTF.getText()), korImeTF.getText(),
						imeTF.getText(), prezimeTF.getText(), telefonTF.getText(), mdConverter(lozinkaTF.getText()),
						emailTF.getText(), pol);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			t.dodajTrgovca();
			setTrgovacScene(trgovac);

		});

		lijevo.setPadding(new Insets(30, 0, 0, 70));
		desno.setPadding(new Insets(30, 70, 0, 0));

		root.setLeft(lijevo);
		root.setRight(desno);
		root.setTop(top);
		root.setBottom(potvrdi);
		root.setPadding(new Insets(10, 10, 40, 10));
		root.setAlignment(potvrdi, Pos.CENTER);
		root.setStyle("-fx-background-color: " + SIVA_BOJA);
		Scene scene = new Scene(root, 700, 500);
		window.setScene(scene);

	}

	public void azurirajProizvodScene() {

		BorderPane root = new BorderPane();

		HBox hb = new HBox(150);
		Label naslov = new Label("AŽURIRAJ PROIZVODE");
		naslov.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		naslov.setPrefWidth(300);
		Button nazad = new Button("< Nazad");
		nazad.setPrefWidth(170);
		Button dodajP = new Button("Dodaj proizvod");
		dodajP.setPrefWidth(220);
		hb.getChildren().addAll(nazad, naslov, dodajP);
		hb.setPadding(new Insets(10, 10, 10, 10));

		// --------------------------------------------------------------------

		TableView tabela = new TableView();
		tabela.setEditable(true);
		tabela.setStyle("-fx-base: " + SIVA_BOJA + "-fx-control-inner-background:" + SIVA_BOJA
				+ "-fx-background-color: " + SIVA_BOJA + "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;" + "-fx-padding: 5;");

		ArrayList<Proizvod> l = Proizvod.getSpisak_proizvoda();

		TableColumn<Proizvod, String> kol1 = new TableColumn<>("Naziv");
		kol1.setCellValueFactory(new PropertyValueFactory<>("naziv"));
		kol1.setMinWidth(200);
		kol1.setStyle("-fx-text-fill:" + TAMNA_BOJA);

		TableColumn<Proizvod, Float> kol2 = new TableColumn<>("Cijena");
		kol2.setCellValueFactory(new PropertyValueFactory<>("cijena"));
		kol2.setMinWidth(100);
		kol2.setStyle("-fx-text-fill:" + TAMNA_BOJA);

		TableColumn<Proizvod, Float> kol3 = new TableColumn<>("Opis");
		kol3.setCellValueFactory(new PropertyValueFactory<>("opis"));
		kol3.setMinWidth(150);
		kol3.setStyle("-fx-text-fill:" + TAMNA_BOJA);

		TableColumn<Proizvod, Void> kol4 = new TableColumn(" ");

		Callback<TableColumn<Proizvod, Void>, TableCell<Proizvod, Void>> cellFactory = new Callback<TableColumn<Proizvod, Void>, TableCell<Proizvod, Void>>() {
			@Override
			public TableCell<Proizvod, Void> call(final TableColumn<Proizvod, Void> param) {
				final TableCell<Proizvod, Void> cell = new TableCell<Proizvod, Void>() {

					private final Button btn = new Button("Ažuriraj");

					{
						btn.setOnAction(event -> {
							// da otvori popup u kojem unesem nove podatke(vec su uneseni u polja trenutne
							// vrijednosti)
							Proizvod p = getTableView().getItems().get(getIndex());
							popup4(p);
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};

				return cell;
			}
		};

		kol4.setCellFactory(cellFactory);

		tabela.getColumns().add(kol1);
		tabela.getColumns().add(kol2);
		tabela.getColumns().add(kol3);
		tabela.getColumns().add(kol4);

		for (int i = 0; i < l.size(); i++) {
			try {
				tabela.getItems().addAll(
						new Proizvod(l.get(i).getId(), l.get(i).getNaziv(), l.get(i).getCijena(), l.get(i).getOpis()));
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// ---------------------------------------------------------------------

		nazad.setOnMouseClicked(event -> {
			setTrgovacScene(trgovac);
		});
		nazad.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);

		dodajP.setOnMouseClicked(event -> {
			popup5();
		});
		dodajP.setStyle("-fx-background-color: " + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + SIVA_BOJA);

		root.setTop(hb);
		root.setCenter(tabela);
		root.setStyle("-fx-background-color: " + SIVA_BOJA);
		Scene scene = new Scene(root, 770, 500);
		window.setScene(scene);

	}

	public void popup4(Proizvod p) {

		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(window);
		dialog.setTitle("Ažuriraj proizvod");

		VBox vb = new VBox(20);

		Label nazivL = new Label("Naziv:");
		TextField nazivTF = new TextField(p.getNaziv());
		Label opisL = new Label("Opis:");
		TextField opisTF = new TextField(p.getOpis());
		Label cijenaL = new Label("Cijena");
		TextField cijenaTF = new TextField(Double.toString(p.getCijena()));
		Label l = new Label();

		nazivL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + TAMNA_BOJA);
		nazivTF.setStyle("-fx-background-color:" + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);
		opisL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + TAMNA_BOJA);
		opisTF.setStyle("-fx-background-color:" + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);
		cijenaL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + TAMNA_BOJA);
		cijenaTF.setStyle("-fx-background-color:" + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);

		Button potvrdi = new Button("Potvrdi");
		potvrdi.setOnAction(event -> {
			p.azurirajProizvod(nazivTF.getText(), opisTF.getText(), Double.parseDouble(cijenaTF.getText()));
			dialog.close();
			azurirajProizvodScene();
		});

		vb.getChildren().addAll(nazivL, nazivTF, opisL, opisTF, cijenaL, cijenaTF, l, potvrdi);
		vb.setPadding(new Insets(10, 40, 10, 40));
		vb.setStyle("-fx-background-color:" + SIVA_BOJA);

		Scene scene = new Scene(vb, 400, 400);
		dialog.setScene(scene);
		dialog.show();

	}

	public void popup5() {

		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(window);
		dialog.setTitle("Dodaj proizvod");

		VBox vb = new VBox(20);

		Label nazivL = new Label("Naziv:");
		TextField nazivTF = new TextField();
		Label opisL = new Label("Opis:");
		TextField opisTF = new TextField();
		Label cijenaL = new Label("Cijena");
		TextField cijenaTF = new TextField();
		Label l = new Label();

		nazivL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + TAMNA_BOJA);
		nazivTF.setStyle("-fx-background-color:" + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);
		opisL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + TAMNA_BOJA);
		opisTF.setStyle("-fx-background-color:" + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);
		cijenaL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + TAMNA_BOJA);
		cijenaTF.setStyle("-fx-background-color:" + TAMNA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + SIVA_BOJA);

		Button potvrdi = new Button("Potvrdi");
		potvrdi.setOnAction(event -> {
			Proizvod p = null;
			try {
				p = new Proizvod(Proizvod.zadnjiId(), nazivTF.getText(), Double.parseDouble(cijenaTF.getText()),
						opisTF.getText());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog.close();
			p.dodajProizvod();
		});

		vb.getChildren().addAll(nazivL, nazivTF, opisL, opisTF, cijenaL, cijenaTF, l, potvrdi);
		vb.setPadding(new Insets(10, 40, 10, 40));
		vb.setStyle("-fx-background-color:" + SIVA_BOJA);

		Scene scene = new Scene(vb, 400, 400);
		dialog.setScene(scene);
		dialog.show();

	}

	// funkcije
	public String mdConverter(String ulaz) {
		MessageDigest md;
		String hashtext = "";
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(ulaz.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			hashtext += number.toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashtext;
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	public void unosiIzBaze() {
		Prodajno_mjesto.unosIzBaze();
		Proizvod.unosIzBaze();
		Artikal_narudzbe.unosIzBaze();
		Narudzba.unosIzBaze();
		Kupac.unosIzBaze();
		Trgovac.unosIzBaze();
	}
}