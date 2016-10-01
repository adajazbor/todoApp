package com.ada.todoapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
public class ItemDetailFragment extends DialogFragment {

    private Item mItem;
    private DetailItemDialogListener mListener;
    private String[] mPriorities;

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
        void onDataChanged();
    }

    public static ItemDetailFragment newInstance(DetailItemDialogListener listener, Item item) {
        ItemDetailFragment frag = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.PARAM_ITEM, Parcels.wrap(item));
        frag.setArguments(args);
        frag.setListener(listener);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPriorities = getResources().getStringArray(R.array.array_priorities);

        getDialog().setTitle(R.string.title_item_detail);

        mItem = Parcels.unwrap(getArguments().getParcelable(Constants.PARAM_ITEM));
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvDue = (TextView) view.findViewById(R.id.tvDue);
        tvNotes = (TextView) view.findViewById(R.id.tvNotes);
        tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);

        refreshUiFromItem();

        Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
        View.OnClickListener onEdit = getOnEditListener();
        btnEdit.setOnClickListener(onEdit);

        Button btnCancel = (Button) view.findViewById(R.id.btnDelete);
        View.OnClickListener onCancel = getOnDeleteListener();
        btnCancel.setOnClickListener(onCancel);

        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        View.OnClickListener onBack = getOnBackListener();
        btnBack.setOnClickListener(onBack);
    }

    private void refreshUiFromItem() {
        tvName.setText(mItem.getName());
        tvDue.setText(Utils.formatDay(mItem.getDueDate()));
        tvNotes.setText(mItem.getNotes());
        tvPriority.setText(mPriorities[mItem.getPriority()]);
        tvStatus.setText(mItem.getStatus());
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
                mListener.onDataChanged();
                dismiss();
            }
        };
    }

    private View.OnClickListener getOnBackListener() {
     return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    private void showEditDialog(Item item, String title) {
        ItemEditFragment fragment = ItemEditFragment.newInstance(
                new ItemEditFragment.EditItemDialogListener() {
                    @Override
                    public void onItemSaved(Parcelable parcel) {
                        mItem = Parcels.unwrap(parcel);
                        refreshUiFromItem();
                        mListener.onDataChanged();
                    }
                },
                title,
                item);
        fragment.showFragment(getFragmentManager(), this);
    }

    public void setListener(DetailItemDialogListener listener) {
        mListener = listener;
    }

    //======= db operations
    private void deleteItem(Item item) {
        item.delete();
    }

}
