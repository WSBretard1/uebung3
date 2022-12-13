Hallo Gianna,

Ich hätte es nicht geschafft eine vollständige Übung 1 nachzuholen (in Übung 1 hatte ich sehr wenig geschafft), wollte aber auch nicht riskieren,
dass ich Punktabzug bekomme, wenn ich viel aus der Musterlösung übernehme (ist meiner Meinung nach nicht das beste System).
Deshalb habe ich mich in meinem Projekt nur auf die Aufgaben aus Übung 2 konzentriert und nicht die Dinge aus Übung 1 nachgeholt.
Demnach habe ich keine File_Impl Klassen, sondern nur die MongoDB_Impl Klassen und das jeweilige Interface dazu.
Die Objekte mit denen ich in den MongoDB_Impl Klassen arbeite, kreiere ich deshalb in der XMLParser Klasse.

Die Klasse MongoDBOperator kann nicht alles, was in der Aufgabe gefordert ist, aber ich konnte alle Objekte in die Datenbank laden.

Die KLasse MongoDBConnectionHandler funktioniert so, dass man seine txt Datei mit den Datenbank Daten in den resource Ordner packt und diese
dann von der Klasse gelesen werden und automatisch eine Verbindung hergestellt wird.
Mir ist auch klar, dass ich einige Klassen wie "Kommentar" nicht habe, aber diese sind trotzdem in der Datenbank und als Methoden
in den anderen Klassen. Aus Zeit- und Einfachheitsgründen habe ich es so implementiert. Auch mit Redner/RedenListe.

Das Einlesen der XML-Dateien funktioniert folgendermaßen:
Man muss die jeweiligen XML-Dateien in den file Ordner in resources packen. In dem Projekt sind gerade nur 2 XML-Dateien,
weil wir die ja nicht mit Hochladen sollten und es auch sehr lange so dauert, das Programm auszuführen und alle XML-Dateien in die Datenbank zu laden.

Das Menu ist eigentlich eher für mich und noch nicht fertig entworfen, für zB die Abfragen für Aufgabe 2. Es ist nur dafür da um XML-Dateien
in eine Datenbank hochzuladen. Das kannst Du auch gerne mal ausprobieren, bis auf Duplikate key Errors, sollte das einwandfrei funktionieren.

Ich habe dieses Mal sehr darauf geachtet, dass mein Code mit dem Klassendiagramm übereinstimmt und habe so gut wie möglich kommentiert. :)