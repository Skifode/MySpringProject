package main.repository;

public interface CountsPostsByDate {
  String getDate();
  Integer getCount();

  default String getFormattedDate() {
    return getDate().split("\\s")[0];
  }
}
