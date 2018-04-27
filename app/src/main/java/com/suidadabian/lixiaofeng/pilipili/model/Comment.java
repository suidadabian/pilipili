package com.suidadabian.lixiaofeng.pilipili.model;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Comment {
    @Id(autoincrement = true)
    private long id;
    @NonNull
    private long userId;
    @NonNull
    private long infoPictureId;
    @NonNull
    private String comment;
    @NonNull
    private Date date;
    @NonNull
    private int index;
    @ToMany(referencedJoinProperty = "commentId")
    private List<Reply> replies;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1903578761)
    private transient CommentDao myDao;
    @Generated(hash = 683239547)
    public Comment(long id, long userId, long infoPictureId,
            @NonNull String comment, @NonNull Date date, int index) {
        this.id = id;
        this.userId = userId;
        this.infoPictureId = infoPictureId;
        this.comment = comment;
        this.date = date;
        this.index = index;
    }
    @Generated(hash = 1669165771)
    public Comment() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getInfoPictureId() {
        return this.infoPictureId;
    }
    public void setInfoPictureId(long infoPictureId) {
        this.infoPictureId = infoPictureId;
    }
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getIndex() {
        return this.index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 55553826)
    public List<Reply> getReplies() {
        if (replies == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReplyDao targetDao = daoSession.getReplyDao();
            List<Reply> repliesNew = targetDao._queryComment_Replies(id);
            synchronized (this) {
                if (replies == null) {
                    replies = repliesNew;
                }
            }
        }
        return replies;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2101789245)
    public synchronized void resetReplies() {
        replies = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2038262053)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCommentDao() : null;
    }
}
