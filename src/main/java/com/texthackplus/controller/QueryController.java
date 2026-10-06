package com.texthackplus.controller;

import com.texthackplus.service.QueryResult;
import com.texthackplus.service.QueryService;
import com.texthackplus.service.QueryService.QueryType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    static class MatchRequest {
        public String text1;
        public String text2;
    }

    static class PrimalityRequest {
        public long number;
    }

    @PostMapping("/exact")
    public QueryResult exactMatch(@RequestBody MatchRequest req) {
        return QueryService.executeQuery(QueryType.EXACT_MATCH, req.text1, req.text2);
    }

    @PostMapping("/fuzzy")
    public QueryResult fuzzyMatch(@RequestBody MatchRequest req) {
        return QueryService.executeQuery(QueryType.FUZZY_MATCH, req.text1, req.text2);
    }

    @PostMapping("/similarity")
    public QueryResult similarity(@RequestBody MatchRequest req) {
        return QueryService.executeQuery(QueryType.SIMILARITY, req.text1, req.text2);
    }

    @PostMapping("/primality")
    public QueryResult primality(@RequestBody PrimalityRequest req) {
        return QueryService.executeQuery(QueryType.PRIMALITY_TEST, req.number, 5);
    }
}
