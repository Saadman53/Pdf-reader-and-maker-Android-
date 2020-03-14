package com.example.nyoreaderpdfmaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePDF extends AppCompatActivity {

    ImageView imageView;

    Button add,convert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create PDF");
        setContentView(R.layout.activity_create_pdf);

        imageView = findViewById(R.id.pdfImageID);

        add= findViewById(R.id.addID);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

    }

    private void addImage() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,120);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 120 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();


            String filepath[] =  new String[]{MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imageUri, filepath, null, null, null);

            cursor.moveToFirst();

            int ColumnIndex = cursor.getColumnIndex(filepath[0]);
            String myPath = cursor.getString(ColumnIndex);

            cursor.close();


            Bitmap bitmap = BitmapFactory.decodeFile(myPath);

            imageView.setImageBitmap(bitmap);


            PdfDocument pdfDocument = new PdfDocument();

            PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

            PdfDocument.Page page = pdfDocument.startPage(pi);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            paint.setColor(Color.parseColor("#FFFFFF"));

            canvas.drawPaint(paint);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            paint.setColor(Color.BLUE);


            canvas.drawBitmap(bitmap, 0, 0, null);

            pdfDocument.finishPage(page);

            ///save bitmap

            File root = new File(Environment.getExternalStorageDirectory(),"My PDFs");

            if(!root.exists()){
                root.mkdir();
            }

            File file = new File(root,"picture.pdf");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                pdfDocument.writeTo(fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            pdfDocument.close();

            Toast.makeText(getApplicationContext(),"Your PDF has been created, check storage",Toast.LENGTH_LONG).show();




        }
    }
}
