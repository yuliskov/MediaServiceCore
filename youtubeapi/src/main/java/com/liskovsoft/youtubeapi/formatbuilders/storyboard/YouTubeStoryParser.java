package com.liskovsoft.youtubeapi.formatbuilders.storyboard;

import androidx.annotation.NonNull;
import com.liskovsoft.sharedutils.mylogger.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Storyboard parser</b><br/>
 * <em>Storyboard</em> is a collection of timeline thumbnails<br/>
 * Parses input (get_video_info) to {@link Storyboard}
 */
public class YouTubeStoryParser {
    private static final String TAG = YouTubeStoryParser.class.getSimpleName();
    private static final String SPEC_DELIM = "#";
    private static final String SECTION_DELIM = "\\|";
    private final String mSpec;
    private int mSegmentDurationUs;
    private int mStartSegmentNum;

    /**
     * Extracts storyboard (timeline thumbnails) from the <em>get_video_info</em> file
     * <br/>
     * @param spec specification e.g. <code>https:\/\/i.ytimg.com\/sb\/Pk2oW4SDDxY\/storyboard3_L$L\/$N.jpg|48#27#100#10#10#0#default#vpw4l5h3xmm2AkCT6nMZbvFIyJw|80#45#90#10#10#2000#M$M#hCWDvBSbgeV52mPYmOHjgdLFI1o|160#90#90#5#5#2000#M$M#ys1MKEnwYXA1QAcFiugAA_cZ81Q</code>
     */
    private YouTubeStoryParser(String spec) {
        mSpec = spec;
    }

    public static YouTubeStoryParser from(String storyboardSpec) {
        return new YouTubeStoryParser(storyboardSpec);
    }

    public Storyboard extractStory() {
        if (mSpec == null) {
            return null;
        }

        return parseStoryboardSpec(mSpec);
    }

    public void setSegmentDurationUs(int segmentDurationUs) {
        mSegmentDurationUs = segmentDurationUs;
    }

    public void setStartSegmentNum(int startSegmentNum) {
        mStartSegmentNum = startSegmentNum;
    }

    private Storyboard parseStoryboardSpec(String spec) {
        // EX: https:\/\/i.ytimg.com\/sb\/Pk2oW4SDDxY\/storyboard3_L$L\/$N.jpg|48#27#100#10#10#0#default#vpw4l5h3xmm2AkCT6nMZbvFIyJw|80#45#90#10#10#2000#M$M#hCWDvBSbgeV52mPYmOHjgdLFI1o|160#90#90#5#5#2000#M$M#ys1MKEnwYXA1QAcFiugAA_cZ81Q
        // Live EX: https://i.ytimg.com/sb/CFsd4UxzpLo/storyboard_live_90_3x3_b1/M$M.jpg?rs=AOn4CLAa9egpicnNt15TtgKW270vNRy5Bw#159#90#3#3
        String[] sections = spec.split(SECTION_DELIM);

        if (sections.length == 1) {
            return parseLiveStoryboard(spec, sections);
        } else {
            return parseRegularStoryboard(spec, sections);
        }
    }

    // Live EX: https://i.ytimg.com/sb/CFsd4UxzpLo/storyboard_live_90_3x3_b1/M$M.jpg?rs=AOn4CLAa9egpicnNt15TtgKW270vNRy5Bw#159#90#3#3
    private Storyboard parseLiveStoryboard(String spec, String[] sections) {
        Storyboard storyboard = new Storyboard();
        String[] sizes = sections[0].split(SPEC_DELIM);
        if (sizes.length != 5) {
            Log.e(TAG, "Error inside spec: " + spec);
            return null;
        }

        String baseUrl = sizes[0];
        storyboard.mBaseUrl = baseUrl;

        Size size = new Size();
        size.mWidth = Integer.parseInt(sizes[1]);
        size.mHeight = Integer.parseInt(sizes[2]);
        size.mColsCount = Integer.parseInt(sizes[3]);
        size.mRowsCount = Integer.parseInt(sizes[4]);
        size.mDurationEachMS = mSegmentDurationUs / 1_000;
        size.mStartNum = mStartSegmentNum;

        storyboard.mSizes.add(size);

        return storyboard;
    }

    // EX: https:\/\/i.ytimg.com\/sb\/Pk2oW4SDDxY\/storyboard3_L$L\/$N.jpg|48#27#100#10#10#0#default#vpw4l5h3xmm2AkCT6nMZbvFIyJw|80#45#90#10#10#2000#M$M#hCWDvBSbgeV52mPYmOHjgdLFI1o|160#90#90#5#5#2000#M$M#ys1MKEnwYXA1QAcFiugAA_cZ81Q
    private Storyboard parseRegularStoryboard(String spec, String[] sections) {
        Storyboard storyboard = new Storyboard();
        String baseUrl = sections[0];
        storyboard.mBaseUrl = baseUrl;

        for (int i = 1; i < sections.length; i++) {
            String[] sizes = sections[i].split(SPEC_DELIM);
            if (sizes.length != 8) {
                Log.e(TAG, "Error inside spec: " + spec);
                return null;
            }

            Size size = new Size();
            size.mWidth = Integer.parseInt(sizes[0]);
            size.mHeight = Integer.parseInt(sizes[1]);
            size.mQuality = Integer.parseInt(sizes[2]);
            size.mColsCount = Integer.parseInt(sizes[3]);
            size.mRowsCount = Integer.parseInt(sizes[4]);
            size.mDurationEachMS = Integer.parseInt(sizes[5]);
            size.mImageName = sizes[6];
            size.mSignature = sizes[7];

            storyboard.mSizes.add(size);
        }

        return storyboard;
    }

    public class Storyboard {
        private static final int MIN_WIDTH = 4;
        private static final String INDEX_VAR = "$L";
        private static final String IMG_NAME_VAR = "$N";
        private static final String IMG_NUM_VAR = "$M";
        private static final String SIGNATURE_PARAM = "sigh";
        private String mBaseUrl;
        private List<Size> mSizes = new ArrayList<>();
        private int mCachedIdx = -1;
        private int mCachedLenMS = -1;

        public String getGroupUrl(int imgNum) {
            // EX: https:\/\/i.ytimg.com\/sb\/2XY3AvVgDns\/storyboard3_L$L\/$N.jpg
            // EX: https://i.ytimg.com/sb/k4YRWT_Aldo/storyboard3_L2/M0.jpg?sigh=RVdv4fMsE-eDcsCUzIy-iCQNteI

            String link = mBaseUrl.replace("\\", "");
            int bestIdx = chooseBestSizeIdx();
            Size bestSize = mSizes.get(bestIdx);

            //Log.d(TAG, "Found sizes: %s", mSizes);
            //Log.d(TAG, "Found best size: %s", bestSize);

            link = link.replace(INDEX_VAR, String.valueOf(bestIdx));
            if (bestSize.mImageName != null) {
                link = link.replace(IMG_NAME_VAR, bestSize.mImageName);
            }
            link = link.replace(IMG_NUM_VAR, String.valueOf(imgNum));

            if (bestSize.mSignature != null) {
                if (link.contains("?")) {
                    link += "&";
                } else {
                    link += "?";
                }

                link += SIGNATURE_PARAM + "=" + bestSize.mSignature;
            }

            return link;
        }

        /**
         * Get duration of all thumbnails group
         * @return duration
         */
        public int getGroupDurationMS() {
            if (mCachedLenMS != -1) {
                return mCachedLenMS;
            }

            Size size = mSizes.get(chooseBestSizeIdx());

            int lenMS = size.mRowsCount * size.mColsCount * size.mDurationEachMS;

            mCachedLenMS = lenMS;

            return lenMS;
        }

        public Size getGroupSize() {
            int bestIdx = chooseBestSizeIdx();
            return mSizes.get(bestIdx);
        }

        private int chooseBestSizeIdx() {
            if (mCachedIdx != -1) {
                return mCachedIdx;
            }

            int widest = 0;
            int widestIdx = -1;

            for (Size size : mSizes) {
                if (widest < size.mWidth  || (widest == size.mWidth && mSizes.get(widestIdx).mColsCount < size.mColsCount)) {
                    if (size.mColsCount >= MIN_WIDTH) {
                        widest = size.mWidth;
                        widestIdx = mSizes.indexOf(size);
                    }
                }
            }

            if (widestIdx == -1) {
                widestIdx = 0;
            }

            mCachedIdx = widestIdx;

            return widestIdx;
        }
    }

    public class Size {
        private int mWidth;
        private int mHeight;
        private int mQuality;
        private int mColsCount;
        private int mRowsCount;
        private int mDurationEachMS; // duration of each thumbnail
        private int mStartNum;
        private String mImageName;
        private String mSignature;

        /**
         * Duration of each thumbnail in ms
         * @return thumbnail duration
         */
        public int getDurationEachMS() {
            return mDurationEachMS;
        }

        public int getStartNum() {
            return mStartNum;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public int getRowCount() {
            return mRowsCount;
        }

        public int getColCount() {
            return mColsCount;
        }

        @NonNull
        @Override
        public String toString() {
            return String.format("{width: %s, height: %s, quality: %s}", mWidth, mHeight, mQuality);
        }
    }
}
