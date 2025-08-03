package com.liskovsoft.googlecommon.service.oauth;

import androidx.annotation.NonNull;
import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.models.auth.info.AccountInt;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;

public class YouTubeAccount implements Account {
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

    public static YouTubeAccount from(AccountInt accountInt) {
        YouTubeAccount account = new YouTubeAccount();
        
        account.mName = accountInt.getName();
        account.mEmail = accountInt.getEmail();
        account.mImageUrl = YouTubeHelper.findOptimalResThumbnailUrl(accountInt.getThumbnails());
        account.mIsSelected = accountInt.isSelected();
        account.mHasChannel = accountInt.hasChannel();
        account.mPageIdToken = accountInt.getPageIdToken();
        account.mChannelName = accountInt.getChannelName();

        return account;
    }

    public static YouTubeAccount from(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.splitDataLegacy(spec);

        YouTubeAccount account = new YouTubeAccount();

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

    public static YouTubeAccount fromToken(String token) {
        YouTubeAccount account = new YouTubeAccount();

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
        if (obj instanceof YouTubeAccount) {
            YouTubeAccount account = (YouTubeAccount) obj;
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

    public boolean isSearchBroken() {
        return mRefreshToken2 == null && mEmail == null;
    }

    public void merge(Account account) {
        if (!equals(account)) {
            return;
        }

        YouTubeAccount originAccount = (YouTubeAccount) account;

        if (Helpers.equals(getPageIdToken(), originAccount.getPageIdToken())) { // same account types
            if (mRefreshToken == null)
                mRefreshToken = originAccount.getRefreshToken();
            if (mRefreshToken2 == null)
                mRefreshToken2 = originAccount.getRefreshToken2();
        } else if (getPageIdToken() != null) { // origin has old type, make it second (old type used to the search suggestions)
            if (mRefreshToken == null)
                mRefreshToken = originAccount.getRefreshToken();
            if (mRefreshToken2 == null)
                mRefreshToken2 = Helpers.firstNonNull(originAccount.getRefreshToken2(), originAccount.getRefreshToken());
            mEmail = originAccount.getEmail();
        } else if (mRefreshToken == null) { // origin has a new type, make it main
            //mRefreshToken2 = mRefreshToken;
            if (originAccount.getRefreshToken() != null) {
                mRefreshToken = originAccount.getRefreshToken();
            }
            if (originAccount.getPageIdToken() != null) {
                mPageIdToken = originAccount.getPageIdToken();
            }
        }
    }
}
