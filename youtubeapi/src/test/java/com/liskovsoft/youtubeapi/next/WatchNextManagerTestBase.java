package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.next.models.WatchNextItem;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.next.models.WatchNextSection;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WatchNextManagerTestBase {
    public void checkWatchNextResultFields(WatchNextResult watchNextResult) {
        assertNotNull("Watch next not empty", watchNextResult);
        List<WatchNextSection> allRows = watchNextResult.getSections();
        assertNotNull("Watch next contains rows", allRows);

        WatchNextSection firstRow = allRows.get(0);

        assertNotNull("Row has title", firstRow.getTitle());
        assertNotNull("Row has continuation data", firstRow.getNextPageKey());

        WatchNextItem watchNextItem = firstRow.getWatchNextItems().get(0);

        checkFields(watchNextItem);
    }

    private void checkFields(WatchNextItem watchNextItem) {
        assertNotNull("Watch next item has title", watchNextItem.getTitle());
        assertNotNull("Watch next item has video id", watchNextItem.getVideoId());
        assertNotNull("Watch next item has user name", watchNextItem.getUserName());
        assertNotNull("Watch next item has channel id", watchNextItem.getChannelId());
        assertNotNull("Watch next item has view count", watchNextItem.getViewCount());
        assertNotNull("Watch next item has length", watchNextItem.getLengthText());
        assertNotNull("Watch next item has thumbnails", watchNextItem.getThumbnails());
        assertTrue("Watch next item thumbnails not empty", watchNextItem.getThumbnails().size() > 0);
    }
}
