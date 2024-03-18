package org.example.avoris.service;

import org.example.avoris.dto.SearchRequest;
import org.example.avoris.dto.SearchResultResponse;

public interface SearchService {
public String processSearchRequest (SearchRequest request);

public SearchResultResponse getSearchRequestBySearchId(String searchID);

public String generateSearchID();
}
