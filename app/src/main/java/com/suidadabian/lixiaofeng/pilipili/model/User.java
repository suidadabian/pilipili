package com.suidadabian.lixiaofeng.pilipili.model;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

@Entity
public class User {
    @Id(autoincrement = true)
    private long id;
    @NotNull
    private String account;
    @NotNull
    private String password;
    @NonNull
    private String avatarUrl;
    @NonNull
    private String name;
    @Convert(columnType = Integer.class, converter = SexConverter.class)
    private Sex sex;
    private String sign;
    @ToMany(referencedJoinProperty = "userId")
    private List<InfoPicture> infoPictures;
    @ToMany(referencedJoinProperty = "userId")
    private List<Comment> comments;
    @ToMany(referencedJoinProperty = "userId")
    private List<Reply> replies;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 1155262296)
    public User(long id, @NotNull String account, @NotNull String password,
                @NotNull String avatarUrl, @NotNull String name, Sex sex, String sign) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.sex = sex;
        this.sign = sign;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return this.sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1087134892)
    public List<InfoPicture> getInfoPictures() {
        if (infoPictures == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoPictureDao targetDao = daoSession.getInfoPictureDao();
            List<InfoPicture> infoPicturesNew = targetDao
                    ._queryUser_InfoPictures(id);
            synchronized (this) {
                if (infoPictures == null) {
                    infoPictures = infoPicturesNew;
                }
            }
        }
        return infoPictures;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 352742211)
    public synchronized void resetInfoPictures() {
        infoPictures = null;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 948559981)
    public List<Comment> getComments() {
        if (comments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CommentDao targetDao = daoSession.getCommentDao();
            List<Comment> commentsNew = targetDao._queryUser_Comments(id);
            synchronized (this) {
                if (comments == null) {
                    comments = commentsNew;
                }
            }
        }
        return comments;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 249603048)
    public synchronized void resetComments() {
        comments = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1320130910)
    public List<Reply> getReplies() {
        if (replies == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReplyDao targetDao = daoSession.getReplyDao();
            List<Reply> repliesNew = targetDao._queryUser_Replies(id);
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    public enum Sex {
        MALE(1, "男"), FEMALE(2, "女"), SECRET(3, "保密");

        private int id;
        private String info;

        Sex(int id, String info) {
            this.id = id;
            this.info = info;
        }

        public static Sex getSex(int id) {
            Sex[] sexes = Sex.values();
            for (Sex sex : sexes) {
                if (sex.id == id) {
                    return sex;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public static class SexConverter implements PropertyConverter<Sex, Integer> {
        @Override
        public Sex convertToEntityProperty(Integer databaseValue) {
            return Sex.getSex(databaseValue);
        }

        @Override
        public Integer convertToDatabaseValue(Sex entityProperty) {
            return entityProperty.id;
        }
    }
}
