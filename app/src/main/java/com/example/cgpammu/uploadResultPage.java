package com.example.cgpammu;


import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.VideoView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class uploadResultPage extends AppCompatActivity   {
    boolean isShown=false;
    VideoView videoView1,videoView2;
    ScrollView scrollView;
    MediaController mediaController,mediaController2;
    Dialog settingsDialog;
    ArrayList<String> StudentInfo = new ArrayList<>();


    public void camsys(View view){
        openCustomTab("https://cms.mmu.edu.my/psc/csprd/EMPLOYEE/HRMS/c/N_SR_STUDENT_RECORDS.N_ON_RSLT_PNL.GBL?PORTALPARAM_PTCNAV=ONLINE_RESULT&amp;EOPP.SCNode=HRMS&amp;EOPP.SCPortal=EMPLOYEE&amp;EOPP.SCName=CO_EMPLOYEE_SELF_SERVICE&amp;EOPP.SCLabel=Self%20Service&amp;EOPP.SCPTfname=CO_EMPLOYEE_SELF_SERVICE&amp;FolderPath=PORTAL_ROOT_OBJECT.CO_EMPLOYEE_SELF_SERVICE.HCCC_ACADEMIC_RECORDS.ONLINE_RESULT&amp;IsFolder=false&amp;PortalActualURL=https%3a%2f%2fcms.mmu.edu.my%2fpsc%2fcsprd%2fEMPLOYEE%2fHRMS%2fc%2fN_SR_STUDENT_RECORDS.N_ON_RSLT_PNL.GBL&amp;PortalContentURL=https%3a%2f%2fcms.mmu.edu.my%2fpsc%2fcsprd%2fEMPLOYEE%2fHRMS%2fc%2fN_SR_STUDENT_RECORDS.N_ON_RSLT_PNL.GBL&amp;PortalContentProvider=HRMS&amp;PortalCRefLabel=Academic%20Achievement&amp;PortalRegistryName=EMPLOYEE&amp;PortalServletURI=https%3a%2f%2fcms.mmu.edu.my%2fpsp%2fcsprd%2f&amp;PortalURI=https%3a%2f%2fcms.mmu.edu.my%2fpsc%2fcsprd%2f&amp;PortalHostNode=HRMS&amp;NoCrumbs=yes&amp;PortalKeyStruct=yes");
    }
    public void pdf_to_text_web(View view){
        openCustomTab("https://www.zamzar.com/convert/pdf-to-txt/");
    }
    public void upload(View view) {
        Intent pdf_to_text = new Intent(this, pdf_to_text.class);
        startActivity(pdf_to_text);
        finish();
    }
    public void closeShowNum(View view) {
        settingsDialog.dismiss();
        if (!isShown) {
            isShown = true;
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            });
            videoView1.start();
        }
    }
    public void showSteps(View view) {
        videoView1.pause();
        videoView2.pause();
        settingsDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_result_page);

        StudentInfo = loadData("studentInfo");

        videoStarter();
        scrollView = findViewById(R.id.scrollid);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                mediaController.hide();
                mediaController2.hide();
            }
        });

        settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout3
                , null));
        settingsDialog.show();
    }

    void openCustomTab(String url) {
        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        builder.addDefaultShareMenuItem();
//
//        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_share));
//
//        CustomTabsIntent anotherCustomTab = new CustomTabsIntent.Builder().build();
//
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share);
////        builder.setCloseButtonIcon(icon);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_upload);
//
//        int requestCode = 100;
//        Intent intent = anotherCustomTab.intent;
//        intent.setData(Uri.parse("http://www.journaldev.com/author/anupam"));
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setActionButton(bitmap, "Android", pendingIntent, true);
        builder.setShowTitle(true);


        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();
        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }


    @Override
    public void onBackPressed() {
        Intent setResultChoice = new Intent(this, setResultChoice.class);
        startActivity(setResultChoice);
        finish();
    }

    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void videoStarter(){
        videoView1 = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.instr1;
        Uri uri = Uri.parse(videoPath);
        videoView1.setVideoURI(uri);
        mediaController = new MediaController(this);
        videoView1.setMediaController(mediaController);
        mediaController.setAnchorView(videoView1);
        videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView2.start();
            }
        });


        videoView2 = findViewById(R.id.videoView2);
        String videoPath2 = "android.resource://" + getPackageName() + "/" + R.raw.instr2;
        Uri uri2 = Uri.parse(videoPath2);
        videoView2.setVideoURI(uri2);
        mediaController2 = new MediaController(this);
        videoView2.setMediaController(mediaController2);
        mediaController2.setAnchorView(videoView2);
        videoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            }
        });




    }

}

