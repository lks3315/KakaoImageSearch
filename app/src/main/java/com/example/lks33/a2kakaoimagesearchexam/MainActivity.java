package com.example.lks33.a2kakaoimagesearchexam;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lks33.a2kakaoimagesearchexam.adapter.ImageAdapter;
import com.example.lks33.a2kakaoimagesearchexam.model.Image;
import com.example.lks33.a2kakaoimagesearchexam.response.ImageResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, AbsListView.OnScrollListener{

    List<Image> list;
    ImageAdapter adapter;
    EditText editText;
    Button button;
    ListView listView;

    AsyncHttpClient client;
    ImageResponse response;

    // footer 영역
    LinearLayout footer = null;

    // 페이징 처리
    // 한 페이지에 보여질 수 있는 목록의 수
    public static final int PAGE_SIZE = 20;
    // 현재 페이지
    public static int PAGE = 1;
    // 화면에 리스트의 마지막 아이템이 보여지는 지 체크
    boolean lastItemVisibleFlag = false;

    // 다음 페이지 요청시에도 검색어 보전을 위한 문자열
    String keyword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // footerView
        View footerView = getLayoutInflater().inflate(R.layout.list_footer, null, false);
        // list_footer.xml 안에 포함된 LinearLayout
        footer = (LinearLayout)footerView.findViewById(R.id.footerContainer);
        footer.setVisibility(View.GONE);

        list = new ArrayList<Image>();
        adapter = new ImageAdapter(this, R.layout.list_item, list);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listView);
        listView.addFooterView(footerView);
        listView.setAdapter(adapter);

        button.setOnClickListener(this);
        listView.setOnScrollListener(this);

        client = new AsyncHttpClient();
        response = new ImageResponse(this,adapter,listView,footer);
    }

    @Override
    public void onClick(View view) {
        keyword = editText.getText().toString().trim();

        if (keyword.equals("")) {
            Toast.makeText(this,"검색어를 입력하세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        // 버튼을 통한 검색은 신규 검색이므로 페이지수를 초기화 하고
        // listView에 보여지고 있는 데이터를 삭제한다.
        PAGE = 1;
        int start, end;
        start = adapter.getCount();
        adapter.clear();        // 기존 검색 결과 데이터 지우기
        end = adapter.getCount();
        Toast.makeText(this, start + " / " + end, Toast.LENGTH_LONG).show();

        getKakaoData(keyword);
    }

    private void getKakaoData(String query) {
        // 파라미터 정보를 저장할 수 있는 객체
        RequestParams params = new RequestParams();
        params.put("query",query);
        params.put("size",String.valueOf(PAGE_SIZE));
        params.put("page",String.valueOf(PAGE));

        // 헤더 파일 api 키 추가
        client.addHeader("Authorization", "KakaoAK 752130634dd6f10998cf3bd87d1e9907");

        // 네트워크 접속
        client.get("https://dapi.kakao.com/v2/search/image",params,response); // params의 정보를 주고
        // 서버에 요청하면 response의 onStart() 메소드가 호출되어 실행되게 된다.
    }

    // 리스트 뷰의 스크롤이 이동하는 동안 발생한다.
    // view     - 화면에 보여지는 View 객체
    // firstVisibleItem     - 현재 화면에 보이는 첫번째 리스트 아이템의 번호 // 0번~ 19번 한 페이지에 최대 20개까지 보여주니까
    // visibleItemCount     - 현재 화면에 보이는 리스트 아이템의 갯수 // 디바이스 기준으로 10개
    // totalItemCount       - 리스트 항목의 전체 갯수 // 20개
    @Override
    public void onScroll(AbsListView view,
                         int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem + visibleItemCount가 totalItemCount 보다 크거나 같을 때
        // ==> 스크롤이 맨 끝에 도착한 경우임
        lastItemVisibleFlag = (totalItemCount > 0) && // 예를 들어 토탈은 20개니 0보다 크고
                (firstVisibleItem + visibleItemCount >= totalItemCount); // firstVisibleItem가 10번, visibleItemCount가 10개 보여서 10+10 =20개고 totalItemCount이 20개니 조건이 만족
        // 이때 lastItemVisibleFlag을 true로 바꾸게 된다.
    }

    // 스크롤 이동 상태가 변경되었을 때 발생한다.
    // view     - 화면에 보여지는 View 객체
    // scrollState      - 화면의 스크롤 상태
    //  => OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하다가 멈추었을 때
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 스크롤이 바닥에 닿아 멈춘 상태의 처리
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && // 페이지를 쭉 내리다가 스크롤이 멈추게 되고 또한 lastItemVisibleFlag가 true일때
                lastItemVisibleFlag) {
            // 현재 페이지가 전체 페이지보다 작을 경우
            // 페이지 수를 1 증가시키고 다시 통신을 시도한다.
            if (PAGE < Image.pageable_count) {
                PAGE++;
                getKakaoData(keyword);
            }
        }
    }
}

