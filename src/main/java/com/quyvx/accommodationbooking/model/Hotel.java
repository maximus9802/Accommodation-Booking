package com.quyvx.accommodationbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull(message = "Name should not be null")
    private String name;

    @NotNull(message = "Location should not be null")
    private String location;

    private float score = 0;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "detail_description")
    private String detailDescription;

    @NotNull(message = "Assess should not be null")
    private int assess;

    @Column(name = "avatar_hotel")
    private String avatarHotel;

    @Column(name = "number_rating")
    private int numberRating = 0;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
