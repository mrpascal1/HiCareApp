package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.adapter.FlexItemAdapter;
import com.ab.hicarerun.adapter.MessageItemAdapter;
import com.ab.hicarerun.adapter.RecycleBazaarAdapter;
import com.ab.hicarerun.databinding.FragmentAssessChatBinding;
import com.ab.hicarerun.network.models.MessageModel.Message;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssessChatFragment extends BaseFragment {
    FragmentAssessChatBinding mFragmentAssessChatBinding;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<Message> listQuestions = new ArrayList<Message>();
    ArrayList<String> listOptions = new ArrayList<String>();
    private MessageItemAdapter messageAdapter;
    RecyclerView.LayoutManager layoutMsgManager;
    Handler handler = new Handler();

    private FlexItemAdapter adapter;
    private int count = 1;
    private int ic = 3;
    private boolean belongsToCurrentUser = false;

    public AssessChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static AssessChatFragment newInstance() {
        Bundle args = new Bundle();
        AssessChatFragment fragment = new AssessChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentAssessChatBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_assess_chat, container, false);
//        if (getActivity() != null) {
//            LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
//            toolbar.setVisibility(View.GONE);
//            LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
//            custom_toolbar.setVisibility(View.VISIBLE);
//            TextView tool = getActivity().findViewById(R.id.txtTool);
//            tool.setText(getResources().getString(R.string.incentives_in));
//        }
        return mFragmentAssessChatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentAssessChatBinding.messagesView.setHasFixedSize(true);
        layoutMsgManager = new LinearLayoutManager(getActivity());
        mFragmentAssessChatBinding.messagesView.setLayoutManager(layoutMsgManager);
        messageAdapter = new MessageItemAdapter(getActivity());
        mFragmentAssessChatBinding.messagesView.setAdapter(messageAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setMaxLine(3);
        mFragmentAssessChatBinding.recyclerView.setLayoutManager(layoutManager);
        adapter = new FlexItemAdapter(getActivity());
        mFragmentAssessChatBinding.recyclerView.setAdapter(adapter);
        showTypingIndicator();

        messageAdapter.notifyDataSetChanged();
        if (messageAdapter.getItemCount() > 1) {
            mFragmentAssessChatBinding.messagesView.getLayoutManager().smoothScrollToPosition(mFragmentAssessChatBinding.messagesView, null, messageAdapter.getItemCount() - 1);
        }

    }

    private void getMessages() {
        try {
            JSONArray myData = new JSONArray(loadJSONFromAsset());
            JSONObject json = new JSONObject(myData.getString(0));
            JSONObject ansJson = new JSONObject(myData.getString(1));
            JSONArray st = ansJson.getJSONArray(String.valueOf(1));

            for (int i = 1; i <= 2; i++) {
                Message message = new Message(json.getString(String.valueOf(i)), belongsToCurrentUser, Message.TYPE_THEIR);
                messageAdapter.add(message);
            }

            for (int a = 0; a < st.length(); a++) {
                arrayList.add(st.getString(a));
            }
            adapter.addData(arrayList);
            adapter.notifyDataSetChanged();

            adapter.setOnItemClickHandler(position -> {
                nextQuestion(json, position, ansJson);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTypingIndicator() {
        mFragmentAssessChatBinding.typing.setVisibility(View.VISIBLE);
        mFragmentAssessChatBinding.recyclerView.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentAssessChatBinding.typing.setVisibility(View.GONE);
                mFragmentAssessChatBinding.recyclerView.setVisibility(View.VISIBLE);
                getMessages();
            }
        }, 1000);
    }

    private void nextQuestion(JSONObject json, int position, JSONObject ansJson) {
        if (count <= json.length() - 1) {
            mFragmentAssessChatBinding.typing.setVisibility(View.VISIBLE);
            mFragmentAssessChatBinding.recyclerView.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFragmentAssessChatBinding.typing.setVisibility(View.GONE);
                    mFragmentAssessChatBinding.recyclerView.setVisibility(View.VISIBLE);

                    try {
                        Message message = null;
                        count++;
                        message = new Message(arrayList.get(position), true, Message.TYPE_MINE);
                        messageAdapter.add(message);

                        Message msg = new Message(json.getString(String.valueOf(ic)), false, Message.TYPE_THEIR);
                        messageAdapter.add(msg);
                        ic++;
                        arrayList.clear();

                        JSONArray st = ansJson.getJSONArray(String.valueOf(count));
                        for (int a = 0; a < st.length(); a++) {
                            arrayList.add(st.getString(a));
                        }
                        adapter.addData(arrayList);
                        adapter.notifyDataSetChanged();


                        mFragmentAssessChatBinding.scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                mFragmentAssessChatBinding.scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, 1000);
        } else {
            mFragmentAssessChatBinding.recyclerView.setVisibility(View.GONE);
        }

    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = Objects.requireNonNull(getActivity()).getAssets().open("json/faq.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
