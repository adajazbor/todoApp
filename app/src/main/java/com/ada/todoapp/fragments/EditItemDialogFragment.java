package com.ada.todoapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ada.todoapp.R;
import com.ada.todoapp.models.Item;
import com.ada.todoapp.utils.Constants;

import org.parceler.Parcels;

/**
 * Created by ada on 9/26/16.
 */
public class EditItemDialogFragment extends DialogFragment {

    private Item mItem;
    private Integer mPosition;

    private EditText etName;

    public EditItemDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(Parcelable parcel, int position);
    }

    public static EditItemDialogFragment newInstance(String title, Item item, int position) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable(Constants.PARAM_ITEM, Parcels.wrap(item));
        args.putInt(Constants.PARAM_POSITION, position);
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

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        mItem = Parcels.unwrap(getArguments().getParcelable(Constants.PARAM_ITEM));
        mPosition = getArguments().getInt(Constants.PARAM_POSITION, -1);

        etName = (EditText) view.findViewById(R.id.etName);
        etName.setText(mItem.getName());
       // etName.setOnEditorActionListener(this);

        Button btnSave = (Button) view.findViewById(R.id.btnSave);

        View.OnClickListener onCancel =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String itemName = etName.getText().toString();
                        mItem.setName(itemName);
                        mItem.setStatus("Updated");
                        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
                        listener.onFinishEditDialog(Parcels.wrap(mItem), mPosition);
                        dismiss();
                    }
                };

        btnSave.setOnClickListener(onCancel);

        // Show soft keyboard automatically and request focus to field
        etName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
