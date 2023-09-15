package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    PermissionSupport permission;
    private ArrayList<MainData> arrayList = new ArrayList<MainData>();
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    String formattedDate="";
    int page =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.ry);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        permissionCheck();
        getImage();
    }

    private Cursor getCursor() {
        // MediaStore provider의 사진의 id, title,date를 가져온다.
        String projection[] = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                //MediaStore.Images.ImageColumns.DATE_TAKEN
        };

        String sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";//내림차순
        //"${MediaStore.Images.ImageColumns.DATE_TAKEN} ASC" //오름차순

        //모두 가져오고 싶으면 selection과 selectionArgs에 null을 넣어주면 된다.
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );

        return cursor;
    }

    private void getImage() {
        Cursor cursor = getCursor();
        if (cursor == null) {
            System.out.println("에러 대응 코드 작성. cursor 사용하지 말 것");
        } else if (cursor.getCount() == 0) {
            System.out.println("사용자에게 검색이 실패했음을 알리는 코드를 여기에 삽입");
        } else {
            while (cursor.moveToNext()) {
                int idColNum =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
                int titleColNum =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
//                int dateTakenColNum =
//                        cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN);


                long id = cursor.getLong(idColNum);
                String title = cursor.getString(titleColNum);
               // Long date = cursor.getLong(dateTakenColNum);

                Uri imageUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                );
                String dateString = title.substring(0, 8); // "yyyyMMdd" 형식의 날짜 문자열

                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

                try {
                    Date date = inputDateFormat.parse(dateString); // 문자열을 Date 객체로 파싱
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy년MM월dd일", Locale.getDefault());
                    formattedDate = outputDateFormat.format(date); // 날짜를 원하는 형식으로 포맷
                    System.out.println("변환된 날짜: " + formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace(); // 날짜 파싱 중 오류 처리
                }
                MainData mainData = new MainData(imageUri, title+" #"+page, ""+formattedDate);
                page++;
                arrayList.add(mainData);

               TextView tv_num = findViewById(R.id.textView);
               tv_num.setText(""+arrayList.size()+" 개");
            }
            cursor.close();
            mainAdapter.notifyDataSetChanged();
        }
    }
    private void permissionCheck(){
        // sdk 23버전 이하 버전에서는 permission이 필요하지 않음
        if(Build.VERSION.SDK_INT>= 23){

            // 클래스 객체 생성
            permission =  new PermissionSupport(this, this);

            // 권한 체크한 후에 리턴이 false일 경우 권한 요청을 해준다.
            if(!permission.checkPermission()){
                permission.requestPermission();
            }
        }
    }

    // Request Permission에 대한 결과 값을 받는다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 리턴이 false일 경우 다시 권한 요청
        if (!permission.permissionResult(requestCode, permissions, grantResults)){
            permission.requestPermission();
        }
    }

}