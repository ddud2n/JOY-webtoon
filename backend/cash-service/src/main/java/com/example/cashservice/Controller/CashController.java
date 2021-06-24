package com.example.cashservice.Controller;


import com.example.cashservice.MongoTemplate.CashRecord;
import com.example.cashservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequestMapping("/cash-service")
@RestController
public class CashController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtTokenProvider tokenProvider;

/*
{
"change_amount" : "100",
"toon_name" : "",
"epi_name": "",
"epi_no": ""
"content" : "CHARGE"
}
{
    "id": {
        "timestamp": 1624290570,
        "date": "2021-06-22T00:49:30.000+09:00"
    },
    "userid": "2",
    "change_amount": 100,
    "content": "CHARGE",
    "toonname": "",
    "epiname": ""
}


 */
    // 캐시 충전
    @PostMapping(value = "/pushmongo")
    public CashRecord PushMongo(@RequestBody Map<String,String> param, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String jwt = bearerToken.substring(7, bearerToken.length());
        String userId = tokenProvider.getUserIdFromJWT(jwt).toString();

        Long cash = Long.valueOf(Integer.parseInt(param.get("change_amount")));
        CashRecord cr = CashRecord.builder().userid(userId).change_amount(cash).content(param.get("content"))
                .toonname(param.get("toon_name")).epiname(param.get("epi_name")).epino(param.get("epi_no")).build();
        mongoTemplate.insert(cr);

        return cr;
    }
/*
[
    {
        "id": {
            "timestamp": 1624290570,
            "date": "2021-06-22T00:49:30.000+09:00"
        },
        "userid": "2",
        "change_amount": 100,
        "content": "CHARGE",
        "toonname": "",
        "epiname": ""
    },
    {
        "id": {
            "timestamp": 1624290918,
            "date": "2021-06-22T00:55:18.000+09:00"
        },
        "userid": "2",
        "change_amount": 100,
        "content": "CHARGE",
        "toonname": "",
        "epiname": ""
    },
    {
        "id": {
            "timestamp": 1624291418,
            "date": "2021-06-22T01:03:38.000+09:00"
        },
        "userid": "2",
        "change_amount": 100,
        "content": "CHARGE",
        "toonname": "",
        "epiname": ""
    }
]
 */

    // 캐시내역 조회
    @GetMapping(value = "/getmongo")
    public List<CashRecord> GetMongo(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String jwt = bearerToken.substring(7, bearerToken.length());
        String userId = tokenProvider.getUserIdFromJWT(jwt).toString();
        Criteria criteria = new Criteria("userid");
        criteria.is(userId);
        Query query = new Query(criteria);
        List<CashRecord> crs= mongoTemplate.find(query, CashRecord.class, "cashRecord");
        return crs;
    }


    // 대여권 조회
    @GetMapping(value = "/CheckRentIng")
    public List<CashRecord> CheckRentIng(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String jwt = bearerToken.substring(7, bearerToken.length());
        String userId = tokenProvider.getUserIdFromJWT(jwt).toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now_1 = now.minusHours(24);
        Query query = Query.query(Criteria.where("userid").is(userId)
                .and("content").is("RENT_WEBTOON")
                .and("createdAt").gte(now_1).lte(now)
        );
        // userid == userId && content == RENT_WEBTOON && id.timestamp 이 24시간보다 뒤에있다.
        List<CashRecord> crs= mongoTemplate.find(query, CashRecord.class, "cashRecord");
        return crs;
    }



    // 특정 에피소드의 대여권 조회
    @GetMapping(value = "/CheckRentEpi/{epino}")
    public String CheckRentEpi(@PathVariable int epino, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String jwt = bearerToken.substring(7, bearerToken.length());
        String userId = tokenProvider.getUserIdFromJWT(jwt).toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now_1 = now.minusHours(24);
        String epi_no = Integer.toString(epino);
        Query query = Query.query(Criteria.where("userid").is(userId)
                .and("content").is("RENT_WEBTOON")
                .and("createdAt").gte(now_1).lte(now)
                .and("epino").is(epi_no)
        );
        // userid == userId && content == RENT_WEBTOON && id.timestamp 이 24시간보다 뒤에있다. && epino==epino
        List<CashRecord> crs= mongoTemplate.find(query, CashRecord.class, "cashRecord");
        if(crs.isEmpty()) return "false";
        return "true";
    }




}
