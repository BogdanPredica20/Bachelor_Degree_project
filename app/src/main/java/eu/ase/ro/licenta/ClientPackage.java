package eu.ase.ro.licenta;

import android.os.Parcel;
import android.os.Parcelable;

public class ClientPackage implements Parcelable {
    private String idCurier;
    private String colet;
    private double suma;
    private double latitude;
    private double longitude;
    private int pin;
    private boolean livrareEfectuata;

    public ClientPackage(String id_curier, String colet, double suma) {
        this.idCurier = id_curier;
        this.colet = colet;
        this.suma = suma;
    }

    public ClientPackage(String idCurier, String colet, double suma, double latitude, double longitude, int pin, boolean livrareEfectuata) {
        this.idCurier = idCurier;
        this.colet = colet;
        this.suma = suma;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pin = pin;
        this.livrareEfectuata = livrareEfectuata;
    }

    public String getIdCurier() {
        return idCurier;
    }

    public void setIdCurier(String idCurier) {
        this.idCurier = idCurier;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public boolean isLivrareEfectuata() {
        return livrareEfectuata;
    }

    public void setLivrareEfectuata(boolean livrareEfectuata) {
        this.livrareEfectuata = livrareEfectuata;
    }

    public String getId_curier() {
        return idCurier;
    }

    public void setId_curier(String id_curier) {
        this.idCurier = id_curier;
    }

    public String getColet() {
        return colet;
    }

    public void setColet(String colet) {
        this.colet = colet;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(float suma) {
        this.suma = suma;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
