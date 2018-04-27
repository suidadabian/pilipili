package com.suidadabian.lixiaofeng.pilipili.model;

import java.util.Date;

public interface Picture {
    long getId();

    void setId(long id);

    long getUserId();

    void setUserId(long userId);

    String getUrl();

    void setUrl(String url);

    Date getDate();

    void setDate(Date date);

    String getTags();

    void setTags(String tags);

    boolean isShare();

    void setShare(boolean share);
}
