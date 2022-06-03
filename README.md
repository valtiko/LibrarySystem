
# Dokumentacja projetu: **System do zarządzania biblioteka

## Opis programu / systemu
System zarządzania zadaniami w bibliotece

## Cel projektu 
Stworzenie oprogramowania umozliwiającego zarzadzanie pracownikami oraz ich zadaniami w bibliotece.

## Zakres projektu 


## Wymagania stawiane aplikacji / systemowi 
- System powinien mieć kilka modułów 
  - Moduł administracji użytkownikami (role)
		Role: gość, użytkownik, administrator, księgowy, bibliotekarz
  - Moduł raportów 
		rapotry generowane  są przez: bibliotekarza księgowego		
  - Moduł konfiguracji:
	 System umożliwia generowanie raportów PDF
	 System współpracuje z bazą danych
	 System umożliwia wysyłanie maili


## Panele / zakładki systemu, które będą oferowały potrzebne funkcjonalności 
- Panel administratora 
  - dodawanie, edycja, usuwanie książek
  - nadawanie ról uzytkownikom  
- Panel ksiegowego
  -Uwzgledniajac budzet sporządza liste dotyczacą zakupu książek z listy wysłanej przez blibliotekarza. Lista bedzie zawierał inofmracje o cenie oraz ilosci danej książki. 
  -Zarządza fakturami otrzymywanymi po zakupie książęk
- Panel bibliotekarza
  -Zarządza wypozyczeniami uzytkowników(akceptuje, odrzuca)
  -Sporzadza raporty zawierajacy informacje o książkach ktore zostana usuniete z systemu lub dodane do niego. Raport zostaje wyslany do administratora.
  -Sporzadza liste zawierajacy informacje o książkach na które jest zapotrzebowanie. Lista zostaje wyslany do ksiegowego.
-Panel uzytkownika
  -Wypożyczenie książki: uzytkownik moze wybrac kilka pozycji do wypozyczenia (maksymalnie 10) na maksymalnie 14dni, jezeli ilosc ksiazek dostepnych jest wieksza od 0 to uzytkownik moze wysłac prosbe o wypozyczenie ksiazki. Pracownik akceptuje wypozyczenie.
  -Panel wypozyczonych ksiazek który wysiwetla uzytkownikowi wypozyczone pozycje, uzytkownik ma w nim mozliwosc zwrotu. Najpierw bedą wyswietlac sie ksiazki aktualnie wypozyczone a pozniej ktore kiedys byly wypozyczone
  -Rezerwacja: uzytkownik moze zarezerwować ksiązke tylko w momencie braku jej dostepnosci, w momencie gdy ktos zwroci ksiazke dostanie powiadomienie na miala ze ksiazka jest dostepna, książka dla osoby bedzie dostepna przez 3h które ją zarezrwowały. Pracownik bedzie wybierał po dacie zarezerwowania kto wypozyczy ta ksiazke.
  -Wyslanie zapytania o zamowienie brakującecj książki której w ogole nie ma, wypelnia formularz(autora i nazwe ksiazki) którą proponuje aby biblioteka zamowiła.
  -Jednorazowa możliwość przedluzenia wypozyczenia o 7 dni
Panel goscia:
  -Przegląda liste książek: lista ksiązek jest wczytana z bazy danych i wyswietlona na ekranie, uzytkownik moze przeglądać dostepne książki
  -Logowanie: uzytkownik podaje email i haslo, w przypadku zapomnienia wybiera przycisk "Zapomniałem hasło" nastepnie przenosi go do okna gdzie podaje email, nastepnie na ten email zostanie wysalne to haslo ktore mial
  -Rejestracja: uzytkownik podaje imie nazwisko email i haslo, nastepnie zatwierdza dane i zostaje wysalny kod na email, uzytkownik musi podac kod w celu potwierdzenia rejestracji.
- Zakładka raportów 
  - Generowanie raportów 
  


## Typy wymaganych dokumentów w projekcie oraz dostęp do nich 
- Raporty PDF 
  - rodzaje raportów


## Przepływ informacji w środowisku systemu 
Scentralizowany oparty na bazie danych.

## Użytkownicy aplikacji i ich uprawnienia
- Gość
	- Ma możliwość przeglądania książek
	- Może się zalogować lub zarejestrować
- Użytkownik 
	- Ma możliwość wypożyczenia książek 
	- Ma dostęp do swojej historii wypożyczeń
	- Ma możliwość wysłania prośby o zakupienie książki do biblioteki
	- Ma możliwość złożenia rezerwacji na książkę
	- Ma możliwość jednorazowego przedłużenia wypożyczenia ksiązki
- Bibliotekarz 
	- Ma dostęp do panelu wypożyczeń użytkowników 
	- Ma dostęp do panelu próśb o zakup książek.
	- Ma możliwość tworzenia raportów dotyczących stanów ksiązek w bibliotece
- Księgowy
	- Ma dostęp do list sporządzonych przez Bibliotekarza oraz modyfikuje książki w nich zawarte o cenę 
	- Ma dostęp do budżetu biblioteki
	- Ma dostęp do faktur wysyłanych do biblioteki
- Administrator
	- Ma możliwość modyfikowania ról pracowników
	- Ma możliwość dodawania, usuwania oraz modyfikowania książek w systemie
	- Ma możliwość tworzenia oraz usuwania kategorii
## Interesariusze 
- Interesariusze wewnętrzni 
  -  księgowy, bibliotekarz, administrator
- Interesariusze zewnętrzni 
  -  gość, uzytkownik

## Diagramy UML
Diagramy sekwencji 

![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/Diagram%20Administratora/DiagramSekwencjiDodanieKsiazek.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/Diagram%20Administratora/DiagramSekwencjiNadanieRoli.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/Diagram%20U%C5%BCytkownika/Panel_ksi%C4%85%C5%BCek.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/Diagram%20U%C5%BCytkownika/Wypo%C5%BCyczenie_ksi%C4%85%C5%BCek.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/DiagramBibliotekarz_Ksiegowy/Sek_ksiegowy_sprListyFinanse.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/DiagramBibliotekarz_Ksiegowy/sek_Bibliotekarz_Zarz%C4%85dzaWypozyczeniamiUzytkownik%C3%B3w.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/DiagramBibliotekarz_Ksiegowy/sek_bib_ListaZapotrzebowanie.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/DiagramBibliotekarz_Ksiegowy/sek_bib_raportozmianachZasobow.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/Diagramy%20Go%C5%9Bcia/Logowanie%20Sekwencji.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Sekwencji/Diagramy%20Go%C5%9Bcia/Rejestracja%20Sekwencji.png)



Diagramy aktywnosci
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/Diagramy%20Go%C5%9Bcia/Logowanie%20Aktywno%C5%9Bci.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/Diagramy%20Go%C5%9Bcia/Rejestracja%20Aktywno%C5%9Bci.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/Diagramy%20U%C5%BCytkownika/Diagram%20aktywno%C5%9Bci%20wypo%C5%BCyczenia.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/Diagramy%20U%C5%BCytkownika/Diagram%20aktywno%C5%9Bci%20zwrotu%20i%20przed%C5%82u%C5%BCenia%20wypo%C5%BCyczenia.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/Diagramy%20Go%C5%9Bcia/Logowanie%20Aktywno%C5%9Bci.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/DiagramyAktywnosci%20KierwonikAdmin/Admin%20dodanie%20ksiazek.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/DiagramyAktywnosci%20KierwonikAdmin/Edycja%20roli.png)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/pngAktywnosciBibliotekarz_Ksiegowy/BibliotekarzReportOZmianach.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/pngAktywnosciBibliotekarz_Ksiegowy/BibliotekarzZapotrzebowanieLista.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/pngAktywnosciBibliotekarz_Ksiegowy/KsiegowyListaZakupowFinanse.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/pngAktywnosciBibliotekarz_Ksiegowy/BibliotekarzZarzadzanieWyporzyczeniami.PNG)
![alt text](https://github.com/UR-INF/21-22-pz-projekt-lab1-l1_g4-1/blob/main/Diagramy%20Aktywno%C5%9Bci/pngAktywnosciBibliotekarz_Ksiegowy/KsiegowyZarzadzanieFakturami.PNG)

## Baza danych
###### Diagram ERD

###### Skrypt do utworzenia struktury bazy danych

###### Opis bazy danych

## Wykorzystane technologie 
- Język Java 17
  - JavaFX
  - Intelij Ultimate
  - Hibernate
  - Maven
   

## Pliki instalacyjne wraz z opisem instalacji i konfiguracji wraz pierwszego uruchomienia
