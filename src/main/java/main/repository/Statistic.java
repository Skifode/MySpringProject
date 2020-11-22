package main.repository;

import java.util.Date;

public interface Statistic {

  Integer getPostsCount();

  Integer getLikesCount();

  Integer getDislikesCount();

  Integer getViewsCount();

  Date getFirstPublication();
}
