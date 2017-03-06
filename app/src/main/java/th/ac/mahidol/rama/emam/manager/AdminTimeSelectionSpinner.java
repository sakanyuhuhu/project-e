package th.ac.mahidol.rama.emam.manager;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminTimeSelectionSpinner extends Spinner implements DialogInterface.OnMultiChoiceClickListener{
    String[] _items = null;
    boolean[] mSelection = null;
    ArrayAdapter<String> simple_adapter;
    List<String> timeSelect = new ArrayList<>();

    public AdminTimeSelectionSpinner(Context context) {
        super(context);

        simple_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public AdminTimeSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null & which < mSelection.length) {
            mSelection[which] = isChecked;
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }
        else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }
    }
    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Please select adminTime");
        builder.setMultiChoiceItems(_items, mSelection, this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0; i<_items.length;i++){
                    if(mSelection[i] == true){
                        timeSelect.add(_items[i]);
                    }
                }
            }
        });
//        builder.setNegativeButton("Cancle", null);
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported by AdminTimeSelectionSpinner.");
    }

    public List<String> get_items(){
        List<String> checkUniqe = new ArrayList<>();
        for(String s : timeSelect){
            if(!checkUniqe.contains(s)){
                checkUniqe.add(s);
            }
        }
        Collections.sort(checkUniqe, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        return checkUniqe;
    }

    public void set_items(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
//        simple_adapter.add(_items[0]); //show time on viewer but not set value. This line set default vale 0:00
        Arrays.fill(mSelection, false);
    }


    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 & index < mSelection.length) {
            mSelection[index] = true;
        }
        else {
            throw new IllegalArgumentException("Index " + index + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }


}
