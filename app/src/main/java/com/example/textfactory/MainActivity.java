package com.example.textfactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static Context AppContext;
    ArrayList<MainMenuItem> list = new ArrayList<>();
    static Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        face = Typeface.createFromAsset(getAssets(), "fonts/TekturTight-Regular.ttf");
        setContentView(R.layout.activity_main);
        AppContext = this.getApplicationContext();

        ((ImageButton)findViewById(R.id.info)).setMinimumWidth(getPx(100));
        ((ImageButton)findViewById(R.id.info)).setMinimumHeight(getPx(50));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView rv = findViewById(R.id.menuItemsView);
        rv.setLayoutManager(mLayoutManager);

        //for empty card set image=0, title = null, for standart textsize textSize = 0(default 40sp)
        list.add(new MainMenuItem(R.drawable.ic_font_24, R.drawable.ic_myfonts_24, "создать новый шрифт", "мои шрифты", 35, 45));
        list.add(new MainMenuItem(R.drawable.ic_newfile_24, R.drawable.ic_share_24, "новый документ", "поделиться", 0, 0));
        list.add(new MainMenuItem(R.drawable.ic_createnote_24, R.drawable.ic_webtotext_24, "добавить заметку", "преобразовать из веб-страницы", 0, 30));


        LinearLayoutManager llm = new  LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        CardViewAdapter adapter = new CardViewAdapter(list);
        rv.setAdapter(adapter);
    }

    public void onInfoClick(View info_button)
    {
        Snackbar snackbar = Snackbar.make(info_button, "TextFactory app | All RIGHTS RESERVED© 2022", Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                return;
            }
        });
        snackbar.setDuration(7000);
        snackbar.show();
    }

    public int getPx(int dp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    static class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder>
    {
        private ArrayList<MainMenuItem> list;
        public CardViewAdapter(ArrayList<MainMenuItem> list){
            this.list=list;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            private ImageView icon_left;
            private TextView title_left;
            private ImageView icon_right;
            private TextView title_right;
            private LinearLayout llL;
            private LinearLayout llR;
            public MyViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                icon_left = (ImageView) itemView.findViewById(R.id.menuitemicon_left);
                title_left = (TextView) itemView.findViewById(R.id.menuitemtitle_left);
                icon_right = (ImageView) itemView.findViewById(R.id.menuitemicon_right);
                title_right = (TextView) itemView.findViewById(R.id.menuitemtitle_right);
                llL = (LinearLayout) itemView.findViewById(R.id.linearLayoutLeft);
                llR = (LinearLayout) itemView.findViewById(R.id.linearLayoutRight);
                this.title_left.setTypeface(face);
                this.title_right.setTypeface(face);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            try{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card,parent,false);
                return new MyViewHolder(view);}
            catch (Exception ex)
            {
                Toast.makeText(AppContext, ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            if (list.get(i).getImage_left()!=0){
                holder.icon_left.setImageResource(list.get(i).getImage_left());}
            else
                holder.icon_left.setImageResource(R.drawable.ic_android_empty_24dp);
            if (list.get(i).getTitle_left()!=null){
                holder.title_left.setText(list.get(i).getTitle_left());}
            else
                holder.title_left.setText("");
            if (list.get(i).getImage_right()!=0){
                holder.icon_right.setImageResource(list.get(i).getImage_right());}
            else
                holder.icon_right.setImageResource(R.drawable.ic_android_empty_24dp);
            if (list.get(i).getTitle_right()!=null){
                holder.title_right.setText(list.get(i).getTitle_right());}
            else
                holder.title_right.setText("");
            if (list.get(i).textSize_left!=0)
                holder.title_left.setTextSize(list.get(i).textSize_left);
            if (list.get(i).textSize_right!=0)
                holder.title_right.setTextSize(list.get(i).textSize_right);
            final int position = i;

            holder.llL.setOnClickListener(new View.OnClickListener() {//left card click
                @Override
                public void onClick(View v) {
                    Intent intent;
                    switch (((TextView)v.findViewById(R.id.menuitemtitle_left)).getText().toString()){
                        case "создать новый шрифт":
                            intent = new Intent(AppContext, NewFontActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppContext.startActivity(intent);
                            break;
                        case "новый документ":
                            intent = new Intent(AppContext, NewDocActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppContext.startActivity(intent);
                            break;
                        case "преобразовать из веб-страницы":
                            intent = new Intent(AppContext, WebToTextActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppContext.startActivity(intent);
                            break;
                        case "мои шрифты":
                            intent = new Intent(AppContext, MyFontsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppContext.startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            });

            holder.llR.setOnClickListener(new View.OnClickListener() {//right card clic
                @Override
                public void onClick(View v) {
                    Intent intent;
                    switch (((TextView)v.findViewById(R.id.menuitemtitle_right)).getText().toString()){
                        case "поделиться":
                            intent = new Intent(AppContext, ShareActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppContext.startActivity(intent);
                            break;
                        case "добавить заметку":
                            intent = new Intent(AppContext, CreateNoteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppContext.startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}

class MainMenuItem {
    int textSize_left = 40;
    int textSize_right = 40;
    int i_left;
    String t_left;
    int i_right;
    String t_right;

    public int getTextSize_left(){ return textSize_left;}
    public int getTextSize_right(){ return textSize_right;}
    public int getImage_left() {
        return i_left;
    }
    public String getTitle_left() {
        return t_left;
    }
    public int getImage_right() {
        return i_right;
    }
    public String getTitle_right() {
        return t_right;
    }

    public MainMenuItem(int icon_res_left, int icon_res_right, String title_left, String title_right, int textSize_left, int textSize_right)
    {
        this.textSize_left = textSize_left;
        this.textSize_right = textSize_right;
        i_left = icon_res_left;
        t_left = title_left;
        i_right = icon_res_right;
        t_right = title_right;
    }
}