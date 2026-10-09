package com.siyam.landshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.*;

import java.util.Locale;

public class MainActivity extends Activity {
    static final int BG = Color.rgb(248,250,253);
    static final int TEXT = Color.rgb(24,31,42);
    static final int MUTED = Color.rgb(103,112,126);
    static final int BORDER = Color.rgb(222,227,235);
    static final int ORANGE = Color.rgb(247,128,32);
    static final int BLUE = Color.rgb(28,116,238);
    static final int RED = Color.rgb(239,67,78);
    static final int WHITE = Color.WHITE;

    Typeface kalpurush;
    LinearLayout root;
    TextView ana, gonda, kora, kranti, til;
    EditText totalInput;
    LinearLayout resultBox;

    final String[] anaSymbols = {"⁄","৵","৶","৷","৷⁄","৷৵","৷৶","৷৷","৷৷⁄","৷৷৵","৷৷৶","৸","৸⁄","৸৵","৸৶","১"};
    final String[] koraSymbols = {"৷","৷৷","৸"};
    final String[] krantiSymbols = {"৴","৴৴"};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        kalpurush = Typeface.createFromAsset(getAssets(), "fonts/Kalpurush.ttf");
        Window w = getWindow();
        if (android.os.Build.VERSION.SDK_INT >= 30) w.setDecorFitsSystemWindows(false);
        showHome();
    }

    int dp(float n) { return (int)(n * getResources().getDisplayMetrics().density + .5f); }

    TextView tv(String s, float size) {
        TextView t = new TextView(this);
        t.setText(s); t.setTextSize(size); t.setTextColor(TEXT); t.setTypeface(kalpurush);
        return t;
    }

    TextView english(String s, float size) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(MUTED); t.setTypeface(Typeface.DEFAULT); return t;
    }

    GradientDrawable rounded(int[] colors, int radius, int strokeColor) {
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        g.setCornerRadius(dp(radius));
        if (strokeColor != 0) g.setStroke(dp(1), strokeColor);
        return g;
    }

    GradientDrawable solid(int color, int radius) { return rounded(new int[]{color,color}, radius, 0); }

    void base(LinearLayout content) {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(BG);
        root.setPadding(dp(20), dp(10), dp(20), dp(14)); root.addView(content, new LinearLayout.LayoutParams(-1,-1));
        setContentView(root);
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            root.setOnApplyWindowInsetsListener((v, insets) -> {
                android.graphics.Insets i = insets.getInsets(android.view.WindowInsets.Type.systemBars());
                v.setPadding(dp(20), dp(8)+i.top, dp(20), dp(12)+i.bottom); return insets;
            });
            root.requestApplyInsets();
        } else root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    LinearLayout header(String title, String subtitle) {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL);
        TextView back = tv("‹", 32); back.setGravity(Gravity.CENTER); back.setOnClickListener(v -> showHome());
        box.addView(back, new LinearLayout.LayoutParams(dp(42),dp(40)));
        TextView h = tv(title, 26); h.setTypeface(kalpurush, Typeface.BOLD); box.addView(h,new LinearLayout.LayoutParams(-1,dp(38)));
        TextView sub = tv(subtitle, 14); sub.setTextColor(MUTED); box.addView(sub,new LinearLayout.LayoutParams(-1,dp(34)));
        return box;
    }

    TextView homeTitle() { TextView h=tv("জমির হিসাব",26); h.setTypeface(kalpurush,Typeface.BOLD); return h; }

    Button menuButton(String text, int[] colors) {
        Button b = new Button(this); b.setText(text); b.setTextSize(17); b.setTextColor(WHITE); b.setTypeface(kalpurush,Typeface.BOLD);
        b.setGravity(Gravity.CENTER); b.setAllCaps(false); b.setMinHeight(0); b.setMinimumHeight(0); b.setPadding(dp(18),0,dp(18),0);
        b.setBackground(rounded(colors,22,0)); b.setElevation(dp(4)); return b;
    }

    TextView iconText(String s) {
        TextView i=tv(s,28); i.setTextColor(WHITE); i.setGravity(Gravity.CENTER); i.setBackground(rounded(new int[]{Color.argb(45,255,255,255),Color.argb(45,255,255,255)},18,0)); return i;
    }

    void addHomeCard(LinearLayout parent, String title, String icon, int[] colors, int height, View.OnClickListener click) {
        LinearLayout card=new LinearLayout(this); card.setGravity(Gravity.CENTER_VERTICAL); card.setPadding(dp(18),dp(10),dp(12),dp(10));
        card.setBackground(rounded(colors,22,0)); card.setElevation(dp(4));
        TextView ic=iconText(icon); card.addView(ic,new LinearLayout.LayoutParams(dp(68),dp(68)));
        TextView text=tv(title,17); text.setTextColor(WHITE); text.setTypeface(kalpurush,Typeface.BOLD); text.setGravity(Gravity.CENTER_VERTICAL); text.setPadding(dp(14),0,dp(8),0);
        card.addView(text,new LinearLayout.LayoutParams(0,-1,1));
        TextView arrow=tv("›",32); arrow.setTextColor(WHITE); arrow.setGravity(Gravity.CENTER); arrow.setBackground(rounded(new int[]{Color.argb(42,255,255,255),Color.argb(42,255,255,255)},25,0));
        card.addView(arrow,new LinearLayout.LayoutParams(dp(52),dp(52)));
        card.setOnClickListener(click); parent.addView(card,new LinearLayout.LayoutParams(-1,dp(height)));
    }

    void showHome() {
        LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setGravity(Gravity.CENTER_HORIZONTAL);
        Space top=new Space(this); c.addView(top,new LinearLayout.LayoutParams(1,dp(46)));
        TextView h=homeTitle(); h.setGravity(Gravity.CENTER); c.addView(h,new LinearLayout.LayoutParams(-1,dp(48)));
        Space gap0=new Space(this); c.addView(gap0,new LinearLayout.LayoutParams(1,dp(34)));
        addHomeCard(c,"আনা, গণ্ডা, কড়া, ক্রান্তি ও তিলের সাংকেতিক চিহ্ন","⌂",new int[]{Color.rgb(255,126,28),Color.rgb(248,166,44)},118,v->showSymbols());
        Space s1=new Space(this); c.addView(s1,new LinearLayout.LayoutParams(1,dp(18)));
        addHomeCard(c,"খতিয়ানের হিসাব","＋",new int[]{Color.rgb(20,139,245),Color.rgb(32,95,224)},106,v->showCalculator());
        Space s2=new Space(this); c.addView(s2,new LinearLayout.LayoutParams(1,dp(18)));
        addHomeCard(c,"* ব্যবহারিক নীতিমালা *","!",new int[]{Color.rgb(255,75,82),Color.rgb(225,53,66)},76,v->showDisclaimer());
        Space flex=new Space(this); c.addView(flex,new LinearLayout.LayoutParams(1,0,1));
        TextView d=english("Developed by Md. Ridoyanul Hoq Siyam",11); d.setGravity(Gravity.CENTER); d.setAlpha(.65f); c.addView(d,new LinearLayout.LayoutParams(-1,dp(20)));
        TextView p=english("Phone: +8801780103463",11); p.setGravity(Gravity.CENTER); p.setAlpha(.65f); c.addView(p,new LinearLayout.LayoutParams(-1,dp(20)));
        base(c);
    }

    void showSymbols() {
        LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL);
        LinearLayout head=header("সাংকেতিক চিহ্ন","আনা, গণ্ডা, কড়া, ক্রান্তি ও তিলের ধারাবাহিক বিবরণ"); c.addView(head,new LinearLayout.LayoutParams(-1,dp(118)));
        ScrollView sv=new ScrollView(this); LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); list.setPadding(0,dp(4),0,dp(10));
        addSymbolSection(list,"১. আনা","১৬ আনা = ১ পূর্ণ অংশ",16,anaSymbols,"আনা");
        addSymbolSection(list,"২. গণ্ডা","২০ গণ্ডা = ১ আনা",19,null,"গণ্ডা");
        addSymbolSection(list,"৩. কড়া","৪ কড়া = ১ গণ্ডা",3,koraSymbols,"কড়া");
        addSymbolSection(list,"৪. ক্রান্তি","৩ ক্রান্তি = ১ কড়া",2,krantiSymbols,"ক্রান্তি");
        addSymbolSection(list,"৫. তিল","২০ তিল = ১ ক্রান্তি",19,null,"তিল");
        sv.addView(list); c.addView(sv,new LinearLayout.LayoutParams(-1,0,1)); base(c);
    }

    void addSymbolSection(LinearLayout list,String title,String relation,int count,String[] symbols,String unit){
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(dp(14),dp(12),dp(14),dp(12));
        card.setBackground(rounded(new int[]{WHITE,Color.rgb(247,249,252)},16,BORDER));
        TextView h=tv(title,18); h.setTypeface(kalpurush,Typeface.BOLD); card.addView(h,new LinearLayout.LayoutParams(-1,dp(30)));
        TextView r=tv(relation,15); r.setTextColor(BLUE); card.addView(r,new LinearLayout.LayoutParams(-1,dp(28)));
        for(int i=0;i<count;i++){
            LinearLayout row=new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL); row.setPadding(dp(6),0,dp(6),0);
            TextView n=tv(bn(i+1)+" "+unit,15); n.setTypeface(kalpurush,Typeface.BOLD); row.addView(n,new LinearLayout.LayoutParams(dp(105),dp(34)));
            if(symbols!=null){
                TextView eq=tv("—  "+symbols[i],18); eq.setTextColor(TEXT); row.addView(eq,new LinearLayout.LayoutParams(-1,dp(34)));
            }
            card.addView(row,new LinearLayout.LayoutParams(-1,dp(34)));
        }
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.setMargins(0,dp(5),0,dp(5)); list.addView(card,p);
    }

    void showCalculator() {
        ScrollView sv=new ScrollView(this); LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL);
        LinearLayout head=header("খতিয়ানের হিসাব","সঠিকভাবে অংশ নির্বাচন করে ফলাফল দেখুন"); c.addView(head,new LinearLayout.LayoutParams(-1,dp(118)));
        LinearLayout total=new LinearLayout(this); total.setOrientation(LinearLayout.VERTICAL); total.setPadding(dp(14),dp(10),dp(14),dp(10)); total.setBackground(rounded(new int[]{WHITE,Color.rgb(248,250,253)},16,BORDER));
        total.addView(tv("মোট জমি (শতাংশ)",15),new LinearLayout.LayoutParams(-1,dp(30)));
        totalInput=new EditText(this); totalInput.setHint("০"); totalInput.setTextSize(20); totalInput.setTypeface(kalpurush); totalInput.setSingleLine(true); totalInput.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); totalInput.setPadding(dp(12),0,dp(12),0); totalInput.setBackground(solid(WHITE,10)); addBengaliNumberWatcher(totalInput); total.addView(totalInput,new LinearLayout.LayoutParams(-1,dp(50)));
        c.addView(total,new LinearLayout.LayoutParams(-1,dp(96)));
        LinearLayout grid=new LinearLayout(this); grid.setOrientation(LinearLayout.VERTICAL); String[] names={"আনা","গণ্ডা","কড়া","ক্রান্তি","তিল"};
        for(int i=0;i<5;i++) addCalcRow(grid,names[i],i);
        LinearLayout.LayoutParams gp=new LinearLayout.LayoutParams(-1,dp(5*68+10)); gp.topMargin=dp(10); c.addView(grid,gp);
        Button calc=new Button(this); calc.setText("হিসাব করুন"); calc.setTextSize(17); calc.setTypeface(kalpurush,Typeface.BOLD); calc.setTextColor(WHITE); calc.setAllCaps(false); calc.setBackground(rounded(new int[]{Color.rgb(48,142,246),BLUE},16,0)); calc.setElevation(dp(3)); c.addView(calc,new LinearLayout.LayoutParams(-1,dp(56)));
        resultBox=new LinearLayout(this); resultBox.setOrientation(LinearLayout.VERTICAL); resultBox.setPadding(dp(14),dp(12),dp(14),dp(12)); resultBox.setBackground(rounded(new int[]{WHITE,Color.rgb(247,249,252)},16,BORDER));
        TextView rh=tv("ফলাফল",19); rh.setTypeface(kalpurush,Typeface.BOLD); resultBox.addView(rh,new LinearLayout.LayoutParams(-1,dp(32)));
        TextView hint=tv("হিসাব করার পর ফলাফল এখানে দেখাবে",14); hint.setTextColor(MUTED); resultBox.addView(hint,new LinearLayout.LayoutParams(-1,dp(38)));
        c.addView(resultBox,new LinearLayout.LayoutParams(-1,dp(290)));
        Space bottom=new Space(this); c.addView(bottom,new LinearLayout.LayoutParams(1,dp(16)));
        calc.setOnClickListener(v->calculate());
        sv.addView(c); baseScroll(sv);
    }

    void baseScroll(ScrollView sv){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(BG); root.setPadding(dp(20),dp(8),dp(20),dp(12)); root.addView(sv,new LinearLayout.LayoutParams(-1,-1)); setContentView(root);
        if(android.os.Build.VERSION.SDK_INT>=30){root.setOnApplyWindowInsetsListener((v,insets)->{android.graphics.Insets i=insets.getInsets(android.view.WindowInsets.Type.systemBars());v.setPadding(dp(20),dp(5)+i.top,dp(20),dp(10)+i.bottom);return insets;});root.requestApplyInsets();}
        else root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    void addCalcRow(LinearLayout grid,String name,int index){
        LinearLayout row=new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL);
        TextView l=tv(name,16); l.setTypeface(kalpurush,Typeface.BOLD); row.addView(l,new LinearLayout.LayoutParams(dp(72),dp(62)));
        TextView value=tv(defaultDisplay(name),17); value.setGravity(Gravity.CENTER_VERTICAL); value.setPadding(dp(14),0,dp(10),0); value.setBackground(rounded(new int[]{WHITE,Color.rgb(248,250,253)},12,BORDER)); row.addView(value,new LinearLayout.LayoutParams(0,dp(62),1));
        row.setPadding(0,dp(3),0,dp(3)); grid.addView(row);
        if(index==0)ana=value; else if(index==1)gonda=value; else if(index==2)kora=value; else if(index==3)kranti=value; else til=value;
        value.setOnClickListener(v->showPicker(name,value,index));
    }

    String defaultDisplay(String unit){ return "০ "+unit; }

    void showPicker(String unit,TextView target,int index){
        String[] symbols,names;
        if(index==0){symbols=anaSymbols;names=names(16,unit);}
        else if(index==1){symbols=nums(19);names=names(19,unit);}
        else if(index==2){symbols=koraSymbols;names=names(3,unit);}
        else if(index==3){symbols=krantiSymbols;names=names(2,unit);}
        else {symbols=nums(19);names=names(19,unit);}
        LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); list.setPadding(dp(8),dp(4),dp(8),dp(4));
        ScrollView sv=new ScrollView(this); sv.addView(list); AlertDialog dlg=new AlertDialog.Builder(this).setTitle(unit+" নির্বাচন করুন").setView(sv).create();
        for(int i=0;i<symbols.length;i++){
            final String s=symbols[i], n=names[i];
            String display;
            if(index==0 || index==2 || index==3) display=s+"  "+n;
            else display=n;
            TextView opt=tv(display,17); opt.setGravity(Gravity.CENTER_VERTICAL); opt.setPadding(dp(16),0,dp(12),0); opt.setBackground(solid(WHITE,10));
            LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(50)); p.setMargins(0,dp(3),0,dp(3)); list.addView(opt,p);
            opt.setOnClickListener(v->{target.setText(display);dlg.dismiss();});
        }
        dlg.show();
    }

    String[] nums(int n){String[] a=new String[n];for(int i=0;i<n;i++)a[i]=bn(i+1);return a;}
    String[] names(int n,String u){String[] a=new String[n];for(int i=0;i<n;i++)a[i]=bn(i+1)+" "+u;return a;}
    String bn(int v){StringBuilder b=new StringBuilder();for(char ch:String.valueOf(v).toCharArray())b.append((char)('\u09E6'+(ch-'0')));return b.toString();}
    int indexOf(String[] a,String x){for(int i=0;i<a.length;i++)if(a[i].equals(x))return i;return -1;}
    int anaIndex(String s){for(int i=0;i<anaSymbols.length;i++)if(s.startsWith(anaSymbols[i]))return i+1;return 0;}
    int parseInt(String s){try{return Integer.parseInt(toLatin(s).replaceAll("[^0-9]",""));}catch(Exception e){return 0;}}
    String toLatin(String s){StringBuilder b=new StringBuilder();for(char ch:s.toCharArray())b.append(ch>='\u09E6'&&ch<='\u09EF'?(char)('0'+(ch-'\u09E6')):ch);return b.toString();}
    double parse(String s){try{return Double.parseDouble(toLatin(s).replace(',','.').trim());}catch(Exception e){return 0;}}

    void addBengaliNumberWatcher(EditText e){
        e.addTextChangedListener(new TextWatcher(){boolean busy=false; public void beforeTextChanged(CharSequence s,int st,int c,int a){} public void onTextChanged(CharSequence s,int st,int b,int c){} public void afterTextChanged(Editable ed){if(busy)return;String s=ed.toString();String n=toBengaliDigits(s);if(!s.equals(n)){busy=true;ed.replace(0,ed.length(),n);busy=false;}}});
    }
    String toBengaliDigits(String s){StringBuilder b=new StringBuilder();for(char ch:s.toCharArray()){if(ch>='0'&&ch<='9')b.append((char)('\u09E6'+(ch-'0')));else b.append(ch);}return b.toString();}

    void calculate(){
        double total=parse(totalInput.getText().toString());
        if(total<=0){totalInput.setError("মোট জমির পরিমাণ লিখুন");return;}
        int a=parseInt(ana.getText().toString());
        int g=parseInt(gonda.getText().toString());
        int k=parseInt(kora.getText().toString());
        int c=parseInt(kranti.getText().toString());
        int t=parseInt(til.getText().toString());
        long shareTil=((long)a*20*4*3*20)+((long)g*4*3*20)+((long)k*3*20)+((long)c*20)+t;
        if(shareTil<=0){showResultMessage("অংশ নির্বাচন করুন");return;}
        if(shareTil>76800){showResultMessage("মোট অংশ ১৬ আনার বেশি হতে পারে না");return;}
        double fraction=shareTil/76800.0;
        double area=total*fraction;
        double pct=fraction*100.0;
        showResult(area,pct);
    }

    void showResultMessage(String msg){resultBox.removeAllViews();TextView h=tv("ফলাফল",19);h.setTypeface(kalpurush,Typeface.BOLD);resultBox.addView(h,new LinearLayout.LayoutParams(-1,dp(32)));TextView m=tv(msg,16);m.setTextColor(RED);m.setGravity(Gravity.CENTER_VERTICAL);resultBox.addView(m,new LinearLayout.LayoutParams(-1,dp(60)));}

    void showResult(double area,double pct){
        resultBox.removeAllViews(); TextView h=tv("ফলাফল",19);h.setTypeface(kalpurush,Typeface.BOLD);resultBox.addView(h,new LinearLayout.LayoutParams(-1,dp(32)));
        double ajut=area*100.0;
        double katha=area/1.65;
        double bigha=area/33.0;
        double acre=area/100.0;
        addResultRow("%",bnDecimal(pct)+" %");
        addResultRow("শতাংশ",bnDecimal(area)+" শতাংশ");
        addResultRow("অযুতাংশ",bnDecimal(ajut)+" অযুতাংশ");
        addResultRow("কাঠা",bnDecimal(katha)+" কাঠা");
        addResultRow("বিঘা",bnDecimal(bigha)+" বিঘা");
        addResultRow("একর",bnDecimal(acre)+" একর");
    }

    void addResultRow(String label,String value){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.setPadding(dp(8),0,dp(8),0);row.setBackground(rounded(new int[]{WHITE,Color.rgb(249,250,252)},10,0));TextView l=tv(label,15);l.setTypeface(kalpurush,Typeface.BOLD);row.addView(l,new LinearLayout.LayoutParams(0,dp(34),1));TextView v=tv(value,15);v.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);v.setTextColor(BLUE);v.setTypeface(kalpurush,Typeface.BOLD);row.addView(v,new LinearLayout.LayoutParams(0,dp(34),1));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(35));p.setMargins(0,dp(2),0,dp(2));resultBox.addView(row,p);}

    String bnDecimal(double x){String s=String.format(Locale.US,"%.4f",x).replaceAll("0+$","").replaceAll("\\.$","");StringBuilder b=new StringBuilder();for(char ch:s.toCharArray()){if(ch>='0'&&ch<='9')b.append((char)('\u09E6'+(ch-'0')));else if(ch=='.')b.append('.');else b.append(ch);}return b.toString();}

    void showDisclaimer(){
        LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);LinearLayout head=header("ব্যবহারিক নীতিমালা","ব্যবহারের আগে নিচের নির্দেশনাগুলো পড়ুন");c.addView(head,new LinearLayout.LayoutParams(-1,dp(118)));
        ScrollView sv=new ScrollView(this);LinearLayout list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);
        String[] items={
                "১. এই অ্যাপ ব্যবহারের আগে আনা, গণ্ডা, কড়া, ক্রান্তি ও তিলের সাংকেতিক চিহ্নগুলো ভালোভাবে পড়ে ও বুঝে নিন।",
                "২. হিসাব করার সময় প্রতিটি ইনপুট সঠিকভাবে প্রদান করুন। ভুল বা অসম্পূর্ণ ইনপুট দিলে ফলাফলও ভুল হতে পারে।",
                "৩. এই অ্যাপটি শুধুমাত্র শিক্ষামূলক ও শেখার উদ্দেশ্যে তৈরি করা হয়েছে।",
                "৪. জমি সংক্রান্ত গুরুত্বপূর্ণ সিদ্ধান্ত নেওয়ার আগে ভূমি বিশেষজ্ঞ, আইনজীবী বা সংশ্লিষ্ট সরকারি কর্তৃপক্ষের পরামর্শ ও যাচাই গ্রহণ করুন।",
                "৫. এই অ্যাপ কোনো সরকারি, আইনগত বা পেশাদার ভূমি-সেবা ব্যবস্থা নয়। এর ফলাফল সরকারি নথি বা আইনগত প্রমাণ হিসেবে ব্যবহার করা যাবে না।",
                "৬. কোনো তথ্য, হিসাব, সাংকেতিক চিহ্ন বা পদ্ধতিতে ভুল বা অসংগতি লক্ষ্য করলে ডেভেলপারের সঙ্গে যোগাযোগ করুন।",
                "৭. এটি একজন ডিজাইনারের ব্যক্তিগত উদ্যোগ ও আগ্রহ থেকে তৈরি একটি শিক্ষামূলক প্রকল্প।",
                "৮. অ্যাপের তথ্য ও হিসাবের যথার্থতা নিশ্চিত করার চেষ্টা করা হলেও সম্পূর্ণ নির্ভুলতার নিশ্চয়তা প্রদান করা হচ্ছে না।",
                "৯. এই অ্যাপের ফলাফলের ভিত্তিতে নেওয়া কোনো সিদ্ধান্ত, লেনদেন, চুক্তি বা কার্যক্রমের জন্য ডেভেলপার দায়ী থাকবেন না।",
                "১০. কোনো হিসাব বা তথ্য নিয়ে সন্দেহ থাকলে যোগ্য ব্যক্তি বা সংশ্লিষ্ট কর্তৃপক্ষের মাধ্যমে যাচাই করুন।",
                "১১. এই অ্যাপে কোনো বিজ্ঞাপন প্রদর্শন করা হয় না এবং অ্যাপ ব্যবহারের জন্য কোনো অ্যাকাউন্ট বা লগইন প্রয়োজন হয় না।",
                "১২. হিসাবের ইনপুট ও ফলাফল কোনো অনলাইন সার্ভারে পাঠানো হয় না এবং হিসাব ডিভাইসেই সম্পন্ন হয়।",
                "১৩. অ্যাপটি কোনো সরকারি ভূমি-তথ্য সিস্টেম বা তৃতীয় পক্ষের ডাটাবেসের সঙ্গে সংযুক্ত নয়।"
        };
        for(String s:items){TextView t=tv(s,14);t.setTextColor(Color.rgb(55,65,81));t.setPadding(dp(12),dp(9),dp(12),dp(9));t.setBackground(rounded(new int[]{WHITE,Color.rgb(249,250,252)},12,BORDER));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(3),0,dp(3));list.addView(t,p);}
        TextView credit=english("Developed by Md. Ridoyanul Hoq Siyam\nPhone: +8801780103463",11);credit.setGravity(Gravity.END);credit.setAlpha(.7f);credit.setPadding(0,dp(8),0,dp(8));list.addView(credit,new LinearLayout.LayoutParams(-1,dp(54)));
        sv.addView(list);c.addView(sv,new LinearLayout.LayoutParams(-1,0,1));base(c);
    }
}
