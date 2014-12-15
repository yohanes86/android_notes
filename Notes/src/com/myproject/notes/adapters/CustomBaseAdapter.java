package com.myproject.notes.adapters;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myproject.notes.R;
import com.myproject.notes.entity.RowItem;

public class CustomBaseAdapter extends BaseAdapter {
	Context context;
	List<RowItem> listRowsItems;
	Map<String, Integer> maps = null;
	
	public CustomBaseAdapter(Context context, List<RowItem> listRowsItems) {
		this.context = context;
		this.listRowsItems = listRowsItems;
	}
	
	/*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        LayoutInflater mInflater = (LayoutInflater) 
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_list_view2, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.detail);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        RowItem rowItem = (RowItem) getItem(position);
         
        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());
         
        return convertView;
    }
 
    @Override
    public int getCount() {     
        return listRowsItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return listRowsItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
    	//original -> listRowsItems.indexOf(getItem(position))
    	RowItem rowItem = listRowsItems.get(listRowsItems.indexOf(getItem(position)));
        return new Long(rowItem.getId());
    }
	
}
