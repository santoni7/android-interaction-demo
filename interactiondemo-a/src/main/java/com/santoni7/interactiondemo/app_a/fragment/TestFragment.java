package com.santoni7.interactiondemo.app_a.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.app_a.activity.ContractA;
import com.santoni7.interactiondemo.app_a.base.FragmentBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestFragment extends FragmentBase<ContractA.Presenter> implements ContractA.View.TestView {

    @BindView(R.id.btnOk) Button okButton;
    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.btnClear) Button clearButton;

    public TestFragment() {
    }

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btnOk)
    public void onOkClicked(){
        getPresenter().onTestOkButtonClicked(editText.getText().toString());
    }

//    @OnClick(R.id.btnClear)
//    public void onClearClicked(){
//        getPresenter().clearAll();
//    }
}
