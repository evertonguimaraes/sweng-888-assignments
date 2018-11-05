package com.sweng888.androiduiandlogin_ericbratter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sweng888.androiduiandlogin_ericbratter.R;
import com.sweng888.androiduiandlogin_ericbratter.fragment.UserListFragment;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.utilities.Constants;

import java.util.List;

public class UserListItemRecyclerViewAdapter extends RecyclerView.Adapter<UserListItemRecyclerViewAdapter.ViewHolder> {

    private List<User> data;
    private UserListFragment.OnFragmentInteractionListener listener;

    public UserListItemRecyclerViewAdapter(List<User> data, UserListFragment.OnFragmentInteractionListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_user_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.configure(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mView;
        private TextView mUserEmail, mUserPassword, mUserDOB;
        private Button mUserFullName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserFullName = view.findViewById(R.id.userFullName);
            mUserPassword = view.findViewById(R.id.userPassword);
            mUserDOB = view.findViewById(R.id.userDOB);
            mUserEmail = view.findViewById(R.id.userEmail);
        }

        public void configure(User u) {
            this.mUserEmail.setText(u.getEmail());
            this.mUserFullName.setText(
                    Constants.PADDING_FOR_USER_FULL_NAME +
                            u.getFirstName() +
                            " " +
                            u.getLastName() +
                            Constants.PADDING_FOR_USER_FULL_NAME
            );
            this.mUserPassword.setText(u.getPassword());
            this.mUserDOB.setText(u.getBirthday());

            this.mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            int selectedPosition = getAdapterPosition();
            User u = data.get(selectedPosition);
            listener.onFragmentInteraction(u);
        }
    }
}
