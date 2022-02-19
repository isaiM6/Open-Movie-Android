package com.daclink.drew.sp22.cst438_project01_starter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.daclink.drew.sp22.cst438_project01_starter.db.AppDatabase;
import com.daclink.drew.sp22.cst438_project01_starter.db.UserDao;
import com.daclink.drew.sp22.cst438_project01_starter.db.UserEntity;
import com.daclink.drew.sp22.cst438_project01_starter.utilities.Constants;

/*
 * Class: ChangePasswordActivity.java
 * Description: Creates bindings and interactable
 * aspects of the Change password screen.
 * */

public class ChangePasswordActivity extends AppCompatActivity {
    private String mOldPassword;
    private String mNewPassword;

    private EditText mOldPasswordField;
    private EditText mNewPasswordField;

    Button mConfirmBtn;

    private int mUserId;

    private UserEntity mUser;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // wire up text fields and button
        wireUpDisplay();
    }

    // wire up the display text fields and button
    private void wireUpDisplay() {
        mOldPasswordField = findViewById(R.id.old_password_edittext);
        mNewPasswordField = findViewById(R.id.new_password_edittext);
        mConfirmBtn = findViewById(R.id.change_password_confirm_btn);

        mUserId = getIntent().getIntExtra(Constants.USER_ID_KEY, -1);
        mUserDao = getDatabase();
        mUser = mUserDao.getUserById(mUserId);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();

                // check that the user entered their correct password
                if (!validateOldPassword(mUser, mOldPassword) || !validateNewPassword(mNewPassword, mOldPassword)) {
                    if (!validateOldPassword(mUser, mOldPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "Please enter your correct old password", Toast.LENGTH_SHORT).show();
                    } else if (!validateNewPassword(mNewPassword, mOldPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "New password can't be the same as old password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    changePassword();
                }
            }
        });
    }

    // creates instance of our database
    private UserDao getDatabase() {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        return db.userDao();
    }

    // grabs the values entered into the password fields
    private void getValuesFromDisplay() {
        mOldPassword = mOldPasswordField.getText().toString();
        mNewPassword = mNewPasswordField.getText().toString();
    }

    // checks that the user entered their correct password
    private boolean validateOldPassword(UserEntity user, String oldPassword) {
        String userPassword = user.getPassword();
        return userPassword.equals(oldPassword);
    }

    // checks that new password isn't the same as old password
    private boolean validateNewPassword(String newPassword, String oldPassword) {
        return !newPassword.equals(oldPassword);
    }

    // confirmation prompt for changing the user's password
    private void changePassword() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Dialog_Alert);

        alertBuilder.setMessage("Are you sure you want to change your password?");

        // alert dialog prompt
        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // update the user's password
                        mUser.setPassword(mNewPassword);
                        mUserDao.updateUser(mUser);

                        Toast.makeText(ChangePasswordActivity.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

        alertBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });

        alertBuilder.show();
    }

    // intent for switching to this activity
    public static Intent newIntent(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, ChangePasswordActivity.class);
        intent.putExtra(Constants.USER_ID_KEY, userId);
        return intent;
    }
}