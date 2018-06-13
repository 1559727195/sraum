package com.example.zhu.listview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.R.attr.name;
import static android.R.id.input;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  implements  OnClickListener{


    private EditText nameEdit;
    private EditText passWordEdit;
    private ImageView clearPassWord ;
    private ImageView hidePassImg;
    private ImageView clearImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        getInitEvent();
    }

    private void getInitEvent() {
        nameEdit.addTextChangedListener(mTextWatcher);
        clearImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                nameEdit.setText("");
            }
        });

        passWordEdit.addTextChangedListener(mTextWatcher);
        clearPassWord.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                passWordEdit.setText("");
            }
        });
        chageImgColor(.5f);
        hidePassImg.setOnClickListener(this);
        hidePassImg.setClickable(false);//
    }

    private void initView() {
        nameEdit = (EditText) findViewById(R.id.edt_operator_name);
        passWordEdit = (EditText) findViewById(R.id.edt_operator_password);
        clearImg = (ImageView) findViewById(R.id.delete);
        hidePassImg = (ImageView) findViewById(R.id.img_see);
        clearPassWord = (ImageView) findViewById(R.id.pass_del);

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        public void afterTextChanged(Editable s) {
            if (nameEdit.getText().toString() != null
                    && !nameEdit.getText().toString().equals("")) {
                clearImg.setVisibility(View.VISIBLE);
            } else {
                clearImg.setVisibility(View.INVISIBLE);
            }
            if (passWordEdit.getText().toString() != null
                    && !passWordEdit.getText().toString().equals("")) {
                clearPassWord.setVisibility(View.VISIBLE);
                chageImgColor(1);
                hidePassImg.setClickable(true);
            } else {
                clearPassWord.setVisibility(View.INVISIBLE);
                chageImgColor(.5f);
                hidePassImg.setClickable(false);
                //设置成密文
                //
                passWordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    };

    //將ImageVIew顏色變為灰色
    private void chageImgColor(float color){
        //将ImageView变成灰色
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(color);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        hidePassImg.setColorFilter(filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_see:
                //设置成明文
                //This transformation method causes any carriage return characters (\r)
                //to be hidden by displaying them as zero-width non-breaking space characters (﻿).
                passWordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
        }
    }
}

