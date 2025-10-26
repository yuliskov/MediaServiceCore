package com.liskovsoft.googleapi.oauth2.impl;

import androidx.annotation.NonNull;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;
import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.models.auth.info.AccountInt;

public class GoogleAccount implements Account {
    private int mId;
    private String mName;
    private String mEmail;
    private String mImageUrl;
    private boolean mIsSelected;
    private String mRefreshToken;
    private String mRefreshToken2;
    private boolean mHasChannel;
    private String mPageIdToken;
    private String mChannelName;

    public static GoogleAccount from(AccountInt accountInt) {
        GoogleAccount account = new GoogleAccount();
        
        account.mName = accountInt.getName();
        account.mEmail = accountInt.getEmail();
        account.mImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(accountInt.getThumbnails());
        account.mIsSelected = accountInt.isSelected();
        account.mHasChannel = accountInt.hasChannel();
        account.mPageIdToken = accountInt.getPageIdToken();
        account.mChannelName = accountInt.getChannelName();

        return account;
    }

    public static GoogleAccount from(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.splitData(spec);

        GoogleAccount account = new GoogleAccount();

        account.mId = Helpers.parseInt(split, 0);
        account.mName = Helpers.parseStr(split, 1);
        account.mImageUrl = Helpers.parseStr(split, 2);
        account.mIsSelected = Helpers.parseBoolean(split, 3);
        account.mRefreshToken = Helpers.parseStr(split, 4);
        account.mEmail = Helpers.parseStr(split, 5);
        account.mHasChannel = Helpers.parseBoolean(split, 6, true);
        account.mPageIdToken = Helpers.parseStr(split, 7);
        account.mChannelName = Helpers.parseStr(split, 8);
        account.mRefreshToken2 = Helpers.parseStr(split, 9);

        account.mImageUrl = YouTubeHelper.avatarBlockFix(account.mImageUrl);

        return account;
    }

    public static GoogleAccount fromToken(String token) {
        GoogleAccount account = new GoogleAccount();

        account.mRefreshToken = token;
        account.mIsSelected = true;

        return account;
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.mergeData(mId, mName, mImageUrl, mIsSelected, mRefreshToken, mEmail, mHasChannel, mPageIdToken, mChannelName, mRefreshToken2);
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
        return getName() == null && getEmail() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GoogleAccount) {
            GoogleAccount account = (GoogleAccount) obj;
            String token = account.getRefreshToken();
            String pageId = account.getPageIdToken();
            String name = account.getName();
            String email = account.getEmail();
            String channel = account.getChannelName();
            boolean tokenEquals = Helpers.equals(token, getRefreshToken());
            boolean pageIdEquals = Helpers.equals(pageId, getPageIdToken());
            boolean nameEquals = Helpers.equals(name, getName());
            boolean emailEquals = email == null || getEmail() == null || Helpers.equals(email, getEmail());
            boolean channelEquals = channel == null || getChannelName() == null || Helpers.equals(channel, getChannelName());
            return (tokenEquals && pageIdEquals) || (nameEquals && emailEquals && channelEquals);
        }

        return super.equals(obj);
    }

    public String getChannelName() {
        return mChannelName;
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

    /**
     * Every YouTube account has a channel<br/>
     * This method could be served as a simple testing<br/>
     * whether the account is belongs to YouTube
     */
    public boolean hasChannel() {
        return mHasChannel;
    }

    /**
     * This token token used along with the access token to support restricted videos
     */
    public String getPageIdToken() {
        return mPageIdToken;
    }

    public String getRefreshToken2() {
        return mRefreshToken2;
    }

    public void merge(Account account) {
        GoogleAccount youTubeAccount = (GoogleAccount) account;

        if (Helpers.equals(getPageIdToken(), youTubeAccount.getPageIdToken()) && Helpers.equals(getRefreshToken2(), youTubeAccount.getRefreshToken2())) {
            return;
        }

        if (getPageIdToken() != null) {
            mRefreshToken2 = Helpers.firstNonNull(youTubeAccount.getRefreshToken2(), youTubeAccount.getRefreshToken());
            mEmail = youTubeAccount.getEmail();
        } else {
            mRefreshToken2 = mRefreshToken;
            mRefreshToken = youTubeAccount.getRefreshToken();
            mPageIdToken = youTubeAccount.getPageIdToken();
        }
    }
}
