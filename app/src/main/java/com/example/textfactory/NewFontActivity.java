package com.example.textfactory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class NewFontActivity extends AppCompatActivity {
    TextView letterTextView = null;
    ArrayList<Character> lettersList = new ArrayList<>();
    int listCursor = 0;
    int currentLetterCode = 0;
    FontBuilder fontBuilder = null;
    String FontName=null;
    AlertDialog dialog = null;
    Painter painter;

    static Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        face = Typeface.createFromAsset(getAssets(), "fonts/TekturTight-Regular.ttf");
        setContentView(R.layout.activity_new_font);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewFontActivity.this);
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.create_font_dialog, null);
        dialogBuilder.setView(dialogView);
        EditText dialogInput = dialogView.findViewById(R.id.DialogInput);
        dialogInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FontName = dialogInput.getText().toString();
                if (!(FontName.matches("\\w{1,40}")))
                {
                    ((Button)(dialogView.findViewById(R.id.dialogButtonOK))).setClickable(false);
                    ((TextView)(dialogView.findViewById(R.id.DialogtextView))).setText("Ошибка! имя может содержать только буквенно-цифровой символ или знак подчёркивания!");
                }
                else {
                    ((Button)(dialogView.findViewById(R.id.dialogButtonOK))).setClickable(true);
                    fontBuilder.name = FontName;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ((Button)(dialogView.findViewById(R.id.dialogButtonOK))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();

        letterTextView = findViewById(R.id.letterTextView);
        for (char i = 'A'; i <= 'Z'; i++)
        {
            lettersList.add(i);
        }
        painter = new Painter(this);
        ((LinearLayout)findViewById(R.id.WorkSpaseView)).addView(painter);

        fontBuilder = new FontBuilder("fontname");
        currentLetterCode = 'A';
        fontBuilder.AddLetter(new LetterHandler(currentLetterCode));
    }

    public void onInfoClick(View info_button)
    {
        painter.cancel();
    }

    public void onPrevClick(View v)
    {
        if (listCursor>0) {
            fontBuilder.saveCurrentLetter(painter.getImageCoords());
            letterTextView.setText((lettersList.get(listCursor - 1)).toString());
            currentLetterCode = (int)(lettersList.get(listCursor -=1));
            painter.clearData();
            if (fontBuilder.getLetter(currentLetterCode)!=null)
            {
                painter.setImage((fontBuilder.getLetter(currentLetterCode)).LetterCoods);
            }
        }
        else return;
    }

    public void onNextClick(View v)
    {
        if (listCursor<lettersList.size()-1) {
            fontBuilder.saveCurrentLetter(painter.getImageCoords());
            painter.clearData();
            letterTextView.setText((lettersList.get(listCursor + 1)).toString());
            currentLetterCode = (int)(lettersList.get(listCursor +=1));
            if (fontBuilder.getLetter(currentLetterCode)==null)
                fontBuilder.AddLetter(new LetterHandler(currentLetterCode));
            else
                painter.setImage((fontBuilder.getLetter(currentLetterCode)).LetterCoods);
        }
        else return;
    }

    public void onCreateFontClick(View v)
    {
        /*HashMap<Integer, Integer[]> letterList = new HashMap<>();
        String myLetters = "";
        for (Character c:lettersList
             ) {
            if (fontBuilder.getLetter(c)!=null) {
                Integer id = fontBuilder.getLetter((int) c).getLetterId();
                Integer[] l = fontBuilder.getLetter((int) c).getLetter();
                letterList.put(id, l);
                String weightsString = "";
                boolean flag = false;
                for(int i = 0; i<l.length; i++)
                {
                    if (l[i]!=0) flag=true;
                    weightsString+=l[i]+",";
                }
                if (flag) {
                    weightsString = weightsString.substring(0, weightsString.length() - 1);
                    myLetters += "\"" + id + "\":[" + weightsString + "],";
                }
            }
        }
        String sample = "{"+myLetters+"\"name\":\""+fontBuilder.name+"\",\"copy\":\"\",\"letterspace\":\"64\",\"basefont_size\":\"512\",\"basefont_left\":\"62\",\"basefont_top\":\"0\",\"basefont\":\"Arial\",\"basefont2\":\"\"}";
        Toast.makeText(this, sample, Toast.LENGTH_LONG).show();//--------------
        Log.i("DataOutput",sample);*/
        //create dataOutput---------------------------------------
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewFontActivity.this);
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.accept_creation_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void onClearLetterClick(View v)
    {
        painter.clearData();
    }

    public void onClearAllClick(View v)
    {
        finish();
        startActivity(getIntent());
    }

    public void onExitClick(View v)
    {
        this.finish();
    }

    public class LetterHandler
    {
        HashMap<String, String> LetterCoods;
        int letterId;

        public LetterHandler(int letterId)
        {
            this.letterId = letterId;
        }

        public HashMap<String, String> getLetter() {
            return LetterCoods;
        }

        public int getLetterId() {
            return letterId;
        }
    }

    public class FontBuilder
    {
        ArrayList<LetterHandler> letters = new ArrayList<>();
        String name;
        int letterspace = 64;
        int basefont_size = 512;
        int basefont_left = 62;
        int basefont_top = 0;
        String basefont = "Arial";
        String basefont2 = "";

        public void setBasefont(String basefont) {this.basefont = basefont;}
        public void setBasefont_left(int basefont_left) {this.basefont_left = basefont_left;}
        public void setBasefont_size(int basefont_size) {this.basefont_size = basefont_size;}
        public void setBasefont_top(int basefont_top) {this.basefont_top = basefont_top;}
        public void setLetterspace(int letterspace) {this.letterspace = letterspace;}

        public FontBuilder(String FontName)
        {
            this.name = FontName;
        }

        public void AddLetter(LetterHandler letter)
        {
            letters.add(letter);
            sort();
        }

        public void saveCurrentLetter(HashMap<String, String> letter)
        {
            for (LetterHandler lh:letters
                 ) {
                if (lh.letterId==currentLetterCode)
                {
                    lh.LetterCoods=letter;
                    break;
                }
            }
        }

        public LetterHandler getLetter(int code)
        {
            for (LetterHandler lh:letters
                 ) {
                if (lh.letterId==code) return lh;
            }
            return null;
        }

        void sort()
        {
            Collections.sort(letters, new Comparator<LetterHandler>() {
                @Override
                public int compare(LetterHandler o1, LetterHandler o2) {
                    return o1.letterId < o2.letterId ? -1 : (o1.letterId > o2.letterId) ? 1 : 0;
                }
            });
        }
    }
}