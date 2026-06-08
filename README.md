# GeoTrace Lab11 – GPS & Google Maps Activity

Application Android développée en **Java** permettant d’afficher une **Google Map interactive**, de demander la permission de localisation, de suivre la position de l’utilisateur en temps réel et d’ajouter des markers dynamiques sur la carte.

Ce laboratoire a été personnalisé avec une interface moderne, colorée et professionnelle afin de ne pas se limiter au template basique généré par Android Studio.

---

## Objectif

Le but de ce laboratoire est de :

- Créer une application Android basée sur **Google Maps Activity**
- Intégrer une clé **Google Maps API**
- Afficher une carte Google Maps dans l’application
- Ajouter les permissions nécessaires dans le Manifest
- Demander la permission de localisation au moment de l’exécution
- Écouter les changements de position avec `LocationManager`
- Utiliser les providers :
  - `GPS_PROVIDER`
  - `NETWORK_PROVIDER`
- Ajouter un marker à chaque nouvelle position
- Déplacer automatiquement la caméra vers la position actuelle
- Gérer le cas où la localisation/GPS est désactivée
- Créer une interface plus esthétique avec des layouts XML personnalisés

---

## Description de l’application

L’application **GeoTrace Lab11** contient deux écrans principaux :

### 1. Écran d’accueil

L’écran d’accueil présente l’application avec une interface visuelle moderne.

Il contient :

- Le titre de l’application
- Une courte description du lab
- Les fonctionnalités principales
- Un bouton pour lancer la carte

Cet écran est géré par :

```text
MainActivity.java
activity_main.xml
```

### 2. Écran Google Maps

L’écran principal de la carte permet de :

- Afficher une Google Map
- Demander la permission de localisation
- Afficher la position actuelle
- Ajouter un marker principal
- Ajouter des markers de trace pour chaque mouvement
- Afficher les coordonnées GPS
- Afficher la précision de la localisation
- Recentrer la caméra
- Basculer entre mode normal et mode satellite
- Afficher une boîte de dialogue si la localisation est désactivée

Cet écran est géré par :

```text
MapsActivity.java
activity_maps.xml
```

---

## Fonctionnalités

- Affichage d’une Google Map interactive
- Demande de permission runtime pour la localisation
- Gestion de la permission acceptée ou refusée
- Suivi de la position en temps réel
- Utilisation du GPS et du réseau pour obtenir la localisation
- Ajout d’un marker principal pour la position actuelle
- Ajout de markers secondaires pour l’historique des déplacements
- Zoom automatique sur la position détectée
- Bouton personnalisé pour recentrer la caméra
- Bouton pour changer le style de la carte
- Mode carte standard / satellite
- Cercle de précision autour de la position actuelle
- Boîte de dialogue en cas de GPS désactivé
- Interface personnalisée avec :
  - Dégradés
  - Cartes arrondies
  - Boutons modernes
  - Panneau flottant sur la carte
  - Couleurs harmonisées

---

## Technologies utilisées

- Android Studio
- Java
- XML
- Google Maps SDK for Android
- LocationManager
- GoogleMap API
- Gradle Kotlin DSL
- API Android minimum selon configuration du projet

---

## Aperçu de l’application

▶️ Une démonstration vidéo complète est disponible dans le dossier **Demo** du repository.

⚠️ En cas de problème de lecture depuis la plateforme, la vidéo peut être consultée directement dans :

```text
Demo/
```


https://github.com/user-attachments/assets/be7dfd8e-95f6-4cbf-9b66-a46c100f7595



La vidéo montre :

- Le lancement de l’application
- L’écran d’accueil personnalisé
- L’ouverture de la carte
- La demande de permission localisation
- L’affichage de la carte Google Maps
- Le suivi de la position
- L’apparition des markers
- Le changement du style de carte
- Le recentrage automatique sur la position

---

## Structure du projet

```text
GPS/
│
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   │
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   │
│       │   ├── java/
│       │   │   └── com/
│       │   │       └── malak/
│       │   │           └── gps/
│       │   │               ├── MainActivity.java
│       │   │               └── MapsActivity.java
│       │   │
│       │   └── res/
│       │       ├── drawable/
│       │       │   ├── bg_badge_geo.xml
│       │       │   ├── bg_btn_primary.xml
│       │       │   ├── bg_btn_secondary.xml
│       │       │   ├── bg_home_gradient.xml
│       │       │   ├── bg_map_panel.xml
│       │       │   ├── ic_launcher_background.xml
│       │       │   └── ic_launcher_foreground.xml
│       │       │
│       │       ├── layout/
│       │       │   ├── activity_main.xml
│       │       │   └── activity_maps.xml
│       │       │
│       │       ├── values/
│       │       │   ├── colors.xml
│       │       │   ├── google_maps_api.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       │
│       │       ├── values-night/
│       │       │   └── themes.xml
│       │       │
│       │       └── xml/
│       │           ├── backup_rules.xml
│       │           └── data_extraction_rules.xml
│       │
│       ├── androidTest/
│       └── test/
│
├── Demo/
│   └── video_demo.mp4
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

---

## Fichiers principaux

### MainActivity.java

Ce fichier représente l’écran d’accueil de l’application.

Il permet de :

- Charger le layout `activity_main.xml`
- Afficher la date du lab
- Présenter les fonctionnalités principales
- Rediriger l’utilisateur vers l’écran Google Maps

La navigation vers la carte se fait avec un `Intent` :

```java
Intent intentCarte = new Intent(MainActivity.this, MapsActivity.class);
startActivity(intentCarte);
```

---

### MapsActivity.java

Ce fichier contient la logique principale du lab.

Il permet de :

- Initialiser Google Maps
- Préparer l’interface de la carte
- Vérifier les permissions de localisation
- Demander les permissions runtime
- Démarrer le suivi GPS/réseau
- Gérer les changements de position
- Ajouter et déplacer les markers
- Afficher les coordonnées
- Afficher la précision
- Recentrer la caméra
- Basculer entre mode normal et satellite
- Afficher une alerte lorsque la localisation est désactivée

---

### activity_main.xml

Ce layout représente l’écran d’accueil.

Il contient :

- Un fond en dégradé
- Une carte centrale arrondie
- Un badge “GPS TRACKING”
- Le titre `GeoTrace Lab11`
- Une description courte
- Une liste des fonctionnalités
- Un bouton `Lancer GeoTrace`

L’objectif de cet écran est de rendre l’application plus professionnelle et plus agréable visuellement.

---

### activity_maps.xml

Ce layout contient :

- Le fragment Google Maps
- Un panneau flottant au-dessus de la carte
- Les coordonnées de la position actuelle
- L’état du GPS
- La précision de localisation
- Un bouton `Recentrer`
- Un bouton `Satellite`

Le fragment principal utilisé est :

```xml
<fragment
    android:id="@+id/map"
    android:name="com.google.android.gms.maps.SupportMapFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

---

### AndroidManifest.xml

Le Manifest contient les permissions nécessaires :

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Il contient aussi la clé Google Maps :

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="@string/google_maps_key" />
```

L’activité lancée au démarrage est :

```xml
<activity
    android:name=".MainActivity"
    android:exported="true">
```

L’activité de la carte est :

```xml
<activity
    android:name=".MapsActivity"
    android:exported="false" />
```

---

### google_maps_api.xml

Ce fichier contient la clé API Google Maps :

```xml
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">
    VOTRE_CLE_ICI
</string>
```

⚠️ Pour des raisons de sécurité, la vraie clé API ne doit pas être exposée publiquement dans le README.

---

### colors.xml

Le fichier `colors.xml` contient la palette utilisée dans l’application.

La palette choisie donne un rendu moderne et harmonieux :

- Bleu foncé pour le texte principal
- Bleu/cyan pour les éléments GPS
- Vert pour les éléments positifs
- Orange pour les badges
- Blanc transparent pour les cartes flottantes

---

### Drawables personnalisés

Les fichiers dans `res/drawable` permettent de personnaliser l’interface.

#### bg_home_gradient.xml

Fond principal de l’écran d’accueil avec un dégradé coloré.

#### bg_map_panel.xml

Carte flottante affichée au-dessus de Google Maps.

#### bg_btn_primary.xml

Bouton principal utilisé pour lancer la carte ou recentrer la position.

#### bg_btn_secondary.xml

Bouton secondaire utilisé pour changer le style de la carte.

#### bg_badge_geo.xml

Badge affiché dans l’écran d’accueil.

---

## Logique de localisation

L’application utilise `LocationManager` pour récupérer la position de l’utilisateur.

Deux providers sont utilisés :

```java
LocationManager.NETWORK_PROVIDER
LocationManager.GPS_PROVIDER
```

### NETWORK_PROVIDER

- Plus rapide
- Fonctionne souvent en intérieur
- Utilise le réseau Wi-Fi ou mobile
- Moins précis que le GPS

### GPS_PROVIDER

- Plus précis
- Peut être plus lent
- Fonctionne mieux à l’extérieur
- Nécessite souvent un accès clair au ciel

---

## Permission runtime

Même si la permission est déclarée dans `AndroidManifest.xml`, Android demande aussi une autorisation au moment de l’exécution.

La permission est vérifiée avec :

```java
ActivityCompat.checkSelfPermission(...)
```

Si elle n’est pas encore accordée, l’application lance :

```java
ActivityCompat.requestPermissions(...)
```

La réponse de l’utilisateur est ensuite gérée dans :

```java
onRequestPermissionsResult(...)
```

---

## Gestion des markers

L’application utilise deux types de markers :

### Marker principal

Le marker principal représente la position actuelle.

S’il existe déjà, sa position est simplement mise à jour :

```java
repereActuel.setPosition(pointActuel);
```

### Markers de trace

À chaque changement de position, un marker secondaire est ajouté pour garder un historique visuel du déplacement.

Cela permet d’observer le trajet suivi par l’utilisateur pendant le test.

---

## Zoom automatique

Lorsque la position est détectée, la caméra se déplace automatiquement vers l’utilisateur.

```java
carteGoogle.animateCamera(
    CameraUpdateFactory.newLatLngZoom(pointActuel, 15.0f)
);
```

Le zoom `15.0f` permet d’avoir une vue proche au niveau quartier/rue.

---

## Gestion du GPS désactivé

Si la localisation est désactivée, l’application affiche une boîte de dialogue.

Cette boîte permet à l’utilisateur d’ouvrir directement les paramètres Android :

```java
Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
startActivity(intent);
```

La boîte de dialogue contient :

- Un titre clair
- Un message explicatif
- Un bouton pour ouvrir les paramètres
- Un bouton pour annuler

---

## Tests réalisés

### Test 1 : Compilation

Objectif :

- Vérifier que le projet compile correctement

Résultat attendu :

```text
BUILD SUCCESSFUL
```

---

### Test 2 : Lancement de l’application

Objectif :

- Vérifier que `MainActivity` est lancée en premier

Résultat attendu :

- Affichage de l’écran d’accueil
- Bouton `Lancer GeoTrace` visible

---

### Test 3 : Navigation vers la carte

Objectif :

- Vérifier que le bouton ouvre `MapsActivity`

Résultat attendu :

- La carte Google Maps s’ouvre correctement

---

### Test 4 : Affichage de Google Maps

Objectif :

- Vérifier que la carte n’est pas blanche

Résultat attendu :

- Carte visible
- Routes/grille affichées
- Panneau flottant visible

---

### Test 5 : Permission localisation

Objectif :

- Vérifier la demande de permission runtime

Résultat attendu :

- Android affiche la demande de permission
- L’application continue sans crash après acceptation

---

### Test 6 : Simulation de position

Objectif :

- Tester la localisation depuis l’émulateur

Position utilisée :

```text
Latitude  : 31.6295
Longitude : -7.9811
```

Résultat attendu :

- La caméra zoome vers Marrakech
- Le marker apparaît
- Les coordonnées s’affichent dans le panneau

---

### Test 7 : Changement de position

Objectif :

- Vérifier la mise à jour de la position

Positions testées :

```text
31.6295, -7.9811
31.6350, -7.9900
31.6400, -7.9800
```

Résultat attendu :

- Le marker principal se déplace
- Des markers de trace apparaissent
- La caméra suit le déplacement

---

### Test 8 : Bouton Recentrer

Objectif :

- Vérifier que le bouton recentre la caméra

Résultat attendu :

- La carte revient vers la dernière position connue

---

### Test 9 : Mode Satellite

Objectif :

- Vérifier le changement de style de carte

Résultat attendu :

- Le mode satellite s’active
- Le bouton change de texte
- Un second clic revient au mode standard

---

### Test 10 : GPS désactivé

Objectif :

- Vérifier l’affichage de la boîte de dialogue

Résultat attendu :

- L’application détecte la désactivation de la localisation
- Une boîte de dialogue propose d’ouvrir les paramètres Android

---

## Captures recommandées

Pour le rapport, les captures suivantes sont recommandées :

```text
1. Structure du projet avec MainActivity.java et MapsActivity.java
2. Fichier AndroidManifest.xml avec les permissions
3. Fichier google_maps_api.xml avec la clé masquée
4. Écran d’accueil GeoTrace Lab11
5. Demande de permission localisation
6. Carte Google Maps affichée
7. Marker de position avec coordonnées visibles
8. Plusieurs markers de trace après changement de position
9. Mode satellite activé
10. Boîte de dialogue lorsque la localisation est désactivée
11. Résultat de compilation réussie
12. Dossier Demo contenant la vidéo
```

---

## Sécurité de la clé API

La clé Google Maps API doit être protégée.

Bonnes pratiques :

- Ne pas publier la vraie clé dans le README
- Ne pas afficher la clé complète dans les screenshots
- Restreindre la clé dans Google Cloud Console
- Limiter la clé au package Android du projet
- Ajouter le SHA-1 correspondant
- Activer uniquement l’API nécessaire : Maps SDK for Android

Dans un dépôt public, il est préférable d’utiliser :

```xml
VOTRE_CLE_ICI
```

au lieu de la vraie clé.

---

## Problèmes rencontrés et solutions

### Erreur XML avec le caractère `&`

Problème :

```text
The entity name must immediately follow the '&'
```

Cause :

En XML, le caractère `&` ne peut pas être écrit directement.

Solution :

```xml
GPS &amp; Google Maps
```

au lieu de :

```xml
GPS & Google Maps
```

---

### Erreur `cannot find symbol R.id.txtEtatGps`

Problème :

```text
cannot find symbol variable txtEtatGps
```

Cause :

L’ID utilisé dans `MapsActivity.java` n’existait pas dans `activity_maps.xml`.

Solution :

Ajouter le bon ID dans le layout :

```xml
android:id="@+id/txtEtatGps"
```

---

### Carte blanche ou message “For development purposes only”

Causes possibles :

- Clé API incorrecte
- Maps SDK for Android non activé
- Mauvaise restriction de clé
- Package name incorrect
- SHA-1 non ajouté
- Émulateur sans connexion Internet

Solution :

- Vérifier la clé API
- Activer Maps SDK for Android
- Vérifier le package `com.malak.gps`
- Vérifier la configuration Google Cloud
- Redémarrer l’application après correction

---

## Personnalisation réalisée

Cette version ne se limite pas au code généré automatiquement par Android Studio.

Les personnalisations ajoutées sont :

- Ajout d’un écran d’accueil complet
- Nom d’application personnalisé : `GeoTrace Lab11`
- Interface colorée et moderne
- Panneau flottant sur la carte
- Boutons personnalisés
- Variables Java personnalisées
- Gestion plus robuste de la permission
- Gestion GPS + réseau
- Marker principal dynamique
- Historique de déplacement avec markers secondaires
- Cercle de précision
- Mode satellite
- Bouton recentrer
- Gestion propre de la désactivation GPS

---

## Conclusion

Ce laboratoire a permis de développer une application Android complète basée sur Google Maps.

Il met en pratique :

- L’intégration de Google Maps dans Android
- La configuration d’une clé API
- La gestion des permissions Android
- L’utilisation de la localisation GPS et réseau
- Le suivi dynamique de la position
- L’ajout de markers sur une carte
- Le déplacement automatique de la caméra
- La gestion des paramètres de localisation
- La création d’une interface moderne et personnalisée

L’application **GeoTrace Lab11** constitue une base solide pour développer des applications de géolocalisation plus avancées, comme le suivi de trajet, l’enregistrement d’itinéraires ou l’affichage de points d’intérêt.
