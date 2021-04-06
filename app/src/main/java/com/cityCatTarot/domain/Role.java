package com.cityCatTarot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자 권한 정보.
 */
@Entity(name="Role")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="userId")
    private Long userId;

    @Column(name="name")
    private String name;

    public Role(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Role(String name){
        this(null, name);
    }
}
