Zeitverwaltung
==============

zu verwalten
------------
* Aktuelle Zeit + Wochentag
* Erinnerungs-Zeiten
	* nächste Erinnerungs-Zeit
	* vergangene Zeiten (als Einschränkung beim Ändern)
* tatsächliche Umfrage-Zeiten
* Zuordnung Button -> Zeit/Wochentag

Ideen dazu
----------
* Grundsätzlich nur volle Stunden
* Int-Vergleich (Calendar.HOUR_OF_DAY für aktuelle Zeit)
* Erinnerungen im 2D-Date-Array[7][4] (Tag | Zeitslot)
	* array[Tag][0] -> 0900 - 1200
	* array[Tag][1] -> 1300 - 1500
	* array[Tag][2] -> 1600 - 1900
	* array[Tag][3] -> 2000 - 2300
	* Button-Press ermittelt Zeile (-> i)
		* schreibt gewählte Zeit in array[Tag][i]
* bei gewählter Tag == aktueller Tag
	* vergangene Zeiten sperren
	* Timeslots, die bereits Alarm ausgelöst haben, sperren
* wiederkehrende Wochentage
	* [optional] Datum anzeigen
* [optional] Endzeit/-Tag im Admin-Panel voreinstellen
