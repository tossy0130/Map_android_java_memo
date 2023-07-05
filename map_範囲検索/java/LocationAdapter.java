package com.example.map_kensaku_app_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 *  リストビュー　用　アダプター
 */
public class LocationAdapter extends BaseAdapter {

    private Context context;
    private List<LocationData> locationDataList;

    public LocationAdapter(Context context) {
        this.context = context;
        this.locationDataList = new ArrayList<>();
    }

    public void setLocationDataList(List<LocationData> locationDataList) {
        this.locationDataList = locationDataList;
    }

    @Override
    public int getCount(){
        return locationDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return locationDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            viewHolder.textViewLatitude = convertView.findViewById(R.id.textViewLatitude);
            viewHolder.textViewLongitude = convertView.findViewById(R.id.textViewLongitude);
            viewHolder.textView_addres = convertView.findViewById(R.id.textView_addres);

            // 地図アイコン ボタン
            viewHolder.list_image_icon = convertView.findViewById(R.id.list_image_icon);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LocationData locationData = locationDataList.get(position);

        // === 値をセット
        viewHolder.textViewName.setText(locationData.getFacilityName());
        viewHolder.textViewLatitude.setText("●緯度：" + String.valueOf(locationData.getLatitude()));
        viewHolder.textViewLongitude.setText("●経度：" + String.valueOf(locationData.getLongitude()));

        viewHolder.textView_addres.setText(locationData.getZyuusyo()); // === 住所

        viewHolder.list_image_icon.setText("");

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewName;
        TextView textViewLatitude;
        TextView textViewLongitude;
        TextView textView_addres; // 住所

        TextView list_image_icon;
    }



}
