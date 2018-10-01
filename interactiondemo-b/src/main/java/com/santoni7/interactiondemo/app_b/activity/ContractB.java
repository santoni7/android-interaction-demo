package com.santoni7.interactiondemo.app_b.activity;

import android.graphics.Bitmap;

import com.santoni7.interactiondemo.app_b.R;
import com.santoni7.interactiondemo.lib.model.ImageLink;
import com.santoni7.interactiondemo.lib.mvp.MvpPresenter;
import com.santoni7.interactiondemo.lib.mvp.MvpView;

import io.reactivex.annotations.Nullable;

public class ContractB {
    public interface View extends MvpView {
        void showProgressBar();

        void hideProgressBar();

        void displayData(ImageLink imageLink, @Nullable Bitmap image);

        void scheduleDeleteLink(ImageLink imageLink, long delayMs);

        void scheduleDownloadImage(ImageLink imageLink, String destinationPath, long delayMs);

        void showMessage(Message message);
    }

    public interface Presenter extends MvpPresenter<View> {
        void onActionNewLink(String url);

        void onActionOpenLink(long id);
    }

    /**
     * A set of messages displayed to user.
     */
    public enum Message {
        ERR_FILE_NOT_FOUND(R.string.file_url_not_found),
        ERR_UNKNOWN_HOST(R.string.could_not_resolve_host),
        ERR_UPDATING_LINK(R.string.error_updating_link),
        ERR_FETCHING_LINK(R.string.error_fetching_link_from_db),
        ERR_INSERTING_LINK(R.string.error_while_inserting),
        ERR_WRONG_ACTIVITY_ACTION(R.string.error_wrong_action),
        SERVICE_SCHEDULED(R.string.service_is_scheduled),
        ERR_NULL_RESPONSE(R.string.error_null_response),
        ERR_CONNECTION_REFUSED(R.string.error_connection_refused);


        private int stringResource;

        Message(int resId) {
            this.stringResource = resId;
        }

        public int getId() {
            return stringResource;
        }
    }
}
