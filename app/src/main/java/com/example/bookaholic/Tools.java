package com.example.bookaholic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean emailPatternValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean checkValidPassword(String password) {
        return !password.contains(" ") && password.length() >= 6;
    }

    public static void showToast(Context context, int stringId) {
        Toast.makeText(context, context.getResources().getString(stringId), Toast.LENGTH_SHORT).show();
    }

    public static void showErrorToast(Context context) {
        Toast.makeText(context, R.string.error_occurred_pls_try_again_later, Toast.LENGTH_LONG).show();
    }

    public static boolean checkValidName(String name) {
        return !TextUtils.isEmpty(name);
    }

    public static boolean checkValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]+")
                && (phoneNumber.length() == 10 || phoneNumber.length() == 11);
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
