package com.br.projetoestagio.hubpitang.utils;

import com.br.projetoestagio.hubpitang.models.Actor;
import com.br.projetoestagio.hubpitang.models.Director;
import org.apache.commons.text.WordUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DirectorSpecification {
    public static Specification<Director> searchDirector(String name){
        return new Specification<Director>() {
            @Override
            public Predicate toPredicate(Root<Director> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(criteriaBuilder.like(root.get("name"),"%"+ WordUtils.capitalize(name) + "%"));
            }
        };
    }
}
