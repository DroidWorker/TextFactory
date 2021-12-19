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
    DrawPanel drawPanel = null;
    String FontName=null;
    AlertDialog dialog = null;

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

        ArrayList<String> weights = new ArrayList<>();
        for (int j = 0; j<16; j++) {
            int i;
            for (i = 1; i <= 32768; ) {
                weights.add(String.valueOf(i));
                i = i * 2;
            }
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        drawPanel = new DrawPanel(this.getApplicationContext(), width);
        drawPanel.createPanel();
        ((LinearLayout)findViewById(R.id.WorkSpaseView)).addView(drawPanel.getDrawPanel());
        UpdateControlPanel();

        fontBuilder = new FontBuilder("fontname");
        currentLetterCode = 'A';
        fontBuilder.AddLetter(new LetterHandler(currentLetterCode));
    }

    void UpdateControlPanel()
    {
        ConstraintSet set = new ConstraintSet();
        set.clone((ConstraintLayout) findViewById(R.id.parentLayout));
        set.connect(R.id.ControlPanel, ConstraintSet.TOP, R.id.WorkSpaseView, ConstraintSet.BOTTOM, 0);
        set.connect(R.id.ControlPanel, ConstraintSet.BOTTOM, R.id.parentLayout, ConstraintSet.BOTTOM, 0);
        set.applyTo(findViewById(R.id.parentLayout));
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

    public void onPrevClick(View v)
    {
        if (listCursor>0) {
            drawPanel.clearPanel(false);
            letterTextView.setText((lettersList.get(listCursor - 1)).toString());
            currentLetterCode = (int)(lettersList.get(listCursor -=1));
            drawPanel.fillPanel(fontBuilder);
        }
        else return;
    }

    public void onNextClick(View v)
    {
        if (listCursor<lettersList.size()-1) {
            drawPanel.clearPanel(false);
            letterTextView.setText((lettersList.get(listCursor + 1)).toString());
            currentLetterCode = (int)(lettersList.get(listCursor +=1));
            drawPanel.fillPanel(fontBuilder);
            if (fontBuilder.getLetter(currentLetterCode)==null)
                fontBuilder.AddLetter(new LetterHandler(currentLetterCode));
        }
        else return;
    }

    public void onCreateFontClick(View v)
    {
        HashMap<Integer, Integer[]> letterList = new HashMap<>();
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
        Log.i("DataOutput",sample);
        //create dataOutput---------------------------------------

        //this.finish();
    }

    public void onClearLetterClick(View v)
    {
        drawPanel.clearPanel(true);
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

    public class DrawPanel
    {
        Context AppContext = null;
        TableLayout DrawPanelLayout = null;
        private int screenWidth;

        ArrayList<TextView> tvList = new ArrayList<>();
        ArrayList<TableRow> rowsList = new ArrayList<>();

        public DrawPanel(Context appContext, int screenWidth)
        {
            this.AppContext = appContext;
            this.screenWidth = screenWidth;
        }

        public void createPanel()
        {
            DrawPanelLayout = new TableLayout(AppContext);
            for (int i =0; i<16; i++)
            {
                rowsList.add(new TableRow(AppContext));
                rowsList.get(i).setId(i+1);
            }
            //create textViews wit content 'weight'
            int[] weights = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768};
            for (int i = 0; i<256; i++)
            {
                tvList.add(new TextView(AppContext));
                tvList.get(i).setText(String.valueOf(weights[i%16]));
                tvList.get(i).setWidth(screenWidth/16);
                tvList.get(i).setHeight(screenWidth/16);
                tvList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//if tag==0 - not pressed if tag==1 - pressed
                        if ((v).getTag()==null||(Integer)((v).getTag())!=1) {
                            (v).setTag(1);
                            (v).setBackgroundColor(AppContext.getResources().getColor(R.color.brown3));
                        }
                        else {
                            (v).setTag(0);
                            (v).setBackgroundColor(AppContext.getResources().getColor(R.color.white));
                        }
                        TableRow tr = ((TableRow)((v).getParent()));
                        int weight = 0;
                        TextView tv;
                        for (int i = 0; i<16; i++)
                        {
                            tv = (TextView) tr.getChildAt(i);
                            if ((tv).getTag()!=null&&(Integer)((tv).getTag())==1)
                                weight += Integer.parseInt(tv.getText().toString());
                        }
                        fontBuilder.getLetter(currentLetterCode).SetWeight(tr.getId(),weight);
                    }
                });
            }
            int iteration = 0;
            for (TableRow r:rowsList
                 ) {
                for (int j = 0; j<16; j++)
                {
                    r.addView(tvList.get(j+(iteration*16)));
                }
                iteration++;
                DrawPanelLayout.addView(r);
            }
        }

        public void clearPanel(boolean clearData)
        {
            for (TextView tv:tvList
                 ) {
                tv.setTag(0);
                tv.setBackgroundColor(AppContext.getResources().getColor(R.color.white));
            }
            if (!clearData) return;
            for (int i = 1; i<=16; i++) {
                fontBuilder.getLetter(currentLetterCode).SetWeight(i, 0);
            }
        }

        public void fillPanel(FontBuilder fontBuilder)
        {
            if (fontBuilder.getLetter(currentLetterCode)==null) return;
            for (TableRow tr:rowsList
                 ) {
                if ( fontBuilder.getLetter(currentLetterCode).getWeight(tr.getId())!=0)
                {
                    int weight = fontBuilder.getLetter(currentLetterCode).getWeight(tr.getId());
                    ArrayList<Integer> weights = numFactoring(weight);
                    for (int i=0; i<16; i++)
                    {
                        if (weights.contains(Integer.parseInt(((TextView)tr.getChildAt(i)).getText().toString())))
                        {
                            tr.getChildAt(i).setTag("1");
                            tr.getChildAt(i).setBackgroundColor(AppContext.getResources().getColor(R.color.brown3));
                        }
                    }
                }
            }
        }

        ArrayList<Integer> numFactoring(int num)
        {
            String binString = Integer.toBinaryString(num);
            Integer[] binNum = new Integer[binString.length()];
            for (int i = 0; i<binString.length(); i++) {
                binNum[i] = Character.getNumericValue(binString.charAt(binString.length()-i-1));
            }
            ArrayList<Integer> result = new ArrayList<>();
            for (int i = 0; i<binNum.length; i++)
            {
                if (binNum[i]==1)
                    result.add((int)(Math.pow(2, i)));
            }
            return result;
        }

        public View getDrawPanel()
        {
            return DrawPanelLayout;
        }
    }

    public class LetterHandler
    {
        Integer[] letter = new Integer[16];
        int letterId;

        public LetterHandler(int letterId)
        {
            this.letterId = letterId;
            for (int i=0; i<16; i++)
                letter[i]=0;
        }

        public void SetWeight(int height, int weight)
        {
            letter[height-1] = weight;
        }

        public Integer getWeight(int height)
        {
            return letter[height-1];
        }

        public Integer[] getLetter() {
            return letter;
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