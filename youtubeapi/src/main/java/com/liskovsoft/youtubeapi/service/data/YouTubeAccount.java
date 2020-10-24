package com.liskovsoft.youtubeapi.service.data;

import androidx.annotation.NonNull;
import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.auth.models.info.AccountInt;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

public class YouTubeAccount implements Account {
    private int mId;
    private String mName;
    private String mImageUrl;
    private boolean mIsSelected;
    private String mRefreshToken;

    public static YouTubeAccount from(AccountInt accountInt) {
        YouTubeAccount account = new YouTubeAccount();
        
        account.mName = accountInt.getName();
        account.mImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(accountInt.getThumbnails());
        account.mIsSelected = accountInt.isSelected();

        return account;
    }

    public static YouTubeAccount from(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = spec.split(",");

        if (split.length != 5) {
            return null;
        }

        YouTubeAccount account = new YouTubeAccount();

        account.mId = Helpers.parseInt(split[0]);
        account.mName = split[1];
        account.mImageUrl = split[2];
        account.mIsSelected = Helpers.parseBoolean(split[3]);
        account.mRefreshToken = split[4];

        return account;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", mId, mName, mImageUrl, mIsSelected, mRefreshToken);
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getAvatarImageUrl() {
        return mImageUrl;
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setRefreshToken(String token) {
        mRefreshToken = token;
    }
}
