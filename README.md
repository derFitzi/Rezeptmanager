**Rezept-Manager**
*Ziel:*
Die Nutzer sollen über eine graphische Oberfläche Kochrezepte aus einer DB (quasi das eigene Kochbuch) Rezepte suchen und öffnen können. Um aber Rezepte in die Datenbank zu bekommen kann man auch über eine API der spoonacular.com neue Rezepte suchen, die man dann favorisieren (in die DB speichern) kann. Des Weiteren soll man dann zu den gespeicherten Rezepten Notizen aufschreiben. Diese können aber auch wieder gelöscht werden, genauso wie die Favorisierten.

*Funktionsweise:*
Auf der Vue App soll auf der linken Seite einer Liste mit Suchfeld für die Rezepte der Spoonacular. Diese aktualisiert sich mit jeder Anwendung. Wenn das Rezept gespeichert wird werden die Daten über die API-Schnittstelle an Springboot gegeben und in der Datenbank gespeichert. Auf der rechten Seite der Anwendung ist die Liste mit Suchfeld der favorisierten Rezepte aus der Datenbank. Diese Liste wird vollständig angezeigt und aktualisiert sich, wenn man in der Suche filtert. Wenn man auf eines der Rezepte in der Liste klickt soll in der Mitte das volle aufpoppen. Durch Knöpfe werden sowohl die favorisierten Rezepte als auch die Notizen aus der DB gelöscht.
Verwendete Technologien:
•	Frontend: Vue.js (mit REST Calls an das Backend)
•	Backend: Spring Boot mit 
•	Datenbank: H2 mit Zugriff über Spring Data JPA
•	Externe Schnittstelle: Spoonacular REST API
•	Kommunikation: JSON / HTTP
*Entitäten:*
•	Recipe 
•	Ingredient 
•	UserNote
*Mögliche Herausforderungen:*
•	Die Spoonacular API hat ein Limit von 150 Anfragen pro Tag. Das könnte beim Testen schnell ausgereizt werden
•	Probleme beim Speichern und Umformatierung der Daten für die Datenbank
•	Benutzeroberfläche muss die komplexen Daten gut übersichtlich Darstellen
