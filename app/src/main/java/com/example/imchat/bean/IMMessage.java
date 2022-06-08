package com.example.imchat.bean;

public class IMMessage {
    //对方账号
    private String userName;
    //创建时间
    private long time;
    //类型，发送；接受
    private int type;
    //状态，失败;取消;成功
    private int status;
    //种类，文本;语言;图片
    private int kind;
    //内容,文本是内容;语言图片是路径
    private String content;

    public IMMessage(String userName, long time, int type, int status, int kind, String content) {
        this.userName = userName;
        this.time = time;
        this.type = type;
        this.status = status;
        this.kind = kind;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
