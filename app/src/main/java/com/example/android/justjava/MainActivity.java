package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        Log.v("MainActivity", "Pidieron whipped cream: " + hasWhippedCream);
        //Esta opcion resume las dos lineas anterioes en una sola
        // boolean hasWhippedCream = ((CheckBox) findViewById(R.id.whipped_cream_checkbox)).isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        Log.v("MainActivity", "Pidieron chocolate: " + hasChocolate);

        EditText nameEditText = (EditText) findViewById(R.id.name_edittext);
        String userName = nameEditText.getText().toString();
        Log.v("MainActivity", "Nombre: " + userName);

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        sendOrderSummary(createOrderSummary(price, hasWhippedCream, hasChocolate, userName));
    }

    /**
     * Creates summary of the order
     *
     * @param price of the order
     * @return the text summary
     */
    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate,
                                      String userName) {
        return getString(R.string.name_hint) + ": " + userName +
                "\nAdd whipped cream: " + hasWhippedCream +
                "\nAdd chocolate: " + hasChocolate +
                "\n" + getString(R.string.quantity_header) + ": " + quantity +
                "\nTotal: $" + price +
                "\n" + getString(R.string.thank_you);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity < 100) {
            quantity += 1;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "No puedes ordenar mas de 100 cafes";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity > 0) {
            quantity -= 1;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Debes ordenar al menos una taza";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
//    private void displayMessage(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
//        orderSummaryTextView.setText(message);
//    }

    /**
     * This method sends the order summary by email.
     */
    private void sendOrderSummary(String message) {
        // Build the intent
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
// The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Just Java Order Summary");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

// Verify it resolves and send the Intent
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }



    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int unitPrice = 5;
        if (hasWhippedCream) {
            unitPrice += 1;
        }
        if (hasChocolate) {
            unitPrice += 2;
        }

        return quantity * unitPrice;
    }
}

