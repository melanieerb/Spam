# Spam Filter (dist HS2016)

## Konfiguration

- ``THRESHOLD_VALUE`` Schwellenwert
- ``DEFAULT_ALPHA`` Alpha
- ``AMOUNT_OF_EMAILS`` Anzahl Emails die eingelesen werden (nur für Test Modus)

### Test Modus

- ``TEST = true;``

Der Test Modus orientiert sich am folgenden Beispiel:
http://www.math.kit.edu/ianm4/~ritterbusch/seite/spam/de

Es wird eine Mail aus ``resources/custom/ham`` sowie aus ``resources/custom/spam``
gelesen. Danach wird eine vordefinierte Email*  mit 2 Wörtern eingelesen und
auf die Spamwahrscheinlichekeit geprüft. Das Ergebnis sollte ~0.38% sein.

``* resources/testMail``

## Funktionsweise
### Anlern
#### 1. Anlern Emails einlesen

Emails werden nacheinander eingelesen und geparst. Das Wort und die
Häufigkeit wird in zwei HashMaps gespeichert .

- ``HashMap<String, Double> wordCounterHam``
- ``HashMap<String, Double> wordCounterSpam``

Zusätzlich wird die Anzahl der eingelesen Emails in den
folgdenen Variablen gespeichert.

- ``amountOfHamEmails``
- ``amountOfSpamEmails``

#### 2. HashMaps zusammenführen

Um die beiden Hashmaps zu mergen werden zuerst alle Wörter in einer Liste
gespeichert.

- ``HashSet<String> words``

Danach werden die beiden Hashmaps in einer neuen HashMap zusammengeführt und
gespeichert.

- ``HashMap<String, Word> spamProbability``

```java
private static class Word {
    String word;
    Double pH; /* Wahrscheinlichkeit in Ham Mails */
    Double pS; /* Wahrscheinlichkeit in Spam Mails */
}
```

### Kalibrierung

#### 1. Emails einlesen

Jede Email wird einglesen und in einzelnen Wörtern in eine Liste gespeichert.

- ``List<String> email``

#### 2. Spamwahrscheinlichkeit berechnen

Nun wird die Spamwahrscheinlichkeit mithilfe der ``spamProbability`` HashMap
berrechnet und anschliessend mit dem eingstellten Schwellenwert verglichen.

```java
double AnB_S = 1
double AnB = 1
for(String word : email){
Word w = spamProbability.get(word)
  if(w != null) {
      AnB_S = AnB_S * w.pS
      AnB = AnB * w.pH
  }
}
return (AnB_S / (AnB_S + AnB));
```

### Test

#### 1. Emails einlesen

Ananlog zu Kalibrierung.

#### 2. Spamwahrscheinlichkeit berechnen

Ananlog zu Kalibrierung.

#### 3. Output

Prozentwert der markierten Spam- bzw. Hammails ausgeben.

### Email zu Anlernphase hinzufügen

Ein vordefiniertes Email wird eingelesen und die Spamwahrscheinlichkeit
berrechnet. Anschliessend kann man wählen ob dies künftig für den Spam oder den
Ham in der Anlernphase verwendet werden soll. 

## Bemerkungen

### Vorteil

- Der Spamfilter lernt dazu

### Nachteil

- Der Spamfilter muss jedes mal alles neu lernen -> keine Datenbank
