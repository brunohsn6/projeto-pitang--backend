package com.br.projetoestagio.hubpitang.utils;

import com.br.projetoestagio.hubpitang.models.Actor;
import org.apache.commons.text.WordUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ActorSpecification {
    public static Specification<Actor> searchActor(String name){
        return new Specification<Actor>() {
            @Override
            public Predicate toPredicate(Root<Actor> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(criteriaBuilder.like(root.get("name"),"%"+ WordUtils.capitalize(name) + "%"));
            }
        };
    }
}

