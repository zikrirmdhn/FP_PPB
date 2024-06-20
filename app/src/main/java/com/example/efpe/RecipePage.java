package com.example.efpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipePage extends AppCompatActivity {

    //ini aku kmrn masih pake yg dari tugas listview Bang Nuna
    //aku coba ngeditnya di tugas itu juga wkwk
    //jadi masih ada yg belum diubah kaya Adapter sama array nya
    //kalau di tugas listview ku udh work sih, cuma sisa scroll nya doang yg belum bener

    private ListView lv_ingredient;
    private ListView lv_step;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);

//        ArrayList<> listIngredients = new ArrayList<>();

//        lv_ingredient = (ListView) findViewById(R.id.list_ingredient);
//        //ini masih pake adapter dari tugas listview
//        kontakAdapter kAdapter = new kontakAdapter(this,0,listKontak);
//        lv_ingredient.setAdapter(kAdapter);
//
//        lv_step = (ListView) findViewById(R.id.list_step);
//        kontakAdapter2 kAdapter2 = new kontakAdapter2(this,0,listKontak);
//        lv_step.setAdapter(kAdapter2);



    }
}