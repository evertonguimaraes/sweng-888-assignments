package com.sweng888.androiduiandlogin_ericbratter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweng888.androiduiandlogin_ericbratter.R;
import com.sweng888.androiduiandlogin_ericbratter.adapter.UserListItemRecyclerViewAdapter;
import com.sweng888.androiduiandlogin_ericbratter.events.GetAllUsersEvent;
import com.sweng888.androiduiandlogin_ericbratter.model.User;
import com.sweng888.androiduiandlogin_ericbratter.repository.UserRepository;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserListFragment extends Fragment {
    private static final String TAG = "UserListFragment";

    private OnFragmentInteractionListener mListener;
    private UserRepository repository;
    private Disposable getAllUsersSubscription;

    public UserListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserListFragment.
     */

    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
//        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Not sure I like having this here need to better setup MVC of MVVM
        repository = new UserRepository(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_listing, container, false);

        // Set the adapter
        Context context = view.getContext();
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        GetAllUsersEvent event = GetAllUsersEvent.getInstance(this.repository);

        // Probably a better way to do this. I wonder if there is a means for a dynamic Recycler view
        // where we do not know the number of records up front. Maybe some sort of observer interface
        // for the data is necessary here?? Need to investigate
        getAllUsersSubscription = event.execute().subscribe(
                new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        recyclerView.setAdapter(new UserListItemRecyclerViewAdapter(users, mListener));
                    }
                }
        );

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        repository.destroy();
        getAllUsersSubscription.dispose();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(User u);
    }
}