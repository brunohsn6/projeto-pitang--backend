package com.br.projetoestagio.hubpitang.utils;

import com.br.projetoestagio.hubpitang.models.Movie;
import com.br.projetoestagio.hubpitang.models.Tvshow;
import org.apache.commons.text.WordUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TvshowSpecification {
    public static Specification<Tvshow> searchTvshow(String title, String year, String language){
        return new Specification<Tvshow>() {
            @Override
            public Predicate toPredicate(Root<Tvshow> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(criteriaBuilder.like(root.get("title"),"%"+ WordUtils.capitalize(title) + "%"),
                        criteriaBuilder.like(root.get("language"), language),
                        criteriaBuilder.like(root.get("release_year"), "%"+year+"%"));
            }
        };
    }
}
