package com.example.videolibrary.shortvideo.editor.paster;


/**
 * Created by vinsonswang on 2017/10/26.
 *
 * 用于保存 贴纸控件相关参数的类
 *
 * 主要是用于恢复编辑贴纸的场景
 */
public class TCPasterViewInfo {
    private int viewType;
    private float viewCenterX, viewCenterY;// 控件的x y
    private float imageRotation;
    private float imageScale;
    private String pasterPath;
    private String name;
    private long startTime,endTime;

    public float getViewCenterX() {
        return viewCenterX;
    }

    public void setViewCenterX(float viewCenterX) {
        this.viewCenterX = viewCenterX;
    }

    public float getViewCenterY() {
        return viewCenterY;
    }

    public void setViewCenterY(float viewCenterY) {
        this.viewCenterY = viewCenterY;
    }

    public float getRotation() {
        return imageRotation;
    }

    public void setRotation(float viewRotation) {
        this.imageRotation = viewRotation;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPasterPath() {
        return pasterPath;
    }

    public void setPasterPath(String pasterPath) {
        this.pasterPath = pasterPath;
    }

    public float getImageScale() {
        return imageScale;
    }

    public void setImageScale(float imageScale) {
        this.imageScale = imageScale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
