package com.lqr.articlepatch.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * @创建者 CSDN_LQR
 * @描述 下载任务表
 */

public class TaskModel extends DataSupport {

    @Column(nullable = false)
    private int taskId;//不能命名为id

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    public TaskModel(int taskId, String url, String name, String path) {
        this.taskId = taskId;
        this.url = url;
        this.name = name;
        this.path = path;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
