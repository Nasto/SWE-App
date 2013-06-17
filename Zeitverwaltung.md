zu verwalten
------------
* Aktuelle Zeit + Datum
* Erinnerungs-Zeiten
	* nächste Erinnerungs-Zeit
	* vergangene Zeiten (als Einschränkung beim Ändern)
* tatsächliche Umfrage-Zeiten
* Zuordnung Button -> Zeit/Datum

Ideen dazu
----------
* Date/Calender von Android nutzen
* Erinnerungen im 2D-Date-Array[7][4] (Tag | 4 Zeiten)
	* array[Tag][0] -> 0900 - 1200
	* array[Tag][1] -> 1300 - 1500
	* array[Tag][2] -> 1600 - 1900
	* array[Tag][3] -> 2000 - 2300
	* Button-Press ermittelt Zeile (-> i)
	* schreibt gewählte Zeit in array[Tag][i]
	* bei gewählter Tag == aktueller Tag -> gewälte Zeit.before(aktuelle Zeit)
* als int/string in globalen Settings speichern
	* bei bedarf holen und in Date parsen



---------

* eigene Zeit-Klasse schreiben
	* comparable
	* mit/ohne Datum enthalten
		* Datum ggf. als eigene Klasse
		* Tages-Buttons anders als Zeitslot-Buttons
