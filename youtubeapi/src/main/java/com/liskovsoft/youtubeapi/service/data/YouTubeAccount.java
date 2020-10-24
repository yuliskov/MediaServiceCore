package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.Account;
import com.liskovsoft.youtubeapi.auth.models.info.AccountInt;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

import java.util.List;

public class YouTubeAccount implements Account {
    private String mName;
    private String mImageUrl;
    private boolean mIsSelected;

    public static List<Account> from(List<AccountInt> accounts) {
        return null;
    }

    public static Account from(AccountInt accountInt) {
        YouTubeAccount account = new YouTubeAccount();

        account.mName = accountInt.getName();
        account.mImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(accountInt.getThumbnails());
        account.mIsSelected = accountInt.isSelected();

        return account;
    }

    @Override
    public int getId() {
        return 0;
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
}
