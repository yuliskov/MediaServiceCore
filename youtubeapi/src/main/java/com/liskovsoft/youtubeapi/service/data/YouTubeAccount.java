package com.liskovsoft.youtubeapi.service.data;

import androidx.annotation.NonNull;
import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.auth.models.info.AccountInt;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

public class YouTubeAccount implements Account {
    private int mId;
    private String mName;
    private String mEmail;
    private String mImageUrl;
    private boolean mIsSelected;
    private String mRefreshToken;

    public static YouTubeAccount from(AccountInt accountInt) {
        YouTubeAccount account = new YouTubeAccount();
        
        account.mName = accountInt.getName();
        account.mEmail = accountInt.getEmail();
        account.mImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(accountInt.getThumbnails());
        account.mIsSelected = accountInt.isSelected();

        return account;
    }

    public static YouTubeAccount from(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.splitObjectLegacy(spec);

        YouTubeAccount account = new YouTubeAccount();

        account.mId = Helpers.parseInt(split, 0);
        account.mName = Helpers.parseStr(split, 1);
        account.mImageUrl = Helpers.parseStr(split, 2);
        account.mIsSelected = Helpers.parseBoolean(split, 3);
        account.mRefreshToken = Helpers.parseStr(split, 4);
        account.mEmail = Helpers.parseStr(split, 5);

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
        return Helpers.mergeObject(mId, mName, mImageUrl, mIsSelected, mRefreshToken, mEmail);
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
    public String getEmail() {
        return mEmail;
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
    public boolean isEmpty() {
        return mName == null && mEmail == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof YouTubeAccount) {
            YouTubeAccount account = (YouTubeAccount) obj;
            String token = account.getRefreshToken();
            String name = account.getName();
            String email = account.getEmail();
            boolean tokenEquals = token != null && token.equals(getRefreshToken());
            boolean nameEquals = name != null && name.equals(getName());
            boolean emailEquals = email == null || getEmail() == null || email.equals(getEmail());
            return tokenEquals || (nameEquals && emailEquals);
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
