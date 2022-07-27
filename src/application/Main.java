package application;
	
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

import java.awt.*;
//import java.awt.Font;
import javafx.scene.text.Font;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

import java.text.DateFormat;  
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;  
import java.util.Calendar; 

import javafx.util.Callback;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.*;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;


public class Main extends Application {
	
	Stage primaryStage;
	
	Kupac kupac;
	Trgovac trgovac;
	static ArrayList<Kupac> sviKupci = new ArrayList<Kupac>();
	static ArrayList<Trgovac> sviTrgovci = new ArrayList<Trgovac>();
	Button kupacB, trgovacB, registracijaB, regNoviB, prijavaB;
	Button SpisakNarudzbiB, NarNaCekanjuB, ProizvodiB;
	Button sveNarudzbeB, prodMjestoB, noviTrgovacB, azurirajProizvodB;
	Scene pocetna;
	TextField usernameF;
	PasswordField passwordF;
	boolean kupacBool = false, trgovacBool = false;
	Label greska;
	TextField imeReg, prezimeReg, usernameReg, telefonReg, emailReg, adresaReg, gradReg, drzavaReg, polReg, postBrReg;
	PasswordField passwordReg;
	int brojac = 0;
	
	//BOJE
	String SVIJETLA_BOJA = "#c4ccc7;";
	String SREDNJA_BOJA = "#798885;";
	String TAMNA_BOJA = "#3F4A48;";
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			
			//pocetna stranica
			primaryStage.setTitle("Prodavnica");
			setPocetna();
			
			//login kao kupac ili trgovac
			kupacB.setOnAction(actionEvent ->  {
				kupacBool = true;
				kupac = new Kupac();
				sviKupci = Kupac.ucitajKupce();
			    setLoginScene();
			});
			trgovacB.setOnAction(actionEvent ->  {
				trgovacBool = true;
				trgovac = new Trgovac();
				sviTrgovci = Trgovac.ucitajTrgovce();
			    setLoginScene();
			});
			//ili registracija novog kupca
			registracijaB.setOnAction(actionEvent ->  {
				dodajKupcaScene();
			});
			
			//Prijava kao kupac ILI trgovac
			prijavaB = new Button("Prijavi se");
			prijavaB.setOnAction(actionEvent ->  {
				
				String tempUser = usernameF.getText();
				String tempPass = passwordF.getText();
				
				//ako smo pritisnuli da se p kao kupac onda trazimo u tabeli za kupca
				if(kupacBool)
					for(Kupac k: sviKupci) {
						if(k.korisnicko_ime.equals(tempUser) && k.lozinka.equals(mdConverter(tempPass))) {
							kupac = k;
							setKupacScene(k);
						}
					} 
				else if(trgovacBool)
					for(Trgovac t: sviTrgovci) {
						
						if(t.korisnicko_ime.equals(tempUser) && t.lozinka.equals(mdConverter(tempPass))) {
							trgovac = t;
							setTrgovacScene(t);
						}
					}
					
			    greska.setText("DOŠLO JE DO GREŠKE!");
			});
			
			SpisakNarudzbiB = new Button();
			//PRIKAZI SPISAK ZAVRSENIH NARUDZBI - KUPAC
			SpisakNarudzbiB.setOnAction(actionEvent -> {
				setNarudzbeScene(kupac);
			});
			//PRIKAZI SPISAK NARUDZBI NA CEKANJU - KUPAC
			NarNaCekanjuB = new Button();
			NarNaCekanjuB.setOnAction(actionEvent -> {
				setNarNaCekanjuScene();
			});
			//PRIKAZI SVE PROIZVODE
			ProizvodiB = new Button();
			ProizvodiB.setOnMouseClicked(actionEvent -> {
				setProizvodiScene();
			});
			
			
			//PRIKAZI SVE NARUDZBE - TRGOVAC
			sveNarudzbeB = new Button();
			sveNarudzbeB.setOnAction(event -> {
				setNarudzbeTrgovacScene();
			});
			//DODAVANJE NOVOG PRODAJNOG MJESTA - TRGOVAC
			prodMjestoB = new Button();
			prodMjestoB.setOnAction(event -> {
				dodajProdajnoMjestoScene();
			});
			//DODAVANJE NOVOG TRGOVCA
			noviTrgovacB = new Button();
			noviTrgovacB.setOnAction(action -> {
				dodajTrgovcaScene();
			});
			//AZURIRANJE PROIZVODA - TRGOVAC
			azurirajProizvodB = new Button();
			azurirajProizvodB.setOnAction(event -> {
				azurirajProizvodScene();
			});
			
			
			primaryStage.setScene(pocetna);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPocetna() {
		
		BorderPane root = new BorderPane();
		
		Label naslov = new Label("Prijavite se kao:");

		naslov.setStyle("-fx-font-weight: bold; -fx-font-size: 35; -fx-text-fill: " + SVIJETLA_BOJA);
		naslov.setPadding(new Insets(50, 0, 0, 0));
		
		kupacB = new Button("Kupac");
		trgovacB = new Button("Trgovac");
		kupacB.setPrefSize(150, 35);
		kupacB.setStyle("-fx-font-weight: bold; -fx-background-color: " + SVIJETLA_BOJA + ";-fx-text-fill: " + TAMNA_BOJA);
		trgovacB.setPrefSize(150, 35);
		trgovacB.setStyle("-fx-font-weight: bold; -fx-background-color: " + SVIJETLA_BOJA + ";-fx-text-fill: " + TAMNA_BOJA);
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(60, 12, 15, 80));
		hbox.setSpacing(50);
		root.setStyle("-fx-background-color: #3F4A48;"); 
		hbox.getChildren().addAll(kupacB, trgovacB);
		
		registracijaB = new Button("Novi kupac");
		registracijaB.setPrefSize(130, 50);
		registracijaB.setStyle("-fx-font-size: 20; -fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: " + SREDNJA_BOJA);
		registracijaB.setPadding(new Insets(0, 0, 50, 0));
		
		BorderPane.setAlignment(naslov, Pos.CENTER);
		BorderPane.setAlignment(registracijaB, Pos.CENTER);
		root.setTop(naslov);
		root.setCenter(hbox);
		root.setBottom(registracijaB);
		
		pocetna = new Scene(root,500,400);
	}
	
	
	public void setLoginScene() {
		
		Label userL = new Label("Korisnièko ime");
		userL.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		Label passL = new Label("Password");
		passL.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		
		usernameF = new TextField();
		usernameF.setPrefWidth(50);
		usernameF.setStyle("-fx-font-weight: bold; -fx-background-color:" + SVIJETLA_BOJA + ";-fx-text-fill: " + TAMNA_BOJA);
		passwordF = new PasswordField();
		passwordF.setStyle("-fx-background-color:" + SVIJETLA_BOJA);
		
		prijavaB.setPrefSize(150, 35);
		prijavaB.setStyle("-fx-font-weight: bold; -fx-background-color:" + SVIJETLA_BOJA + ";-fx-text-fill:" + TAMNA_BOJA);
		
		greska = new Label("");
		greska.setPrefHeight(10);
		greska.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #b41616");
		
		VBox vbox = new VBox();
		vbox.setSpacing(20);
		vbox.getChildren().addAll(userL, usernameF, passL, passwordF, greska, prijavaB);
		vbox.setPadding(new Insets(50, 100, 0, 100));
		vbox.setStyle("-fx-background-color: " + TAMNA_BOJA);
		vbox.setPrefSize(300,200);
		
		Scene scene = new Scene(vbox,500,400);
		primaryStage.setScene(scene);
		
	}
	
	
	public String mdConverter(String ulaz) {
		MessageDigest md;
		String hash = "";
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(ulaz.getBytes());
			BigInteger br = new BigInteger(1, messageDigest);
			hash += br.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		System.out.println(hash);
		return hash;
	}
	
	
	
	public void dodajKupcaScene() {
		
		
		BorderPane root = new BorderPane();
		VBox lijevo = new VBox(12);
		VBox desno = new VBox(12);
		
		Label naslov = new Label("Registracija kupca");
		naslov.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		
		
		Label imeL = new Label("Ime:");
		TextField imeTF = new TextField();
		Label prezimeL = new Label("Prezime:");
		TextField prezimeTF = new TextField();
		Label korImeL = new Label("Korisnièko ime:");
		TextField korImeTF = new TextField();
		Label lozinkaL = new Label("Lozinka:");
		PasswordField lozinkaTF = new PasswordField();
		Label emailL = new Label("Email:");
		TextField emailTF = new TextField();
		Label telefonL = new Label("Telefon:");
		TextField telefonTF = new TextField();
		
		lijevo.getChildren().addAll(imeL, imeTF, prezimeL, prezimeTF, korImeL, korImeTF, lozinkaL, lozinkaTF,emailL, emailTF, telefonL, telefonTF);
		
		
		Label drzavaL = new Label("Država:");
		TextField drzavaTF = new TextField();
		Label gradL = new Label("Grad:");
		TextField gradTF = new TextField();
		Label adresaL = new Label("Adresa:");
		TextField adresaTF = new TextField();
		Label postanskiL = new Label("Poštanski broj:");
		TextField postanskiTF = new TextField();
		Label polL = new Label("Pol:");
		CheckBox mCB = new CheckBox("Muško");
		CheckBox zCB = new CheckBox("Žensko");
		HBox hb = new HBox(20);
		hb.getChildren().addAll(mCB, zCB);
		
		desno.getChildren().addAll(drzavaL, drzavaTF, gradL, gradTF, adresaL, adresaTF, postanskiL, postanskiTF, polL, hb);
		
		
		//css
		imeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		prezimeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		korImeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		lozinkaL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		emailL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		telefonL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		drzavaL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		adresaL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		gradL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		postanskiL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		polL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		imeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		prezimeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		korImeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		lozinkaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		emailTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		telefonTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		drzavaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		gradTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		adresaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		postanskiTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		mCB.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		zCB.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		
		
		Button potvrdi = new Button("Potvrdi");
		potvrdi.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		potvrdi.setOnAction(event -> {
			String pol;
			if(mCB.isSelected())
				pol = "M";
			else
				pol = "Ž";
			
			kupac = new Kupac(imeTF.getText(), prezimeTF.getText(), korImeTF.getText(), mdConverter(lozinkaTF.getText()), emailTF.getText(),
					telefonTF.getText(), drzavaTF.getText(), gradTF.getText(), adresaTF.getText(), pol, Integer.parseInt(postanskiTF.getText()));
			
			kupac.dodajKupca();
			setKupacScene(kupac);
			//kupac = k;
			
		});
		
		lijevo.setPadding(new Insets(30,0,0,70));
		desno.setPadding(new Insets(30,70,0,0));
		
		root.setLeft(lijevo);
		root.setRight(desno);
		root.setTop(naslov);
		root.setBottom(potvrdi);
		root.setPadding(new Insets(10,10,40,10));
		root.setAlignment(potvrdi, Pos.CENTER);
		root.setAlignment(naslov, Pos.CENTER);
		root.setStyle("-fx-background-color: " + TAMNA_BOJA);
		Scene scene = new Scene(root, 750, 630);
		primaryStage.setScene(scene);
		
	}
	
	
	
	public void setKupacScene(Kupac k) {
		
		BorderPane root = new BorderPane();
		
		Label naslov = new Label("PRIJAVLJENI STE KAO KUPAC");
		naslov.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-background-color: " + SREDNJA_BOJA);
		naslov.setPadding(new Insets(10, 10, 10, 10));
		
		//LIJEVI MENI
		Label imeL = new Label("Ime: " + k.ime + " " + k.prezime);
		imeL.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		Label gradL = new Label("Grad: " + k.grad);
		gradL.setStyle("-fx-font-size: 17; -fx-text-fill: " + SVIJETLA_BOJA);
		Label adresaL = new Label("Adresa: " + k.adresa);
		adresaL.setStyle("-fx-font-size: 17; -fx-text-fill: " + SVIJETLA_BOJA);
		Label drzavaL = new Label("Država: " + k.drzava);
		drzavaL.setStyle("-fx-font-size: 17; -fx-text-fill: " + SVIJETLA_BOJA);
		Label brNarudzbiL = new Label("Broj narudžbi: " + kupac.brojNarudzbi());
		brNarudzbiL.setStyle("-fx-font-size: 17; -fx-text-fill: " + SVIJETLA_BOJA);
		
		VBox lijevo = new VBox();
		lijevo.getChildren().addAll(imeL, gradL, adresaL, drzavaL, brNarudzbiL);
		lijevo.setSpacing(12);
		lijevo.setPadding(new Insets(60, 20, 40, 40));
		
		//DESNI MENI
		SpisakNarudzbiB.setText("Spisak narudžbi");
		SpisakNarudzbiB.setPrefSize(180, 40);
		SpisakNarudzbiB.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA +  ";-fx-background-color: " + SVIJETLA_BOJA);
		NarNaCekanjuB.setText("Narudžbe na èekanju");
		NarNaCekanjuB.setPrefSize(180, 40);
		NarNaCekanjuB.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + ";-fx-background-color: " + SVIJETLA_BOJA);
		ProizvodiB.setText("Prikaži proizvide");
		ProizvodiB.setPrefSize(180, 40);
		ProizvodiB.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + ";-fx-background-color: " + SVIJETLA_BOJA);
		
		VBox desno = new VBox();
		desno.getChildren().addAll(SpisakNarudzbiB, NarNaCekanjuB, ProizvodiB);
		desno.setSpacing(30);
		desno.setPadding(new Insets(70, 40, 40, 20));
		
		
		root.setTop(naslov);
		root.setLeft(lijevo);
		root.setRight(desno);
		root.setStyle("-fx-background-color: " + TAMNA_BOJA);
		BorderPane.setAlignment(naslov, Pos.CENTER);
		
		Scene scene = new Scene(root,600,400);
		primaryStage.setScene(scene);
		
	}
	
	
	
	public void setNarudzbeScene(Kupac k) {  
		
		//Stefan, 7da816581a2c54d43939e79e34899083
			
		Button nazad = new Button("< Nazad");
		nazad.setPrefSize(80, 35);
		nazad.setStyle("-fx-font-weight: bold; -fx-background-color: " + SREDNJA_BOJA + "-fx-text-fill: " + TAMNA_BOJA);
		
		nazad.setOnMouseClicked(event -> {
			setKupacScene(k);
		});
		
		//---------------------------------------------------------------------------
		
		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		TableColumn<Narudzba, Integer> kol1 = new TableColumn<>("Cijena narudžbe");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("vrijednostNarudzbe"));
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<Narudzba, String> kol2 = new TableColumn<>("Datum Narudžbe");
	    kol2.setCellValueFactory(new PropertyValueFactory<>("datum_narudzbe"));
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<Narudzba, String> kol3 = new TableColumn<>("Datum Isporuke");
	    kol3.setCellValueFactory(new PropertyValueFactory<>("datum_isporuke"));
	    kol3.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);
	    

	    tabela.getColumns().add(kol1);
	    tabela.getColumns().add(kol2);
	    tabela.getColumns().add(kol3);

	    ArrayList<Narudzba> l = k.ucitajSveNarudzbe();

	    for(int i = 0 ; i < l.size() ; i++) {
	    	if(l.get(i).datum_isporuke != null)
	    		tabela.getItems().add(new Narudzba(l.get(i).id, l.get(i).vrijednostNarudzbe, l.get(i).datum_narudzbe, l.get(i).datum_isporuke, l.get(i).napomena)); 
	    }
	    
	    tabela.setRowFactory(tv -> {
	        TableRow<Narudzba> kolona = new TableRow<>();
	        kolona.setOnMouseClicked(event -> {
	            if (!kolona.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 2) {
	            	Narudzba odabranaKol = kolona.getItem();
	                popup1(odabranaKol);
	            }
	        });
	        return kolona;
	    });
	    
	   
	    VBox vbox = new VBox(15);
	    vbox.getChildren().addAll(nazad,tabela);
	    vbox.setStyle("-fx-background-color: " + TAMNA_BOJA);
	    vbox.setPadding(new Insets(10,10,10,10));

	    Scene scene = new Scene(vbox,600,400);
	    primaryStage.setScene(scene);
		
	}
	
	public void popup1(Narudzba n) {
		
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Detaljno o narudzbi");
        
        // Rajko, ce55e837ba3d1d3bf3b01d0ef41dc537

        VBox vb = new VBox(10);
        vb.setPadding(new Insets(10,10,10,10));
        Label napomenaL = new Label("Napomena: " + n.napomena);
        napomenaL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
        

		//---------------------------------------------------------
		
		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		TableColumn<ArtiklNarudzbe, Integer> kol1 = new TableColumn<>("ID");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("id"));
	    kol1.setMinWidth(120);
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<ArtiklNarudzbe, Float> kol2 = new TableColumn<>("Cijena");
	    kol2.setCellValueFactory(new PropertyValueFactory<>("cijena"));
	    kol2.setMinWidth(120);
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<ArtiklNarudzbe, Integer> kol3 = new TableColumn<>("Kolièina");
	    kol3.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
	    kol3.setMinWidth(120);
	    kol3.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);


	    tabela.getColumns().add(kol1);
	    tabela.getColumns().add(kol2);
	    tabela.getColumns().add(kol3);


	    for(int i = 0 ; i < n.spisakArtikala.size() ; i++) {
	    	tabela.getItems().add(new ArtiklNarudzbe(n.spisakArtikala.get(i).id, n.spisakArtikala.get(i).cijena, n.spisakArtikala.get(i).kolicina));   
	    }
		
	    //-----------------------------------------------------------------------------
        
        vb.getChildren().addAll(napomenaL, tabela);
        vb.setStyle("-fx-background-color: " + TAMNA_BOJA);
        
        
        Scene dialogScene = new Scene(vb, 400, 300);
        dialog.setScene(dialogScene);
        dialog.show();
		
	}
	
	
	public void setNarNaCekanjuScene() {
		
		//Stefan, 7da816581a2c54d43939e79e34899083
		
		Button nazad = new Button("< Nazad");
		nazad.setPrefSize(80, 35);
		nazad.setStyle("-fx-font-weight: bold; -fx-background-color: " + SREDNJA_BOJA + "-fx-text-fill: " + TAMNA_BOJA);
		
		nazad.setOnMouseClicked(event -> {
			setKupacScene(kupac);
		});
		
		//---------------------------------------------------------------------------
		
		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		TableColumn<Narudzba, Integer> kol1 = new TableColumn<>("Cijena narudžbe");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("vrijednostNarudzbe"));
	    kol1.setMinWidth(150);
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<Narudzba, String> kol2 = new TableColumn<>("Datum Narudžbe");
	    kol2.setCellValueFactory(new PropertyValueFactory<>("datum_narudzbe"));
	    kol2.setMinWidth(150);
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);
	    

	    tabela.getColumns().add(kol1);
	    tabela.getColumns().add(kol2);

	    ArrayList<Narudzba> l = kupac.ucitajSveNarudzbe();

	    for(int i = 0 ; i < l.size() ; i++) {
	    	if(l.get(i).datum_isporuke == null)
	    		tabela.getItems().addAll(new Narudzba(l.get(i).id, l.get(i).vrijednostNarudzbe, l.get(i).datum_narudzbe, l.get(i).napomena)); 
	    }
	    
	    tabela.setRowFactory(tv -> {
	        TableRow<Narudzba> kolona = new TableRow<>();
	        kolona.setOnMouseClicked(event -> {
	            if (!kolona.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 2) {
	            	Narudzba odabranaKol = kolona.getItem();
	                popup2(odabranaKol);
	            }
	        });
	        return kolona;
	    });
	    
	   
	    VBox vbox = new VBox(10);
	    vbox.getChildren().addAll(nazad,tabela);
	    vbox.setStyle("-fx-background-color: " + TAMNA_BOJA);
	    vbox.setPadding(new Insets(10,10,10,10));
	    
	    Scene scene = new Scene(vbox,600,400);
		primaryStage.setScene(scene);
	    
	}
	
	
	public void popup2(Narudzba n) {
		
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Detaljno o narudzbi");
        
        // Svetlana, c3d636bce8c725b5f2f9a20db7821c12

        VBox vb = new VBox(10);
        vb.setPadding(new Insets(10,10,10,10));
        
        Label napomenaL = new Label("Napomena: " + n.napomena);
        napomenaL.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
        
		//---------------------------------------------------------
		
		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		TableColumn<ArtiklNarudzbe, Integer> kol1 = new TableColumn<>("ID");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("id"));
	    kol1.setMinWidth(80);
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<ArtiklNarudzbe, Float> kol2 = new TableColumn<>("Cijena");
	    kol2.setCellValueFactory(new PropertyValueFactory<>("cijena"));
	    kol2.setMinWidth(80);
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<ArtiklNarudzbe, Integer> kol3 = new TableColumn<>("Kolièina");
	    kol3.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
	    kol3.setMinWidth(80);
	    kol3.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);


	    tabela.getColumns().add(kol1);
	    tabela.getColumns().add(kol2);
	    tabela.getColumns().add(kol3);


	    for(int i = 0 ; i < n.spisakArtikala.size() ; i++) {
	    	tabela.getItems().add(new ArtiklNarudzbe(n.spisakArtikala.get(i).id, n.spisakArtikala.get(i).cijena, n.spisakArtikala.get(i).kolicina));   
	    }
		
	    //-----------------------------------------------------------------------------
	    
	    Button otkazi = new Button("Otkaži narudžbu");
        otkazi.setOnMouseClicked(event -> {
			n.otkaziNarudzbu();
			setNarNaCekanjuScene();
		});
        otkazi.setStyle("-fx-font-weight: bold; -fx-background-color: " + SREDNJA_BOJA + "-fx-text-fill: " + TAMNA_BOJA);
        
        vb.getChildren().addAll(otkazi, napomenaL, tabela);
        vb.setStyle("-fx-background-color:" + TAMNA_BOJA);
        
        
        Scene dialogScene = new Scene(vb, 400, 300);
        dialog.setScene(dialogScene);
        dialog.show();
		
	}
	

	public void setProizvodiScene() {
		
		BorderPane root = new BorderPane();
		
		
		HBox hb = new HBox(150);
		Label naslov = new Label("SVI PROIZVODI");
		naslov.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		Button nazad = new Button("< Nazad");
		hb.getChildren().addAll(nazad,naslov);
		hb.setPadding(new Insets(10,10,10,10));
		
		
		//--------------------------------------------------------------------
		
		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		Proizvod p = new Proizvod();
		ArrayList<Proizvod> l = p.ucitajProizvode();
		
		TableColumn<Proizvod, String> kol1 = new TableColumn<>("Naziv");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("naziv"));
	    kol1.setMinWidth(200);
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<Proizvod, Float> kol2 = new TableColumn<>("Cijena");
	    kol2.setCellValueFactory(new PropertyValueFactory<>("cijena"));
	    kol2.setMinWidth(100);
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);
	    
	    TableColumn<Proizvod, Void> kol3 = new TableColumn(" ");
	    
	    
        Narudzba n = new Narudzba();
	    Date datum = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
        String danasnjiDatum = dateFormat.format(datum); 
        Trgovac t = new Trgovac();
        ArrayList<ArtiklNarudzbe> artikli = new ArrayList<ArtiklNarudzbe>();
	    
	    
	    Callback<TableColumn<Proizvod, Void>, TableCell<Proizvod, Void>> cellFactory = new Callback<TableColumn<Proizvod, Void>, TableCell<Proizvod, Void>>() {
            @Override
            public TableCell<Proizvod, Void> call(final TableColumn<Proizvod, Void> param) {
                final TableCell<Proizvod, Void> cell = new TableCell<Proizvod, Void>() {

                    private final Button btn = new Button("Naruèi");

                    {
                        btn.setOnAction(event -> {
                            Proizvod proizvod = getTableView().getItems().get(getIndex());
                            n.id = n.zadnjiId();
                            n.kupac_id = kupac.id;
                            n.datum_narudzbe = danasnjiDatum;
                            brojac++;
                            int artiklID = ArtiklNarudzbe.zadnjiId();
                            ArtiklNarudzbe a = new ArtiklNarudzbe(proizvod.cijena, proizvod.id, n.id, artiklID + brojac);
                            artikli.add(a);
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
        
        
        kol3.setCellFactory(cellFactory);
	    

	    tabela.getColumns().add(kol1);
	    tabela.getColumns().add(kol2);
	    tabela.getColumns().add(kol3);


	    for(int i = 0 ; i < l.size() ; i++) {
	    	tabela.getItems().addAll(new Proizvod(l.get(i).naziv, l.get(i).id, l.get(i).cijena));
	    }
	    
	    
		
		//---------------------------------------------------------------------
	    

		nazad.setOnMouseClicked(event -> {
			if(brojac > 0) {
				ArrayList<ArtiklNarudzbe> bezDuplikata = ukloniDuplikate(artikli);
				Narudzba.dodajNovuNarudzbu(n);
				ArtiklNarudzbe.dodajNoveArtikleNarudzbe(bezDuplikata);
			}
			brojac = 0;
			setKupacScene(kupac);
		});
		nazad.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		
		root.setTop(hb);
		root.setCenter(tabela);
		root.setStyle("-fx-background-color: " + TAMNA_BOJA);
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		
	}
	
	public ArrayList<ArtiklNarudzbe> ukloniDuplikate(ArrayList<ArtiklNarudzbe> l){
		ArrayList<ArtiklNarudzbe> rez = new ArrayList<ArtiklNarudzbe>();
		int[] niz = new int[l.size()];
	    for(int i = 0 ; i < l.size() ; i++) {
	    	int brojac = 1;
	    	for(int j = 0 ; j < l.size() ; j++) {
	    		if(i != j && l.get(i).proizvod_id == l.get(j).proizvod_id) {
	    			brojac++;
	    			niz[j] = -1;
	    		}
	    	}
	    		
	    	if(niz[i] != -1) {
	    		l.get(i).kolicina = brojac;
		    	rez.add(l.get(i));
	    	}
	    }
		
		return rez;
	}
	
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	public void setTrgovacScene(Trgovac t) {
		
		
		BorderPane root = new BorderPane();
		
		Label naslov = new Label("PRIJAVLJENI STE KAO TRGOVAC");
		naslov.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-background-color: " + SVIJETLA_BOJA + "-fx-text-fill: " + TAMNA_BOJA);
		naslov.setPadding(new Insets(10, 10, 10, 10));
		
		//LIJEVI MENI
		Label imeL = new Label("Ime: " + t.ime + " " + t.prezime);
		imeL.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		Label korImeL = new Label("Korisnièko ime: " + t.korisnicko_ime);
		korImeL.setStyle("-fx-font-size: 16; -fx-text-fill:" + SVIJETLA_BOJA);
		Label emailL = new Label("Email: " + t.email);
		emailL.setStyle("-fx-font-size: 16; -fx-text-fill:" + SVIJETLA_BOJA);
		Label telL = new Label("Tel: " + t.telefon);
		telL.setStyle("-fx-font-size: 16; -fx-text-fill: " + SVIJETLA_BOJA);
		Label polL = new Label("Pol: " + t.pol);
		polL.setStyle("-fx-font-size: 16; -fx-text-fill:" + SVIJETLA_BOJA);
		
		HBox hb1 = new HBox(30);
		HBox hb2 = new HBox(30);
		sveNarudzbeB.setText("Sve narudžbe");
		prodMjestoB.setText("Dodaj prodajno mjesto");
		hb1.getChildren().addAll(sveNarudzbeB, prodMjestoB);
		hb1.setPadding(new Insets(60,0,0,0));
		noviTrgovacB.setText("Dodaj trgovca");
		azurirajProizvodB.setText("Azuriraj Proizvod");
		hb2.getChildren().addAll(noviTrgovacB, azurirajProizvodB);
		
		sveNarudzbeB.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		prodMjestoB.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		noviTrgovacB.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		azurirajProizvodB.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		
		VBox lijevo = new VBox();
		lijevo.getChildren().addAll(imeL, korImeL, emailL, telL, polL, hb1, hb2);
		lijevo.setSpacing(15);
		lijevo.setPadding(new Insets(50, 20, 40, 30));
		
		//DESNI MENI
		
		
		VBox desno = new VBox(10);
		
		Label obavjestenjaL = new Label("Obavještenja o narudžbama");
		obavjestenjaL.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		
		ArrayList<Narudzba> obavjestenja = Narudzba.ucitajObavjestenja(trgovac);
		
		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		TableColumn<Narudzba, Integer> kol1 = new TableColumn<>("ID");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("id"));
	    kol1.setMinWidth(100);
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);
	    
	    TableColumn<Narudzba, Void> kol2 = new TableColumn(" ");
	    kol2.setMinWidth(100);
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);
	    
	    
	    Callback<TableColumn<Narudzba, Void>, TableCell<Narudzba, Void>> cellFactory = new Callback<TableColumn<Narudzba, Void>, TableCell<Narudzba, Void>>() {
            @Override
            public TableCell<Narudzba, Void> call(final TableColumn<Narudzba, Void> param) {
                final TableCell<Narudzba, Void> cell = new TableCell<Narudzba, Void>() {

                    private final Button btn = new Button("Prihvati");
                    {
                        btn.setOnAction(event -> {
                        	Narudzba n = getTableView().getItems().get(getIndex());
                        	popup3(n.id);
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


	    for(int i = 0 ; i < obavjestenja.size() ; i++) {
	    	tabela.getItems().add(new Narudzba(obavjestenja.get(i).id));   
	    }
		
		
		
		desno.getChildren().addAll(obavjestenjaL, tabela);
		desno.setSpacing(30);
		desno.setPadding(new Insets(50, 40, 60, 0));
		
		
		root.setTop(naslov);
		root.setLeft(lijevo);
		root.setRight(desno);
		root.setStyle("-fx-background-color: " + TAMNA_BOJA);
		BorderPane.setAlignment(naslov, Pos.CENTER);
		
		Scene scene = new Scene(root,700,500);
		primaryStage.setScene(scene);
		
	}
	
	
	public void popup3(int narudzbaID) {
		
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Prihvati narudžbu");
        
        VBox vb = new VBox(30);
        
        Label naslov = new Label("Unesi datum isporuke:");
        naslov.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
        
        VBox vb2 = new VBox(5);
        TextField unos = new TextField();
        unos.setStyle("-fx-font-weight: bold; -fx-background-color: " + SVIJETLA_BOJA + "-fx-text-fill:" + TAMNA_BOJA);
        Label format = new Label("*Format: gggg-mm-dd");
        format.setStyle("-fx-font-size: 12; -fx-text-fill:" + SREDNJA_BOJA);
        Label g = new Label("");
        vb2.getChildren().addAll(unos, format, g);
        
        HBox hb = new HBox(50);
        Button otkazi = new Button("Otkaži");
        otkazi.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
        Button prihvati = new Button("Prihvati");
        prihvati.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
        hb.getChildren().addAll(otkazi, prihvati);
	    
        otkazi.setOnMouseClicked(event -> {
        	dialog.close();
			setTrgovacScene(trgovac);
		});
        
        prihvati.setOnMouseClicked(event -> {
        	if(!unos.getText().equals("")) {
        		Narudzba.prihvatiNarudzbu(narudzbaID, trgovac.id, unos.getText());
        		dialog.close();
        		setTrgovacScene(trgovac);
        	} else {
        		g.setText("UNESITE DATUM!");
        		g.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #de2e21");
        	}
        });
        
        
        vb.getChildren().addAll(naslov, vb2, hb);
        vb.setPadding(new Insets(20, 30, 30, 20));
        vb.setStyle("-fx-background-color: " + TAMNA_BOJA);
        
        
        Scene dialogScene = new Scene(vb, 350, 250);
        dialog.setScene(dialogScene);
        dialog.show();
		
    
	}
	
	
	public void setNarudzbeTrgovacScene() {
		
		Button nazad = new Button("< Nazad");
		nazad.setPrefSize(80, 35);
		nazad.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		
		nazad.setOnMouseClicked(event -> {
			setTrgovacScene(trgovac);
		});
		
		//---------------------------------------------
		
		TableView tabela = new TableView();
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		TableColumn<Narudzba, Integer> kol1 = new TableColumn<>("Cijena narudžbe");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("vrijednostNarudzbe"));
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<Narudzba, String> kol2 = new TableColumn<>("Datum Narudžbe");
	    kol2.setCellValueFactory(new PropertyValueFactory<>("datum_narudzbe"));
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<Narudzba, String> kol3 = new TableColumn<>("Datum Isporuke");
	    kol3.setCellValueFactory(new PropertyValueFactory<>("datum_isporuke"));
	    kol3.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);


	    tabela.getColumns().add(kol1);
	    tabela.getColumns().add(kol2);
	    tabela.getColumns().add(kol3);

	    ArrayList<Narudzba> l = Trgovac.ucitajNarudzbeTrgovca(trgovac);

	    for(int i = 0 ; i < l.size() ; i++) {
	    	if(l.get(i).datum_isporuke != null)
	    		tabela.getItems().add(new Narudzba(l.get(i).id, l.get(i).vrijednostNarudzbe, l.get(i).datum_narudzbe, l.get(i).datum_isporuke, l.get(i).napomena)); 
	    }
	    
	   
	    VBox vbox = new VBox(10);
	    vbox.getChildren().addAll(nazad,tabela);
	    vbox.setStyle("-fx-background-color: " + TAMNA_BOJA);
	    vbox.setPadding(new Insets(10,10,10,10));

	    Scene scene = new Scene(vbox,600,400);
	    primaryStage.setScene(scene);
		
	}
	
	
	public void dodajProdajnoMjestoScene() {
		
		VBox vb = new VBox(10);
		
		Label naslov = new Label("Dodaj prodajno mjesto");
		naslov.setStyle("-fx-font-size: 25; -fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		Label s1 = new Label("");
		
		Label gradLabel = new Label("Grad:");
		TextField gradTF = new TextField();
		Label drzavaLabel = new Label("Država:");
		TextField drzavaTF = new TextField();
		Label adresaLabel = new Label("Adresa:");
		TextField adresaTF = new TextField();
		Label telefonLabel = new Label("Telefon:");
		TextField telefonTF = new TextField();
		
		gradLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		drzavaLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		adresaLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		telefonLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		gradTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color: " + SVIJETLA_BOJA);
		drzavaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color: " + SVIJETLA_BOJA);
		adresaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color: " + SVIJETLA_BOJA);
		telefonTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color: " + SVIJETLA_BOJA);
		
		Label greska = new Label("");
		Button dodaj = new Button("Dodaj");
		Button otkazi = new Button("Otkaži");
		HBox hb = new HBox(30);
		hb.getChildren().addAll(dodaj, otkazi);
		
		vb.getChildren().addAll(naslov, s1, gradLabel, gradTF, drzavaLabel, drzavaTF, adresaLabel, adresaTF, telefonLabel, telefonTF, greska, hb);
		vb.setPadding(new Insets(30,50,0,50));
		
		dodaj.setOnAction(event -> {
			if(!gradTF.getText().equals("") && !drzavaTF.getText().equals("") && !adresaTF.getText().equals("") && !telefonTF.getText().equals("")) {
				ProdajnoMjesto mjesto = new ProdajnoMjesto(gradTF.getText(), drzavaTF.getText(), adresaTF.getText(), telefonTF.getText());
				mjesto.dodajProdajnoMjesto();
				setTrgovacScene(trgovac);
			} else {
				greska.setText("POPUNITE SVA POLJA!");
				greska.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #de2e21");
			}
		});
		dodaj.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		
		otkazi.setOnAction(event -> {
			setTrgovacScene(trgovac);
		});
		otkazi.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		
		Scene scene = new Scene(vb, 400, 500);
		vb.setStyle("-fx-background-color: " + TAMNA_BOJA);
		primaryStage.setScene(scene);
		
	}
	
	
	public void dodajTrgovcaScene() {
		
		BorderPane root = new BorderPane();
		VBox lijevo = new VBox(15);
		VBox desno = new VBox(15);
		
		Label naslov = new Label("Dodaj novog trgovca");
		naslov.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		Button nazad = new Button("< Nazad");
		nazad.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		HBox top = new HBox(150);
		top.getChildren().addAll(nazad,naslov);
		
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
		
		
		//css
		imeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		prezimeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		korImeL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		lozinkaL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		emailL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		telefonL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		prodMjL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		polL.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		imeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		prezimeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		korImeTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		lozinkaTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		emailTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		telefonTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		prodMjTF.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		mCB.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		zCB.setStyle("-fx-font-weight: bold; -fx-text-fill:" + SVIJETLA_BOJA);
		
		
		Button potvrdi = new Button("Potvrdi");
		potvrdi.setStyle("-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA + "-fx-background-color:" + SVIJETLA_BOJA);
		potvrdi.setOnAction(event -> {
			String pol;
			if(mCB.isSelected())
				pol = "M";
			else
				pol = "Ž";
			
			Trgovac t = new Trgovac(korImeTF.getText(), imeTF.getText(), prezimeTF.getText(), mdConverter(lozinkaTF.getText()), pol, 
					telefonTF.getText(), emailTF.getText(), Integer.parseInt(prodMjTF.getText()));
			
			t.dodajTrgovca();
			setTrgovacScene(trgovac);
			
		});
		
		lijevo.setPadding(new Insets(30,0,0,70));
		desno.setPadding(new Insets(30,70,0,0));
		
		root.setLeft(lijevo);
		root.setRight(desno);
		root.setTop(top);
		root.setBottom(potvrdi);
		root.setPadding(new Insets(10,10,40,10));
		root.setAlignment(potvrdi, Pos.CENTER);
		root.setStyle("-fx-background-color: " + TAMNA_BOJA);
		Scene scene = new Scene(root, 700, 500);
		primaryStage.setScene(scene);
		
	}
	
	
	public void azurirajProizvodScene() {
		
		BorderPane root = new BorderPane();
		
		
		HBox hb = new HBox(150);
		Label naslov = new Label("AŽURIRAJ PROIZVODE");
		naslov.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: " + SVIJETLA_BOJA);
		naslov.setPrefWidth(300);
		Button nazad = new Button("< Nazad");
		nazad.setPrefWidth(170);
		Button dodajP = new Button("Dodaj proizvod");
		dodajP.setPrefWidth(220);
		hb.getChildren().addAll(nazad,naslov, dodajP);
		hb.setPadding(new Insets(10,10,10,10));
		
		
		//--------------------------------------------------------------------
		
		TableView tabela = new TableView();
		tabela.setEditable(true);
		tabela.setStyle("-fx-base: " + TAMNA_BOJA
				+ "-fx-control-inner-background:" + TAMNA_BOJA
				+ "-fx-background-color: " + TAMNA_BOJA
				+ "-fx-table-cell-border-color: transparent;"
				+ "-fx-table-header-border-color: transparent;"
				+ "-fx-padding: 5;");
		
		Proizvod p = new Proizvod();
		ArrayList<Proizvod> l = p.ucitajProizvode();
		
		TableColumn<Proizvod, String> kol1 = new TableColumn<>("Naziv");
	    kol1.setCellValueFactory(new PropertyValueFactory<>("naziv"));
	    kol1.setMinWidth(200);
	    kol1.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);

	    TableColumn<Proizvod, Float> kol2 = new TableColumn<>("Cijena");
	    kol2.setCellValueFactory(new PropertyValueFactory<>("cijena"));
	    kol2.setMinWidth(100);
	    kol2.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);
	    
	    TableColumn<Proizvod, Float> kol3 = new TableColumn<>("Opis");
	    kol3.setCellValueFactory(new PropertyValueFactory<>("opis"));
	    kol3.setMinWidth(150);
	    kol3.setStyle("-fx-text-fill:" + SVIJETLA_BOJA);
	    
	    TableColumn<Proizvod, Void> kol4 = new TableColumn(" ");
	    
	    
	    Callback<TableColumn<Proizvod, Void>, TableCell<Proizvod, Void>> cellFactory = new Callback<TableColumn<Proizvod, Void>, TableCell<Proizvod, Void>>() {
            @Override
            public TableCell<Proizvod, Void> call(final TableColumn<Proizvod, Void> param) {
                final TableCell<Proizvod, Void> cell = new TableCell<Proizvod, Void>() {

                    private final Button btn = new Button("Ažuriraj");

                    {
                        btn.setOnAction(event -> {
                        	//da otvori popup u kojem unesem nove podatke(vec su uneseni u polja trenutne vrijednosti)
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
	    


	    for(int i = 0 ; i < l.size() ; i++) {
	    	tabela.getItems().addAll(new Proizvod(l.get(i).naziv, l.get(i).id, l.get(i).cijena, l.get(i).opis));
	    }
	    
	    
		
		//---------------------------------------------------------------------
	    

		nazad.setOnMouseClicked(event -> {
			setTrgovacScene(trgovac);
		});
		nazad.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		
		dodajP.setOnMouseClicked(event -> {
			popup5();
		});
		dodajP.setStyle("-fx-background-color: " + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill: " + TAMNA_BOJA);
		
		root.setTop(hb);
		root.setCenter(tabela);
		root.setStyle("-fx-background-color: " + TAMNA_BOJA);
		Scene scene = new Scene(root, 770, 500);
		primaryStage.setScene(scene);
		
	}
	
	
	public void popup4(Proizvod p) {
		
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Ažuriraj proizvod");
		
		VBox vb = new VBox(20);
		
		Label nazivL = new Label("Naziv:");
		TextField nazivTF = new TextField(p.naziv);
		Label opisL = new Label("Opis:");
		TextField opisTF = new TextField(p.opis);
		Label cijenaL = new Label("Cijena");
		TextField cijenaTF = new TextField(Float.toString(p.cijena));
		Label l = new Label();
		
		nazivL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + SVIJETLA_BOJA);
		nazivTF.setStyle("-fx-background-color:" + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		opisL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + SVIJETLA_BOJA);
		opisTF.setStyle("-fx-background-color:" + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		cijenaL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + SVIJETLA_BOJA);
		cijenaTF.setStyle("-fx-background-color:" + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		
		Button potvrdi = new Button("Potvrdi");
		potvrdi.setOnAction(event -> {
			p.azurirajProizvod(nazivTF.getText(), opisTF.getText(), Float.parseFloat(cijenaTF.getText()));
			dialog.close();
			azurirajProizvodScene();
		});
		
		vb.getChildren().addAll(nazivL, nazivTF, opisL, opisTF, cijenaL, cijenaTF, l, potvrdi);
		vb.setPadding(new Insets(10,40,10,40));
		vb.setStyle("-fx-background-color:" + TAMNA_BOJA);
		
		Scene scene = new Scene(vb,400,400);
		dialog.setScene(scene);
		dialog.show();
		
	}
	
	
	public void popup5() {
		
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Dodaj proizvod");
		
		VBox vb = new VBox(20);
		
		Label nazivL = new Label("Naziv:");
		TextField nazivTF = new TextField();
		Label opisL = new Label("Opis:");
		TextField opisTF = new TextField();
		Label cijenaL = new Label("Cijena");
		TextField cijenaTF = new TextField();
		Label l = new Label();
		
		nazivL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + SVIJETLA_BOJA);
		nazivTF.setStyle("-fx-background-color:" + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		opisL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + SVIJETLA_BOJA);
		opisTF.setStyle("-fx-background-color:" + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		cijenaL.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + SVIJETLA_BOJA);
		cijenaTF.setStyle("-fx-background-color:" + SVIJETLA_BOJA + "-fx-font-weight: bold; -fx-text-fill:" + TAMNA_BOJA);
		
		Button potvrdi = new Button("Potvrdi");
		potvrdi.setOnAction(event -> {
			Proizvod p = new Proizvod(nazivTF.getText(), opisTF.getText(), Float.parseFloat(cijenaTF.getText()));
			dialog.close();
			p.dodajProizvod();
		});
		
		vb.getChildren().addAll(nazivL, nazivTF, opisL, opisTF, cijenaL, cijenaTF, l, potvrdi);
		vb.setPadding(new Insets(10,40,10,40));
		vb.setStyle("-fx-background-color:" + TAMNA_BOJA);
		
		Scene scene = new Scene(vb,400,400);
		dialog.setScene(scene);
		dialog.show();
		
	}
	
	
	
}





























