package th.ac.mahidol.rama.emam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import th.ac.mahidol.rama.emam.R;

/**
 * Created by mi- on 6/7/2559.
 */
public class CustomAdapter extends BaseAdapter {

    Context mcontext;
    List<String> listWard;


    public CustomAdapter(Context context, List<String> listWards) {
        this.mcontext = context;
        this.listWard = listWards;
    }

    @Override
    public int getCount() {
        if(listWard == null)
                return 0;
        return listWard.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView tvWardName;
        RadioButton rbtnWard;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int selectedPosition = 0;
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.custom_listview_select_ward, null);
            holder = new ViewHolder();
            holder.tvWardName = (TextView) convertView.findViewById(R.id.tvWardName);
            holder.rbtnWard = (RadioButton) convertView.findViewById(R.id.rbtnWard);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvWardName.setText(listWard.get(position));
        holder.rbtnWard.setText(listWard.get(position));

        holder.rbtnWard.setChecked(position == selectedPosition);
        holder.rbtnWard.setTag(position);
        holder.rbtnWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
