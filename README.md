# Modeling-Environment

Program został zrealizowany w ramach zadania z kursu programowania: 

**1. Założenia ogólne**

W klasach umieszczonych w pakiecie models zdefiniowano modele symulacyjne, wykonujące jakieś obliczenia dla okresów (np. lat)

Definicja modelu zawiera:
- pole o nazwie LL, oznaczające liczbę lat symulacji,
- pola oznaczające zmienne modelu - są to tablice liczb rzeczywistych.
- ew. pola pomocnicze
- metodę public void run(), wykonująca obliczenia.
- ew. inne metody pomocnicze.
  
Pola 1 i 2 są oznaczane adnotacją @Bind, co umozliwia:
- nadanie wartości zmiennym wejściowym modelu przed wykonaniem obliczeń,
- pobranie wartości zmiennych wyliczonych w modelu (po wykonaniu obliczeń).

Wszystkie zmienne oznaczone adnotacją @Bind są dostępne dla skryptów, które mogą być uruchamiane po wykonaniu obliczen modelowych i  wykonywać jakieś dalsze obliczenia.
Są także dostępne dla ew. innych modeli.

Zarządzaniem obliczeniami zajmuje się klasa Controller, która ma następujące publiczne składowe:
- konstruktor - Controller(String modelName) - parametrem jest tu nazwa klasy modelu,
- Controller readDataFrom(String fname) - wczytuje dane do obliczeń z  pliku o nazwie fname.
- Controller runModel() - uruchamia obliczenia modelowe,
- Controller runScriptFromFile(String fname)  - wykonuje skrypt z pliku o nazwie fname,
- Controller runScript(String script) - wykonuje kod skryptu podany jako napis,
- String getResultsAsTsv() - zwraca wyniki obliczeń (wszystkie zmienne z modelu oraz zmienne utworzone w skryptach) w postaci  napisu, którego kolejne wiersze zawierają nazwę zmiennej i jej wartosci, rozdzielone znakami tabulacji.


**2.  Założenia co do danych wejściowych**

Pliki z danymi wejściowymi zawierają w kolejnych wierszach dane w postaci:

nazwa_zmiennej wartość1  [ wartość2 ... wartoścN ]

Specjalny wiersz zaczynający się słowem LATA specyfikuje lata obliczeń, np.
LATA  2015 2016 2017 2018 2019

Na podstawie tego wiersza definiowana jest wartość specjalnej zmiennej LL (liczba lat obliczeń), dostepnej w modelu i w skryptach.

Wartości dla zmiennych może być od 1 do LL.
Jesli jest ich mniej niż LL, to pozostałe są ustalane na ostatnią z podanych wartości.


**3. Opis plików:**

W folderze Modeling znajduje się plik typu .txt, w którym znajdują się dane oraz plik .groovy, zawierajacy treść sktyptu.
