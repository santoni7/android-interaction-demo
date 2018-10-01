package com.santoni7.interactiondemo.app_a.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import com.santoni7.interactiondemo.app_a.ApplicationA;
import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.app_a.activity.ContractA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestFragment extends Fragment implements ContractA.View.TestView {

    @BindView(R.id.btnOk) Button okButton;
    @BindView(R.id.editText) TextInputEditText editText;
    @BindView(R.id.textInputLayout) TextInputLayout textInputLayout;

    private ContractA.Presenter presenter;

    public TestFragment() {
    }

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = ApplicationA.getComponent().providePresenter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btnOk)
    public void onOkClicked(){
        String url = editText.getText().toString();
        if(URLUtil.isValidUrl(url)) {
            presenter.onTestOkButtonClicked(editText.getText().toString());
            textInputLayout.setError(null);
        } else {
            textInputLayout.setError(getString(R.string.wrong_url_hint));
        }
    }

}
