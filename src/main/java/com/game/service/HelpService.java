package com.game.service;


import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HelpService {

    public static Integer getLevel (Integer exp)
    {
        int out = (int) ((Math.sqrt(2500+200*exp)-50)/100);

        return  Integer.valueOf (out);
    }

    public static Integer getExpUntilNextLevel (Integer exp)
    {
        int out = 50*(getLevel(exp)+1)*(getLevel(exp)+2)-exp;

        return Integer.valueOf(out);
    }

    public static boolean validatePlayer(Player player)
    {
        if(player.getName()==null
            ||player.getTitle()==null
            ||player.getRace()==null
            ||player.getProfession()==null
            ||player.getBirthday()==null
            ||player.getExperience()==null
            ||player.getBirthday().getTime()<0
            ||player.getName().length()>12
            ||player.getTitle().length()>30
            ||player.getName().isEmpty()
            ||player.getExperience()>10000000
            ||player.getExperience()<0) return false;

        return true;
    }

    public static Long validateId(String id)
    {
        Long out;

        try {
            out = Long.parseLong(id);
            if (out < 1)
            out = null;
        } catch (NumberFormatException e)
        {
            out = null;
        }
        return out;
    }

    public static List<Player> findPagingByCriteria(PlayerService service,
                                                    String name, String title,
                                                    Race race, Profession profession,
                                                    Long after, Long before,
                                                    Boolean banned,
                                                    Integer minExperience,
                                                    Integer maxExperience,
                                                    Integer minLevel, Integer maxLevel,
                                                    PlayerOrder playerOrder,
                                                    Integer pageNumber, Integer pageSize)
    {
         Pageable pageable = PageRequest.of(pageNumber, pageSize);

         Page page = service.getPlayersList(new Specification<Player>() {
             @Override
             public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                 List <Predicate> predicates = new ArrayList<>();
                 if (name!=null)  predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"),"%"+name+"%")));
                 if (title!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"),"%"+title+"%")));
                 if (race!=null) predicates.add(criteriaBuilder.equal(root.get("race"),race));
                 if (profession!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("profession"),profession)));
                 if (after!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"),new Date(after))));
                 if (before!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("birthday"), new Date(before))));
                 if (banned!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("banned"), banned)));
                 if (minExperience!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"),minExperience)));
                 if (maxExperience!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("experience"),maxExperience)));
                 if (minLevel!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("level"),minLevel)));
                 if (maxLevel!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("level"),maxLevel)));
                 if (playerOrder!=null) criteriaQuery.orderBy(criteriaBuilder.asc(root.get(playerOrder.getFieldName())));
                 return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
             }
         }, pageable);

         return page.getContent();
    }

    public static Integer countByCriteria(PlayerService service,
                                          String name, String title,
                                          Race race, Profession profession,
                                          Long after, Long before,
                                          Boolean banned,
                                          Integer minExperience,
                                          Integer maxExperience,
                                          Integer minLevel, Integer maxLevel)
    {
        return service.getPlayersCount(new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List <Predicate> predicates = new ArrayList<>();
                if (name!=null)  predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"),"%"+name+"%")));
                if (title!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"),"%"+title+"%")));
                if (race!=null) predicates.add(criteriaBuilder.equal(root.get("race"),race));
                if (profession!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("profession"),profession)));
                if (after!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"),new Date(after))));
                if (before!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("birthday"), new Date(before))));
                if (banned!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("banned"), banned)));
                if (minExperience!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"),minExperience)));
                if (maxExperience!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("experience"),maxExperience)));
                if (minLevel!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("level"),minLevel)));
                if (maxLevel!=null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("level"),maxLevel)));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }
}
