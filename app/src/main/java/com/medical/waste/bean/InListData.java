package com.medical.waste.bean;

import java.util.List;

public class InListData {
    public List<Rubbish> list;

    /**
     * localArrayList : [1]
     * totalCount : 13
     * pageSize : 20
     * pageNo : 1
     * minPage : 1
     * maxPage : 1
     * totalPage : 1
     */

    private int totalCount;
    private int pageSize;
    private int pageNo;
    private int minPage;
    private int maxPage;
    private int totalPage;
    private List<Integer> localArrayList;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getMinPage() {
        return minPage;
    }

    public void setMinPage(int minPage) {
        this.minPage = minPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<Integer> getLocalArrayList() {
        return localArrayList;
    }

    public void setLocalArrayList(List<Integer> localArrayList) {
        this.localArrayList = localArrayList;
    }
}
