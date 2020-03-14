package com.example.nyoreaderpdfmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class view_pdf_files extends AppCompatActivity {

    PDFView pdfView;

    int position=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf_files);

        pdfView=(PDFView) findViewById(R.id.pdfView);
        position=getIntent().getIntExtra("position",-1);

        displayPDF();

        String name= pdf_MainActivity.fileList.get(position).getName();

        String header="";

        for(int i=0;i<name.length();i++){
            if(name.charAt(i)=='.'){
                break;
            }
            header+=name.charAt(i);
        }

        setTitle(header);

    }

    private void displayPDF() {

        pdfView.fromFile(pdf_MainActivity.fileList.get(position)).enableSwipe(true).enableAnnotationRendering(true).scrollHandle(new DefaultScrollHandle(this)).load();
    }
}
