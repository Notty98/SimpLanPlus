# SimpLanPlus

Progetto di Compilatori e Interpreti 2021/22.

## Report

La cartella report contiene:
* Report_Progetto_Compilatori_SimpLanPlus_parte_1: report della prima consegna
* Report_Progetto_Compilatori_SimpLanPlus_parte_2: report della seconda consegna

## Dipendenze

Per poter eseguire il compilatore è necessario aver installato SDK 17+ e ANTLR 4.9.3 e ST 4.3.1.

## Esecuzione

Per eseguire il programma è necessario:
1. Inserire il codice nel path ```./src/input/test```, se il file ```test``` non è presente è necessario crearlo (senza estensione)
2. Eseguire il Main (```src/main/Main```)
3. Se sono presenti degli errori semantici o di effetti l'esecuzione termina e vengono mostrati sulla console e salvati
nella directory ```src/output```

## Possibili problemi nell'esecuzione (IntelliJ Ultimate)

Se non si riesce a eseguire il programma:
1. ```File > Project Structure > Project```, verificare il path del ```compiler output```
2. ```File > Invalidate Caches```, spuntare le prime due voci e poi selezionare  ```invalidate and restart```

## Autori
* Stefano Notari
* Francesco Santilli