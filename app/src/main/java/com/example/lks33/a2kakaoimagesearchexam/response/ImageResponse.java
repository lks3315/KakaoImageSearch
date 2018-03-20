package com.example.lks33.a2kakaoimagesearchexam.response;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lks33.a2kakaoimagesearchexam.adapter.ImageAdapter;
import com.example.lks33.a2kakaoimagesearchexam.model.Image;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResponse extends AsyncHttpResponseHandler{ // 통신 처리 결과를 위한 클래스 정의
    Activity activity;          // Toast를 표시하는데 사용하기 위한 Context
    ListView listView;          // 로딩중임을 표시하기 위한 포커스 조절을 위해 사용
    ImageAdapter adapter;   // 데이터 추가를 위해서 사용
    View footerView;            // 로딩중임을 표시하기 위해서 사용

    public ImageResponse(Activity activity, ImageAdapter adapter,
                         ListView listView, View footerView) {
        super();
        this.activity = activity;
        this.listView = listView;
        this.adapter = adapter;
        this.footerView = footerView; // footer를 추가함
    }

    // 코드 200 통신 시작시 실행

    @Override
    public void onStart() {
        // 다이얼로그 대신 숨겨진 footer를 표시
        footerView.setVisibility(View.VISIBLE);
        // 맨 마지막(footer) 요소에 대한 강제 선택( 화면에 표시하기 위함)
        listView.setSelection(adapter.getCount() -1); // 제일 끝 부분에 이 모습을 보여라

        // 전달되는 URI 확인
        Toast.makeText(activity,this.getRequestURI().toString(),Toast.LENGTH_LONG).show();
        // 테스트 용으로 현재 주소를 보이게 했으나 실제로는 이 부분을 주석 처리하여 완성도를 높이면 됨
    }

    // 통신 성공시에 실행된다.
    @Override
    public void onSuccess(String content) {
        // 공통 변수는 클래스 static 변수에 대하여 직접 초기화한다.
        Image.total_count = 0;
        Image.pageable_count = 0;
        Image.is_end = false;

        // 추출한 데이터를 저장할 임시 변수들
        // 원래 생성자의 매개변수로 쓰려고 했으나 너무 많아서 그냥 안씀.
        // 그래서 현재 프로그램에서 쓰이는 용도가 없으므로 걍 지워도 됨.
         String collection;   // 컬렉션
         String thumbnail_url;    // 이미지 썸네일 URL
         String image_url;       // 이미지 URL
         int width;  //이미지 가로 크기
         int height; // 이미지 세로 크기
         String display_sitename;    // 출처 명
         String doc_url;     // 문서 URL
         String datetime;        // 문서 작성 기간

        // 통신 데이터 처리
        try {
            JSONObject json = new JSONObject(content);
            JSONObject meta = json.getJSONObject("meta");

            // 공통 변수 얻기
            Image.total_count = meta.getInt("total_count");
            Image.pageable_count = meta.getInt("pageable_count");
            Image.is_end = meta.getBoolean("is_end");

            JSONArray documents = json.getJSONArray("documents");
            // 배열 안에서 데이터 추출
            for(int i = 0; i < documents.length() ; i++) {
                JSONObject temp = documents.getJSONObject(i);

                Image image = new Image();
                image.setCollection(temp.getString("collection"));
                image.setThumbnail_url(temp.getString("thumbnail_url"));
                image.setImage_url(temp.getString("image_url"));
                image.setWidth(temp.getInt("width"));
                image.setHeight(temp.getInt("height"));
                image.setDisplay_sitename(temp.getString("display_sitename"));
                image.setDoc_url(temp.getString("doc_url"));
                image.setDatetime(temp.getString("datetime"));

                adapter.add(image);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 실패시에 실행
    @Override
    public void onFailure(int statusCode, Throwable error, String content) {
        Toast.makeText(activity,"통신 실패",Toast.LENGTH_LONG).show();
    }

    //성공 , 실패 여부 상관없이 통신 종료되면 실행
    @Override
    public void onFinish() {
        footerView.setVisibility(View.GONE);
    }
}
