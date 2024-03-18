package org.example.avoris.model;

import org.example.avoris.dto.SearchRequest;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "hotel_availability_searches")
public class MongoDBSearchRequest {
  private String searchId;
  private SearchRequest request;
  @Field("count")
  private int count = 0;

  public MongoDBSearchRequest() {
  }

  public String getSearchId() {
    return searchId;
  }

  public void setSearchId(String searchId) {
    this.searchId = searchId;
  }

  public SearchRequest getRequest() {
    return request;
  }

  public void setRequest(SearchRequest request) {
    this.request = request;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
