package com.project.shopapp.repository.impl;

import com.project.shopapp.dtos.info.OrderPageInfo;
import com.project.shopapp.helper.QueryUtils;
import com.project.shopapp.models.Order;
import com.project.shopapp.repository.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    private Query buildSearchQuery(OrderPageInfo model, boolean count) {
        Query query = new Query();
        if (model.getStatus() != null) {
           switch (model.getStatus()) {
               case "cancel":
                   query.addCriteria(Criteria.where("status").is("cancel"));
                   break;
               case "done":
                   query.addCriteria(Criteria.where("status").is("done"));
                   break;
               case "delivery":
                   query.addCriteria(Criteria.where("status").is("delivery"));
                   break;
               case "prepare":
                   query.addCriteria(Criteria.where("status").is("prepare"));
                   break;
               case "pending":
                   query.addCriteria(Criteria.where("status").is("pending"));
                   break;
           }
        }

        if (!count) {
            QueryUtils.getQuerySortAndPageable(query, model);
        }

        return query;
    }

    @Override
    public List<Order> search(OrderPageInfo model) {
        List<Order> orders = mongoTemplate.find(buildSearchQuery(model, false), Order.class);
        if(orders.isEmpty()) {
            return orders;
        }
        return orders;
    }

    @Override
    public Long count(OrderPageInfo model) {
        return mongoTemplate.count(buildSearchQuery(model, true), Order.class);
    }
}
