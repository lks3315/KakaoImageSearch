package com.example.lks33.a2kakaoimagesearchexam.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lks33.a2kakaoimagesearchexam.R;
import com.example.lks33.a2kakaoimagesearchexam.model.Image;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<Image> {
    Activity activity;
    int resource;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    public ImageAdapter(@NonNull Context context, int resource, @NonNull List<Image> objects) {
        super(context, resource, objects);
        this.activity = (Activity)context;
        this.resource = resource;

        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.showImageOnFail(R.drawable.ic_error);
        builder.cacheOnDisc(true);
        builder.cacheOnDisc(true);
        builder.considerExifParams(true);
        options = builder.build();

        imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()) {
            ImageLoaderConfiguration configuration =
                    ImageLoaderConfiguration.createDefault(activity);
            imageLoader.init(configuration);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null) {
            itemView = this.activity.getLayoutInflater().inflate(this.resource,null);
        }

        // 전달받은 List에서 데이터를 하나 꺼낸다.
        Image item = getItem(position);

        // 데이터가 존재한다면 레이아웃 객체에 컴포넌트들에게 데이터를 출력한다.
        if(item != null) {
            ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView);
            TextView textView1 = (TextView)itemView.findViewById(R.id.textView1);
            TextView textView2 = (TextView)itemView.findViewById(R.id.textView2);

            // 이미지를 다운로드 처리;
            imageLoader.displayImage(item.getThumbnail_url(), imageView, options);
            // == imageView.setImageResource(item.getThumbnail());

            textView1.setText(item.getDisplay_sitename());
            textView2.setText(item.getWidth() + "x" + item.getHeight());
        }
        return itemView;
    }
}
