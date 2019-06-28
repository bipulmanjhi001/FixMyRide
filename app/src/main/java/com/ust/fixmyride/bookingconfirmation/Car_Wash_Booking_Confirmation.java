package com.ust.fixmyride.bookingconfirmation;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;

import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;

import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import com.ust.fixmyride.R;

import com.ust.fixmyride.home.HomeActivity;

import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Bipul on 19-09-2016.
 */
public class Car_Wash_Booking_Confirmation extends AppCompatActivity {
    com.ust.fixmyride.model.FooterBarLayout carwashbookingconfirm;
    TextView saveconfirmid;
    com.ust.fixmyride.model.CircularImageView carwashbookingconfirmfab;
    private CoordinatorLayout coordinatorlayout;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    boolean gpsEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_booking_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.car_confirmation_coordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check for Internet status
        if (!isInternetPresent && !gpsEnabled) {
            // Internet connection is not present
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            snackbarActionTextView.setTextSize(14);

            snackbarActionTextView.setTextColor(Color.RED);
            snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(1);
            textView.setTextSize(14);
            textView.setSingleLine(true);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }

        carwashbookingconfirm = (com.ust.fixmyride.model.FooterBarLayout) findViewById(R.id.carwashbookingconfirm);
        carwashbookingconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thankyoupopup();
            }
        });
        saveconfirmid=(TextView)findViewById(R.id.saveconfirmid);
        carwashbookingconfirmfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.carwashbookingconfirmfab);
        carwashbookingconfirmfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ForAllDrawerFragment frag = new ForAllDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        }
        );
    }
    private void thankyoupopup() {
        final Dialog dialog1;
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow();
        Window window = dialog1.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        dialog1.setTitle(null);
        window.setAttributes(wlp);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.thank_you_screen);
        ImageView imageView=(ImageView)dialog1.findViewById(R.id.saveinpdfformat);
        ImageView sendemail=(ImageView)dialog1.findViewById(R.id.sendemail);
        imageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  createPDF();
                  dialog1.dismiss();
                  Intent intent=new Intent(Car_Wash_Booking_Confirmation.this,HomeActivity.class);
                  startActivity(intent);
                  Car_Wash_Booking_Confirmation.this.finish();
              }
          });
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
      dialog1.show();
}
    public void createPDF() {
        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, "FMR_Invoice.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);
            // various fonts
            BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);

            HeaderFooter header = new HeaderFooter(
                    new Phrase("THANK YOU FOR YOUR BUSINESS...", new Font(bf_helv)), false);
            header.setAlignment(Element.ALIGN_CENTER);
            doc.setHeader(header);
            //open the document
            doc.open();

           //* Inserting Image in PDF *//*
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.header);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            com.lowagie.text.Image myImg = com.lowagie.text.Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.ALIGN_JUSTIFIED);
            myImg.scaleAbsoluteHeight(50);
            myImg.scaleAbsoluteWidth(700);
            myImg.setBorder(Element.RECTANGLE);
            //add image to document
            doc.add(myImg);
            //* Create Paragraph and Set Font *//*
           Paragraph p2 = new Paragraph("WELCOME TO FIXMYRIDE CAR SERVICE." +
                   "PLEASE KEEP THIS RECEIPT FOR QUICK SERVICE . ");

          //* You can also SET FONT and SIZE like this *//**//*
            Font paraFont = new Font(Font.BOLD, 17.0f, Color.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont);
            p2.getFont().setStyle(Font.BOLD);
            //add paragraph to document
            doc.add(p2);

            // demonstrate some table features
            Table table = new Table(3);
            table.setBorderWidth(2);
            table.setWidth(100);
            table.setPadding(5);
            table.setSpacing(5);

            Cell c = new Cell("INVOICE");
            c.setHeader(true);
            c.setBorderColor(harmony.java.awt.Color.BLUE);
            c.setColspan(3);

            table.addCell(c);
            table.endHeaders();

            c = new Cell("JOHN CDE 302,ECOSPACE,KOLKATA-700156,Mob-900334455,john@fixmyride.biz");
            c.setBorderColor(harmony.java.awt.Color.BLUE);
            c.setRowspan(2);
            table.addCell(c);

            table.addCell("SERVICE-2000");
            table.setBorderColor(harmony.java.awt.Color.black);
            table.addCell("TAX/VAT-500");
            table.addCell("DISCOUNT-200");
            table.addCell("TOTAL-2300");

            c = new Cell("INVOICE ID #");
            c.setBorderColor(harmony.java.awt.Color.BLUE);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);

            Cell cell = new Cell("YOUR ORDER # ABCD12345 IS CONFIRMED.IT HAS BEEN FORWARDED TO THE SERVICE TEAM.WE WILL CONTACT YOU SHORTLY.");
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);
            cell.setBorderColor(harmony.java.awt.Color.BLUE);
            c = new Cell("DATE");
            c.setBorderColor(harmony.java.awt.Color.BLUE);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(c);
            doc.add(table);

            //* Inserting Image in PDF *//
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            Bitmap bitmap2 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.footer);
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            com.lowagie.text.Image myImg2 = com.lowagie.text.Image.getInstance(stream2.toByteArray());
            myImg.setAlignment(Image.ALIGN_JUSTIFIED);
            myImg.scaleAbsoluteHeight(50);
            myImg.scaleAbsoluteWidth(700);
            myImg.setBorder(Element.RECTANGLE);

            //add image to document
            doc.add(myImg2);
            Toast.makeText(getApplicationContext(), "Saved...", Toast.LENGTH_SHORT).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }
    }
/****************************************************************************************************/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Car_Wash_Booking_Confirmation.this, HomeActivity.class);
        startActivity(intent);
        Car_Wash_Booking_Confirmation.this.finish();
    }
}
