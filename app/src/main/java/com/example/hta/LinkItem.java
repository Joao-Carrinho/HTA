package com.example.hta;

public class LinkItem {
    private String title;
    private int resourceId;  // Referência ao arquivo PDF

    public LinkItem(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }
}
