package com.abhidas.myhealthnews;

import java.util.ArrayList;

public class Model {
    String status;
    String totalResults;
    ArrayList<articles> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Model.articles> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Model.articles> articles) {
        this.articles = articles;
    }

    public  class articles{
        String title;
        String description;
        String urlToImage;
        String url;

        public String getTitle() {
            return title;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
