package com.it.entity;

import java.io.Serializable;

public class SubSystemInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    String subSystemId;//子系统id，权限id
    String subSystemName;//子系统名
    String subSystemType;//子系统类型 team、project、build等
    String teamName;//团队或项目名称
    String teamId;//团队或项目的id
    String groupId;//分组id
    String url;
    String mark;
    String permissionType;
    String priority;
    String img;

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String subSystemId) {
        this.subSystemId = subSystemId;
    }

    public String getSubSystemName() {
        return subSystemName;
    }

    public void setSubSystemName(String subSystemName) {
        this.subSystemName = subSystemName;
    }

    public String getSubSystemType() {
        return subSystemType;
    }

    public void setSubSystemType(String subSystemType) {
        this.subSystemType = subSystemType;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
