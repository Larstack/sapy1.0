Sapy 1.0
=======

Interprete Java per il Sapienza BASIC v.1.0

Il progetto Sapy(Sapienza BASIC) v.1.0 implementa la prima versione dell'interprete del dialetto BASIC 
chiamato Sapienza BASIC v.1.0.<br />
Il progetto è stato sviluppato interamente in Java. Questo interpreta un file di testo .sapy e ne esegue le istruzioni.<br />

L'interprete è suddiviso in diversi moduli, tra i quali quelli di riferimento sono:<br />
• l'analizzatore lessicale, chiamato Lexer,<br />
• l'analizzatore sintattico, definito Parser,<br />
• l'Interprete,<br />
• il modulo chiamato Sapy, punto di partenza di tutto l'interprete.<br />

Lexer (it.uniroma1.sapy.lexer)<br />
L'analizzatore lessicale riconosce in una stringa di testo le parole(nel senso più ampio del termine) valide, <br />
riconosciute come Token dall'interprete.<br />
I Token sono organizzati in un package (it.uniroma1.sapy.lexer.token), dove ogni classe, sottoclasse di Token, <br />
ne definisce uno specifico. I Token validi sono quelli elencati nell'enumeration Tok (appartenente <br />
allo stesso package dei Token).<br />
Durante l'analisi lessicale, il Lexer riconosce i Token validi anche non separati da spazi <br />
(es: 3+2/8 oppure $c=25:PRINT $c) e istanzia per ogni Token riconosciuto un oggetto del tipo specifico,<br /> 
i quali vanno a comporre una lista ordinata.<br />
Il Lexer analizza il codice linea per linea, individua immediatamente se si tratta di linea di commento e, <br />
in caso positivo, istanzia come Token di tipo FUNZIONE ogni parola successiva all'istruzione REM; <br />
in caso di linea non di commento, il Lexer individua nella linea di codice i blocchi separati da spazio. <br />
Se il blocco analizzato è un Token valido, mediante reflection istanzia subito l'oggetto di tipo specifico. <br />
Qualora il blocco individuato non costituisca un Token valido, il blocco viene analizzato carattere per carattere <br />
al fine di riconoscere eventuali Token non separati da spazio. <br />
Se il blocco non viene in nessun caso riconosciuto, viene istanziato un Token di tipo FUNZIONE.<br />

Parser (it.uniroma1.sapy.parsing)<br />
L'analizzatore sintattico, partendo da una lista ordinata di Token, interpreta la sequenza e la traduce <br />
in una lista di istruzioni che va a costituire il ProgrammaEseguibile (it.uniroma1.sapy.runtime).<br />
Il Parser, attraverso il metodo creaListaDiIstruzioni, esegue un ciclo che itera sulla lista di Token; <br />
Individuato il tipo di Token ad inizio linea(quindi successivo ad un Token di tipo EOL per le linee successive<br /> 
alla prima), crea l'istruzione constatando la correttezza della sintassi.<br />
Per i Token di tipo FOR e IF, l'istruzione creata conterrà al suo interno una lista di istruzioni <br />
che verrà eseguita solo se la condizione è vera, nel caso dell'istruzione IF, <br />
oppure verrà eseguita tante volte finché la condizione non diventerà falsa, nel caso dell'istruzione FOR.<br />
Le istruzioni, organizzate nel package it.uniroma1.sapy.runtime.istruzioni, implementano l'interfaccia Istruzione.<br /> 
Ogni istruzione ha un metodo esegui() e un metodo getLabel(); quest'ultimo ritorna l'eventuale valore di etichetta <br />
che identifica la linea di codice, altrimenti, se non presente, restituisce null.<br />

Le istruzioni valide, riconosciute dall'interprete, sono:<br />
• END - termina il programma;<br />
• FOR - esegue una lista di istruzioni fintanto che la condizione risulti vera. La condizione nel FOR è costituita <br />
da un range entro il quale il valore della variabile iterata deve eseere contenuto; <br />
una volta che il valore della variabile esce da questo range, la condizione risulta falsa e, <br />
pertanto, il programma esce dal ciclo;<br />
• GOTO - il programma salta alla linea di codice identificata dall'etichetta numerica definita nell'istruzione;<br />
• IF - esegue la lista di istruzioni contenuta al suo interno solo la condizione è verificata. <br />
La condizione, nell'istruzione IF, può essere costituita da un'espressione booleana oppure da un confronto;<br />
• INPUT - riceve una stringa in input e salva il valore nella variabile indicata nell'istruzione;<br />
• PRINT - stampa a video il contenuto di una variabile, il risultato di un'espressione o una stringa.<br />

L'assegnamento di un valore ad una variabile (ad es: $num=5), anche se non propriamente un'istruzione, <br />
è considerato dall'interprete come tale, pertanto anch'essa implementa l'interfaccia Istruzione.<br />

Interprete (it.uniroma1.sapy.runtime)<br />
Viene costruito da una stringa contenente il sorgente Sapy da interpretare.L'interprete esegue l'analisi <br />
prima lessicale e poi sintattica del sorgente, per mezzo del Lexer e del Parser, crea il programma eseguibile e lo<br />
esegue. <br />
Il programma eseguibile creato dall'interprete è un'istanza della classe ProgrammaEseguibile<br /> (it.uniroma1.sapy.runtime).<br />
Questa contiene un metodo esegui() che esegue la lista di istruzioni con la quale è stato costruito <br />
il programma eseguibile. <br />
L'esecuzione di un'istruzione ritorna sempre null, tranne che nel caso di un'istruzione GOTO, <br />
che ritorna invece il valore dell'etichetta su cui va effettutato il salto.<br />
Il metodo esegui() del ProgrammaEseguibile itera sulla lista di istruzioni, che vengono eseguite in successione; <br />
se, una volta eseguita, l'istruzione ritorna null, si passa all'istruzione successiva; <br />
in caso contrario, si itera sulla lista di istruzioni senza eseguirle, fino a trovare quella con etichetta<br />
uguale al valore ritornato dall'istruzione. Una volta trovata l'etichetta, il programma ricomincia ad iterare <br />
sulle istruzioni, eseguendole, partendo da quella con etichetta uguale al valore ritornato dall'istruzione GOTO.<br />

Sapy (it.uniroma1.sapy)<br />
Questo modulo prende in input da consolle il percorso di un file sorgente Sapy da interpretare.<br />
Il file sorgente viene convertito in stringa, in modo da poter essere analizzato ed eseguito <br />
dai moduli dell'interprete Sapy.<br />

Le espressioni in Sapy (it.uniroma1.sapy.runtime.espressioni)<br />
Tutte le espressioni estendono la classe astratta Espressione, che dichiara il metodo getRisultato().<br />
Sapy riconosce come espressioni:<br />
•espressioni booleane - espressioni contenenti gli operatori booleani AND, OR e NOT, espressioni di tipo confronto, <br />
variabili, valori booleani (TRUE, FALSE);<br />
• espressioni matematiche - espressioni contenenti gli operatori matematici (+,-,*,/), variabili, valori interi;<br />
• espressioni di tipo confronto - espressioni contenenti gli operatori <,>,<=,>=,<>, espressioni matematiche, <br />
stringhe, variabili;<br />
• condizioni - sono le condizioni utilizzate dalle istruzioni FOR e IF; possono essere costituite da <br />
espressioni booleane ed espressioni di tipo confronto.<br />
