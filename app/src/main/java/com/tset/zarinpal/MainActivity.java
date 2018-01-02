package com.tset.zarinpal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        Uri data = getIntent().getData();
        ZarinPal.getPurchase(this).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
            @Override
            public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {
                if (isPaymentSuccess) {
                    textView.setText(" پرداخت با موفقیت انجام شد " + refID);
                } else {
                    textView.setText("پرداخت انجام نشد");
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myPayment();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, " فیلد مورد نظر خالی است", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void setupViews() {
        editText = (EditText) findViewById(R.id.editText_main);
        textView = (TextView) findViewById(R.id.textView_main_show);
        button = (Button) findViewById(R.id.button_main_purchase);

    }

    private void myPayment() {
        ZarinPal purchase = ZarinPal.getPurchase(this);
        PaymentRequest payment = ZarinPal.getPaymentRequest();
        payment.setMerchantID("edc45f40-675e-11e6-8dc9-005056a205be");
        payment.setAmount(Long.parseLong(editText.getText().toString()));
        payment.setDescription("پرداخت جهت تست برنامه");
        payment.setCallbackURL("return://app");
        purchase.startPayment(payment, new OnCallbackRequestPaymentListener() {
            @Override
            public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {
                if (status == 100) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "خطا در ایجاد درخواست پرداخت", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
