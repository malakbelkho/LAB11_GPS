package com.malak.gps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap carteGoogle;
    private LocationManager gestionnairePosition;

    private Marker repereActuel;
    private Circle cerclePrecision;
    private LatLng derniereCoordonnee;

    private TextView txtEtatGps;
    private TextView txtCoordonnees;
    private TextView txtPrecision;
    private Button btnRecentrer;
    private Button btnStyleCarte;

    private boolean modeSatelliteActive = false;
    private int compteurTraces = 0;

    private static final int CODE_PERMISSION_LOCALISATION = 411;
    private static final long DELAI_MAJ_MS = 2000L;
    private static final float DISTANCE_MIN_M = 0f;

    private final LocationListener ecouteurPosition = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            afficherNouvellePosition(location, "Position mise à jour");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Méthode gardée pour compatibilité avec les anciennes versions Android.
        }

        @Override
        public void onProviderEnabled(String provider) {
            txtEtatGps.setText("Provider activé : " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            txtEtatGps.setText("Provider désactivé : " + provider);
            buildAlertMessageNoGps();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        txtEtatGps = findViewById(R.id.txtEtatGps);
        txtCoordonnees = findViewById(R.id.txtCoordonnees);
        txtPrecision = findViewById(R.id.txtPrecision);
        btnRecentrer = findViewById(R.id.btnRecentrer);
        btnStyleCarte = findViewById(R.id.btnStyleCarte);

        btnRecentrer.setOnClickListener(v -> recentrerSurDernierePosition());
        btnStyleCarte.setOnClickListener(v -> changerStyleCarte());

        SupportMapFragment fragmentCarte =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (fragmentCarte != null) {
            fragmentCarte.getMapAsync(this);
        } else {
            Toast.makeText(this, "Fragment Google Maps introuvable", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        carteGoogle = googleMap;

        preparerInterfaceCarte();
        demarrerLocalisationSiPossible();
    }

    private void preparerInterfaceCarte() {
        LatLng marrakech = new LatLng(31.6295, -7.9811);

        carteGoogle.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        carteGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(marrakech, 6.0f));

        carteGoogle.getUiSettings().setZoomControlsEnabled(true);
        carteGoogle.getUiSettings().setCompassEnabled(true);
        carteGoogle.getUiSettings().setMapToolbarEnabled(true);

        carteGoogle.addMarker(
                new MarkerOptions()
                        .position(marrakech)
                        .title("Point de départ")
                        .snippet("Carte initialisée depuis Marrakech")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        );

        txtEtatGps.setText("Carte prête • demande de localisation...");
        Toast.makeText(this, "Carte Google Maps prête", Toast.LENGTH_SHORT).show();
    }

    private void demarrerLocalisationSiPossible() {
        if (carteGoogle == null) {
            return;
        }

        if (!permissionLocalisationAccordee()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    CODE_PERMISSION_LOCALISATION
            );
            return;
        }

        try {
            carteGoogle.setMyLocationEnabled(true);
        } catch (SecurityException exception) {
            txtEtatGps.setText("Impossible d’activer la couche MyLocation");
        }

        gestionnairePosition = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (gestionnairePosition == null) {
            txtEtatGps.setText("Service de localisation indisponible");
            return;
        }

        boolean gpsActif = gestionnairePosition.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean reseauActif = gestionnairePosition.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsActif && !reseauActif) {
            buildAlertMessageNoGps();
            txtEtatGps.setText("GPS/Réseau désactivé • active la localisation");
            return;
        }

        try {
            if (reseauActif) {
                gestionnairePosition.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        DELAI_MAJ_MS,
                        DISTANCE_MIN_M,
                        ecouteurPosition
                );
            }

            if (gpsActif) {
                gestionnairePosition.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        DELAI_MAJ_MS,
                        DISTANCE_MIN_M,
                        ecouteurPosition
                );
            }

            Location dernierePosition = recupererDernierePositionConnue();

            if (dernierePosition != null) {
                afficherNouvellePosition(dernierePosition, "Dernière position connue");
            } else {
                txtEtatGps.setText("Localisation activée • attente du premier signal...");
            }

        } catch (SecurityException exception) {
            txtEtatGps.setText("Permission localisation manquante");
            Toast.makeText(this, "Permission localisation non accordée", Toast.LENGTH_LONG).show();
        }
    }

    private boolean permissionLocalisationAccordee() {
        boolean fineOk = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseOk = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        return fineOk || coarseOk;
    }

    private Location recupererDernierePositionConnue() {
        if (!permissionLocalisationAccordee() || gestionnairePosition == null) {
            return null;
        }

        Location positionGps = null;
        Location positionReseau = null;

        try {
            positionGps = gestionnairePosition.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            positionReseau = gestionnairePosition.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException ignored) {
            return null;
        }

        if (positionGps != null && positionReseau != null) {
            return positionGps.getTime() > positionReseau.getTime() ? positionGps : positionReseau;
        }

        if (positionGps != null) {
            return positionGps;
        }

        return positionReseau;
    }

    private void afficherNouvellePosition(Location location, String sourceMessage) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng pointActuel = new LatLng(latitude, longitude);
        derniereCoordonnee = pointActuel;
        compteurTraces++;

        String coordonnees = String.format(
                Locale.US,
                "Lat : %.5f | Lng : %.5f",
                latitude,
                longitude
        );

        txtEtatGps.setText(sourceMessage);
        txtCoordonnees.setText(coordonnees);

        if (location.hasAccuracy()) {
            txtPrecision.setText(String.format(Locale.US, "Précision : %.1f mètres", location.getAccuracy()));
        } else {
            txtPrecision.setText("Précision : non fournie");
        }

        if (repereActuel == null) {
            repereActuel = carteGoogle.addMarker(
                    new MarkerOptions()
                            .position(pointActuel)
                            .title("Position actuelle")
                            .snippet("Marker principal mis à jour en temps réel")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
        } else {
            repereActuel.setPosition(pointActuel);
            repereActuel.setSnippet("Dernière mise à jour : trace " + compteurTraces);
        }

        carteGoogle.addMarker(
                new MarkerOptions()
                        .position(pointActuel)
                        .title("Trace #" + compteurTraces)
                        .snippet("Historique de passage")
                        .alpha(0.60f)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        );

        dessinerPrecision(pointActuel, location);
        carteGoogle.animateCamera(CameraUpdateFactory.newLatLngZoom(pointActuel, 15.0f));

        Toast.makeText(this, coordonnees, Toast.LENGTH_SHORT).show();
    }

    private void dessinerPrecision(LatLng centre, Location location) {
        double rayon = location.hasAccuracy() ? location.getAccuracy() : 25.0;

        if (cerclePrecision == null) {
            cerclePrecision = carteGoogle.addCircle(
                    new CircleOptions()
                            .center(centre)
                            .radius(rayon)
                            .strokeWidth(3f)
                            .strokeColor(0xAA1E88E5)
                            .fillColor(0x221E88E5)
            );
        } else {
            cerclePrecision.setCenter(centre);
            cerclePrecision.setRadius(rayon);
        }
    }

    private void recentrerSurDernierePosition() {
        if (carteGoogle == null || derniereCoordonnee == null) {
            Toast.makeText(this, "Aucune position reçue pour le moment", Toast.LENGTH_SHORT).show();
            return;
        }

        carteGoogle.animateCamera(CameraUpdateFactory.newLatLngZoom(derniereCoordonnee, 16.0f));
    }

    private void changerStyleCarte() {
        if (carteGoogle == null) {
            return;
        }

        if (modeSatelliteActive) {
            carteGoogle.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            btnStyleCarte.setText("Satellite");
            modeSatelliteActive = false;
        } else {
            carteGoogle.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            btnStyleCarte.setText("Standard");
            modeSatelliteActive = true;
        }
    }

    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Localisation désactivée")
                .setMessage("Le GPS ou la localisation réseau semble désactivé. Voulez-vous ouvrir les paramètres ?")
                .setCancelable(false)
                .setPositiveButton("Ouvrir paramètres", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Plus tard", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODE_PERMISSION_LOCALISATION) {
            boolean autorisee = false;

            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    autorisee = true;
                    break;
                }
            }

            if (autorisee) {
                Toast.makeText(this, "Permission localisation accordée", Toast.LENGTH_SHORT).show();
                demarrerLocalisationSiPossible();
            } else {
                txtEtatGps.setText("Permission refusée • impossible de suivre la position");
                Toast.makeText(this, "Permission localisation refusée", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (gestionnairePosition != null) {
            try {
                gestionnairePosition.removeUpdates(ecouteurPosition);
            } catch (SecurityException ignored) {
                // Sécurité : évite un crash si la permission change.
            }
        }
    }
}