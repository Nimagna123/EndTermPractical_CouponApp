package com.swastikcolors.couponApp.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.swastikcolors.couponApp.Common;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ManageServices {

    private static boolean isInternetConnection(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean rValue=false;
        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    rValue =  (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE ||
                            ni.getType() == ConnectivityManager.TYPE_VPN));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);
                    rValue =  (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN));
                }
            }
        }
        return rValue;
    }

    public static void showInternetConnectionAlertDialog(final Context context){
        if(!isInternetConnection(context)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Your internet seems to be switched off, Please turn it ON to coutinue.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, id) -> showInternetConnectionAlertDialog(context));
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public static boolean isGPSOn(Context context) {
        final LocationManager manager = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );

        try {
            if (!Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps(context);
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private static void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, Please turn it ON to coutinue?")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
