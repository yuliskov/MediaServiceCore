package com.liskovsoft.youtubeapi.service.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    public static YouTubeAccount fromToken(String token) {
        YouTubeAccount account = new YouTubeAccount();

        account.mRefreshToken = token;
        account.mIsSelected = true;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof YouTubeAccount) {
            YouTubeAccount account = (YouTubeAccount) obj;
            String token = account.getRefreshToken();
            String name = account.getName();
            return (token != null && token.equals(getRefreshToken())) ||
                    (name != null && name.equals(getName()));
        }

        return super.equals(obj);
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setRefreshToken(String token) {
        mRefreshToken = token;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }
}
