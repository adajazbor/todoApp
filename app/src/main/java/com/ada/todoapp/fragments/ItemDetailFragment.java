package com.ada.todoapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ada.todoapp.R;
import com.ada.todoapp.models.Item;
import com.ada.todoapp.utils.Constants;
import com.ada.todoapp.utils.Utils;

import org.parceler.Parcels;

/**
 * Created by ada on 9/26/16.
 */
public class ItemDetailFragment extends DialogFragment implements ItemEditFragment.EditItemDialogListener {

    private Item mItem;

    private TextView tvName;
    private TextView tvNotes;
    private TextView tvDue;
    private TextView tvPriority;
    private TextView tvStatus;

    public ItemDetailFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface DetailItemDialogListener {
        void onFinishDetailDialog();
    }

    public static ItemDetailFragment newInstance(Item item) {
        ItemDetailFragment frag = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.PARAM_ITEM, Parcels.wrap(item));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_item, container);
    }

    //@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(R.string.title_item_detail);

        mItem = Parcels.unwrap(getArguments().getParcelable(Constants.PARAM_ITEM));

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(mItem.getName());

        tvDue = (TextView) view.findViewById(R.id.tvDue);
        tvDue.setText(Utils.formatDay(mItem.getDueDate()));

        tvNotes = (TextView) view.findViewById(R.id.tvNotes);
        tvNotes.setText(mItem.getNotes());

        tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        tvPriority.setText(mItem.getPriority());

        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvStatus.setText(mItem.getStatus());

        View.OnClickListener onEdit = getOnEditListener();
        Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(onEdit);

        View.OnClickListener onCancel = getOnDeleteListener();
        Button btnCancel = (Button) view.findViewById(R.id.btnDelete);
        btnCancel.setOnClickListener(onCancel);

        View.OnClickListener onBack = getOnBackListener();
        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(onBack);
    }

    private View.OnClickListener getOnEditListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(mItem, getString(R.string.title_item_edit));
            }
        };
    }

    private View.OnClickListener getOnDeleteListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO alert
                deleteItem(mItem);
                backToMain();
            }
        };
    }

    private View.OnClickListener getOnBackListener() {
     return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        };
    }

    private void backToMain() {
        DetailItemDialogListener listener = (DetailItemDialogListener) getActivity();
        listener.onFinishDetailDialog();
        dismiss();
    }

    private void showEditDialog(Item item, String title) {
        FragmentManager fm = getFragmentManager();
        ItemEditFragment editNameDialogFragment = ItemEditFragment.newInstance(title, item);
        editNameDialogFragment.show(fm, Constants.FRAGMENT_EDIT_ITEM);
    }

    @Override
    public void onFinishEditDialog(Parcelable parcel) {
        mItem = Parcels.unwrap(parcel);
        writeItem(mItem);
    }

    //======= db operations
    private void deleteItem(Item item) {
        item.delete();
    }

    private void writeItem(Item item) {
        Item.save(item);
    }
}
