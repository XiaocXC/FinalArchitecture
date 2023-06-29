package com.zjl.finalarchitecture.module.toolbox.imeAnim.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Objects;

/**
 * @author Xiaoc
 * @since 2023-06-29
 *
 * 信息Data
 **/
public class MessageData implements MultiItemEntity {

    public static final int MESSAGE_TYPE_TEXT_SELF = 0;
    public static final int MESSAGE_TYPE_TEXT_OTHER = 1;

    private String id;

    private String message;

    /**
     * 信息类型，可能是语音、文字等类型
     */
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }

    /**
     * 必需重写！以ID为准
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageData)) return false;
        MessageData that = (MessageData) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
