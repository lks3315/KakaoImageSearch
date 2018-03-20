package com.example.lks33.a2kakaoimagesearchexam.model;

public class Image {
    // 페이징 처리에 필요한 공통 변수
    public static int total_count = 0;  // 검색어에 검색된 문서 수
    public static int pageable_count = 0;   // total_count 중 노출 가능한 문서 수
    public static boolean is_end = false;   // 현재 페이지가 마지막 페이지인지 여부,
                                                        // 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음.

    // JSON 구조에 따른 멤버 변수 선언
    private String collection;   // 컬렉션
    private String thumbnail_url;    // 이미지 썸네일 URL
    private String image_url;       // 이미지 URL
    private int width;  //이미지 가로 크기
    private int height; // 이미지 세로 크기
    private String display_sitename;    // 출처 명
    private String doc_url;     // 문서 URL
    private String datetime;        // 문서 작성 기간

    public static int getTotal_count() {
        return total_count;
    }

    public static void setTotal_count(int total_count) {
        Image.total_count = total_count;
    }

    public static int getPageable_count() {
        return pageable_count;
    }

    public static void setPageable_count(int pageable_count) {
        Image.pageable_count = pageable_count;
    }

    public static boolean isIs_end() {
        return is_end;
    }

    public static void setIs_end(boolean is_end) {
        Image.is_end = is_end;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDisplay_sitename() {
        return display_sitename;
    }

    public void setDisplay_sitename(String display_sitename) {
        this.display_sitename = display_sitename;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public void setDoc_url(String doc_url) {
        this.doc_url = doc_url;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
