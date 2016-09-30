package com.ada.todoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.ada.todoapp.R;
import com.ada.todoapp.models.Item;
import com.ada.todoapp.utils.Constants;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ada on 9/26/16.
 */
public class EditItemDialogFragment extends DialogFragment {

    private Item mItem;

    private EditText etName;
    private EditText etNotes;
    private DatePicker dpDue;
    private Spinner sPriority;
    private Spinner sStatus;

    public EditItemDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(Parcelable parcel);
    }

    public static EditItemDialogFragment newInstance(String title, Item item) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable(Constants.PARAM_ITEM, Parcels.wrap(item));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    //@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString(Constants.PARAM_TITLE, "");
        getDialog().setTitle(title);

        mItem = Parcels.unwrap(getArguments().getParcelable(Constants.PARAM_ITEM));

        etName = (EditText) view.findViewById(R.id.etName);
        etName.setText(mItem.getName());

        dpDue = (DatePicker) view.findViewById(R.id.dpDue);
        dateToDatePicker(mItem.getDueDate());

        etNotes = (EditText) view.findViewById(R.id.etNotes);
        etNotes.setText(mItem.getNotes());

        sPriority = (Spinner) view.findViewById(R.id.sPriority);
        sPriority.setAdapter(getArrayAdapter(view.getContext(), R.array.array_priorities));

        sStatus = (Spinner) view.findViewById(R.id.sStatus);
        sStatus.setAdapter(getArrayAdapter(view.getContext(), R.array.array_statuses));

        View.OnClickListener onSave = getOnSaveListener();
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onSave);

        View.OnClickListener onCancel = getOnCancelListener();
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(onCancel);

        // Show soft keyboard automatically and request focus to field
        //etName.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private View.OnClickListener getOnSaveListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItem.setName(etName.getText().toString());
                mItem.setStatus((String) sStatus.getSelectedItem());
                mItem.setPriority((String) sPriority.getSelectedItem());
                mItem.setNotes((String) etNotes.getText().toString());
                mItem.setDueDate(datePickerToDate());
                EditItemDialogListener listener = (EditItemDialogListener) getActivity();
                listener.onFinishEditDialog(Parcels.wrap(mItem));
                dismiss();
            }
        };
    }

    private View.OnClickListener getOnCancelListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    private ArrayAdapter<CharSequence> getArrayAdapter(Context context, int testArrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                testArrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void dateToDatePicker(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        dpDue.setMinDate(cal.getTimeInMillis());
        dpDue.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    private Date datePickerToDate() {
        int day = dpDue.getDayOfMonth();
        int month = dpDue.getMonth();
        int year = dpDue.getYear();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0,0,0);
        return cal.getTime();
    }
}
